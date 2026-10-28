package com.nabil.smartkrishi.features.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SafetyCheck
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nabil.smartkrishi.R
import com.nabil.smartkrishi.data.remote.dto.MarketPriceDto
import com.nabil.smartkrishi.data.remote.dto.toMarketPriceDO
import com.nabil.smartkrishi.domain.model.MarketPrice
import com.nabil.smartkrishi.ui.theme.DarkGreenText
import com.nabil.smartkrishi.ui.theme.KantumruyPro
import com.nabil.smartkrishi.ui.theme.SimpleWhite
import com.nabil.smartkrishi.ui.theme.SmallCircleColor
import com.nabil.smartkrishi.ui.theme.SmartKrishiTheme
import com.nabil.smartkrishi.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield


// ===================== Constants =====================
val bestInvestmentItems = listOf(
    BestInvestmentItems(icon = Icons.Default.Timer, title = R.string.CT_Duration),
    BestInvestmentItems(icon = Icons.Default.ArrowOutward, title = R.string.CT_Return),
    BestInvestmentItems(icon = Icons.Default.SafetyCheck, title = R.string.CT_LowRisk),
    BestInvestmentItems(icon = Icons.Default.EnergySavingsLeaf, title = R.string.CT_Safety),
)

val newsItems = listOf(
    NewsItem(image = R.drawable.agri_news_1, title = R.string.agri_news_1, date = R.string.agri_news_date),
    NewsItem(image = R.drawable.agri_news_2, title = R.string.agri_news_2, date = R.string.agri_news_date),
    NewsItem(image = R.drawable.agri_news_3, title = R.string.agri_news_3, date = R.string.agri_news_date)
)

val farmerTools = listOf(
    FarmerTool(icon = Icons.Default.Timer, title = "Sowing Calendar"),
    FarmerTool(icon = Icons.Default.ArrowOutward, title = "Fertilizer Guide"),
    FarmerTool(icon = Icons.Default.SafetyCheck, title = "Irrigation Plan"),
    FarmerTool(icon = Icons.Default.EnergySavingsLeaf, title = "Pest Control")
)

// ===================== Composables =====================
@Composable
fun HeaderSection(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    date: String,
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Hello " + stringResource(text),
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = KantumruyPro
                ),
                color = DarkGreenText
            )
            Text(
                text = date,
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF6B7280),
                modifier = Modifier.alpha(0.7f)
            )
        }

        // profile avatar
        Box(
            modifier = Modifier
                .clip(CircleShape) // Clip first so the ripple effect stays circular, then add .clickable
                .clickable {
                    onProfileClick()
                }
        ) {
            Image(
                painter = painterResource(drawable),
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape),
            )
        }
    }
}

@Composable
fun WeatherCard(
    modifier: Modifier = Modifier,
    location: String = "Rahitpur, Panchagar, Rangpur",
    temperature: String,
    humidity: String,
    sunSetTime: String,
    sunRiseTime: String
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(313.dp),
        color = SimpleWhite,
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(color = SmallCircleColor, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = DarkGreenText
                        )
                    }


                    Text(
                        text = location,
                        style = TextStyle(
                            color = Color(0xFF6B7280),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = KantumruyPro
                        ),

                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                // Weather icon -> changes with weather
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(color = Color(0xFFF3F4F6), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.weather),
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                    )
                }

            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            // Temperature and Sun set/rise time Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Temperature Column
                Column(

                ) {
                    Text(
                        text = "$temperature°C",
                        style = TextStyle(
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = KantumruyPro
                        ),
                        color = Color(0xFF111827)
                    )

                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = "Humidity $humidity%",
                        style = TextStyle(
                            color = DarkGreenText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = KantumruyPro
                        )
                    )
                }

                // Sunrise/Sunset Column
                Column(
                    modifier = Modifier
                        .width(100.dp)
                        .align(Alignment.Bottom)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.End),
                        text = buildAnnotatedString {
                            append("Sun Rise:  ")
                            withStyle(
                                style = SpanStyle(
                                    color = Color(0xFF111827),
                                    fontSize = 11.sp,
                                    fontFamily = KantumruyPro,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(if (sunRiseTime.contains("AM") || sunRiseTime.contains("PM")) sunRiseTime else "$sunRiseTime AM")
                            }
                        },
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = KantumruyPro
                        ),
                        color = TextSecondary
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        thickness = 1.dp,
                        color = TextSecondary.copy(alpha = 0.3f)
                    )

                    Text(
                        modifier = Modifier.align(Alignment.End),
                        text = buildAnnotatedString {
                            append("Sun Set:  ")
                            withStyle(
                                style = SpanStyle(
                                    color = Color(0xFF111827),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = KantumruyPro
                                )
                            ) {
                                append(if (sunSetTime.contains("AM") || sunSetTime.contains("PM")) sunSetTime else "$sunSetTime PM")
                            }
                        },
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = KantumruyPro
                        ),
                        color = TextSecondary
                    )
                }


            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(47.dp)
                    .background(
                        color = Color(0xFFF0F7F2),
                        shape = MaterialTheme.shapes.large
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = modifier.padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = DarkGreenText,
                                shape = CircleShape
                            )
                    )

                    Text(
                        text = "A good day to apply pesticides",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = DarkGreenText
                    )
                }

            }
        }


    }

}


