package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ItemCondition
import com.example.data.model.UniversityCampus
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun RoleBadge(
  role: UserRole,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor, icon) = when (role) {
    UserRole.STUDENT -> Triple(Color(0xFFE0F2FE), Color(0xFF0369A1), Icons.Default.School)
    UserRole.FACULTY -> Triple(Color(0xFFF3E8FF), Color(0xFF6B21A8), Icons.Default.VerifiedUser)
    UserRole.VENDOR -> Triple(Color(0xFFDCFCE7), Color(0xFF15803D), Icons.Default.Storefront)
    UserRole.RESIDENT -> Triple(Color(0xFFFEF3C7), Color(0xFFB45309), Icons.Default.Home)
  }

  Surface(
    shape = RoundedCornerShape(12.dp),
    color = bgColor,
    modifier = modifier
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = textColor,
        modifier = Modifier.size(13.dp)
      )
      Text(
        text = role.label,
        color = textColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}

@Composable
fun ConditionBadge(
  condition: ItemCondition,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(6.dp),
    color = MaterialTheme.colorScheme.surfaceVariant,
    modifier = modifier
  ) {
    Text(
      text = condition.displayName,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      fontSize = 11.sp,
      fontWeight = FontWeight.Medium,
      modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
    )
  }
}

@Composable
fun CampusBadge(
  campus: UniversityCampus,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = MaterialTheme.colorScheme.secondaryContainer,
    modifier = modifier
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
      Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier.size(12.dp)
      )
      Text(
        text = campus.shortName,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
fun ZarPriceTag(
  priceZar: Double,
  originalPriceZar: Double? = null,
  modifier: Modifier = Modifier
) {
  Row(
    verticalAlignment = Alignment.Bottom,
    horizontalArrangement = Arrangement.spacedBy(6.dp),
    modifier = modifier
  ) {
    Text(
      text = "R ${"%.2f".format(priceZar)}",
      color = MaterialTheme.colorScheme.primary,
      fontSize = 18.sp,
      fontWeight = FontWeight.Bold
    )
    if (originalPriceZar != null && originalPriceZar > priceZar) {
      Text(
        text = "R ${"%.2f".format(originalPriceZar)}",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 13.sp,
        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
      )
    }
  }
}

@Composable
fun EcoBadge(
  co2SavedKg: Double,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = Color(0xFFDCFCE7),
    modifier = modifier
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Eco,
        contentDescription = null,
        tint = Color(0xFF15803D),
        modifier = Modifier.size(12.dp)
      )
      Text(
        text = "-${co2SavedKg}kg CO₂",
        color = Color(0xFF15803D),
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
fun TrustShieldBanner(
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.GppGood,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onPrimary,
          modifier = Modifier.size(20.dp)
        )
      }
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "Mzansi Campus Trust Guarantee",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
          text = "Verified student/vendor IDs • PayFast & SnapScan Escrow • CCTV Safe Zones",
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
          lineHeight = 14.sp
        )
      }
    }
  }
}
