package com.example.data.repository

import com.example.R
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class MarketplaceRepository {

  private val _currentUser = MutableStateFlow(
    UserProfile(
      name = "Nandi Khumalo",
      email = "n.khumalo@uct.ac.za",
      studentOrBizId = "KHMNDI004",
      role = UserRole.STUDENT,
      campus = UniversityCampus.UCT,
      isVerified = true,
      ecoScoreKg = 42.5,
      activeListings = 3,
      totalSavedZar = 1850.0,
      reputationScore = 4.9
    )
  )
  val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

  private val _isLoggedIn = MutableStateFlow(false)
  val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

  fun login(
    name: String,
    email: String,
    studentOrBizId: String,
    role: UserRole,
    campus: UniversityCampus
  ) {
    _currentUser.value = UserProfile(
      name = name,
      email = email,
      studentOrBizId = studentOrBizId,
      role = role,
      campus = campus,
      isVerified = true,
      ecoScoreKg = 24.0,
      activeListings = 2,
      totalSavedZar = 850.0,
      reputationScore = 4.95
    )
    _isLoggedIn.value = true
  }

  fun logout() {
    _isLoggedIn.value = false
    _cart.value = emptyList()
  }

  private val _products = MutableStateFlow<List<ProductItem>>(getInitialProducts())
  val products: StateFlow<List<ProductItem>> = _products.asStateFlow()

  private val _bulletinPosts = MutableStateFlow<List<CommunityPost>>(getInitialBulletinPosts())
  val bulletinPosts: StateFlow<List<CommunityPost>> = _bulletinPosts.asStateFlow()

  private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
  val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

  private val _orders = MutableStateFlow<List<OrderRecord>>(getInitialOrders())
  val orders: StateFlow<List<OrderRecord>> = _orders.asStateFlow()

  // Selected filters
  private val _selectedCampus = MutableStateFlow<UniversityCampus?>(null)
  val selectedCampus: StateFlow<UniversityCampus?> = _selectedCampus.asStateFlow()

  private val _selectedCategory = MutableStateFlow(ProductCategory.ALL)
  val selectedCategory: StateFlow<ProductCategory> = _selectedCategory.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  fun setCampusFilter(campus: UniversityCampus?) {
    _selectedCampus.value = campus
  }

  fun setCategoryFilter(category: ProductCategory) {
    _selectedCategory.value = category
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun updateUserRole(role: UserRole) {
    _currentUser.update { current ->
      val (email, id) = when (role) {
        UserRole.STUDENT -> "n.khumalo@uct.ac.za" to "KHMNDI004"
        UserRole.FACULTY -> "prof.khumalo@uct.ac.za" to "FAC-9842"
        UserRole.VENDOR -> "info@fynbosmarket.co.za" to "CIPC-2022/49102"
        UserRole.RESIDENT -> "nandi.local@gmail.com" to "RES-RONDEBOSCH"
      }
      current.copy(role = role, email = email, studentOrBizId = id)
    }
  }

  fun updateCampus(campus: UniversityCampus) {
    _currentUser.update { it.copy(campus = campus) }
  }

  fun addToCart(product: ProductItem) {
    _cart.update { currentCart ->
      val existing = currentCart.find { it.product.id == product.id }
      if (existing != null) {
        currentCart.map {
          if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
        }
      } else {
        currentCart + CartItem(product = product, quantity = 1)
      }
    }
  }

  fun updateCartQuantity(productId: String, delta: Int) {
    _cart.update { currentCart ->
      currentCart.mapNotNull { item ->
        if (item.product.id == productId) {
          val newQty = item.quantity + delta
          if (newQty > 0) item.copy(quantity = newQty) else null
        } else {
          item
        }
      }
    }
  }

  fun removeFromCart(productId: String) {
    _cart.update { current -> current.filter { it.product.id != productId } }
  }

  fun clearCart() {
    _cart.value = emptyList()
  }

  fun checkout(
    paymentMethod: PaymentMethod,
    deliveryMode: DeliveryMode,
    meetingSpot: String
  ): OrderRecord {
    val cartItems = _cart.value
    val itemsTotal = cartItems.sumOf { it.product.priceZar * it.quantity }
    val grandTotal = itemsTotal + deliveryMode.feeZar
    val co2Saved = cartItems.sumOf { it.product.ecoCo2SavedKg * it.quantity }

    val newOrder = OrderRecord(
      orderId = "CS-ZA-" + (1000..9999).random(),
      date = "Today",
      items = cartItems,
      totalZar = grandTotal,
      paymentMethod = paymentMethod,
      deliveryMode = deliveryMode,
      safeMeetingPoint = meetingSpot,
      status = if (paymentMethod == PaymentMethod.ESCROW) "Escrow Protected - Awaiting Handover" else "Confirmed - Ready for Pickup",
      trackingCode = "TRK-" + UUID.randomUUID().toString().take(8).uppercase()
    )

    _orders.update { listOf(newOrder) + it }
    _currentUser.update { current ->
      current.copy(
        ecoScoreKg = current.ecoScoreKg + co2Saved,
        totalSavedZar = current.totalSavedZar + (itemsTotal * 0.3) // avg savings vs buying brand new
      )
    }
    clearCart()
    return newOrder
  }

  fun addProductListing(
    title: String,
    description: String,
    priceZar: Double,
    category: ProductCategory,
    condition: ItemCondition,
    campus: UniversityCampus,
    safeMeetingSpot: String,
    isEcoFriendly: Boolean,
    imageRes: Int
  ) {
    val newProduct = ProductItem(
      id = UUID.randomUUID().toString(),
      title = title,
      description = description,
      priceZar = priceZar,
      category = category,
      condition = condition,
      campus = campus,
      sellerName = _currentUser.value.name,
      sellerRole = _currentUser.value.role,
      sellerRating = _currentUser.value.reputationScore,
      sellerReviewCount = 12,
      isSellerVerified = _currentUser.value.isVerified,
      safeMeetingSpot = safeMeetingSpot,
      isEcoFriendly = isEcoFriendly,
      ecoCo2SavedKg = if (isEcoFriendly) 4.5 else 1.2,
      imageResId = imageRes,
      datePosted = "Just now"
    )
    _products.update { listOf(newProduct) + it }
    _currentUser.update { it.copy(activeListings = it.activeListings + 1) }
  }

  fun togglePostUpvote(postId: String) {
    _bulletinPosts.update { posts ->
      posts.map { post ->
        if (post.id == postId) {
          val newUpvoted = !post.isUpvoted
          post.copy(
            isUpvoted = newUpvoted,
            upvotes = if (newUpvoted) post.upvotes + 1 else post.upvotes - 1
          )
        } else post
      }
    }
  }

  fun togglePostRsvp(postId: String) {
    _bulletinPosts.update { posts ->
      posts.map { post ->
        if (post.id == postId) {
          val newRsvpd = !post.isRsvpd
          post.copy(
            isRsvpd = newRsvpd,
            rsvpCount = if (newRsvpd) post.rsvpCount + 1 else post.rsvpCount - 1
          )
        } else post
      }
    }
  }

  fun addBulletinPost(
    title: String,
    content: String,
    type: PostType,
    tag: String
  ) {
    val newPost = CommunityPost(
      id = UUID.randomUUID().toString(),
      title = title,
      content = content,
      type = type,
      authorName = _currentUser.value.name,
      authorRole = _currentUser.value.role,
      campus = _currentUser.value.campus,
      date = "Just now",
      upvotes = 1,
      isUpvoted = true,
      tag = tag
    )
    _bulletinPosts.update { listOf(newPost) + it }
  }

  private fun getInitialProducts(): List<ProductItem> {
    return listOf(
      ProductItem(
        id = "prod-1",
        title = "Calculus & Linear Algebra 8th Ed + Past Papers",
        description = "Standard MAM1000W prescribed textbook with annotated lecture summaries and tutorial notes. No torn pages, excellent condition.",
        priceZar = 450.0,
        originalPriceZar = 980.0,
        category = ProductCategory.TEXTBOOKS,
        condition = ItemCondition.LIKE_NEW,
        campus = UniversityCampus.UCT,
        sellerName = "Thabo Mthembu",
        sellerRole = UserRole.STUDENT,
        sellerRating = 4.95,
        sellerReviewCount = 28,
        isSellerVerified = true,
        safeMeetingSpot = "Chancellor Oppenheimer Library Foyer (Upper Campus)",
        isEcoFriendly = true,
        ecoCo2SavedKg = 6.2,
        imageResId = R.drawable.img_textbooks_academic
      ),
      ProductItem(
        id = "prod-2",
        title = "South African Commercial Law 6th Edition (Juta)",
        description = "Essential reading for CML1001F / CML2001F. Includes highlighted case law and exam question prep guide.",
        priceZar = 380.0,
        originalPriceZar = 750.0,
        category = ProductCategory.TEXTBOOKS,
        condition = ItemCondition.GOOD,
        campus = UniversityCampus.WITS,
        sellerName = "Sipho Ndlovu",
        sellerRole = UserRole.STUDENT,
        sellerRating = 4.88,
        sellerReviewCount = 19,
        isSellerVerified = true,
        safeMeetingSpot = "Wartenweiler Library Security Desk (East Campus)",
        isEcoFriendly = true,
        ecoCo2SavedKg = 5.8,
        imageResId = R.drawable.img_textbooks_academic
      ),
      ProductItem(
        id = "prod-3",
        title = "Noise-Cancelling Wireless Headphones & Desk Lamp",
        description = "Perfect for late night studying in dorms. Crystal clear sound, 30hr battery life, comes with adjustable warm LED desk lamp.",
        priceZar = 850.0,
        originalPriceZar = 1600.0,
        category = ProductCategory.ELECTRONICS,
        condition = ItemCondition.LIKE_NEW,
        campus = UniversityCampus.STELLENBOSCH,
        sellerName = "Anika van der Merwe",
        sellerRole = UserRole.STUDENT,
        sellerRating = 4.92,
        sellerReviewCount = 14,
        isSellerVerified = true,
        safeMeetingSpot = "Neelsie Student Centre Security Kiosk",
        isEcoFriendly = true,
        ecoCo2SavedKg = 8.5,
        imageResId = R.drawable.img_dorm_electronics
      ),
      ProductItem(
        id = "prod-4",
        title = "Dorm Study Essentials Bundle: Kettle + Power Strip + Organizer",
        description = "Complete residence starter kit. 1.7L stainless steel kettle, surge-protected multi-plug adapter, and desk organizer rack.",
        priceZar = 390.0,
        originalPriceZar = 790.0,
        category = ProductCategory.DORM_ESSENTIALS,
        condition = ItemCondition.GOOD,
        campus = UniversityCampus.UP,
        sellerName = "Kagiso Dlamini",
        sellerRole = UserRole.RESIDENT,
        sellerRating = 4.85,
        sellerReviewCount = 9,
        isSellerVerified = true,
        safeMeetingSpot = "Hatfield Campus Student Union Building",
        isEcoFriendly = true,
        ecoCo2SavedKg = 4.0,
        imageResId = R.drawable.img_dorm_electronics
      ),
      ProductItem(
        id = "prod-5",
        title = "Organic Student Veggie Box & Artisanal Farm Sourdough",
        description = "Harvested daily from local Stellenbosch & Philippi micro-farms: avocados, organic spinach, heirloom tomatoes, and freshly baked sourdough.",
        priceZar = 180.0,
        originalPriceZar = 260.0,
        category = ProductCategory.PRODUCE_FOOD,
        condition = ItemCondition.NEW,
        campus = UniversityCampus.UCT,
        sellerName = "Fynbos Community Harvest",
        sellerRole = UserRole.VENDOR,
        sellerRating = 4.98,
        sellerReviewCount = 84,
        isSellerVerified = true,
        safeMeetingSpot = "Leslie Social Science Plaza Market Stall",
        isEcoFriendly = true,
        ecoCo2SavedKg = 12.0,
        imageResId = R.drawable.img_community_produce
      ),
      ProductItem(
        id = "prod-6",
        title = "Raw Fynbos Honey (500g) & Artisan Rooibos Treats",
        description = "Pure Cape floral kingdom wildflower honey direct from registered beekeeper. 100% natural, unprocessed, with dried fruit snacks.",
        priceZar = 110.0,
        originalPriceZar = 160.0,
        category = ProductCategory.PRODUCE_FOOD,
        condition = ItemCondition.NEW,
        campus = UniversityCampus.STELLENBOSCH,
        sellerName = "Cape Flora Organics (CIPC #2019/3310)",
        sellerRole = UserRole.VENDOR,
        sellerRating = 5.0,
        sellerReviewCount = 62,
        isSellerVerified = true,
        safeMeetingSpot = "Rooiplein Central Plaza Walkway",
        isEcoFriendly = true,
        ecoCo2SavedKg = 3.5,
        imageResId = R.drawable.img_community_produce
      ),
      ProductItem(
        id = "prod-7",
        title = "Casio FX-991ZA Plus II Scientific Calculator",
        description = "The official calculator approved by South African universities for engineering, finance, and science examinations. Includes slide-on hard case.",
        priceZar = 320.0,
        originalPriceZar = 550.0,
        category = ProductCategory.ELECTRONICS,
        condition = ItemCondition.LIKE_NEW,
        campus = UniversityCampus.UJ,
        sellerName = "Lerato Molefe",
        sellerRole = UserRole.STUDENT,
        sellerRating = 4.9,
        sellerReviewCount = 11,
        isSellerVerified = true,
        safeMeetingSpot = "Sanlam Student Centre Security Post (APK Campus)",
        isEcoFriendly = true,
        ecoCo2SavedKg = 2.1,
        imageResId = R.drawable.img_textbooks_academic
      ),
      ProductItem(
        id = "prod-8",
        title = "Python, Java & Data Structures 1-on-1 Tutoring (2 hrs)",
        description = "Peer tutor with Honours in Computer Science. Help with CSC1015F / CSC1016S assignments, debugging, and mock exam problems.",
        priceZar = 250.0,
        originalPriceZar = 400.0,
        category = ProductCategory.SERVICES_TUTORING,
        condition = ItemCondition.NEW,
        campus = UniversityCampus.UCT,
        sellerName = "Brandon Pillay (Dean's Merit List)",
        sellerRole = UserRole.STUDENT,
        sellerRating = 4.96,
        sellerReviewCount = 47,
        isSellerVerified = true,
        safeMeetingSpot = "Computer Science Computer Lab (Upper Campus)",
        isEcoFriendly = true,
        ecoCo2SavedKg = 1.0,
        imageResId = R.drawable.img_sa_campus_banner
      )
    )
  }

  private fun getInitialBulletinPosts(): List<CommunityPost> {
    return listOf(
      CommunityPost(
        id = "post-1",
        title = "Semester 2 Textbook Swap & Charity Drive",
        content = "Join the Green Campus Initiative outside the student union! Bring any used textbook to swap for another or donate for needy first-year students. Free fair-trade coffee for donors!",
        type = PostType.ECO_DRIVE,
        authorName = "SRC Sustainability Portfolio",
        authorRole = UserRole.STUDENT,
        campus = UniversityCampus.UCT,
        date = "Today at 09:30",
        upvotes = 84,
        rsvpCount = 43,
        isUpvoted = true,
        isRsvpd = true,
        tag = "Eco Drive"
      ),
      CommunityPost(
        id = "post-2",
        title = "Weekly Farmers & Artisan Pop-up Market on Campus",
        content = "Local Western Cape family farmers and student bakers will have stalls set up this Thursday. Fresh bread, biltong, avocados, and handcrafted accessories. Support local!",
        type = PostType.EVENT,
        authorName = "Local Merchant Co-op",
        authorRole = UserRole.VENDOR,
        campus = UniversityCampus.STELLENBOSCH,
        date = "Yesterday",
        upvotes = 56,
        rsvpCount = 31,
        isUpvoted = false,
        isRsvpd = false,
        tag = "Local Market"
      ),
      CommunityPost(
        id = "post-3",
        title = "Economics & Financial Accounting Exam Study Group",
        content = "Looking for 3 more students to form a daily revision pod for ECO1010F & ACC1006F. We meet at Kramer Law building study pods with past paper solutions.",
        type = PostType.SKILL_SWAP,
        authorName = "Khumalo Sibiya",
        authorRole = UserRole.STUDENT,
        campus = UniversityCampus.UCT,
        date = "2 days ago",
        upvotes = 32,
        rsvpCount = 7,
        isUpvoted = false,
        isRsvpd = false,
        tag = "Study Group"
      ),
      CommunityPost(
        id = "post-4",
        title = "Notice: Designated Safe Exchange Zones Active 24/7",
        content = "Reminder to all community members: Use the CCTV-monitored safe exchange points outside main libraries and campus security stations for all marketplace handovers.",
        type = PostType.ANNOUNCEMENT,
        authorName = "Campus Protection Services",
        authorRole = UserRole.FACULTY,
        campus = UniversityCampus.WITS,
        date = "3 days ago",
        upvotes = 119,
        rsvpCount = 0,
        isUpvoted = true,
        isRsvpd = false,
        tag = "Campus Safety"
      )
    )
  }

  private fun getInitialOrders(): List<OrderRecord> {
    return listOf(
      OrderRecord(
        orderId = "CS-ZA-8492",
        date = "Yesterday",
        items = listOf(
          CartItem(
            product = getInitialProducts()[0],
            quantity = 1
          )
        ),
        totalZar = 450.0,
        paymentMethod = PaymentMethod.ESCROW,
        deliveryMode = DeliveryMode.SAFE_ZONE_MEETUP,
        safeMeetingPoint = "Chancellor Oppenheimer Library Foyer",
        status = "Escrow Protected - Ready for Meetup",
        trackingCode = "TRK-SA8921"
      )
    )
  }
}