@Composable
fun BestInvestmentItem(
    modifier: Modifier = Modifier,
    item: BestInvestmentItems
) {
    Surface(
        modifier = modifier
            .width(86.dp)
            .height(99.dp),
        shape = MaterialTheme.shapes.large,
        color = SimpleWhite,
        tonalElevation = 0.dp, // 0.dp prevents Material 3 green primary tinting on plain white
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween

        ) {
            // Card Icon
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = SmallCircleColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    modifier = Modifier.size(20.dp),
                    contentDescription = null,
                    tint = DarkGreenText
                )
            }

            // Card Title
            Text(
                text = stringResource(item.title),
                style = TextStyle(
                    color = colorResource(id = R.color.black),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = KantumruyPro
                )
            )
        }

    }
}

@Composable
fun BestInvestmentRow(
    modifier: Modifier = Modifier
) {
    // Remember the scroll state to maintain position during recompositions
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Using forEach is cleaner than repeat(4) when working with a list
        bestInvestmentItems.forEach { item ->
            BestInvestmentItem(item = item)
        }
    }

}

@Composable
fun AgriNewsItem(
    modifier: Modifier = Modifier,
    item: NewsItem
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(127.dp),
        color = Color(0xFFFFFFFF),
        shape = MaterialTheme.shapes.large
    ) {

        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(width = 112.dp, height = 96.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .align(Alignment.CenterVertically),
                painter = painterResource(item.image),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            // Title and Date
            Column(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(item.title),
                    style = TextStyle(
                        fontFamily = KantumruyPro,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFF0F7F2),
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier,
                        text = stringResource(item.date),
                        style = TextStyle(
                            fontFamily = KantumruyPro,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun AutoScrollingNewsList(
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { newsItems.size })

    // Auto-scroll logic
    LaunchedEffect(Unit) {
        while (true) { // infinite loop to circulate the news cards for infinite time by giving a delay of 3 milisec
            yield()
            delay(5000) // 5 seconds delay
            val nextPage =
                (pagerState.currentPage + 1) % newsItems.size // iterate to the last index then come again to the 0th index to circulate the cards for infinite time
            pagerState.animateScrollToPage(nextPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(end = 48.dp), // Show a bit of the next card
        pageSpacing = 16.dp
    ) { page ->
        AgriNewsItem(
            item = newsItems[page]
        )
    }
}

@Composable
fun CommonHomeSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .padding(start = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 24.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(title).uppercase(),
                style = TextStyle(
                    color = colorResource(id = R.color.black),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = KantumruyPro
                )
            )

            Text(
                text = "View All",
                style = TextStyle(
                    color = DarkGreenText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = KantumruyPro
                )
            )
        }
        content()
    }
}

// Generates the fallback MarketPrice object directly through your toMarketPriceDO() function
val defaultNullMarketPrice = MarketPriceDto(
    id = null,
    cropTitle = null,
    pricePerKg = null,
    unit = null,
    source = null,
    marketName = null,
    marketLocation = null,
    createdAt = null,
    updatedAt = null,
    observedAt = null,
    currency = null
).toMarketPriceDO()

@Composable
fun MarketPriceCard(
    modifier: Modifier = Modifier,
    marketPrice: MarketPrice = defaultNullMarketPrice
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = SimpleWhite,
        shape = MaterialTheme.shapes.large,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image Box / Placeholder
            Box(
                modifier = Modifier
                    .size(width = 96.dp, height = 84.dp)
                    .background(color = Color(0xFFF0F7F2), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 56.dp, height = 36.dp)
                        .background(color = Color(0xFFD1D5DB), shape = RoundedCornerShape(8.dp))
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Header: Crop Title + Date Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = marketPrice.cropTitle,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = KantumruyPro,
                            color = Color(0xFF111827)
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFE2EFE7),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = marketPrice.createdAt,
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = KantumruyPro,
                                color = DarkGreenText
                            )
                        )
                    }
                }

                // Price
                Text(
                    text = marketPrice.formattedPrice,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = KantumruyPro,
                        color = DarkGreenText
                    )
                )

                // Market Name & Location Info Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = DarkGreenText,
                        modifier = Modifier.size(14.dp)
                    )

                    Text(
                        text = "${marketPrice.marketName} • ${marketPrice.marketLocation}",
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = KantumruyPro,
                            color = TextSecondary
                        ),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun MarketPricesSection(
    modifier: Modifier = Modifier,
    marketPrice: MarketPrice = defaultNullMarketPrice
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Market Prices",
                style = TextStyle(
                    color = colorResource(id = R.color.black),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = KantumruyPro
                )
            )

            Text(
                text = "view all",
                style = TextStyle(
                    color = DarkGreenText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = KantumruyPro
                )
            )
        }

        MarketPriceCard(marketPrice = marketPrice)
    }
}

