package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.data.model.CommunityPost
import com.example.data.model.PostType
import com.example.ui.components.CampusBadge
import com.example.ui.components.RoleBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityBulletinScreen(
  posts: List<CommunityPost>,
  onUpvoteClick: (String) -> Unit,
  onRsvpClick: (String) -> Unit,
  onAddPostClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTypeFilter by remember { mutableStateOf<PostType?>(null) }

  val filteredPosts = remember(posts, selectedTypeFilter) {
    if (selectedTypeFilter == null) posts else posts.filter { it.type == selectedTypeFilter }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text("Campus Bulletin Board", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Announcements, skill swap & club events", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = onAddPostClick,
        icon = { Icon(Icons.Default.Campaign, contentDescription = null) },
        text = { Text("Post Notice") },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.testTag("fab_post_bulletin")
      )
    },
    modifier = modifier
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      // Type Filter Chips
      val scrollState = rememberScrollState()
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(scrollState)
          .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        FilterChip(
          selected = selectedTypeFilter == null,
          onClick = { selectedTypeFilter = null },
          label = { Text("All Posts (${posts.size})", fontSize = 12.sp) },
          shape = RoundedCornerShape(12.dp)
        )
        PostType.values().forEach { type ->
          val isSelected = selectedTypeFilter == type
          FilterChip(
            selected = isSelected,
            onClick = { selectedTypeFilter = type },
            label = { Text(type.label, fontSize = 12.sp) },
            shape = RoundedCornerShape(12.dp),
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primary,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            )
          )
        }
      }

      // Posts List
      LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(filteredPosts, key = { it.id }) { post ->
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("post_card_${post.id}")
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              // Header: Role & Campus & Date
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  RoleBadge(role = post.authorRole)
                  CampusBadge(campus = post.campus)
                }

                Text(
                  text = post.date,
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              // Post Title
              Text(
                text = post.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
              )

              // Content
              Text(
                text = post.content,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 19.sp
              )

              // Tag Chip
              SuggestionChip(
                onClick = {},
                label = { Text(post.tag, fontSize = 11.sp) },
                shape = RoundedCornerShape(8.dp)
              )

              HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

              // Engagement Actions: Upvote & RSVP
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                OutlinedButton(
                  onClick = { onUpvoteClick(post.id) },
                  shape = RoundedCornerShape(10.dp),
                  contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                  colors = if (post.isUpvoted) ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                  ) else ButtonDefaults.outlinedButtonColors()
                ) {
                  Icon(
                    imageVector = if (post.isUpvoted) Icons.Default.ThumbUp else Icons.Default.ThumbUpOffAlt,
                    contentDescription = "Upvote",
                    tint = if (post.isUpvoted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "${post.upvotes} Support",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                }

                if (post.type == PostType.EVENT || post.type == PostType.ECO_DRIVE || post.type == PostType.SKILL_SWAP) {
                  FilledTonalButton(
                    onClick = { onRsvpClick(post.id) },
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    colors = if (post.isRsvpd) ButtonDefaults.filledTonalButtonColors(
                      containerColor = Color(0xFFDCFCE7),
                      contentColor = Color(0xFF15803D)
                    ) else ButtonDefaults.filledTonalButtonColors()
                  ) {
                    Icon(
                      imageVector = if (post.isRsvpd) Icons.Default.Check else Icons.Default.Event,
                      contentDescription = null,
                      modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = if (post.isRsvpd) "Attending (${post.rsvpCount})" else "RSVP (${post.rsvpCount})",
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold
                    )
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
