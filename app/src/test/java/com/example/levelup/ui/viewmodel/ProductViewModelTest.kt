package com.example.levelup.ui.viewmodel

import android.app.Application
import app.cash.turbine.test
import com.example.levelup.data.model.Product
import com.example.levelup.data.repository.ProductRepository
import com.example.levelup.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ProductViewModel
    private lateinit var application: Application
    private lateinit var productRepository: ProductRepository

    @Before
    fun setup() {
        application = mockk(relaxed = true)
        productRepository = mockk(relaxed = true)
        
        // Mock para el seeder llamado en init (evitar errores)
        // Esto asume que el seeder llama a getAllProducts o inserta algo. 
        // Si el seeder es llamado, debemos asegurarnos que no rompa el test.
        // Como es un mock relaxed, las llamadas devolverán valores por defecto.
        
        // Sin embargo, loadProducts() se llama en init, así que configuramos el mock antes de instanciar el VM
        // para que la llamada inicial tenga un estado conocido si quisiéramos probar init.
        coEvery { productRepository.getAllProducts() } returns emptyList()
        
        viewModel = ProductViewModel(application, productRepository)
    }

    @Test
    fun `loadProducts updates state successfully`() = runTest {
        // Given
        val products = listOf(
            Product(id = 1, name = "Product 1", price = 100.0, category = "Test"),
            Product(id = 2, name = "Product 2", price = 200.0, category = "Test")
        )
        coEvery { productRepository.getAllProducts() } returns products

        // When
        viewModel.loadProducts()

        // Then
        viewModel.uiState.test {
            // El primer estado emitido puede ser el resultado de la carga en init
            // O el estado inicial vacío.
            // Dependiendo de la velocidad, el loadProducts llamado en el test generará nuevos estados.
            
            // Saltamos lo que haya pasado en init
            skipItems(1) 
             
            val state = awaitItem()
            if (state.isLoading) {
                val finalState = awaitItem()
                assertEquals(products, finalState.products)
                assertEquals(false, finalState.isLoading)
            } else {
                // Si fue tan rápido que ya terminó
                if (state.products == products) {
                     assertEquals(products, state.products)
                } else {
                    // Esperamos uno más
                     val finalState = awaitItem()
                     assertEquals(products, finalState.products)
                }
            }
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `loadProductsByCategory updates state successfully`() = runTest {
        // Given
        val category = "Consolas"
        val products = listOf(
            Product(id = 1, name = "PS5", price = 500.0, category = category)
        )
        coEvery { productRepository.getProductsByCategory(category) } returns products

        // When
        viewModel.loadProductsByCategory(category)

        // Then
        viewModel.uiState.test {
            skipItems(1)
            
            val state = awaitItem()
            if (state.isLoading) {
                val finalState = awaitItem()
                assertEquals(products, finalState.products)
            } else {
                 if (state.products == products) {
                     assertEquals(products, state.products)
                } else {
                     val finalState = awaitItem()
                     assertEquals(products, finalState.products)
                }
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadProducts error updates state with error message`() = runTest {
        // Given
        val errorMessage = "Network Error"
        coEvery { productRepository.getAllProducts() } throws RuntimeException(errorMessage)

        // When
        viewModel.loadProducts()

        // Then
        viewModel.uiState.test {
            skipItems(1)
            
            val state = awaitItem()
            if (state.isLoading) {
                val finalState = awaitItem()
                assertEquals("Error al cargar productos: $errorMessage", finalState.errorMessage)
            } else {
                 if (state.errorMessage == "Error al cargar productos: $errorMessage") {
                     assertEquals("Error al cargar productos: $errorMessage", state.errorMessage)
                } else {
                     val finalState = awaitItem()
                     assertEquals("Error al cargar productos: $errorMessage", finalState.errorMessage)
                }
            }
            cancelAndIgnoreRemainingEvents()
        }
    }
}