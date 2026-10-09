package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UniversityCampus
import com.example.data.model.UserRole
import com.example.ui.components.RoleBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
  onLoginSuccess: (name: String, email: String, studentOrBizId: String, role: UserRole, campus: UniversityCampus) -> Unit,
  onOpenWebPortal: () -> Unit,
  modifier: Modifier = Modifier
) {
  var studentEmail by remember { mutableStateOf("n.khumalo@myuct.ac.za") }
  var studentName by remember { mutableStateOf("Nandi Khumalo") }
  var studentPassword by remember { mutableStateOf("Student2026!") }
  var passwordVisible by remember { mutableStateOf(false) }
  var studentNumber by remember { mutableStateOf("KHMNDI004") }
  var selectedCampus by remember { mutableStateOf(UniversityCampus.UCT) }
  var showCampusDropdown by remember { mutableStateOf(false) }
  var showOtherRoles by remember { mutableStateOf(false) }

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
    studentEmail.contains("@") && (studentEmail.contains(".ac.za") || studentEmail.contains("tuks.co.za") || studentEmail.contains(".edu"))
  }

  val quickDomains = listOf(
    "@myuct.ac.za" to UniversityCampus.UCT,
    "@students.wits.ac.za" to UniversityCampus.WITS,
    "@sun.ac.za" to UniversityCampus.STELLENBOSCH,
    "@tuks.co.za" to UniversityCampus.UP,
    "@student.uj.ac.za" to UniversityCampus.UJ,
    "@campus.ru.ac.za" to UniversityCampus.RHODES
  )

  val scrollState = rememberScrollState()

  Scaffold(
    modifier = modifier.fillMaxSize()
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(scrollState)
        .padding(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Header Card
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(56.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.School,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(32.dp)
            )
          }

          Text(
            text = "Student Email Login",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )

          Text(
            text = "Sign in using your South African university student email (.ac.za) for verified campus marketplace access.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 16.sp
          )
        }
      }

      // Verification Status Banner
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isStudentEmailValid) Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Icon(
            imageVector = if (isStudentEmailValid) Icons.Default.Verified else Icons.Default.Info,
            contentDescription = null,
            tint = if (isStudentEmailValid) Color(0xFF15803D) else Color(0xFFB45309),
            modifier = Modifier.size(22.dp)
          )
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (isStudentEmailValid) "Verified University Student Email" else "Enter Student Email (.ac.za)",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = if (isStudentEmailValid) Color(0xFF15803D) else Color(0xFFB45309)
            )
            Text(
              text = if (isStudentEmailValid)
                "Identified as ${selectedCampus.fullName} • Unlocks student rates, safe campus lockers & textbook exchange."
              else
                "Use your university student email e.g. student@myuct.ac.za to unlock verified marketplace badges.",
              fontSize = 11.sp,
              color = if (isStudentEmailValid) Color(0xFF166534) else Color(0xFF78350F),
              lineHeight = 15.sp
            )
          }
        }
      }

      // Quick Student Domain Chips
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Text(
          text = "Quick Campus Student Domains:",
          fontSize = 12.sp,
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
              onClick = {
                val prefix = studentEmail.substringBefore("@").ifBlank { "student" }
                studentEmail = "$prefix$domain"
                selectedCampus = campus
              },
              label = {
                Text(
                  text = "${campus.shortName} ($domain)",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium
                )
              },
              shape = RoundedCornerShape(8.dp)
            )
          }
        }
      }

      // Student Email Input Field
      OutlinedTextField(
        value = studentEmail,
        onValueChange = { studentEmail = it },
        label = { Text("Student University Email") },
        placeholder = { Text("e.g. n.khumalo@myuct.ac.za") },
        leadingIcon = {
          Icon(Icons.Default.AlternateEmail, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        },
        trailingIcon = {
          if (isStudentEmailValid) {
            Icon(Icons.Default.CheckCircle, contentDescription = "Valid Student Email", tint = Color(0xFF059669))
          }
        },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("input_student_email")
      )

      // Student Name
      OutlinedTextField(
        value = studentName,
        onValueChange = { studentName = it },
        label = { Text("Student Full Name") },
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("input_student_name")
      )

      // Password Field
      OutlinedTextField(
        value = studentPassword,
        onValueChange = { studentPassword = it },
        label = { Text("Student Portal Password") },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
        trailingIcon = {
          IconButton(onClick = { passwordVisible = !passwordVisible }) {
            Icon(
              imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
              contentDescription = if (passwordVisible) "Hide password" else "Show password"
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

      // Student ID / Student Number
      OutlinedTextField(
        value = studentNumber,
        onValueChange = { studentNumber = it },
        label = { Text("Student Number / Campus ID") },
        placeholder = { Text("e.g. KHMNDI004 or 2398410") },
        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("input_student_number")
      )

      // University Campus Selector
      Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
          value = "${selectedCampus.fullName} (${selectedCampus.city})",
          onValueChange = {},
          readOnly = true,
          label = { Text("Selected University Campus") },
          leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
          trailingIcon = {
            IconButton(onClick = { showCampusDropdown = true }) {
              Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
          },
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { showCampusDropdown = true }
        )

        DropdownMenu(
          expanded = showCampusDropdown,
          onDismissRequest = { showCampusDropdown = false }
        ) {
          UniversityCampus.values().forEach { campus ->
            DropdownMenuItem(
              text = { Text("${campus.fullName} (${campus.city})") },
              onClick = {
                selectedCampus = campus
                showCampusDropdown = false
              }
            )
          }
        }
      }

      // PRIMARY LOGIN BUTTON: Log In With Student Email
      Button(
        onClick = {
          if (studentEmail.isNotBlank()) {
            onLoginSuccess(
              studentName.ifBlank { "Verified Student" },
              studentEmail,
              studentNumber.ifBlank { "STU-1004" },
              UserRole.STUDENT,
              selectedCampus
            )
          }
        },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("btn_student_email_login")
      ) {
        Icon(Icons.Default.School, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Log In with Student Email",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )
      }

      HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

      // 1-Click Fast Student Presets
      Text(
        text = "Or 1-Tap Student Accounts:",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = {
            onLoginSuccess("Nandi Khumalo", "n.khumalo@myuct.ac.za", "KHMNDI004", UserRole.STUDENT, UniversityCampus.UCT)
          },
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("demo_student_uct")
        ) {
          Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("UCT Student: n.khumalo@myuct.ac.za", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }

        OutlinedButton(
          onClick = {
            onLoginSuccess("Sipho Ndlovu", "s.ndlovu@students.wits.ac.za", "2391048", UserRole.STUDENT, UniversityCampus.WITS)
          },
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("demo_student_wits")
        ) {
          Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Wits Student: s.ndlovu@students.wits.ac.za", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }

        OutlinedButton(
          onClick = {
            onLoginSuccess("Anika van der Merwe", "26849102@sun.ac.za", "SU-26849102", UserRole.STUDENT, UniversityCampus.STELLENBOSCH)
          },
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("demo_student_sun")
        ) {
          Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Maties Student: 26849102@sun.ac.za", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
      }

      // Expandable section for other campus roles (Faculty, Vendor, Resident)
      TextButton(
        onClick = { showOtherRoles = !showOtherRoles }
      ) {
        Icon(
          imageVector = if (showOtherRoles) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
          contentDescription = null
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = if (showOtherRoles) "Hide Other Personas" else "Not a student? Sign in as Faculty, Vendor, or Resident",
          fontSize = 12.sp
        )
      }

      AnimatedVisibility(visible = showOtherRoles) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = {
              onLoginSuccess("Prof. Sipho Dlamini", "prof.dlamini@wits.ac.za", "FAC-9842", UserRole.FACULTY, UniversityCampus.WITS)
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color(0xFF7C3AED), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log in as Faculty (Wits • Prof. Dlamini)", fontSize = 12.sp)
          }

          OutlinedButton(
            onClick = {
              onLoginSuccess("Cape Flora Organics", "orders@capeflora.co.za", "CIPC-2019/3310", UserRole.VENDOR, UniversityCampus.STELLENBOSCH)
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.Storefront, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log in as Local Vendor (CIPC Registered)", fontSize = 12.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      // Web Portal Desktop View Switcher ("must not be on the phone")
      FilledTonalButton(
        onClick = onOpenWebPortal,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
          containerColor = MaterialTheme.colorScheme.secondaryContainer,
          contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("btn_switch_to_web_portal")
      ) {
        Icon(Icons.Default.DesktopWindows, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("🖥️ Open Campus Web Portal & Admin Console", fontWeight = FontWeight.Bold, fontSize = 13.sp)
      }
    }
  }
}
