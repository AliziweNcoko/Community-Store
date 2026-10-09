package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.R
import com.example.data.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingDialog(
  currentCampus: UniversityCampus,
  onDismiss: () -> Unit,
  onSubmit: (String, String, Double, ProductCategory, ItemCondition, UniversityCampus, String, Boolean, Int) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var priceInput by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf(ProductCategory.TEXTBOOKS) }
  var selectedCondition by remember { mutableStateOf(ItemCondition.LIKE_NEW) }
  var selectedCampus by remember { mutableStateOf(currentCampus) }
  var safeSpot by remember { mutableStateOf("Library Security Foyer") }
  var isEcoFriendly by remember { mutableStateOf(true) }
  var selectedImageRes by remember { mutableStateOf(R.drawable.img_textbooks_academic) }

  val imageOptions = listOf(
    R.drawable.img_textbooks_academic to "Textbooks & Notes",
    R.drawable.img_dorm_electronics to "Dorm Gadgets",
    R.drawable.img_community_produce to "Market Produce",
    R.drawable.img_sa_campus_banner to "Campus Services"
  )

  AlertDialog(
    onDismissRequest = onDismiss,
    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
    title = {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(Icons.Default.AddBusiness, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text("List an Item for Sale / Trade", fontWeight = FontWeight.Bold, fontSize = 17.sp)
      }
    },
    text = {
      val scrollState = rememberScrollState()
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Title (e.g. MAM1000W Calculus 8th Ed)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("input_listing_title")
        )

        OutlinedTextField(
          value = priceInput,
          onValueChange = { priceInput = it.filter { ch -> ch.isDigit() || ch == '.' } },
          label = { Text("Price in Rands (ZAR)") },
          prefix = { Text("R ") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("input_listing_price")
        )

        // Select Photo
        Text(text = "Select Photo Asset:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
        Row(
          modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          imageOptions.forEach { (resId, label) ->
            val isSelected = selectedImageRes == resId
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { selectedImageRes = resId }
                .padding(2.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(64.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                  .padding(if (isSelected) 3.dp else 0.dp)
              ) {
                Image(
                  painter = painterResource(id = resId),
                  contentDescription = label,
                  modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(6.dp)),
                  contentScale = ContentScale.Crop
                )
              }
              Text(text = label, fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
            }
          }
        }

        // Category dropdown
        var catExpanded by remember { mutableStateOf(false) }
        Box {
          OutlinedButton(
            onClick = { catExpanded = true },
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Category: ${selectedCategory.displayName}")
          }
          DropdownMenu(expanded = catExpanded, onDismissRequest = { catExpanded = false }) {
            ProductCategory.values().filter { it != ProductCategory.ALL }.forEach { cat ->
              DropdownMenuItem(
                text = { Text(cat.displayName) },
                onClick = {
                  selectedCategory = cat
                  catExpanded = false
                }
              )
            }
          }
        }

        // Condition selector
        var condExpanded by remember { mutableStateOf(false) }
        Box {
          OutlinedButton(
            onClick = { condExpanded = true },
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Condition: ${selectedCondition.displayName}")
          }
          DropdownMenu(expanded = condExpanded, onDismissRequest = { condExpanded = false }) {
            ItemCondition.values().forEach { cond ->
              DropdownMenuItem(
                text = { Text(cond.displayName) },
                onClick = {
                  selectedCondition = cond
                  condExpanded = false
                }
              )
            }
          }
        }

        OutlinedTextField(
          value = safeSpot,
          onValueChange = { safeSpot = it },
          label = { Text("Safe Exchange Location (CCTV Monitored)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Description & Condition notes") },
          maxLines = 3,
          modifier = Modifier.fillMaxWidth()
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = "Eco-Friendly / Pre-loved Item", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
          Switch(checked = isEcoFriendly, onCheckedChange = { isEcoFriendly = it })
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val price = priceInput.toDoubleOrNull() ?: 100.0
          if (title.isNotBlank()) {
            onSubmit(
              title,
              description.ifBlank { "Pre-owned item in great condition, verified by community student seller." },
              price,
              selectedCategory,
              selectedCondition,
              selectedCampus,
              safeSpot,
              isEcoFriendly,
              selectedImageRes
            )
          }
        },
        enabled = title.isNotBlank() && priceInput.isNotBlank(),
        modifier = Modifier.testTag("btn_submit_listing")
      ) {
        Text("Publish Listing")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

@Composable
fun AddBulletinPostDialog(
  onDismiss: () -> Unit,
  onSubmit: (String, String, PostType, String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }
  var selectedType by remember { mutableStateOf(PostType.EVENT) }
  var tag by remember { mutableStateOf("Campus") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Post Community Notice / Event", fontWeight = FontWeight.Bold, fontSize = 17.sp) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Post Title") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("input_bulletin_title")
        )

        var typeExpanded by remember { mutableStateOf(false) }
        Box {
          OutlinedButton(onClick = { typeExpanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Type: ${selectedType.label}")
          }
          DropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
            PostType.values().forEach { t ->
              DropdownMenuItem(
                text = { Text(t.label) },
                onClick = {
                  selectedType = t
                  typeExpanded = false
                }
              )
            }
          }
        }

        OutlinedTextField(
          value = tag,
          onValueChange = { tag = it },
          label = { Text("Tag (e.g. Study Group, Fundraiser, Eco)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          label = { Text("Notice description") },
          maxLines = 4,
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank() && content.isNotBlank()) {
            onSubmit(title, content, selectedType, tag)
          }
        },
        enabled = title.isNotBlank() && content.isNotBlank(),
        modifier = Modifier.testTag("btn_submit_post")
      ) {
        Text("Publish Post")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

@Composable
fun OrderSuccessDialog(
  order: OrderRecord,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Icon(Icons.Default.Security, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
    },
    title = { Text("Order Placed & Escrow Locked!", fontWeight = FontWeight.Bold) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Order Number: ${order.orderId}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = "Escrow Code: ${order.trackingCode}", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
        Text(
          text = "Your payment of R ${"%.2f".format(order.totalZar)} is securely protected. The seller will meet you at '${order.safeMeetingPoint}'. Funds will only be released once you inspect your items and tap Confirm.",
          fontSize = 12.sp,
          lineHeight = 17.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    },
    confirmButton = {
      Button(onClick = onDismiss) {
        Text("View in Orders")
      }
    }
  )
}
