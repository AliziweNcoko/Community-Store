package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.data.model.*
import com.example.ui.components.RoleBadge
import com.example.ui.components.ZarPriceTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebPortalScreen(
  products: List<ProductItem>,
  orders: List<OrderRecord>,
  user: UserProfile,
  isLoggedIn: Boolean,
  onExitWebPortal: () -> Unit,
  onProductClick: (ProductItem) -> Unit,
  onLoginClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(0) }
  val tabs = listOf("Marketplace Catalog", "Escrow & Settlements", "Identity & CIPC Registry", "Campus Safety & CCTV")

  Scaffold(
    topBar = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFF1E293B))
      ) {
        // Mock Browser Address Bar
        Surface(
          color = Color(0xFF0F172A),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFEF4444)))
              Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
              Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF10B981)))
            }

            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFF1E293B),
              modifier = Modifier.weight(1f)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(12.dp))
                Text(
                  text = "https://communitystore.ac.za/web-portal/dashboard",
                  color = Color(0xFF94A3B8),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }

            Button(
              onClick = onExitWebPortal,
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
              shape = RoundedCornerShape(6.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
              modifier = Modifier.testTag("btn_return_mobile_app")
            ) {
              Icon(Icons.Default.Smartphone, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Switch to Mobile View", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        // Web Portal Header
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Storefront, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Column {
              Text(
                text = "COMMUNITY STORE WEB PORTAL",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              Text(
                text = "South African Higher Education Marketplace & Admin Console",
                color = Color(0xFF94A3B8),
                fontSize = 10.sp
              )
            }
          }

          if (isLoggedIn) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              RoleBadge(role = user.role)
              Text(text = user.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
          } else {
            Button(
              onClick = onLoginClick,
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text("Web SSO Login", fontSize = 12.sp)
            }
          }
        }

        // Tabs Row
        ScrollableTabRow(
          selectedTabIndex = selectedTab,
          containerColor = Color(0xFF0F172A),
          contentColor = Color.White,
          edgePadding = 16.dp
        ) {
          tabs.forEachIndexed { index, title ->
            Tab(
              selected = selectedTab == index,
              onClick = { selectedTab = index },
              text = { Text(title, fontSize = 12.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
            )
          }
        }
      }
    },
    modifier = modifier.fillMaxSize()
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Metric Summary Cards
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text("Total Active Listings", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("${products.size}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
              Text("Across 6 SA Universities", fontSize = 10.sp, color = Color(0xFF059669))
            }
          }

          Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text("Escrow Protected Vol.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("R 142,850", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
              Text("100% Zero-Loss Rate", fontSize = 10.sp, color = Color(0xFF059669))
            }
          }

          Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text("CO₂ Avoided", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("1,490 kg", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
              Text("Circular Economy", fontSize = 10.sp, color = Color(0xFF15803D))
            }
          }
        }
      }

      when (selectedTab) {
        0 -> {
          // Marketplace Catalog Table / Wide layout
          item {
            Text("Live Web Marketplace Catalog", fontWeight = FontWeight.Bold, fontSize = 16.sp)
          }

          items(products, key = { it.id }) { product ->
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onProductClick(product) }
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
              ) {
                Image(
                  painter = painterResource(id = product.imageResId),
                  contentDescription = null,
                  modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                  contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.weight(1f)) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    Text(text = product.campus.shortName, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    Text("•", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = product.category.displayName, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  }

                  Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1
                  )

                  Text(
                    text = "Seller: ${product.sellerName} (${product.sellerRole.label}) • Spot: ${product.safeMeetingSpot}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                  )
                }

                Column(horizontalAlignment = Alignment.End) {
                  ZarPriceTag(priceZar = product.priceZar, originalPriceZar = product.originalPriceZar)
                  Text("⭐ ${product.sellerRating}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }

        1 -> {
          // Escrow & Settlements
          item {
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFFB45309))
                Column {
                  Text("PayFast & SnapScan Escrow Ledger", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF92400E))
                  Text("Funds are held securely by the institutional escrow vault until the student confirms physical receipt.", fontSize = 11.sp, color = Color(0xFF78350F))
                }
              }
            }
          }

          items(orders) { order ->
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("Transaction #${order.orderId}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                  Text("Tracking: ${order.trackingCode}", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                }
                Text("Gateway: ${order.paymentMethod.title} • Handover Point: ${order.safeMeetingPoint}", fontSize = 11.sp)
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("Status: ${order.status}", color = Color(0xFF059669), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                  Text("R ${"%.2f".format(order.totalZar)}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
              }
            }
          }
        }

        2 -> {
          // Identity & CIPC Registry
          item {
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("University Domain Verification Pipeline", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(
                  "All accounts are authenticated through their institution's DNS directory:\n" +
                      "• UCT: @uct.ac.za\n" +
                      "• Wits: @wits.ac.za\n" +
                      "• Stellenbosch: @sun.ac.za\n" +
                      "• Pretoria: @up.ac.za\n" +
                      "• Vendors: South African CIPC Registry API check against valid company registration number.",
                  fontSize = 12.sp,
                  lineHeight = 18.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }

        3 -> {
          // Campus Safety & CCTV
          item {
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("CCTV Monitored Safe Exchange Stations", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(
                  "Physical meetups are permitted strictly at designated safe zones staffed by Campus Protection Services (CPS):\n" +
                      "1. UCT Upper Campus: Chancellor Oppenheimer Library Security Foyer\n" +
                      "2. Wits East Campus: Wartenweiler Library Entrance Desk\n" +
                      "3. Stellenbosch: Neelsie Student Centre Security Kiosk\n" +
                      "4. UP Hatfield: Student Union Quad (Camera Hub #4)\n" +
                      "5. UJ Auckland Park: Sanlam Student Plaza Security Station",
                  fontSize = 12.sp,
                  lineHeight = 18.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }
  }
}
