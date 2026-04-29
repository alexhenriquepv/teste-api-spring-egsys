package com.example.tarefas.controller

import com.example.tarefas.model.Categoria
import com.example.tarefas.repository.CategoriaRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull


@ExtendWith(MockitoExtension::class)
class CategoriaControllerTest {

    @Mock
    private lateinit var categoriaRepository: CategoriaRepository

    @InjectMocks
    private lateinit var categoriaController: CategoriaController

    @Test
    fun deveListarTodasAsCategorias() {
        val c1 = Categoria(descricao = "Trabalho")
        val c2 = Categoria(descricao = "Estudos")

        `when`(categoriaRepository.findAll()).thenReturn(listOf(c1, c2))

        val resultado = categoriaController.listarTodas()

        assertEquals(2, resultado.size)
        assertEquals("Trabalho", resultado[0].descricao)
        verify(categoriaRepository, times(1)).findAll()
    }

    @Test
    fun deveCriarCategoriaComSucesso() {
        val categoria = Categoria(descricao = "Nova Categoria")

        `when`(categoriaRepository.save(any(Categoria::class.java))).thenReturn(categoria)

        val resultado = categoriaController.criar(categoria)

        assertNotNull(resultado)
        assertEquals("Nova Categoria", resultado.descricao)
        verify(categoriaRepository, times(1)).save(categoria)
    }
}
