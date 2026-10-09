package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProductItem

@Composable
fun ProductCard(
  product: ProductItem,
  onProductClick: (ProductItem) -> Unit,
  onAddToCart: (ProductItem) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("product_card_${product.id}")
      .clickable { onProductClick(product) },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column {
      // Product Image with Overlays
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(145.dp)
      ) {
        Image(
          painter = painterResource(id = product.imageResId),
          contentDescription = product.title,
          modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
          contentScale = ContentScale.Crop
        )

        // Top Badges
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Top
        ) {
          CampusBadge(campus = product.campus)
          if (product.isEcoFriendly) {
            EcoBadge(co2SavedKg = product.ecoCo2SavedKg)
          }
        }
      }

      // Details
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          ConditionBadge(condition = product.condition)
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.secondary,
              modifier = Modifier.size(14.dp)
            )
            Text(
              text = "%.1f".format(product.sellerRating),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        Text(
          text = product.title,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          lineHeight = 18.sp,
          color = MaterialTheme.colorScheme.onSurface
        )

        Text(
          text = "By ${product.sellerName}",
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          ZarPriceTag(
            priceZar = product.priceZar,
            originalPriceZar = product.originalPriceZar
          )

          FilledIconButton(
            onClick = { onAddToCart(product) },
            modifier = Modifier
              .size(36.dp)
              .testTag("add_to_cart_${product.id}"),
            colors = IconButtonDefaults.filledIconButtonColors(
              containerColor = MaterialTheme.colorScheme.primaryContainer,
              contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
          ) {
            Icon(
              imageVector = Icons.Default.AddShoppingCart,
              contentDescription = "Add to cart",
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}
