package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ProductCategory
import com.example.data.model.ProductItem
import com.example.data.model.UniversityCampus
import com.example.ui.components.ProductCard
import com.example.ui.components.TrustShieldBanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceHomeScreen(
  products: List<ProductItem>,
  selectedCategory: ProductCategory,
  selectedCampus: UniversityCampus?,
  searchQuery: String,
  onCategorySelected: (ProductCategory) -> Unit,
  onCampusSelected: (UniversityCampus?) -> Unit,
  onSearchQueryChanged: (String) -> Unit,
  onProductClick: (ProductItem) -> Unit,
  onAddToCart: (ProductItem) -> Unit,
  onOpenProposal: () -> Unit,
  onOpenWebPortal: () -> Unit,
  onAddListingClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showCampusMenu by remember { mutableStateOf(false) }

  Scaffold(
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = onAddListingClick,
        icon = { Icon(Icons.Default.Add, contentDescription = null) },
        text = { Text("Sell / List Item") },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.testTag("fab_add_listing")
      )
    },
    modifier = modifier
  ) { innerPadding ->
    LazyVerticalGrid(
      columns = GridCells.Adaptive(minSize = 165.dp),
      contentPadding = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        top = innerPadding.calculateTopPadding() + 8.dp,
        bottom = innerPadding.calculateBottomPadding() + 80.dp
      ),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      // 1. South African Hero Banner
      item(span = { GridItemSpan(maxLineSpan) }) {
        Card(
          shape = RoundedCornerShape(20.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("hero_banner"),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(180.dp)
          ) {
            Image(
              painter = painterResource(id = R.drawable.img_sa_campus_banner),
              contentDescription = "South African campus marketplace",
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop
            )

            // Gradient Overlay
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(
                  Brush.verticalGradient(
                    colors = listOf(
                      Color.Transparent,
                      Color.Black.copy(alpha = 0.85f)
                    )
                  )
                )
            )

            // Banner Copy & Proposal Action
            Column(
              modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
              verticalArrangement = Arrangement.Bottom
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 6.dp)
                  ) {
                    Text(
                      text = "🇿🇦 MZANSI CAMPUS STORE",
                      color = Color.White,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                  }
                  Text(
                    text = "Trusted University Community Trade",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    lineHeight = 22.sp
                  )
                  Text(
                    text = "Textbooks • Dorm tech • Local produce • Escrow safety",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp
                  )
                }

                Column(
                  verticalArrangement = Arrangement.spacedBy(6.dp),
                  horizontalAlignment = Alignment.End
                ) {
                  FilledTonalButton(
                    onClick = onOpenWebPortal,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                      containerColor = Color.White,
                      contentColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("btn_banner_web_portal")
                  ) {
                    Icon(
                      imageVector = Icons.Default.DesktopWindows,
                      contentDescription = null,
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Web Portal", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                  }

                  FilledTonalButton(
                    onClick = onOpenProposal,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                      containerColor = Color.White.copy(alpha = 0.9f),
                      contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("btn_view_proposal")
                  ) {
                    Icon(
                      imageVector = Icons.Default.Description,
                      contentDescription = null,
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Project Plan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }
          }
        }
      }

      // 2. Trust Shield Banner
      item(span = { GridItemSpan(maxLineSpan) }) {
        TrustShieldBanner()
      }

      // 3. Search Bar & Campus Selector
      item(span = { GridItemSpan(maxLineSpan) }) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            placeholder = { Text("Search textbooks, gadgets, produce...") },
            leadingIcon = {
              Icon(Icons.Default.Search, contentDescription = "Search")
            },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChanged("") }) {
                  Icon(Icons.Default.Close, contentDescription = "Clear")
                }
              }
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("search_input"),
            singleLine = true
          )

          // Campus Filter Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box {
              OutlinedButton(
                onClick = { showCampusMenu = true },
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("btn_campus_filter")
              ) {
                Icon(
                  imageVector = Icons.Default.LocationCity,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                  tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = selectedCampus?.let { "${it.shortName} (${it.city})" } ?: "All SA Campuses",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold
                )
                Icon(
                  imageVector = Icons.Default.ArrowDropDown,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp)
                )
              }

              DropdownMenu(
                expanded = showCampusMenu,
                onDismissRequest = { showCampusMenu = false }
              ) {
                DropdownMenuItem(
                  text = { Text("All South African Campuses") },
                  onClick = {
                    onCampusSelected(null)
                    showCampusMenu = false
                  }
                )
                UniversityCampus.values().forEach { campus ->
                  DropdownMenuItem(
                    text = { Text("${campus.fullName} (${campus.city})") },
                    onClick = {
                      onCampusSelected(campus)
                      showCampusMenu = false
                    }
                  )
                }
              }
            }

            Text(
              text = "${products.size} listings found",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // 4. Horizontal Categories
      item(span = { GridItemSpan(maxLineSpan) }) {
        val scrollState = rememberScrollState()
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          ProductCategory.values().forEach { cat ->
            val isSelected = selectedCategory == cat
            FilterChip(
              selected = isSelected,
              onClick = { onCategorySelected(cat) },
              label = {
                Text(
                  text = cat.displayName,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              shape = RoundedCornerShape(12.dp),
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              ),
              modifier = Modifier.testTag("category_chip_${cat.name}")
            )
          }
        }
      }

      // 5. Product Grid Items
      if (products.isEmpty()) {
        item(span = { GridItemSpan(maxLineSpan) }) {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 32.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = "No listings matching your search",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
              Text(
                text = "Try clearing campus filters or browsing other categories.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Button(
                onClick = {
                  onCategorySelected(ProductCategory.ALL)
                  onCampusSelected(null)
                  onSearchQueryChanged("")
                },
                modifier = Modifier.padding(top = 8.dp)
              ) {
                Text("Reset Filters")
              }
            }
          }
        }
      } else {
        items(products, key = { it.id }) { product ->
          ProductCard(
            product = product,
            onProductClick = onProductClick,
            onAddToCart = onAddToCart
          )
        }
      }
    }
  }
}
