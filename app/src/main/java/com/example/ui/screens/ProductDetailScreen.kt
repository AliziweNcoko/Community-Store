package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProductItem
import com.example.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
  product: ProductItem,
  onBack: () -> Unit,
  onAddToCart: (ProductItem) -> Unit,
  onBuyNow: (ProductItem) -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }
  val scrollState = rememberScrollState()
  var showReportDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Listing Details", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_detail")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          IconButton(onClick = { showReportDialog = true }) {
            Icon(Icons.Default.Flag, contentDescription = "Report listing", tint = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    bottomBar = {
      Surface(
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(16.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedButton(
            onClick = { onAddToCart(product) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .weight(1f)
              .height(50.dp)
              .testTag("btn_detail_add_cart")
          ) {
            Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add to Cart", fontWeight = FontWeight.Bold)
          }

          Button(
            onClick = {
              onAddToCart(product)
              onBuyNow(product)
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
              .weight(1f)
              .height(50.dp)
              .testTag("btn_detail_buy_now")
          ) {
            Icon(Icons.Default.FlashOn, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Buy Now", fontWeight = FontWeight.Bold)
          }
        }
      }
    },
    modifier = modifier
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(scrollState)
    ) {
      // Image Banner
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(260.dp)
      ) {
        Image(
          painter = painterResource(id = product.imageResId),
          contentDescription = product.title,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Top
        ) {
          CampusBadge(campus = product.campus)
          if (product.isEcoFriendly) {
            EcoBadge(co2SavedKg = product.ecoCo2SavedKg)
          }
        }
      }

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Price & Condition
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          ZarPriceTag(
            priceZar = product.priceZar,
            originalPriceZar = product.originalPriceZar
          )
          ConditionBadge(condition = product.condition)
        }

        // Title
        Text(
          text = product.title,
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface,
          lineHeight = 26.sp
        )

        // Trust Shield Alert
        TrustShieldBanner()

        // Description
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
          )
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Item Description",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = product.description,
              fontSize = 14.sp,
              lineHeight = 20.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        // Verified Seller Information
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Seller Credibility & Trust",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              RoleBadge(role = product.sellerRole)
            }

            HorizontalDivider()

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = product.sellerName.take(1),
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
              }

              Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = product.sellerName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                  )
                  if (product.isSellerVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                      imageVector = Icons.Default.CheckCircle,
                      contentDescription = "Verified",
                      tint = Color(0xFF059669),
                      modifier = Modifier.size(16.dp)
                    )
                  }
                }
                Text(
                  text = "⭐ ${product.sellerRating} • ${product.sellerReviewCount} verified student trades",
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }

        // Campus Safe Meeting Zone
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFEF3C7)
          )
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = Color(0xFFB45309),
              modifier = Modifier.size(24.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Designated Campus Safe Exchange Spot",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF92400E)
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = product.safeMeetingSpot,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color(0xFF78350F)
              )
              Text(
                text = "Covered by campus security surveillance. Never meet off-campus after dark.",
                fontSize = 11.sp,
                color = Color(0xFF92400E).copy(alpha = 0.85f),
                modifier = Modifier.padding(top = 4.dp)
              )
            }
          }
        }
      }
    }
  }

  // Report dialog
  if (showReportDialog) {
    AlertDialog(
      onDismissRequest = { showReportDialog = false },
      icon = { Icon(Icons.Default.ReportProblem, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
      title = { Text("Report Listing for Moderation") },
      text = {
        Text("Our campus safety team reviews flagged listings for misleading descriptions, suspicious pricing, or violation of university marketplace standards.")
      },
      confirmButton = {
        Button(
          onClick = { showReportDialog = false },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Submit Report")
        }
      },
      dismissButton = {
        TextButton(onClick = { showReportDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
