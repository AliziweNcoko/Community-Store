package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UniversityCampus
import com.example.data.model.UserRole

enum class DeviceLayoutMode(val label: String, val icon: String) {
  AUTO("Auto Detect", "sync"),
  LAPTOP("Laptop / Desktop", "laptop"),
  PHONE("Phone / Mobile", "smartphone")
}

data class MarketPictureItem(
  val resId: Int,
  val tag: String,
  val title: String,
  val subtitle: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
  onLoginSuccess: (name: String, email: String, studentOrBizId: String, role: UserRole, campus: UniversityCampus) -> Unit,
  onOpenWebPortal: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isSignUp by remember { mutableStateOf(false) }
  var studentEmail by remember { mutableStateOf("n.khumalo@myuct.ac.za") }
  var studentName by remember { mutableStateOf("Nandi Khumalo") }
  var studentPassword by remember { mutableStateOf("Student2026!") }
  var passwordVisible by remember { mutableStateOf(false) }
  var studentNumber by remember { mutableStateOf("KHMNDI004") }
  var selectedCampus by remember { mutableStateOf(UniversityCampus.UCT) }
  var showCampusDropdown by remember { mutableStateOf(false) }

  // Device Layout Mode switcher: allows testing Laptop or Phone mode directly
  var deviceMode by remember { mutableStateOf(DeviceLayoutMode.AUTO) }

  // Outlook Verification PIN flow state
  var isWaitingForOutlookPin by remember { mutableStateOf(false) }
  var enteredPin by remember { mutableStateOf("") }
  var generatedPin by remember { mutableStateOf("849201") }
  var showOutlookNotification by remember { mutableStateOf(false) }
  var showOutlookModal by remember { mutableStateOf(false) }
  var pinErrorMessage by remember { mutableStateOf<String?>(null) }
  var resendCountdown by remember { mutableStateOf(30) }

  // Market pictures gallery
  val marketPictures = remember {
    listOf(
      MarketPictureItem(
        resId = R.drawable.img_sa_craft_market,
        tag = "🇿🇦 Craft & Artisan Market",
        title = "Handmade Crafts & Student Creations",
        subtitle = "Beadwork, leather accessories, and student startup merchandise on campus."
      ),
      MarketPictureItem(
        resId = R.drawable.img_sa_food_market,
        tag = "🔥 Campus Braai & Food Stalls",
        title = "Fresh Hot Meals & Pastries",
        subtitle = "Boerewors rolls, samosas, koeksisters, and artisanal baked breads."
      ),
      MarketPictureItem(
        resId = R.drawable.img_sa_flea_market,
        tag = "🌿 Thrift & Flea Fair",
        title = "Sustainable Vintage Fashion",
        subtitle = "Campus flea market with pre-loved clothes, dorm decor, and zero waste."
      ),
      MarketPictureItem(
        resId = R.drawable.img_community_produce,
        tag = "🥑 Farm Fresh & Biltong",
        title = "Direct From Local Micro-Farmers",
        subtitle = "Organic farm avocados, Cape wildflower honey, and Karoo droëwors."
      ),
      MarketPictureItem(
        resId = R.drawable.img_sa_campus_banner,
        tag = "🏛️ University Campus Quad",
        title = "Vibrant Jacaranda Walkway Trade",
        subtitle = "Safe student commerce under sunny South African campus trees."
      ),
      MarketPictureItem(
        resId = R.drawable.img_textbooks_academic,
        tag = "📚 Academic Textbooks",
        title = "Past Exam Papers & Prescribed Books",
        subtitle = "Save up to 70% on Juta, LexisNexis, and MAM1000W study bundles."
      )
    )
  }
  var selectedPictureIndex by remember { mutableStateOf(0) }

  // Auto-detect campus from email domain
  LaunchedEffect(studentEmail) {
    val lower = studentEmail.lowercase()
    when {
      lower.contains("uct.ac.za") -> selectedCampus = UniversityCampus.UCT
      lower.contains("wits.ac.za") -> selectedCampus = UniversityCampus.WITS
      lower.contains("sun.ac.za") -> selectedCampus = UniversityCampus.STELLENBOSCH
      lower.contains("up.ac.za") || lower.contains("tuks.co.za") -> selectedCampus = UniversityCampus.UP
      lower.contains("uj.ac.za") -> selectedCampus = UniversityCampus.UJ
      lower.contains("ru.ac.za") -> selectedCampus = UniversityCampus.RHODES
    }
  }

  val isStudentEmailValid = remember(studentEmail) {
    studentEmail.contains("@") && (studentEmail.contains(".ac.za") || studentEmail.contains("tuks.co.za") || studentEmail.contains(".edu") || studentEmail.contains("@outlook.com"))
  }

  val quickDomains = listOf(
    "@myuct.ac.za" to UniversityCampus.UCT,
    "@students.wits.ac.za" to UniversityCampus.WITS,
    "@sun.ac.za" to UniversityCampus.STELLENBOSCH,
    "@tuks.co.za" to UniversityCampus.UP,
    "@student.uj.ac.za" to UniversityCampus.UJ,
    "@campus.ru.ac.za" to UniversityCampus.RHODES
  )

  fun triggerSendOutlookPin() {
    isWaitingForOutlookPin = true
    generatedPin = (100000..999999).random().toString()
    showOutlookNotification = true
    showOutlookModal = true
    pinErrorMessage = null
    resendCountdown = 30
  }

  fun verifyPinAndLogin() {
    if (enteredPin.trim() == generatedPin.trim()) {
      pinErrorMessage = null
      onLoginSuccess(
        studentName.ifBlank { "Verified Student" },
        studentEmail,
        studentNumber.ifBlank { "STU-1004" },
        UserRole.STUDENT,
        selectedCampus
      )
    } else {
      pinErrorMessage = "Incorrect PIN '$enteredPin'. Please check your Outlook inbox."
    }
  }

  BoxWithConstraints(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    val screenWide = maxWidth >= 720.dp
    val effectiveIsWide = when (deviceMode) {
      DeviceLayoutMode.AUTO -> screenWide
      DeviceLayoutMode.LAPTOP -> true
      DeviceLayoutMode.PHONE -> false
    }

    Column(modifier = Modifier.fillMaxSize()) {
      // Top Device Mode & Outlook Bar
      Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
              text = "Layout Mode:",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            DeviceLayoutMode.values().forEach { mode ->
              FilterChip(
                selected = deviceMode == mode,
                onClick = { deviceMode = mode },
                label = { Text(mode.label, fontSize = 10.sp) },
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(28.dp)
              )
            }
          }

          if (isWaitingForOutlookPin) {
            FilledTonalButton(
              onClick = { showOutlookModal = true },
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
              modifier = Modifier.height(28.dp),
              colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = Color(0xFF0078D4),
                contentColor = Color.White
              )
            ) {
              Icon(Icons.Default.Mail, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("View Outlook Inbox", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // Top Simulated Outlook Notification Banner
      AnimatedVisibility(
        visible = showOutlookNotification,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
      ) {
        Surface(
          color = Color(0xFF0078D4), // Microsoft Outlook Blue
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              enteredPin = generatedPin
              showOutlookModal = true
            }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Mail, contentDescription = null, tint = Color(0xFF0078D4), modifier = Modifier.size(24.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Microsoft 365 Outlook • Student Webmail Inbox (1 New)",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Community Store Security PIN: $generatedPin",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
              )
              Text(
                text = "Sent to $studentEmail • Tap to open email details & autofill PIN",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 10.sp
              )
            }

            IconButton(onClick = { showOutlookNotification = false }) {
              Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color.White, modifier = Modifier.size(18.dp))
            }
          }
        }
      }

      // Main Responsive Layout
      if (effectiveIsWide) {
        // LAPTOP / DESKTOP WIDE VIEW (Side-by-Side Two Column Layout)
        Row(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          horizontalArrangement = Arrangement.spacedBy(20.dp),
          verticalAlignment = Alignment.Top
        ) {
          // Left Column: Interactive Market Pictures & Campus Showcase
          Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
              .weight(1.1f)
              .fillMaxHeight(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
          ) {
            val currentPic = marketPictures[selectedPictureIndex]
            Column(modifier = Modifier.fillMaxSize()) {
              // Featured Picture Box
              Box(
                modifier = Modifier
                  .weight(1f)
                  .fillMaxWidth()
              ) {
                Image(
                  painter = painterResource(id = currentPic.resId),
                  contentDescription = currentPic.title,
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.Crop
                )

                Box(
                  modifier = Modifier
                    .fillMaxSize()
                    .background(
                      Brush.verticalGradient(
                        colors = listOf(
                          Color.Black.copy(alpha = 0.25f),
                          Color.Black.copy(alpha = 0.85f)
                        )
                      )
                    )
                )

                Column(
                  modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                  verticalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Surface(
                      shape = RoundedCornerShape(8.dp),
                      color = MaterialTheme.colorScheme.primary
                    ) {
                      Text(
                        text = currentPic.tag,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                      )
                    }

                    Surface(
                      shape = RoundedCornerShape(8.dp),
                      color = Color.Black.copy(alpha = 0.6f)
                    ) {
                      Text(
                        text = "Photo ${selectedPictureIndex + 1}/${marketPictures.size}",
                        color = Color.White,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                      )
                    }
                  }

                  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                      text = currentPic.title,
                      color = Color.White,
                      fontWeight = FontWeight.ExtraBold,
                      fontSize = 24.sp,
                      lineHeight = 30.sp
                    )
                    Text(
                      text = currentPic.subtitle,
                      color = Color.White.copy(alpha = 0.9f),
                      fontSize = 13.sp,
                      lineHeight = 18.sp
                    )

                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                      horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                      Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.weight(1f)
                      ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                          Text("🔐 Escrow Guard", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                          Text("Funds held until meetup", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
                        }
                      }
                      Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.weight(1f)
                      ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                          Text("🛡️ Verified Campus", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                          Text(".ac.za student emails only", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
                        }
                      }
                    }
                  }
                }
              }

              // Thumbnail Selector Row at Bottom of Card
              Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Text(
                    text = "Tap to explore more market scenes:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 6.dp)
                  )
                  LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                  ) {
                    itemsIndexed(marketPictures) { index, pic ->
                      val isSelected = index == selectedPictureIndex
                      Box(
                        modifier = Modifier
                          .width(90.dp)
                          .height(60.dp)
                          .clip(RoundedCornerShape(8.dp))
                          .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                          )
                          .clickable { selectedPictureIndex = index }
                      ) {
                        Image(
                          painter = painterResource(id = pic.resId),
                          contentDescription = pic.title,
                          modifier = Modifier.fillMaxSize(),
                          contentScale = ContentScale.Crop
                        )
                      }
                    }
                  }
                }
              }
            }
          }

          // Right Column: Auth Form Card (Scrollable)
          Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
              .weight(1f)
              .fillMaxHeight(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Column(
              modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              LoginFormContent(
                isSignUp = isSignUp,
                onToggleSignUp = {
                  isSignUp = it
                  isWaitingForOutlookPin = false
                  pinErrorMessage = null
                },
                studentEmail = studentEmail,
                onEmailChange = {
                  studentEmail = it
                  pinErrorMessage = null
                },
                studentName = studentName,
                onNameChange = { studentName = it },
                studentPassword = studentPassword,
                onPasswordChange = { studentPassword = it },
                passwordVisible = passwordVisible,
                onTogglePasswordVisible = { passwordVisible = !passwordVisible },
                studentNumber = studentNumber,
                onNumberChange = { studentNumber = it },
                selectedCampus = selectedCampus,
                onCampusSelect = { selectedCampus = it },
                showCampusDropdown = showCampusDropdown,
                onToggleCampusDropdown = { showCampusDropdown = it },
                isStudentEmailValid = isStudentEmailValid,
                quickDomains = quickDomains,
                onDomainSelect = { domain, campus ->
                  val prefix = studentEmail.substringBefore("@").ifBlank { "student" }
                  studentEmail = "$prefix$domain"
                  selectedCampus = campus
                  pinErrorMessage = null
                },
                isWaitingForOutlookPin = isWaitingForOutlookPin,
                enteredPin = enteredPin,
                onPinChange = {
                  enteredPin = it
                  pinErrorMessage = null
                },
                generatedPin = generatedPin,
                pinErrorMessage = pinErrorMessage,
                onSendOutlookPin = { triggerSendOutlookPin() },
                onVerifyPin = { verifyPinAndLogin() },
                onOpenOutlookModal = { showOutlookModal = true },
                onCompleteAuth = {
                  onLoginSuccess(
                    studentName.ifBlank { "Verified Student" },
                    studentEmail,
                    studentNumber.ifBlank { "STU-1004" },
                    UserRole.STUDENT,
                    selectedCampus
                  )
                },
                onOpenWebPortal = onOpenWebPortal
              )
            }
          }
        }
      } else {
        // PHONE / MOBILE COMPACT VIEW (Single Column with scroll)
        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Mobile Market Header with Current Picture & Carousel Selector
          Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(180.dp)
          ) {
            val currentPic = marketPictures[selectedPictureIndex]
            Box(modifier = Modifier.fillMaxSize()) {
              Image(
                painter = painterResource(id = currentPic.resId),
                contentDescription = currentPic.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
              )
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .background(
                    Brush.verticalGradient(
                      colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                    )
                  )
              )

              Column(
                modifier = Modifier
                  .fillMaxSize()
                  .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primary
                  ) {
                    Text(
                      text = currentPic.tag,
                      color = Color.White,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                  }

                  Text(
                    text = "${selectedPictureIndex + 1}/${marketPictures.size}",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                  )
                }

                Column {
                  Text(
                    text = currentPic.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Text(
                    text = currentPic.subtitle,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }
            }
          }

          // Horizontal Thumbnail Strip for Phone Mode
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            itemsIndexed(marketPictures) { index, pic ->
              val isSelected = index == selectedPictureIndex
              Box(
                modifier = Modifier
                  .width(64.dp)
                  .height(44.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .border(
                    width = if (isSelected) 2.5.dp else 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                  )
                  .clickable { selectedPictureIndex = index }
              ) {
                Image(
                  painter = painterResource(id = pic.resId),
                  contentDescription = pic.title,
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.Crop
                )
              }
            }
          }

          // Phone Login Form
          LoginFormContent(
            isSignUp = isSignUp,
            onToggleSignUp = {
              isSignUp = it
              isWaitingForOutlookPin = false
              pinErrorMessage = null
            },
            studentEmail = studentEmail,
            onEmailChange = {
              studentEmail = it
              pinErrorMessage = null
            },
            studentName = studentName,
            onNameChange = { studentName = it },
            studentPassword = studentPassword,
            onPasswordChange = { studentPassword = it },
            passwordVisible = passwordVisible,
            onTogglePasswordVisible = { passwordVisible = !passwordVisible },
            studentNumber = studentNumber,
            onNumberChange = { studentNumber = it },
            selectedCampus = selectedCampus,
            onCampusSelect = { selectedCampus = it },
            showCampusDropdown = showCampusDropdown,
            onToggleCampusDropdown = { showCampusDropdown = it },
            isStudentEmailValid = isStudentEmailValid,
            quickDomains = quickDomains,
            onDomainSelect = { domain, campus ->
              val prefix = studentEmail.substringBefore("@").ifBlank { "student" }
              studentEmail = "$prefix$domain"
              selectedCampus = campus
              pinErrorMessage = null
            },
            isWaitingForOutlookPin = isWaitingForOutlookPin,
            enteredPin = enteredPin,
            onPinChange = {
              enteredPin = it
              pinErrorMessage = null
            },
            generatedPin = generatedPin,
            pinErrorMessage = pinErrorMessage,
            onSendOutlookPin = { triggerSendOutlookPin() },
            onVerifyPin = { verifyPinAndLogin() },
            onOpenOutlookModal = { showOutlookModal = true },
            onCompleteAuth = {
              onLoginSuccess(
                studentName.ifBlank { "Verified Student" },
                studentEmail,
                studentNumber.ifBlank { "STU-1004" },
                UserRole.STUDENT,
                selectedCampus
              )
            },
            onOpenWebPortal = onOpenWebPortal
          )
        }
      }
    }
  }

  // Interactive Microsoft Outlook Dialog / Webmail Inbox Modal
  if (showOutlookModal) {
    AlertDialog(
      onDismissRequest = { showOutlookModal = false },
      title = {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(RoundedCornerShape(6.dp))
              .background(Color(0xFF0078D4)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Mail, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
          }
          Column {
            Text("Microsoft 365 Outlook", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("Student Webmail • Exchange Online", fontSize = 11.sp, color = Color.Gray)
          }
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFF1F5F9),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text(
                text = "From: verification@communitystore.ac.za",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF334155)
              )
              Text(
                text = "To: $studentEmail",
                fontSize = 11.sp,
                color = Color(0xFF475569)
              )
              Text(
                text = "Subject: Your Community Store Verification PIN",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A)
              )
            }
          }

          Text(
            text = "Dumelang / Molo / Hello! Welcome to Community Store. Use the 6-digit security PIN below to verify your student account for ${selectedCampus.fullName}:",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
          )

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFE0F2FE),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0284C7)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Text(
                text = "VERIFICATION PIN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0369A1)
              )
              Text(
                text = generatedPin,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 6.sp,
                color = Color(0xFF0369A1)
              )
              Text(
                text = "Expires in 10 minutes • Do not share this code",
                fontSize = 10.sp,
                color = Color(0xFF0284C7)
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            enteredPin = generatedPin
            showOutlookModal = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0078D4))
        ) {
          Icon(Icons.Default.ContentPaste, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Autofill PIN into Box", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showOutlookModal = false }) {
          Text("Close")
        }
      }
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginFormContent(
  isSignUp: Boolean,
  onToggleSignUp: (Boolean) -> Unit,
  studentEmail: String,
  onEmailChange: (String) -> Unit,
  studentName: String,
  onNameChange: (String) -> Unit,
  studentPassword: String,
  onPasswordChange: (String) -> Unit,
  passwordVisible: Boolean,
  onTogglePasswordVisible: () -> Unit,
  studentNumber: String,
  onNumberChange: (String) -> Unit,
  selectedCampus: UniversityCampus,
  onCampusSelect: (UniversityCampus) -> Unit,
  showCampusDropdown: Boolean,
  onToggleCampusDropdown: (Boolean) -> Unit,
  isStudentEmailValid: Boolean,
  quickDomains: List<Pair<String, UniversityCampus>>,
  onDomainSelect: (String, UniversityCampus) -> Unit,
  isWaitingForOutlookPin: Boolean,
  enteredPin: String,
  onPinChange: (String) -> Unit,
  generatedPin: String,
  pinErrorMessage: String?,
  onSendOutlookPin: () -> Unit,
  onVerifyPin: () -> Unit,
  onOpenOutlookModal: () -> Unit,
  onCompleteAuth: () -> Unit,
  onOpenWebPortal: () -> Unit
) {
  // Sign In vs Sign Up Tabs
  SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
    SegmentedButton(
      selected = !isSignUp,
      onClick = { onToggleSignUp(false) },
      shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
    ) {
      Text("Student Sign In", fontWeight = FontWeight.Bold)
    }
    SegmentedButton(
      selected = isSignUp,
      onClick = { onToggleSignUp(true) },
      shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
    ) {
      Text("Sign Up (Outlook PIN)", fontWeight = FontWeight.Bold)
    }
  }

  // Outlook SSO Badge
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = if (isStudentEmailValid) Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Icon(
        imageVector = if (isStudentEmailValid) Icons.Default.Verified else Icons.Default.Info,
        contentDescription = null,
        tint = if (isStudentEmailValid) Color(0xFF15803D) else Color(0xFFB45309),
        modifier = Modifier.size(20.dp)
      )
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = if (isStudentEmailValid) "Verified University Outlook Webmail" else "Enter Student Email (.ac.za)",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = if (isStudentEmailValid) Color(0xFF15803D) else Color(0xFFB45309)
        )
        Text(
          text = if (isSignUp)
            "A 6-digit security PIN will be delivered straight to your Microsoft Outlook student inbox."
          else
            "Signed via South African university Microsoft 365 identity directory.",
          fontSize = 11.sp,
          color = if (isStudentEmailValid) Color(0xFF166534) else Color(0xFF78350F)
        )
      }
    }
  }

  // Quick Domain Chips
  Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
    Text(
      text = "Select Student Campus Domain:",
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      quickDomains.forEach { (domain, campus) ->
        SuggestionChip(
          onClick = { onDomainSelect(domain, campus) },
          label = { Text("${campus.shortName} ($domain)", fontSize = 11.sp) },
          shape = RoundedCornerShape(8.dp)
        )
      }
    }
  }

  // Student Email Input
  OutlinedTextField(
    value = studentEmail,
    onValueChange = onEmailChange,
    label = { Text("University Student Email") },
    leadingIcon = { Icon(Icons.Default.AlternateEmail, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
    trailingIcon = {
      if (isStudentEmailValid) {
        Icon(Icons.Default.CheckCircle, contentDescription = "Valid", tint = Color(0xFF059669))
      }
    },
    shape = RoundedCornerShape(12.dp),
    singleLine = true,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("input_student_email")
  )

  // In Sign Up mode, show Full Name
  AnimatedVisibility(visible = isSignUp) {
    OutlinedTextField(
      value = studentName,
      onValueChange = onNameChange,
      label = { Text("Student Full Name") },
      leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
      shape = RoundedCornerShape(12.dp),
      singleLine = true,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("input_student_name")
    )
  }

  // Password Input
  OutlinedTextField(
    value = studentPassword,
    onValueChange = onPasswordChange,
    label = { Text("Password") },
    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
    trailingIcon = {
      IconButton(onClick = onTogglePasswordVisible) {
        Icon(
          imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
          contentDescription = null
        )
      }
    },
    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
    shape = RoundedCornerShape(12.dp),
    singleLine = true,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("input_student_password")
  )

  // Campus Dropdown
  Box(modifier = Modifier.fillMaxWidth()) {
    OutlinedTextField(
      value = "${selectedCampus.fullName} (${selectedCampus.city})",
      onValueChange = {},
      readOnly = true,
      label = { Text("University Campus") },
      leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
      trailingIcon = {
        IconButton(onClick = { onToggleCampusDropdown(true) }) {
          Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }
      },
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onToggleCampusDropdown(true) }
    )

    DropdownMenu(
      expanded = showCampusDropdown,
      onDismissRequest = { onToggleCampusDropdown(false) }
    ) {
      UniversityCampus.values().forEach { campus ->
        DropdownMenuItem(
          text = { Text("${campus.fullName} (${campus.city})") },
          onClick = {
            onCampusSelect(campus)
            onToggleCampusDropdown(false)
          }
        )
      }
    }
  }

  // Student Number
  OutlinedTextField(
    value = studentNumber,
    onValueChange = onNumberChange,
    label = { Text("Student ID / Campus Card Number") },
    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
    shape = RoundedCornerShape(12.dp),
    singleLine = true,
    modifier = Modifier
      .fillMaxWidth()
      .testTag("input_student_number")
  )

  // Outlook 6-Digit PIN Verification Block (for Sign Up)
  if (isSignUp) {
    if (!isWaitingForOutlookPin) {
      Button(
        onClick = onSendOutlookPin,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0078D4)),
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("btn_send_outlook_pin")
      ) {
        Icon(Icons.Default.MarkEmailRead, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Send Verification PIN to Outlook", fontWeight = FontWeight.Bold)
      }
    } else {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEBF3FB)),
        modifier = Modifier.fillMaxWidth()
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
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Icon(Icons.Default.Mail, contentDescription = null, tint = Color(0xFF0078D4))
              Text(
                text = "Enter 6-Digit Outlook PIN",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF004578)
              )
            }

            TextButton(onClick = onOpenOutlookModal) {
              Text("Open Email", fontSize = 11.sp, color = Color(0xFF0078D4), fontWeight = FontWeight.Bold)
            }
          }

          Text(
            text = "We sent a security PIN to $studentEmail. Check your Microsoft Outlook webmail inbox or tap the notification banner.",
            fontSize = 11.sp,
            color = Color(0xFF004578)
          )

          OutlinedTextField(
            value = enteredPin,
            onValueChange = onPinChange,
            label = { Text("6-Digit Verification PIN") },
            placeholder = { Text("e.g. $generatedPin") },
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            isError = pinErrorMessage != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_outlook_pin")
          )

          if (pinErrorMessage != null) {
            Text(
              text = pinErrorMessage,
              color = MaterialTheme.colorScheme.error,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            TextButton(onClick = { onPinChange(generatedPin) }) {
              Icon(Icons.Default.ContentPaste, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Autofill PIN ($generatedPin)", fontSize = 11.sp, color = Color(0xFF0078D4))
            }
            TextButton(onClick = onSendOutlookPin) {
              Text("Resend PIN", fontSize = 11.sp, color = Color(0xFF0078D4))
            }
          }

          Button(
            onClick = onVerifyPin,
            enabled = enteredPin.isNotBlank(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("btn_verify_outlook_pin")
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Verify PIN & Create Student Account", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  } else {
    // Normal Sign In Button
    Button(
      onClick = onCompleteAuth,
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .testTag("btn_student_email_login")
    ) {
      Icon(Icons.Default.School, contentDescription = null)
      Spacer(modifier = Modifier.width(8.dp))
      Text("Log In with Student Email", fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }
  }

  // 1-Tap Fast Demo Student Accounts
  Text(
    text = "Quick Demo Accounts (1-Tap):",
    fontSize = 11.sp,
    fontWeight = FontWeight.SemiBold,
    color = MaterialTheme.colorScheme.onSurfaceVariant
  )
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    OutlinedButton(
      onClick = {
        onEmailChange("n.khumalo@myuct.ac.za")
        onCampusSelect(UniversityCampus.UCT)
        onCompleteAuth()
      },
      shape = RoundedCornerShape(8.dp),
      contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
      modifier = Modifier.weight(1f)
    ) {
      Text("UCT", fontSize = 11.sp)
    }

    OutlinedButton(
      onClick = {
        onEmailChange("s.ndlovu@students.wits.ac.za")
        onCampusSelect(UniversityCampus.WITS)
        onCompleteAuth()
      },
      shape = RoundedCornerShape(8.dp),
      contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
      modifier = Modifier.weight(1f)
    ) {
      Text("Wits", fontSize = 11.sp)
    }

    OutlinedButton(
      onClick = {
        onEmailChange("a.vandermerwe@sun.ac.za")
        onCampusSelect(UniversityCampus.STELLENBOSCH)
        onCompleteAuth()
      },
      shape = RoundedCornerShape(8.dp),
      contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
      modifier = Modifier.weight(1f)
    ) {
      Text("Maties", fontSize = 11.sp)
    }

    OutlinedButton(
      onClick = {
        onEmailChange("k.dlamini@tuks.co.za")
        onCampusSelect(UniversityCampus.UP)
        onCompleteAuth()
      },
      shape = RoundedCornerShape(8.dp),
      contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
      modifier = Modifier.weight(1f)
    ) {
      Text("Tuks", fontSize = 11.sp)
    }
  }

  // Switch to Web Portal Button
  FilledTonalButton(
    onClick = onOpenWebPortal,
    shape = RoundedCornerShape(12.dp),
    colors = ButtonDefaults.filledTonalButtonColors(
      containerColor = MaterialTheme.colorScheme.secondaryContainer,
      contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ),
    modifier = Modifier
      .fillMaxWidth()
      .height(46.dp)
      .testTag("btn_switch_to_web_portal")
  ) {
    Icon(Icons.Default.DesktopWindows, contentDescription = null, modifier = Modifier.size(18.dp))
    Spacer(modifier = Modifier.width(8.dp))
    Text("🖥️ Open Campus Web Portal & Admin Console", fontWeight = FontWeight.Bold, fontSize = 12.sp)
  }
}