@Composable
fun FarmerToolCard(
    modifier: Modifier = Modifier,
    tool: FarmerTool
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        shape = MaterialTheme.shapes.large,
        color = SimpleWhite,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = SmallCircleColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = tool.icon,
                    modifier = Modifier.size(20.dp),
                    contentDescription = null,
                    tint = DarkGreenText
                )
            }

            Text(
                text = tool.title,
                style = TextStyle(
                    color = Color(0xFF111827),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = KantumruyPro
                )
            )
        }
    }
}

@Composable
fun FarmerToolsSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Farmer Tools",
                style = TextStyle(
                    color = colorResource(id = R.color.black),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = KantumruyPro
                )
            )

            Text(
                text = "view all",
                style = TextStyle(
                    color = DarkGreenText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = KantumruyPro
                )
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FarmerToolCard(
                    modifier = Modifier.weight(1f),
                    tool = farmerTools[0]
                )
                FarmerToolCard(
                    modifier = Modifier.weight(1f),
                    tool = farmerTools[1]
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FarmerToolCard(
                    modifier = Modifier.weight(1f),
                    tool = farmerTools[2]
                )
                FarmerToolCard(
                    modifier = Modifier.weight(1f),
                    tool = farmerTools[3]
                )
            }
        }
    }
}

// ================= Full Home Screen =============

