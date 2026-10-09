package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UniversityCampus
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.example.ui.components.RoleBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
  user: UserProfile,
  onRoleSwitch: (UserRole) -> Unit,
  onCampusChange: (UniversityCampus) -> Unit,
  onOpenProposal: () -> Unit,
  onOpenWebPortal: () -> Unit,
  onLogout: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()
  var showCampusDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Profile & Identity", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    modifier = modifier
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(scrollState)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Profile Header Card
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = user.name.split(" ").map { it.take(1) }.joinToString(""),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
              )
            }

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = user.name,
                  fontWeight = FontWeight.Bold,
                  fontSize = 17.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                  imageVector = Icons.Default.Verified,
                  contentDescription = "Verified",
                  tint = Color(0xFF059669),
                  modifier = Modifier.size(18.dp)
                )
              }
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = user.email,
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (user.role == UserRole.STUDENT) {
                  Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFDCFCE7)
                  ) {
                    Text(
                      text = "🎓 Student Email",
                      fontSize = 9.sp,
                      color = Color(0xFF15803D),
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                  }
                }
              }
              Text(
                text = "ID: ${user.studentOrBizId} • ${user.campus.fullName}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            RoleBadge(role = user.role)
            Text(
              text = "⭐ ${user.reputationScore} Trust Rating",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      // Multi-Role Switcher (Demonstrating Multi-role accounts from proposal: Student, Faculty, Vendor, Resident)
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Text(
            text = "Switch Account Role (Demo Ecosystem)",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
          Text(
            text = "Test permissions as a Verified Student, Faculty, Registered Local Vendor, or Campus Resident.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            UserRole.values().forEach { role ->
              val isSelected = user.role == role
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { onRoleSwitch(role) }
                  .testTag("role_switch_${role.name}")
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                  RadioButton(
                    selected = isSelected,
                    onClick = { onRoleSwitch(role) }
                  )
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = role.label,
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp
                    )
                    Text(
                      text = when (role) {
                        UserRole.STUDENT -> "Authorized with student .ac.za email for textbook/dorm trades"
                        UserRole.FACULTY -> "University staff badge for academic resources & notices"
                        UserRole.VENDOR -> "CIPC registered business for market produce & retail goods"
                        UserRole.RESIDENT -> "Local community member living near university campus"
                      },
                      fontSize = 11.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }
              }
            }
          }
        }
      }

      // Campus Location Selector
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.clickable { showCampusDialog = true }
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = "Primary Campus Location", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = "${user.campus.fullName} (${user.campus.city})", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          Icon(Icons.Default.ChevronRight, contentDescription = null)
        }
      }

      // Eco-Impact Tracker & Sustainability Dashboard
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7))
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Icon(Icons.Default.Eco, contentDescription = null, tint = Color(0xFF15803D), modifier = Modifier.size(20.dp))
              Text(
                text = "Eco & Sustainability Impact",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF15803D)
              )
            }
            Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF15803D)) {
              Text(
                text = "Level 3 Green Champion",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
              )
            }
          }

          Text(
            text = "Every pre-loved textbook, dorm appliance, and local farm purchase keeps items out of landfill and cuts transport emissions.",
            fontSize = 11.sp,
            color = Color(0xFF166534)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "${user.ecoScoreKg} kg", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF15803D))
              Text(text = "CO₂ Avoided", fontSize = 11.sp, color = Color(0xFF166534))
            }
            Column {
              Text(text = "R ${"%.0f".format(user.totalSavedZar)}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF15803D))
              Text(text = "Community Savings", fontSize = 11.sp, color = Color(0xFF166534))
            }
            Column {
              Text(text = "${user.activeListings}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF15803D))
              Text(text = "Active Listings", fontSize = 11.sp, color = Color(0xFF166534))
            }
          }
        }
      }

      // Project Proposal Roadmap Button
      Button(
        onClick = onOpenProposal,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("btn_profile_view_proposal")
      ) {
        Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("View 7-Month Project Proposal & Specs", fontWeight = FontWeight.Bold)
      }

      // Switch to Web Portal Button
      FilledTonalButton(
        onClick = onOpenWebPortal,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("btn_profile_web_portal")
      ) {
        Icon(Icons.Default.DesktopWindows, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("🖥️ Open Campus Web Portal & Admin View", fontWeight = FontWeight.SemiBold)
      }

      // Sign Out / Switch Identity Button
      OutlinedButton(
        onClick = onLogout,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("btn_logout")
      ) {
        Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Sign Out of Account", fontWeight = FontWeight.Bold)
      }
    }
  }

  // Campus Selector Dialog
  if (showCampusDialog) {
    AlertDialog(
      onDismissRequest = { showCampusDialog = false },
      title = { Text("Select Your University Campus") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          UniversityCampus.values().forEach { campus ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  onCampusChange(campus)
                  showCampusDialog = false
                }
                .padding(vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(selected = user.campus == campus, onClick = {
                onCampusChange(campus)
                showCampusDialog = false
              })
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(text = campus.fullName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Text(text = campus.city, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showCampusDialog = false }) {
          Text("Close")
        }
      }
    )
  }
}
