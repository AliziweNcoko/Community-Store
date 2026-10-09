package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.MarketplaceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val title: String) {
  object Home : Screen("home", "Community Store")
  object Bulletin : Screen("bulletin", "Campus Bulletin")
  object Cart : Screen("cart", "Cart & Checkout")
  object Orders : Screen("orders", "My Orders & Escrow")
  object Profile : Screen("profile", "My Profile")
  object Proposal : Screen("proposal", "Project Proposal")
}

class MarketplaceViewModel(
  private val repository: MarketplaceRepository = MarketplaceRepository()
) : ViewModel() {

  val currentUser = repository.currentUser
  val isLoggedIn = repository.isLoggedIn
  val products = repository.products
  val bulletinPosts = repository.bulletinPosts
  val cart = repository.cart
  val orders = repository.orders
  val selectedCampus = repository.selectedCampus
  val selectedCategory = repository.selectedCategory
  val searchQuery = repository.searchQuery

  private val _isWebPortalMode = MutableStateFlow(false)
  val isWebPortalMode: StateFlow<Boolean> = _isWebPortalMode.asStateFlow()

  private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
  val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

  private val _selectedProduct = MutableStateFlow<ProductItem?>(null)
  val selectedProduct: StateFlow<ProductItem?> = _selectedProduct.asStateFlow()

  private val _isAddListingSheetOpen = MutableStateFlow(false)
  val isAddListingSheetOpen: StateFlow<Boolean> = _isAddListingSheetOpen.asStateFlow()

  private val _isAddPostSheetOpen = MutableStateFlow(false)
  val isAddPostSheetOpen: StateFlow<Boolean> = _isAddPostSheetOpen.asStateFlow()

  private val _activeOrderCompleted = MutableStateFlow<OrderRecord?>(null)
  val activeOrderCompleted: StateFlow<OrderRecord?> = _activeOrderCompleted.asStateFlow()

  private val _snackbarMessage = MutableStateFlow<String?>(null)
  val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

  // Filtered products flow
  val filteredProducts: StateFlow<List<ProductItem>> = combine(
    products,
    selectedCategory,
    selectedCampus,
    searchQuery
  ) { allProducts, category, campus, query ->
    allProducts.filter { item ->
      val matchesCategory = (category == ProductCategory.ALL) || (item.category == category)
      val matchesCampus = (campus == null) || (item.campus == campus)
      val matchesQuery = query.isBlank() ||
          item.title.contains(query, ignoreCase = true) ||
          item.description.contains(query, ignoreCase = true) ||
          item.sellerName.contains(query, ignoreCase = true)
      matchesCategory && matchesCampus && matchesQuery
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun toggleWebPortalMode() {
    _isWebPortalMode.value = !_isWebPortalMode.value
  }

  fun setWebPortalMode(enabled: Boolean) {
    _isWebPortalMode.value = enabled
  }

  fun login(
    name: String,
    email: String,
    studentOrBizId: String,
    role: UserRole,
    campus: UniversityCampus
  ) {
    repository.login(name, email, studentOrBizId, role, campus)
    showSnackbar("Welcome, $name! Authenticated as ${role.label}")
  }

  fun logout() {
    repository.logout()
    _selectedProduct.value = null
    _currentScreen.value = Screen.Home
    showSnackbar("Logged out successfully.")
  }

  fun navigateTo(screen: Screen) {
    _currentScreen.value = screen
    if (screen != Screen.Home) {
      _selectedProduct.value = null
    }
  }

  fun openProductDetail(product: ProductItem) {
    _selectedProduct.value = product
  }

  fun closeProductDetail() {
    _selectedProduct.value = null
  }

  fun setCategoryFilter(category: ProductCategory) {
    repository.setCategoryFilter(category)
  }

  fun setCampusFilter(campus: UniversityCampus?) {
    repository.setCampusFilter(campus)
  }

  fun setSearchQuery(query: String) {
    repository.setSearchQuery(query)
  }

  fun switchUserRole(role: UserRole) {
    repository.updateUserRole(role)
    showSnackbar("Switched profile to ${role.label}")
  }

  fun changeCampus(campus: UniversityCampus) {
    repository.updateCampus(campus)
    showSnackbar("Campus updated to ${campus.fullName}")
  }

  fun addToCart(product: ProductItem) {
    repository.addToCart(product)
    showSnackbar("Added '${product.title.take(24)}...' to cart")
  }

  fun updateCartQuantity(productId: String, delta: Int) {
    repository.updateCartQuantity(productId, delta)
  }

  fun removeFromCart(productId: String) {
    repository.removeFromCart(productId)
    showSnackbar("Item removed from cart")
  }

  fun togglePostUpvote(postId: String) {
    repository.togglePostUpvote(postId)
  }

  fun togglePostRsvp(postId: String) {
    repository.togglePostRsvp(postId)
  }

  fun setAddListingSheetOpen(isOpen: Boolean) {
    _isAddListingSheetOpen.value = isOpen
  }

  fun setAddPostSheetOpen(isOpen: Boolean) {
    _isAddPostSheetOpen.value = isOpen
  }

  fun clearOrderDialog() {
    _activeOrderCompleted.value = null
  }

  fun showSnackbar(message: String) {
    _snackbarMessage.value = message
  }

  fun clearSnackbar() {
    _snackbarMessage.value = null
  }

  fun performCheckout(
    paymentMethod: PaymentMethod,
    deliveryMode: DeliveryMode,
    meetingSpot: String
  ) {
    val order = repository.checkout(paymentMethod, deliveryMode, meetingSpot)
    _activeOrderCompleted.value = order
    _currentScreen.value = Screen.Orders
  }

  fun createListing(
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
    repository.addProductListing(
      title = title,
      description = description,
      priceZar = priceZar,
      category = category,
      condition = condition,
      campus = campus,
      safeMeetingSpot = safeMeetingSpot,
      isEcoFriendly = isEcoFriendly,
      imageRes = imageRes
    )
    _isAddListingSheetOpen.value = false
    showSnackbar("Listing posted successfully on campus marketplace!")
  }

  fun createBulletinPost(
    title: String,
    content: String,
    type: PostType,
    tag: String
  ) {
    repository.addBulletinPost(title, content, type, tag)
    _isAddPostSheetOpen.value = false
    showSnackbar("Bulletin announcement published!")
  }
}
