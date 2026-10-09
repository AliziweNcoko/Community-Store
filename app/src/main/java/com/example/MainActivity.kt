package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddBulletinPostDialog
import com.example.ui.components.AddListingDialog
import com.example.ui.components.OrderSuccessDialog
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MarketplaceViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {

  private val viewModel: MarketplaceViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        MainApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun MainApp(viewModel: MarketplaceViewModel) {
  val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
  val isWebPortalMode by viewModel.isWebPortalMode.collectAsStateWithLifecycle()

  val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
  val selectedProduct by viewModel.selectedProduct.collectAsStateWithLifecycle()
  val allProducts by viewModel.products.collectAsStateWithLifecycle()
  val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()
  val bulletinPosts by viewModel.bulletinPosts.collectAsStateWithLifecycle()
  val cartItems by viewModel.cart.collectAsStateWithLifecycle()
  val orders by viewModel.orders.collectAsStateWithLifecycle()
  val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
  val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
  val selectedCampus by viewModel.selectedCampus.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

  val isAddListingOpen by viewModel.isAddListingSheetOpen.collectAsStateWithLifecycle()
  val isAddPostOpen by viewModel.isAddPostSheetOpen.collectAsStateWithLifecycle()
  val activeOrderCompleted by viewModel.activeOrderCompleted.collectAsStateWithLifecycle()
  val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(snackbarMessage) {
    snackbarMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.clearSnackbar()
    }
  }

  // Dialogs
  if (isAddListingOpen) {
    AddListingDialog(
      currentCampus = currentUser.campus,
      onDismiss = { viewModel.setAddListingSheetOpen(false) },
      onSubmit = { title, desc, price, cat, cond, campus, spot, isEco, img ->
        viewModel.createListing(title, desc, price, cat, cond, campus, spot, isEco, img)
      }
    )
  }

  if (isAddPostOpen) {
    AddBulletinPostDialog(
      onDismiss = { viewModel.setAddPostSheetOpen(false) },
      onSubmit = { title, content, type, tag ->
        viewModel.createBulletinPost(title, content, type, tag)
      }
    )
  }

  activeOrderCompleted?.let { order ->
    OrderSuccessDialog(
      order = order,
      onDismiss = { viewModel.clearOrderDialog() }
    )
  }

  // 1. Campus Web Portal & Management Mode ("must not be on the phone")
  if (isWebPortalMode) {
    WebPortalScreen(
      products = allProducts,
      orders = orders,
      user = currentUser,
      isLoggedIn = isLoggedIn,
      onExitWebPortal = { viewModel.setWebPortalMode(false) },
      onProductClick = {
        viewModel.setWebPortalMode(false)
        viewModel.openProductDetail(it)
      },
      onLoginClick = {
        viewModel.setWebPortalMode(false)
      }
    )
    return
  }

  // 2. Authentication Gate: If not logged in, show Login & Identity Verification Portal
  if (!isLoggedIn) {
    LoginScreen(
      onLoginSuccess = { name, email, studentOrBizId, role, campus ->
        viewModel.login(name, email, studentOrBizId, role, campus)
      },
      onOpenWebPortal = {
        viewModel.setWebPortalMode(true)
      }
    )
    return
  }

  // 3. Authenticated App Experience
  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    bottomBar = {
      // Show bottom navigation bar when not in full-screen detail view or proposal view
      if (selectedProduct == null && currentScreen != Screen.Proposal) {
        NavigationBar(
          windowInsets = WindowInsets.navigationBars,
          containerColor = MaterialTheme.colorScheme.surface,
          tonalElevation = 6.dp
        ) {
          NavigationBarItem(
            selected = currentScreen == Screen.Home,
            onClick = { viewModel.navigateTo(Screen.Home) },
            icon = {
              Icon(
                if (currentScreen == Screen.Home) Icons.Filled.Storefront else Icons.Outlined.Storefront,
                contentDescription = "Store"
              )
            },
            label = { Text("Store", fontSize = 11.sp, fontWeight = if (currentScreen == Screen.Home) FontWeight.Bold else FontWeight.Normal) },
            modifier = Modifier.testTag("nav_store")
          )

          NavigationBarItem(
            selected = currentScreen == Screen.Bulletin,
            onClick = { viewModel.navigateTo(Screen.Bulletin) },
            icon = {
              Icon(
                if (currentScreen == Screen.Bulletin) Icons.Filled.Campaign else Icons.Outlined.Campaign,
                contentDescription = "Bulletin"
              )
            },
            label = { Text("Bulletin", fontSize = 11.sp, fontWeight = if (currentScreen == Screen.Bulletin) FontWeight.Bold else FontWeight.Normal) },
            modifier = Modifier.testTag("nav_bulletin")
          )

          NavigationBarItem(
            selected = currentScreen == Screen.Cart,
            onClick = { viewModel.navigateTo(Screen.Cart) },
            icon = {
              BadgedBox(
                badge = {
                  val totalItems = cartItems.sumOf { it.quantity }
                  if (totalItems > 0) {
                    Badge { Text("$totalItems") }
                  }
                }
              ) {
                Icon(
                  if (currentScreen == Screen.Cart) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                  contentDescription = "Cart"
                )
              }
            },
            label = { Text("Cart", fontSize = 11.sp, fontWeight = if (currentScreen == Screen.Cart) FontWeight.Bold else FontWeight.Normal) },
            modifier = Modifier.testTag("nav_cart")
          )

          NavigationBarItem(
            selected = currentScreen == Screen.Orders,
            onClick = { viewModel.navigateTo(Screen.Orders) },
            icon = {
              Icon(
                if (currentScreen == Screen.Orders) Icons.Filled.ReceiptLong else Icons.Outlined.ReceiptLong,
                contentDescription = "Orders"
              )
            },
            label = { Text("Orders", fontSize = 11.sp, fontWeight = if (currentScreen == Screen.Orders) FontWeight.Bold else FontWeight.Normal) },
            modifier = Modifier.testTag("nav_orders")
          )

          NavigationBarItem(
            selected = currentScreen == Screen.Profile,
            onClick = { viewModel.navigateTo(Screen.Profile) },
            icon = {
              Icon(
                if (currentScreen == Screen.Profile) Icons.Filled.Person else Icons.Outlined.Person,
                contentDescription = "Profile"
              )
            },
            label = { Text("Profile", fontSize = 11.sp, fontWeight = if (currentScreen == Screen.Profile) FontWeight.Bold else FontWeight.Normal) },
            modifier = Modifier.testTag("nav_profile")
          )
        }
      }
    },
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(
          top = if (selectedProduct != null || currentScreen == Screen.Proposal) 0.dp else innerPadding.calculateTopPadding(),
          bottom = if (selectedProduct != null || currentScreen == Screen.Proposal) 0.dp else innerPadding.calculateBottomPadding()
        )
    ) {
      if (selectedProduct != null) {
        ProductDetailScreen(
          product = selectedProduct!!,
          onBack = { viewModel.closeProductDetail() },
          onAddToCart = { viewModel.addToCart(it) },
          onBuyNow = {
            viewModel.addToCart(it)
            viewModel.navigateTo(Screen.Cart)
          }
        )
      } else {
        Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
          when (screen) {
            Screen.Home -> MarketplaceHomeScreen(
              products = filteredProducts,
              selectedCategory = selectedCategory,
              selectedCampus = selectedCampus,
              searchQuery = searchQuery,
              onCategorySelected = { viewModel.setCategoryFilter(it) },
              onCampusSelected = { viewModel.setCampusFilter(it) },
              onSearchQueryChanged = { viewModel.setSearchQuery(it) },
              onProductClick = { viewModel.openProductDetail(it) },
              onAddToCart = { viewModel.addToCart(it) },
              onOpenProposal = { viewModel.navigateTo(Screen.Proposal) },
              onOpenWebPortal = { viewModel.setWebPortalMode(true) },
              onAddListingClick = { viewModel.setAddListingSheetOpen(true) }
            )

            Screen.Bulletin -> CommunityBulletinScreen(
              posts = bulletinPosts,
              onUpvoteClick = { viewModel.togglePostUpvote(it) },
              onRsvpClick = { viewModel.togglePostRsvp(it) },
              onAddPostClick = { viewModel.setAddPostSheetOpen(true) }
            )

            Screen.Cart -> CartAndCheckoutScreen(
              cartItems = cartItems,
              onUpdateQuantity = { id, delta -> viewModel.updateCartQuantity(id, delta) },
              onRemoveItem = { viewModel.removeFromCart(it) },
              onCheckout = { payment, delivery, spot ->
                viewModel.performCheckout(payment, delivery, spot)
              },
              onBrowseMarketplace = { viewModel.navigateTo(Screen.Home) }
            )

            Screen.Orders -> OrdersScreen(
              orders = orders,
              onBrowseMarketplace = { viewModel.navigateTo(Screen.Home) }
            )

            Screen.Profile -> UserProfileScreen(
              user = currentUser,
              onRoleSwitch = { viewModel.switchUserRole(it) },
              onCampusChange = { viewModel.changeCampus(it) },
              onOpenProposal = { viewModel.navigateTo(Screen.Proposal) },
              onOpenWebPortal = { viewModel.setWebPortalMode(true) },
              onLogout = { viewModel.logout() }
            )

            Screen.Proposal -> ProjectProposalScreen(
              onBack = { viewModel.navigateTo(Screen.Home) }
            )
          }
        }
      }
    }
  }
}