/**
 * Stateful HomeScreen composable that manages ViewModel state, side effects (Toasts), and data loading.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    bottomPadding: Dp = 0.dp,
    onProfileClick: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dateState by viewModel.dateState.collectAsStateWithLifecycle()
    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()
    var isErrorDismissed by remember(uiState) { mutableStateOf(false) }

    // Location Permission Launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.any { it }
        if (isGranted) {
            viewModel.fetchWeatherFromGps()
        }
    }

    // Check location permission on launch
    LaunchedEffect(Unit) {
        val hasFineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasFineLocation || hasCoarseLocation) {
            viewModel.fetchWeatherFromGps()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    HomeScreen(
        modifier = modifier,
        uiState = uiState,
        weatherState = weatherState,
        date = dateState.date,
        isErrorDismissed = isErrorDismissed,
        bottomPadding = bottomPadding,
        onProfileClick = onProfileClick,
        onRetry = {
            isErrorDismissed = false
            viewModel.loadHomeData()
            viewModel.fetchWeatherFromGps()
        },
        onCancel = {
            isErrorDismissed = true
        }
    )
}

/**
 * Stateless HomeScreen composable that renders the UI based purely on the given HomeUiState and callbacks.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    weatherState: WeatherState = WeatherState(),
    date: String,
    isErrorDismissed: Boolean = false,
    bottomPadding: Dp = 0.dp,
    onProfileClick: () -> Unit = {},
    onRetry: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    // Determine market price data from state
    val marketPrice = when (uiState) {
        is HomeUiState.Success -> uiState.marketPrice // in case of success, use the data get from API
        else -> defaultNullMarketPrice // in case of error/loading, fall back to defaultNullMarketPrice
    }

    // Extract fetched weather data from weatherState
    val weather = weatherState.weather
    val locationText = if (weather != null && weather.location.isNotBlank() && weather.location != "default") weather.location else "Rahitpur, Panchagar, Rangpur"
    val tempText = weather?.temperature?.toInt()?.toString() ?: "25"
    val humidityText = weather?.relativeHumidity?.toString() ?: "17"
    val sunriseText = weather?.dailyForecasts?.firstOrNull()?.sunrise?.ifBlank { "5:00 AM" } ?: "5:00 AM"
    val sunsetText = weather?.dailyForecasts?.firstOrNull()?.sunset?.ifBlank { "6:30 PM" } ?: "6:30 PM"

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top Space
            Spacer(modifier = Modifier.height(16.dp))

            HeaderSection(
                modifier = Modifier.padding(horizontal = 24.dp),
                drawable = R.drawable.farmer_dp,
                text = R.string.user,
                date = date,
                onProfileClick = onProfileClick // Forwards click event
            )

            Spacer(modifier = Modifier.height(24.dp))

            WeatherCard(
                modifier = Modifier.padding(horizontal = 24.dp),
                location = locationText,
                temperature = tempText,
                humidity = humidityText,
                sunSetTime = sunsetText,
                sunRiseTime = sunriseText
            )

            Spacer(modifier = Modifier.height(24.dp))

            CommonHomeSection(
                title = R.string.bestInvestmet
            ) {
                BestInvestmentRow()
            }

            Spacer(modifier = Modifier.height(24.dp))

            MarketPricesSection(marketPrice = marketPrice)

            Spacer(modifier = Modifier.height(24.dp))

            FarmerToolsSection()

            Spacer(modifier = Modifier.height(24.dp))

            CommonHomeSection(
                title = R.string.agriNews
            ) {
                AutoScrollingNewsList()
            }
            // safe padding to move the contents above the bottom navigation bar
            Spacer(modifier = Modifier.height(bottomPadding + 16.dp))
        }

        // if data loading then show a loading indicator
        if (uiState is HomeUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(
                    color = DarkGreenText
                )
            }
        }
        // If data load fails show a error message box
        if (uiState is HomeUiState.Error && !isErrorDismissed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.padding(24.dp),
                    shape = MaterialTheme.shapes.large,
                    color = SimpleWhite,
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Error Loading Data",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = KantumruyPro,
                                color = Color.Red
                            )
                        )
                        Text(
                            text = uiState.message,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = KantumruyPro,
                                color = TextSecondary
                            ),
                            textAlign = TextAlign.Center
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = onCancel,
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE5E7EB)
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Cancel",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = KantumruyPro,
                                        color = Color(0xFF374151)
                                    )
                                )
                            }

                            Button(
                                onClick = onRetry,
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                    containerColor = DarkGreenText
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Retry",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = KantumruyPro,
                                        color = SimpleWhite
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ======================= Preview =======================
//@Preview(showBackground = true)
//@Composable
//fun HeaderSectionPreview() {
//    SmartKrishiTheme {
//        HeaderSection(
//            drawable = R.drawable.farmer_dp,
//            text = R.string.user,
//            modifier = Modifier
//                .padding(horizontal = 8.dp)
//        )
//    }
//}

//
//@Preview(showBackground = true)
//@Composable
//fun WeatherCardPreview() {
//    SmartKrishiTheme {
//        WeatherCard(
//            temperature = "25",
//            humidity = "76",
//            sunSetTime = "5:25",
//            sunRiseTime = "6:30"
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun BestInvestmentItemPreview() {
//    SmartKrishiTheme {
//        BestInvestmentItem(
//            item = bestInvestmentItems[0]
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun BestInvestmentRowPreview() {
//    SmartKrishiTheme {
//        BestInvestmentRow()
//    }
//}

//@Preview(showBackground = true, backgroundColor = 0XFF000000)
//@Composable
//fun AgriNewsItemPreview() {
//    SmartKrishiTheme {
//        AgriNewsItem(
//            item = newsItems[0]
//        )
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun MarketPriceCardPreview() {
//    SmartKrishiTheme {
//        MarketPriceCard(marketPrice = defaultNullMarketPrice)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MarketPricesSectionPreview() {
//    SmartKrishiTheme {
//        MarketPricesSection(marketPrice = defaultNullMarketPrice)
//    }
//}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SmartKrishiTheme {
        HomeScreen(
            uiState = HomeUiState.Success(marketPrice = defaultNullMarketPrice),
            date = "12 January, 2025"
        )
    }
}