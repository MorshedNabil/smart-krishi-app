package com.nabil.smartkrishi.features.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SafetyCheck
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabil.smartkrishi.R
import com.nabil.smartkrishi.ui.theme.DarkGreenText
import com.nabil.smartkrishi.ui.theme.KantumruyPro
import com.nabil.smartkrishi.ui.theme.SmartKrishiTheme
import com.nabil.smartkrishi.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

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
//
//val navigationItems = listOf(
//    CategoryItem(icon = Icons.Default.Home, title = R.string.bottom_navigation_home),
//    CategoryItem(icon = Icons.Default.Inbox, title = R.string.bottom_navigation_profile),
//    CategoryItem(icon = Icons.Default.Notifications, title = R.string.bottom_navigation_notification),
//)

// ===================== Composables =====================
@Composable
fun HeaderSection(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier,
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
                text = "12 January, 2025",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF6B7280),
                modifier = Modifier.alpha(0.7f)
            )
        }

        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun WeatherCard(
    modifier: Modifier = Modifier,
    temperature: String,
    humidity: String,
    sunSetTime: String,
    sunRiseTime: String
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(313.dp),
        color = Color(0xFFFFFFFF),
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
                            .background(color = Color(0xFFF0F7F2), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = DarkGreenText
                        )
                    }


                    Text(
                        text = "Rahitpur, Panchagar, Rangpur",
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
                            color = DarkGreenText ,
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
                                    color = Color(0xFF111827) ,
                                    fontSize = 11.sp,
                                    fontFamily = KantumruyPro,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("$sunRiseTime AM")
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
                                    color = Color(0xFF111827) ,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = KantumruyPro
                                )
                            ) {
                                append("$sunSetTime AM")
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
        color = colorResource(id = R.color.white),
        tonalElevation = 4.dp, // Added elevation to make the card visible
        shadowElevation = 2.dp
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
                    .background(color = Color(0xFFF8F8F8), shape = CircleShape),
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
            val nextPage = (pagerState.currentPage + 1) % newsItems.size // iterate to the last index then come again to the 0th index to circulate the cards for infinite time
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
fun HomeSection(
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
                .padding(end = 24.dp , bottom = 14.dp),
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

// Full Home Screen
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        // Top Space
        Spacer(modifier = Modifier.height(16.dp))

        HeaderSection(
            modifier = Modifier.padding(horizontal = 24.dp),
            drawable = R.drawable.farmer_dp,
            text = R.string.user
        )

        Spacer(modifier = Modifier.height(24.dp))

        WeatherCard(
            modifier = Modifier.padding(horizontal = 24.dp),
            temperature = "25",
            humidity = "17",
            sunSetTime = "6:30",
            sunRiseTime = "5:00"
        )

        Spacer(modifier = Modifier.height(24.dp))

        HomeSection(
            title = R.string.bestInvestmet
        ) {
            BestInvestmentRow()
        }

        Spacer(modifier = Modifier.height(24.dp))

        HomeSection(
            title = R.string.agriNews
        ) {
            AutoScrollingNewsList()
        }

        // Just for testing the vertical scroll
        Spacer(modifier = Modifier.height(24.dp))

        HomeSection(
            title = R.string.bestInvestmet
        ) {
            BestInvestmentRow()
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
//
//@Preview(showBackground = true, backgroundColor = 0XFF000000)
//@Composable
//fun AgriNewsItemPreview() {
//    SmartKrishiTheme {
//        AgriNewsItem(
//            item = newsItems[0]
//        )
//    }
//}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SmartKrishiTheme {
        HomeScreen()
    }
}