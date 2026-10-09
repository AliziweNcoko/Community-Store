package com.example

import com.example.data.model.*
import com.example.data.repository.MarketplaceRepository
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun repository_initialProducts_loaded() {
    val repo = MarketplaceRepository()
    val products = repo.products.value
    assertTrue("Products should be seeded", products.isNotEmpty())
    assertTrue("Should contain textbook category", products.any { it.category == ProductCategory.TEXTBOOKS })
  }

  @Test
  fun repository_loginWithStudentEmail_assignsStudentRole() {
    val repo = MarketplaceRepository()
    assertFalse("Default should be logged out", repo.isLoggedIn.value)

    repo.login(
      name = "Nandi Khumalo",
      email = "n.khumalo@myuct.ac.za",
      studentOrBizId = "KHMNDI004",
      role = UserRole.STUDENT,
      campus = UniversityCampus.UCT
    )

    assertTrue("Should be logged in", repo.isLoggedIn.value)
    assertEquals("Nandi Khumalo", repo.currentUser.value.name)
    assertEquals("n.khumalo@myuct.ac.za", repo.currentUser.value.email)
    assertEquals(UserRole.STUDENT, repo.currentUser.value.role)
    assertTrue("Student should have verified status", repo.currentUser.value.isVerified)
    assertEquals(UniversityCampus.UCT, repo.currentUser.value.campus)

    repo.logout()
    assertFalse("Should be logged out after logout()", repo.isLoggedIn.value)
  }

  @Test
  fun repository_cartAndCheckoutFlow_works() {
    val repo = MarketplaceRepository()
    val product = repo.products.value.first()
    repo.addToCart(product)

    assertEquals(1, repo.cart.value.size)
    assertEquals(1, repo.cart.value.first().quantity)

    repo.updateCartQuantity(product.id, 1)
    assertEquals(2, repo.cart.value.first().quantity)

    val order = repo.checkout(
      paymentMethod = PaymentMethod.ESCROW,
      deliveryMode = DeliveryMode.SAFE_ZONE_MEETUP,
      meetingSpot = "Library Foyer"
    )

    assertNotNull(order)
    assertTrue("Cart should be cleared after checkout", repo.cart.value.isEmpty())
    assertTrue("Order list should contain the new order", repo.orders.value.any { it.orderId == order.orderId })
  }

  @Test
  fun proposal_containsAllRequiredPhases() {
    assertEquals(3, CommunityStoreProposal.phases.size)
    assertEquals(6, CommunityStoreProposal.teamRoles.size)
    assertEquals(6, CommunityStoreProposal.challengesAndSolutions.size)
  }
}
