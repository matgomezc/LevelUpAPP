package com.example.levelup.data.seed

import com.example.levelup.data.model.Product
import com.example.levelup.data.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Datos iniciales de productos gaming
object ProductSeeder {
    
    // Lista de productos de ejemplo con imágenes
    val initialProducts = listOf(
        Product(
            name = "Mouse Gamer RGB",
            price = 29990.0,
            category = "Mouses",
            description = "Mouse gaming con iluminación RGB y 8000 DPI",
            stock = 15,
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQypdMCkIlcsSTW0VrrGG9iPtRJxmJT_Fviaw&s"
        ),
        Product(
            name = "Teclado Mecánico",
            price = 49990.0,
            category = "Accesorios",
            description = "Teclado mecánico con switches RGB",
            stock = 8,
            imageUrl = "https://rimage.ripley.cl/home.ripley/Attachment/MKP/6787/MPM00082167014/full_image-1.png"
        ),
        Product(
            name = "PlayStation 5",
            price = 499990.0,
            category = "Consolas",
            description = "Consola PlayStation 5 con control DualSense",
            stock = 3,
            imageUrl = "https://www.facilitea.com/on/demandware.static/-/Sites-promocaixa-m-catalog/default/dw33be0a78/electronica/Gaming/Consolas/121-4007379/121-4007379_1_600x600.png"
        ),
        Product(
            name = "PC Gamer RTX 4060",
            price = 899990.0,
            category = "PC gamers",
            description = "PC gaming con RTX 4060, 16GB RAM, SSD 512GB",
            stock = 5,
            imageUrl = "https://cdnx.jumpseller.com/lotendras-cl/image/49894670/thumb/719/719?1755816987"
        ),
        Product(
            name = "Silla Gamer Ergonómica",
            price = 149990.0,
            category = "Sillas gamers",
            description = "Silla gaming con soporte lumbar y reposabrazos ajustables",
            stock = 10,
            imageUrl = "https://cdnx.jumpseller.com/hypelegend/image/15690436/WHITE-RGB.png?1701708440"
        ),
        Product(
            name = "Mousepad RGB XL",
            price = 19990.0,
            category = "Mousepad",
            description = "Mousepad gaming con iluminación RGB y tamaño XL",
            stock = 20,
            imageUrl = "https://trulustore.cl/wp-content/uploads/2025/03/frontline_xl_pd_2000x2000_01.png"
        ),
        Product(
            name = "Polera LevelUp Gamer",
            price = 14990.0,
            category = "Poleras personalizadas",
            description = "Polera personalizada con logo LevelUp Gamer",
            stock = 25,
            imageUrl = "https://themadplug.es/cdn/shop/files/Corteiz-Island-Stencil-Tee-Black-Photoroom_ad925fba-9741-4edf-a0af-8e9277f5067d.png?v=1725388998"
        ),
        Product(
            name = "Ajedrez",
            price = 24990.0,
            category = "Juegos de mesa",
            description = "Set de ajedrez temático gaming",
            stock = 12,
            imageUrl = "https://recrearte.cl/cdn/shop/products/AjedrezTableropiezasmaderaBlancoyNegro2_aaf8aa03-eea2-4971-b66e-0ee2d6dbf962_1080x.png?v=1620000149"
        )
    )
    
    // Función para inicializar productos si la base de datos está vacía
    fun seedProducts(repository: ProductRepository, scope: CoroutineScope) {
        scope.launch {
            val existingProducts = repository.getAllProducts()
            if (existingProducts.isEmpty()) {
                // Agregar todos los productos iniciales con timestamp
                val currentTime = System.currentTimeMillis()
                initialProducts.forEach { product ->
                    val productWithTimestamp = product.copy(createdAt = currentTime)
                    repository.insertProduct(productWithTimestamp)
                }
            }
        }
    }
}

