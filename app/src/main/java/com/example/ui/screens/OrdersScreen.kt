package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.OrderRecord

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
  orders: List<OrderRecord>,
  onBrowseMarketplace: () -> Unit,
  modifier: Modifier = Modifier
) {
  var releasedOrderIds by remember { mutableStateOf(setOf<String>()) }
  var showReleaseSuccessDialog by remember { mutableStateOf<String?>(null) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text("Orders & Escrow Protection", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Track campus handovers & payment security", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    modifier = modifier
  ) { innerPadding ->
    if (orders.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Icon(
            imageVector = Icons.Default.ReceiptLong,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text("No Active Orders", fontWeight = FontWeight.Bold, fontSize = 18.sp)
          Text(
            "When you buy textbooks, dorm gear, or produce, your funds are safely held in escrow until meetup.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
          Button(onClick = onBrowseMarketplace, shape = RoundedCornerShape(12.dp)) {
            Text("Explore Store")
          }
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        items(orders, key = { it.orderId }) { order ->
          val isReleased = releasedOrderIds.contains(order.orderId)
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("order_card_${order.orderId}")
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              // Header
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(text = "Order #${order.orderId}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                  Text(text = "Placed: ${order.date}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = if (isReleased) Color(0xFFDCFCE7) else MaterialTheme.colorScheme.primaryContainer
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Icon(
                      imageVector = if (isReleased) Icons.Default.CheckCircle else Icons.Default.Security,
                      contentDescription = null,
                      tint = if (isReleased) Color(0xFF15803D) else MaterialTheme.colorScheme.onPrimaryContainer,
                      modifier = Modifier.size(14.dp)
                    )
                    Text(
                      text = if (isReleased) "Funds Released" else order.status,
                      color = if (isReleased) Color(0xFF15803D) else MaterialTheme.colorScheme.onPrimaryContainer,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }

              HorizontalDivider()

              // Items summary
              order.items.forEach { cartItem ->
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(12.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Image(
                    painter = painterResource(id = cartItem.product.imageResId),
                    contentDescription = null,
                    modifier = Modifier
                      .size(48.dp)
                      .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                  )
                  Column(modifier = Modifier.weight(1f)) {
                    Text(text = cartItem.product.title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, maxLines = 1)
                    Text(text = "Qty: ${cartItem.quantity} • Seller: ${cartItem.product.sellerName}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  }
                  Text(
                    text = "R ${"%.2f".format(cartItem.product.priceZar * cartItem.quantity)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                  )
                }
              }

              // Meeting Spot & Payment Info
              Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
              ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                    Text(text = "Safe Meetup Point: ${order.safeMeetingPoint}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                  }
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "Payment: ${order.paymentMethod.title} • Escrow Code: ${order.trackingCode}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  }
                }
              }

              // Release Escrow Action
              if (!isReleased) {
                Button(
                  onClick = {
                    releasedOrderIds = releasedOrderIds + order.orderId
                    showReleaseSuccessDialog = order.orderId
                  },
                  shape = RoundedCornerShape(12.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                  modifier = Modifier.fillMaxWidth().testTag("btn_release_escrow_${order.orderId}")
                ) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("Confirm Item Received & Release Escrow", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
              } else {
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = Color(0xFFDCFCE7),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text(
                    text = "✓ Transaction completed! Funds transferred to seller. Both parties earned trust rating.",
                    color = Color(0xFF15803D),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(8.dp)
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  // Release confirmation dialog
  if (showReleaseSuccessDialog != null) {
    AlertDialog(
      onDismissRequest = { showReleaseSuccessDialog = null },
      icon = { Icon(Icons.Default.Celebration, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(32.dp)) },
      title = { Text("Escrow Funds Released!") },
      text = {
        Text("Thank you for confirming your campus handover! The seller has received payment into their account via PayFast/SnapScan, and your community credibility rating increased.")
      },
      confirmButton = {
        Button(onClick = { showReleaseSuccessDialog = null }) {
          Text("Done")
        }
      }
    )
  }
}
