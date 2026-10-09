package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.data.model.*
import com.example.ui.components.EcoBadge
import com.example.ui.components.ZarPriceTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartAndCheckoutScreen(
  cartItems: List<CartItem>,
  onUpdateQuantity: (String, Int) -> Unit,
  onRemoveItem: (String) -> Unit,
  onCheckout: (PaymentMethod, DeliveryMode, String) -> Unit,
  onBrowseMarketplace: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedPayment by remember { mutableStateOf(PaymentMethod.ESCROW) }
  var selectedDelivery by remember { mutableStateOf(DeliveryMode.SAFE_ZONE_MEETUP) }
  var meetingSpot by remember { mutableStateOf("Chancellor Oppenheimer Library Foyer (Upper Campus)") }

  val subtotal = cartItems.sumOf { it.product.priceZar * it.quantity }
  val grandTotal = subtotal + selectedDelivery.feeZar
  val totalCo2Saved = cartItems.sumOf { it.product.ecoCo2SavedKg * it.quantity }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Shopping Cart & Checkout", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    bottomBar = {
      if (cartItems.isNotEmpty()) {
        Surface(
          tonalElevation = 8.dp,
          shadowElevation = 8.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .windowInsetsPadding(WindowInsets.navigationBars)
              .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(text = "Total to Pay", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                  text = "R ${"%.2f".format(grandTotal)}",
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
              }

              if (totalCo2Saved > 0) {
                EcoBadge(co2SavedKg = totalCo2Saved)
              }
            }

            Button(
              onClick = {
                onCheckout(selectedPayment, selectedDelivery, meetingSpot)
              },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("btn_complete_checkout")
            ) {
              Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Place Order • R ${"%.2f".format(grandTotal)}",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }
          }
        }
      }
    },
    modifier = modifier
  ) { innerPadding ->
    if (cartItems.isEmpty()) {
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
            imageVector = Icons.Default.RemoveShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "Your Cart is Empty",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
          Text(
            text = "Explore textbooks, dorm tech, and local farm goods from fellow students and vendors.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
          Button(
            onClick = onBrowseMarketplace,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(top = 8.dp)
          ) {
            Text("Browse Campus Marketplace")
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
        // Cart Items
        item {
          Text(
            text = "Items in Cart (${cartItems.size})",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
        }

        items(cartItems, key = { it.product.id }) { item ->
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.spacedBy(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Image(
                painter = painterResource(id = item.product.imageResId),
                contentDescription = item.product.title,
                modifier = Modifier
                  .size(72.dp)
                  .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
              )

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = item.product.title,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  maxLines = 2
                )
                Text(
                  text = "R ${"%.2f".format(item.product.priceZar)}",
                  color = MaterialTheme.colorScheme.primary,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 14.sp
                )
                Text(
                  text = "Seller: ${item.product.sellerName}",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              // Quantity Controls
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                IconButton(
                  onClick = { onUpdateQuantity(item.product.id, -1) },
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                }
                Text(
                  text = "${item.quantity}",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
                IconButton(
                  onClick = { onUpdateQuantity(item.product.id, 1) },
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                }
              }
            }
          }
        }

        // Delivery Mode Selection
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
          ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
              Text(
                text = "Collection / Delivery Option",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              DeliveryMode.values().forEach { mode ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { selectedDelivery = mode }
                    .padding(8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                  RadioButton(
                    selected = selectedDelivery == mode,
                    onClick = { selectedDelivery = mode }
                  )
                  Column(modifier = Modifier.weight(1f)) {
                    Text(text = mode.label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text(text = mode.detail, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  }
                  Text(
                    text = if (mode.feeZar == 0.0) "FREE" else "R ${"%.2f".format(mode.feeZar)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (mode.feeZar == 0.0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                  )
                }
              }
            }
          }
        }

        // Safe Meeting Point Input
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
          ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFFB45309), modifier = Modifier.size(18.dp))
                Text(
                  text = "Designated Campus Meeting Zone",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = Color(0xFF92400E)
                )
              }
              OutlinedTextField(
                value = meetingSpot,
                onValueChange = { meetingSpot = it },
                label = { Text("Meeting Location") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
              )
              Text(
                text = "CCTV covered zone with campus security patrolling. Always confirm time with seller.",
                fontSize = 11.sp,
                color = Color(0xFF92400E)
              )
            }
          }
        }

        // South African Payment Gateways
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
          ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
              Text(
                text = "South African Payment Gateway",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              PaymentMethod.values().forEach { method ->
                val isSelected = selectedPayment == method
                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else Color.Transparent,
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedPayment = method }
                ) {
                  Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                  ) {
                    RadioButton(
                      selected = isSelected,
                      onClick = { selectedPayment = method }
                    )
                    Column(modifier = Modifier.weight(1f)) {
                      Text(text = method.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                      Text(text = method.subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                  }
                }
              }
            }
          }
        }

        // Order Cost Breakdown
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
          ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
              Text(text = "Summary", fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Subtotal (${cartItems.sumOf { it.quantity }} items)", fontSize = 13.sp)
                Text(text = "R ${"%.2f".format(subtotal)}", fontSize = 13.sp)
              }
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Delivery / Locker fee", fontSize = 13.sp)
                Text(text = if (selectedDelivery.feeZar == 0.0) "FREE" else "R ${"%.2f".format(selectedDelivery.feeZar)}", fontSize = 13.sp)
              }
              HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Total Amount", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = "R ${"%.2f".format(grandTotal)}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
              }
            }
          }
        }
      }
    }
  }
}
