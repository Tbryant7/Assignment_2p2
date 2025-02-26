package com.example.assignment2p2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "country_list") {
                composable("country_list") {
                    CountryListScreen(navController)
                }
                composable(
                    "country_detail/{name}/{map}/{fact}",
                    arguments = listOf(
                        navArgument("name") { type = NavType.StringType },
                        navArgument("map") { type = NavType.IntType },
                        navArgument("fact") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val name = backStackEntry.arguments?.getString("name") ?: ""
                    val map = backStackEntry.arguments?.getInt("map") ?: 0
                    val fact = backStackEntry.arguments?.getString("fact") ?: ""
                    CountryDetailScreen(navController, name, map, fact)
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class) //I was having errors with this part of the code but this made it work
@Composable
fun CountryListScreen(navController: NavController) {
    Scaffold(topBar = {
        TopAppBar(title = { Text("Countries") })
    }) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            items(CountryList) { country ->
                CountryRow(country) {
                    navController.navigate("country_detail/${country.name}/${country.flag}/${country.fact}")
                }
            }
        }
    }
}
@Composable
fun CountryRow(country: Country, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = country.flag),
            contentDescription = null, modifier = Modifier.size(50.dp).clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = "Country: ${country.name}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Capital: ${country.capital}", fontSize = 14.sp, color = Color.Gray)
            Text(text = "Currency: ${country.currency}", fontSize = 12.sp, color = Color.Gray)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class) //I was having errors with this part of the code but this made it work
@Composable
fun CountryDetailScreen(navController: NavController, name: String, map: Int, fact: String) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(name) }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(painterResource(id = android.R.drawable.ic_media_previous), contentDescription = "Back")
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        ) { Image(painter = painterResource(id = map), contentDescription = "Country Map")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = fact, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}
