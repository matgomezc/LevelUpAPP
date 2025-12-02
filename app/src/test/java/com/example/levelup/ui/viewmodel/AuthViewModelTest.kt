package com.example.levelup.ui.viewmodel

import android.app.Application
import app.cash.turbine.test
import com.example.levelup.data.local.DataStoreManager
import com.example.levelup.data.model.User
import com.example.levelup.data.repository.UserRepository
import com.example.levelup.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AuthViewModel
    private lateinit var application: Application
    private lateinit var userRepository: UserRepository
    private lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setup() {
        application = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        dataStoreManager = mockk(relaxed = true)

        // Configurar comportamiento por defecto para init
        coEvery { dataStoreManager.isLoggedIn } returns flowOf(false)
        coEvery { dataStoreManager.userId } returns flowOf(null)

        viewModel = AuthViewModel(application, userRepository, dataStoreManager)
    }

    @Test
    fun `login success updates state to logged in`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password"
        val user = User(id = 1, name = "Test User", email = email, password = password)
        
        coEvery { userRepository.login(email, password) } returns user
        coEvery { dataStoreManager.setLoggedIn(1) } returns Unit

        // When
        viewModel.login(email, password)

        // Then
        // Turbine captura el flujo de estados. El primer estado puede ser el inicial o uno intermedio de loading
        // Dependiendo de cómo coroutines despacha.
        viewModel.uiState.test {
            // Ignorar estados iniciales si es necesario, o verificar secuencia exacta
            // Skip initial state
            skipItems(1) 
            
            // Estado de loading (a veces pasa tan rápido que se salta en tests con UnconfinedDispatcher, 
            // pero aquí estamos usando StandardTestDispatcher implícitamente si no se cambió en Rule, 
            // pero MainDispatcherRule usa Unconfined por defecto en mi implementación anterior... 
            // Si usa Unconfined, los launch suceden inmediatamente)
            
            // Al usar UnconfinedTestDispatcher, el launch se ejecuta inmediatamente.
            // El estado final ya debería estar listo.
            val state = awaitItem()
            
            // Si el primer item capturado es loading, esperamos el siguiente
            if (state.isLoading) {
                val finalState = awaitItem()
                assertEquals(true, finalState.isLoggedIn)
                assertEquals(user, finalState.currentUser)
            } else {
                assertEquals(true, state.isLoggedIn)
                assertEquals(user, state.currentUser)
            }
            
            cancelAndIgnoreRemainingEvents()
        }
        
        coVerify { dataStoreManager.setLoggedIn(1) }
    }

    @Test
    fun `login failure updates state with error`() = runTest {
        // Given
        val email = "wrong@example.com"
        val password = "wrong"
        
        coEvery { userRepository.login(email, password) } returns null

        // When
        viewModel.login(email, password)

        // Then
        viewModel.uiState.test {
            skipItems(1) // Skip initial
            
            val state = awaitItem()
            if (state.isLoading) {
                val finalState = awaitItem()
                assertEquals(false, finalState.isLoggedIn)
                assertEquals("Email o contraseña incorrectos", finalState.errorMessage)
            } else {
                assertEquals(false, state.isLoggedIn)
                assertEquals("Email o contraseña incorrectos", state.errorMessage)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `register success updates state`() = runTest {
         // Given
        val name = "New User"
        val email = "new@example.com"
        val password = "password"
        val newUser = User(id = 2, name = name, email = email, password = password)
        
        coEvery { userRepository.getUserByEmail(email) } returns null
        coEvery { userRepository.insertUser(any()) } returns 2
        coEvery { userRepository.getUserById(2) } returns newUser
        coEvery { dataStoreManager.setLoggedIn(2) } returns Unit

        // When
        viewModel.register(name, email, password)

        // Then
        viewModel.uiState.test {
            skipItems(1)
            val state = awaitItem()
            
             if (state.isLoading) {
                val finalState = awaitItem()
                assertEquals(true, finalState.isLoggedIn)
                assertEquals(newUser, finalState.currentUser)
            } else {
                assertEquals(true, state.isLoggedIn)
                assertEquals(newUser, state.currentUser)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }
}