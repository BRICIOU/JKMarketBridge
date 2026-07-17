package zm.edu.justinkabwe.jkmarketbridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zm.edu.justinkabwe.jkmarketbridge.ui.theme.JKMarketBridgeTheme

// ── Data Models ──────────────────────────────────────────────────────────────

data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val price: String,
    val trader: String,
    val market: String,
    val stall: String,
    val quantity: String,
    val isVerified: Boolean = true
)

data class BuyerRequest(
    val id: Int,
    val product: String,
    val quantity: String,
    val location: String,
    val maxBudget: String
)

// ── Activity ──────────────────────────────────────────────────────────────────

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JKMarketBridgeTheme {
                JKMarketBridgeApp()
            }
        }
    }
}

// ── Root App ──────────────────────────────────────────────────────────────────

@Composable
fun JKMarketBridgeApp() {
    var selectedTab by remember { mutableStateOf(0) }

    val products = remember {
        mutableStateListOf(
            Product(1, "Fresh Tomatoes", "Vegetables", "K25/kg",
                "Alice Banda", "Soweto Market", "Stall B14", "40 kg"),
            Product(2, "Groundnuts", "Nuts", "K40/kg",
                "John Phiri", "Kamwala Market", "Stall C08", "25 kg"),
            Product(3, "Chitenge Fabric", "Textiles", "K120",
                "Mary Tembo", "Chilenje Market", "Stall A04", "16 items"),
            Product(4, "Fresh Eggs", "Poultry", "K65/tray",
                "Grace Mwale", "Matero Market", "Stall D11", "20 trays"),
            Product(5, "Maize", "Grains", "K180/50kg bag",
                "Peter Lungu", "Soweto Market", "Stall F03", "30 bags")
        )
    }

    val buyerRequests = remember {
        mutableStateListOf(
            BuyerRequest(1, "Sweet Potatoes", "50 kg", "Lusaka Central", "K500"),
            BuyerRequest(2, "Dried Fish (Kapenta)", "10 kg", "Chelston", "K350"),
            BuyerRequest(3, "Mixed Vegetables", "20 kg", "Kabulonga", "K600")
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Marketplace") },
                    label = { Text("Marketplace") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Requests") },
                    label = { Text("Requests") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Add, contentDescription = "Add Product") },
                    label = { Text("Add Product") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 }
                )
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> MarketplaceScreen(
                products = products,
                modifier = Modifier.padding(innerPadding)
            )
            1 -> BuyerRequestsScreen(
                requests = buyerRequests,
                onAddRequest = { buyerRequests.add(it) },
                modifier = Modifier.padding(innerPadding)
            )
            2 -> AddProductScreen(
                onProductAdded = { products.add(it) },
                modifier = Modifier.padding(innerPadding)
            )
            3 -> TraderProfileScreen(
                productCount = products.size,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

// ── Marketplace Screen ────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    products: List<Product>,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var reservedProduct by remember { mutableStateOf<Product?>(null) }
    var collectionCode by remember { mutableStateOf("") }

    val categories = listOf("All") + products.map { it.category }.distinct()

    val filtered = products.filter { p ->
        (selectedCategory == "All" || p.category == selectedCategory) &&
            (searchQuery.isBlank() ||
                p.name.contains(searchQuery, ignoreCase = true) ||
                p.trader.contains(searchQuery, ignoreCase = true) ||
                p.market.contains(searchQuery, ignoreCase = true))
    }

    // Dialogs
    selectedProduct?.let { product ->
        ProductDetailDialog(
            product = product,
            onDismiss = { selectedProduct = null },
            onReserve = {
                collectionCode = (1000..9999).random().toString()
                reservedProduct = product
                selectedProduct = null
            }
        )
    }

    reservedProduct?.let { product ->
        ReserveDialog(
            product = product,
            code = collectionCode,
            onDismiss = { reservedProduct = null }
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Header banner
        Surface(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                Text(
                    text = "JK MarketBridge",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Local trade. Trusted connections.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search products, traders, markets…") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        // Category filter chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Product list
        if (filtered.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No products found.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered, key = { it.id }) { product ->
                    ProductCard(product = product, onClick = { selectedProduct = product })
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (product.isVerified) {
                    Spacer(modifier = Modifier.width(8.dp))
                    VerifiedBadge()
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.price,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(label = "Trader", value = product.trader)
            InfoRow(label = "Market", value = "${product.market} · ${product.stall}")
            InfoRow(label = "Stock", value = product.quantity, valueColor = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun VerifiedBadge() {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Verified",
                modifier = Modifier.size(12.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "Verified",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, valueColor: Color = MaterialTheme.colorScheme.onSurfaceVariant) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = valueColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ProductDetailDialog(
    product: Product,
    onDismiss: () -> Unit,
    onReserve: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(product.name, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                if (product.isVerified) VerifiedBadge()
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                DetailItem("Price", product.price)
                DetailItem("Category", product.category)
                DetailItem("Trader", product.trader)
                DetailItem("Market", product.market)
                DetailItem("Stall", product.stall)
                DetailItem("Stock", product.quantity)
                if (product.isVerified) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "This trader is verified and trusted.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onReserve) { Text("Reserve for Collection") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

@Composable
fun DetailItem(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(72.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ReserveDialog(product: Product, code: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reservation Confirmed!", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                Text(
                    text = "Your collection code for\n${product.name}:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Text(
                    text = code,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 8.sp
                )
                Text(
                    text = "Show this code at ${product.stall}, ${product.market}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text("Done") }
        }
    )
}

// ── Buyer Requests Screen ─────────────────────────────────────────────────────

@Composable
fun BuyerRequestsScreen(
    requests: List<BuyerRequest>,
    onAddRequest: (BuyerRequest) -> Unit,
    modifier: Modifier = Modifier
) {
    var showForm by remember { mutableStateOf(false) }
    var productField by remember { mutableStateOf("") }
    var quantityField by remember { mutableStateOf("") }
    var locationField by remember { mutableStateOf("") }
    var budgetField by remember { mutableStateOf("") }
    var nextId by remember { mutableStateOf(requests.size + 1) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Buyer Requests",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${requests.size} active request${if (requests.size != 1) "s" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(onClick = { showForm = !showForm }) {
                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (showForm) "Cancel" else "New Request")
                }
            }
        }

        if (showForm) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "New Buyer Request",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            value = productField,
                            onValueChange = { productField = it },
                            label = { Text("Product Needed *") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = quantityField,
                            onValueChange = { quantityField = it },
                            label = { Text("Quantity *") },
                            placeholder = { Text("e.g. 50 kg") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = locationField,
                            onValueChange = { locationField = it },
                            label = { Text("Delivery Location") },
                            placeholder = { Text("e.g. Lusaka Central") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = budgetField,
                            onValueChange = { budgetField = it },
                            label = { Text("Maximum Budget") },
                            placeholder = { Text("e.g. K500") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                if (productField.isNotBlank() && quantityField.isNotBlank()) {
                                    onAddRequest(
                                        BuyerRequest(
                                            id = nextId,
                                            product = productField.trim(),
                                            quantity = quantityField.trim(),
                                            location = locationField.ifBlank { "Not specified" },
                                            maxBudget = when {
                                                budgetField.isBlank() -> "Negotiable"
                                                budgetField.startsWith("K") -> budgetField.trim()
                                                else -> "K${budgetField.trim()}"
                                            }
                                        )
                                    )
                                    nextId++
                                    productField = ""
                                    quantityField = ""
                                    locationField = ""
                                    budgetField = ""
                                    showForm = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Submit Request")
                        }
                    }
                }
            }
        }

        items(requests, key = { it.id }) { request ->
            BuyerRequestCard(request = request)
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
fun BuyerRequestCard(request: BuyerRequest) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = request.product,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            RequestDetailRow("Quantity", request.quantity)
            RequestDetailRow("Location", request.location)
            RequestDetailRow("Budget", request.maxBudget, isHighlight = true)
        }
    }
}

@Composable
fun RequestDetailRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isHighlight) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// ── Add Product Screen ────────────────────────────────────────────────────────

@Composable
fun AddProductScreen(
    onProductAdded: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var trader by remember { mutableStateOf("") }
    var market by remember { mutableStateOf("") }
    var stall by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var addedProductName by remember { mutableStateOf("") }

    if (showSuccess) {
        AlertDialog(
            onDismissRequest = { showSuccess = false },
            title = { Text("Product Added!", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier
                            .size(56.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "\"$addedProductName\" has been added to the marketplace and is now visible to buyers.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showSuccess = false }) { Text("OK") }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Add New Product",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "List your product on the JK MarketBridge marketplace.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it; nameError = false },
            label = { Text("Product Name *") },
            isError = nameError,
            supportingText = if (nameError) {
                { Text("Product name is required") }
            } else null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            placeholder = { Text("e.g. Vegetables, Grains, Textiles") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it; priceError = false },
            label = { Text("Price in Kwacha *") },
            placeholder = { Text("e.g. K25/kg  or  K180/50kg bag") },
            isError = priceError,
            supportingText = if (priceError) {
                { Text("Price is required") }
            } else null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity in Stock") },
            placeholder = { Text("e.g. 50 kg") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = trader,
            onValueChange = { trader = it },
            label = { Text("Trader Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = market,
            onValueChange = { market = it },
            label = { Text("Market") },
            placeholder = { Text("e.g. Soweto Market") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = stall,
            onValueChange = { stall = it },
            label = { Text("Stall Number") },
            placeholder = { Text("e.g. Stall B14") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                nameError = name.isBlank()
                priceError = price.isBlank()
                if (!nameError && !priceError) {
                    val finalPrice = when {
                        price.startsWith("K", ignoreCase = true) -> price.trim()
                        else -> "K${price.trim()}"
                    }
                    addedProductName = name.trim()
                    onProductAdded(
                        Product(
                            id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                            name = name.trim(),
                            category = category.ifBlank { "General" },
                            price = finalPrice,
                            trader = trader.ifBlank { "Unknown Trader" },
                            market = market.ifBlank { "Unknown Market" },
                            stall = stall.ifBlank { "Unknown Stall" },
                            quantity = quantity.ifBlank { "Limited" }
                        )
                    )
                    showSuccess = true
                    name = ""; category = ""; price = ""
                    quantity = ""; trader = ""; market = ""; stall = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add to Marketplace", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ── Trader Profile Screen ─────────────────────────────────────────────────────

@Composable
fun TraderProfileScreen(
    productCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile header card
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(88.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = null,
                            modifier = Modifier.size(52.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Alice Banda",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFF9A825),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Verified Trader",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Profile details card
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Trader Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ProfileDetailRow("Phone", "+260 97X XXX XXXX")
                ProfileDetailRow("Market", "Soweto Market")
                ProfileDetailRow("Primary Stall", "Stall B14")
                ProfileDetailRow("Products Listed", "$productCount")
                ProfileDetailRow("Member Since", "January 2023")
                ProfileDetailRow("Rating", "4.8 / 5.0 ★")
            }
        }

        // Verification explanation card
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Why Verification Matters",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Text(
                    text = "Trader verification builds trust in local markets. Verified traders " +
                        "have confirmed their identity, market stall registration, and business " +
                        "legitimacy with JK MarketBridge.\n\n" +
                        "When you see the verified badge, you can trade with confidence — " +
                        "knowing the seller is accountable and their contact details have been " +
                        "checked. Verification reduces fraud and supports honest local commerce " +
                        "across Zambia's markets.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
    }
}
