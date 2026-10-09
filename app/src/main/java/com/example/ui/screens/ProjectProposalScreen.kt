package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CommunityStoreProposal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectProposalScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }
  var selectedTab by remember { mutableStateOf(0) }
  val tabTitles = listOf("Overview & Scope", "3 Phases & Timeline", "Roles & Team", "Challenges & Solutions")

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text("Community Store Project Proposal", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("Academic & Technical Specification", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_proposal")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    modifier = modifier
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      // Tab Row
      PrimaryScrollableTabRow(
        selectedTabIndex = selectedTab,
        edgePadding = 16.dp,
        containerColor = MaterialTheme.colorScheme.surface
      ) {
        tabTitles.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = { Text(title, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal, fontSize = 13.sp) }
          )
        }
      }

      LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        when (selectedTab) {
          0 -> {
            // Overview, Scope & Technical Architecture
            item {
              Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
              ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                  Text(text = "1. Executive Summary", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                  Text(text = CommunityStoreProposal.summary, fontSize = 13.sp, lineHeight = 19.sp)
                }
              }
            }

            item {
              Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
              ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                  Text(text = "2. Project Scope & Target Users", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                  Text(
                    text = "• Purpose: Create a scalable, secure, and user-friendly community marketplace.\n" +
                        "• Target Users: University students, academic faculty, local registered vendors, and campus neighborhood residents.\n" +
                        "• Core Features: Multi-role authentication (.ac.za verification), product listings, search & filters, cart & checkout, PayFast & SnapScan escrow, notifications, trust ratings, and campus bulletin board.",
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }

            item {
              Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
              ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                  Text(text = "3. Technical Architecture & Security", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                  Text(
                    text = "• Frontend: Native Jetpack Compose Android client with responsive WindowSizeClasses and Material 3 design system.\n" +
                        "• Backend: Reactive microservices architecture with HTTPS end-to-end encryption and Redis caching.\n" +
                        "• Payments: PayFast Instant EFT & SnapScan QR payment gateways with campus escrow protection.\n" +
                        "• Infrastructure: Cloud hosting (AWS/GCP), fraud detection pattern algorithms, and zero-trust rating validation.",
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }

          1 -> {
            // Timeline & 3 Phases
            item {
              Text(
                text = "Project Timeline (6 to 7 Months) & Deliverables",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }

            items(CommunityStoreProposal.phases) { phase ->
              Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
              ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(text = phase.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                    Surface(shape = RoundedCornerShape(6.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                      Text(text = phase.months, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                    }
                  }

                  Text(text = "Key Objectives:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                  phase.objectives.forEach { obj ->
                    Text(text = "• $obj", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 17.sp)
                  }

                  Text(text = "Deliverables:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                  phase.deliverables.forEach { deliv ->
                    Text(text = "✓ $deliv", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, lineHeight = 17.sp)
                  }
                }
              }
            }
          }

          2 -> {
            // Roles & Responsibilities
            item {
              Text(
                text = "Main Roles & Responsibilities",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }

            items(CommunityStoreProposal.teamRoles) { role ->
              Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                  horizontalArrangement = Arrangement.spacedBy(12.dp),
                  verticalAlignment = Alignment.Top
                ) {
                  Box(
                    modifier = Modifier
                      .size(40.dp)
                      .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = when (role.icon) {
                        "assignment_ind" -> Icons.Default.AssignmentInd
                        "dns" -> Icons.Default.Dns
                        "smartphone" -> Icons.Default.PhoneAndroid
                        "fact_check" -> Icons.Default.FactCheck
                        "security" -> Icons.Default.Security
                        else -> Icons.Default.Handshake
                      },
                      contentDescription = null,
                      tint = MaterialTheme.colorScheme.onPrimaryContainer,
                      modifier = Modifier.size(20.dp)
                    )
                  }

                  Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = role.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = role.responsibilities, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 17.sp)
                  }
                }
              }
            }
          }

          3 -> {
            // Challenges & Solutions (Section 8 of brief)
            item {
              Text(
                text = "8. Challenges & Comprehensive Solutions",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }

            items(CommunityStoreProposal.challengesAndSolutions) { item ->
              Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
              ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                  Text(text = item.challengeTitle, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)

                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                  ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                      Text(text = "Challenge:", fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = MaterialTheme.colorScheme.error)
                      Text(text = item.problemDescription, fontSize = 12.sp, color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                  }

                  Text(text = "Engineered Solutions:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                  item.solutions.forEach { sol ->
                    Row(
                      horizontalArrangement = Arrangement.spacedBy(6.dp),
                      verticalAlignment = Alignment.Top
                    ) {
                      Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(16.dp).padding(top = 2.dp))
                      Text(text = sol, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 17.sp)
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
