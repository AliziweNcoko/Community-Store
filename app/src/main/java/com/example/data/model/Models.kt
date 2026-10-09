package com.example.data.model

import androidx.annotation.DrawableRes
import com.example.R

enum class UserRole(val label: String, val badgeColorHex: Long) {
  STUDENT("Verified Student (.ac.za)", 0xFF0284C7),
  FACULTY("Faculty / Staff", 0xFF7C3AED),
  VENDOR("Verified Local Vendor (CIPC)", 0xFF059669),
  RESIDENT("Campus Resident / Neighbor", 0xFFD97706)
}

enum class UniversityCampus(val fullName: String, val city: String, val shortName: String) {
  UCT("University of Cape Town", "Cape Town, WC", "UCT"),
  WITS("University of the Witwatersrand", "Johannesburg, GP", "Wits"),
  STELLENBOSCH("Stellenbosch University", "Stellenbosch, WC", "SU"),
  UP("University of Pretoria", "Hatfield, Pretoria, GP", "UP"),
  UJ("University of Johannesburg", "Auckland Park, GP", "UJ"),
  RHODES("Rhodes University", "Makhanda, EC", "Rhodes")
}

enum class ProductCategory(val displayName: String, val iconName: String) {
  ALL("All Items", "explore"),
  TEXTBOOKS("Textbooks & Notes", "menu_book"),
  ELECTRONICS("Tech & Electronics", "devices"),
  DORM_ESSENTIALS("Dorm Essentials", "bed"),
  PRODUCE_FOOD("Local Produce & Eats", "storefront"),
  FASHION_THRIFT("Thrift & Fashion", "checkroom"),
  SERVICES_TUTORING("Campus Services & Tutors", "school"),
  ECO_UPCYCLED("Eco & Upcycled", "recycling")
}

enum class ItemCondition(val displayName: String) {
  NEW("Brand New"),
  LIKE_NEW("Like New"),
  GOOD("Good Condition"),
  FAIR("Fair / Used"),
  REFURBISHED("Refurbished / Serviced")
}

data class ProductItem(
  val id: String,
  val title: String,
  val description: String,
  val priceZar: Double,
  val originalPriceZar: Double? = null,
  val category: ProductCategory,
  val condition: ItemCondition,
  val campus: UniversityCampus,
  val sellerName: String,
  val sellerRole: UserRole,
  val sellerRating: Double,
  val sellerReviewCount: Int,
  val isSellerVerified: Boolean,
  val safeMeetingSpot: String,
  val isEcoFriendly: Boolean,
  val ecoCo2SavedKg: Double,
  val isEscrowEligible: Boolean = true,
  @DrawableRes val imageResId: Int,
  val datePosted: String = "Today"
)

enum class PostType(val label: String) {
  EVENT("Campus Event"),
  ANNOUNCEMENT("Community Notice"),
  SKILL_SWAP("Skill Swap / Tutoring"),
  CLUB_INITIATIVE("Student Club Initiative"),
  ECO_DRIVE("Green / Eco Drive")
}

data class CommunityPost(
  val id: String,
  val title: String,
  val content: String,
  val type: PostType,
  val authorName: String,
  val authorRole: UserRole,
  val campus: UniversityCampus,
  val date: String,
  val upvotes: Int = 0,
  val rsvpCount: Int = 0,
  val isUpvoted: Boolean = false,
  val isRsvpd: Boolean = false,
  val tag: String
)

enum class PaymentMethod(val title: String, val subtitle: String, val icon: String) {
  SNAPSCAN("SnapScan QR", "Fast instant mobile scan & pay with PIN / biometric", "qr_code_scanner"),
  PAYFAST("PayFast Instant EFT", "Absa, Capitec, FNB, Nedbank & Standard Bank", "account_balance"),
  ESCROW("Campus Safe Escrow", "Funds held until safe meetup & item inspected", "security"),
  CASH_MEETUP("Cash on Campus Meetup", "Pay in-person at campus safe verification zones", "payments")
}

enum class DeliveryMode(val label: String, val feeZar: Double, val detail: String) {
  SAFE_ZONE_MEETUP("Campus Safe Meeting Zone", 0.0, "Meet at designated CCTV-monitored campus spots"),
  STUDENT_LOCKER("Smart Campus Locker", 15.0, "Collect 24/7 with OTP pin at student center"),
  RESIDENCE_DELIVERY("Res / Dorm Hand Delivery", 25.0, "Delivered directly to dorm reception")
}

data class CartItem(
  val product: ProductItem,
  val quantity: Int = 1
)

data class OrderRecord(
  val orderId: String,
  val date: String,
  val items: List<CartItem>,
  val totalZar: Double,
  val paymentMethod: PaymentMethod,
  val deliveryMode: DeliveryMode,
  val safeMeetingPoint: String,
  val status: String,
  val trackingCode: String
)

data class UserProfile(
  val name: String,
  val email: String,
  val studentOrBizId: String,
  val role: UserRole,
  val campus: UniversityCampus,
  val isVerified: Boolean,
  val ecoScoreKg: Double,
  val activeListings: Int,
  val totalSavedZar: Double,
  val reputationScore: Double
)
