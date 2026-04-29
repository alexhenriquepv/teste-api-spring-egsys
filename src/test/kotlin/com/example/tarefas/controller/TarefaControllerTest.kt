package com.example.tarefas.controller

import com.example.tarefas.model.Categoria
import com.example.tarefas.model.Tarefa
import com.example.tarefas.repository.CategoriaRepository
import com.example.tarefas.repository.TarefaRepository
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
import org.mockito.Mockito.doNothing
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class TarefaControllerTest {

    @Mock
    private lateinit var tarefaRepository: TarefaRepository

    @Mock
    private lateinit var categoriaRepository: CategoriaRepository

    @InjectMocks
    private lateinit var tarefaController: TarefaController

    @Test
    fun deveCriarTarefaComSucesso() {
        val categoria = Categoria(id = 1L, descricao = "Trabalho")
        val tarefa = Tarefa(titulo = "Estudar Kotlin", categoria = categoria)

        `when`(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria))
        `when`(tarefaRepository.save(any(Tarefa::class.java))).thenReturn(tarefa)

        val resultado = tarefaController.criar(tarefa)

        assertNotNull(resultado)
        assertEquals("Estudar Kotlin", resultado.titulo)
        verify(categoriaRepository, times(1)).findById(1L)
        verify(tarefaRepository, times(1)).save(tarefa)
    }

    @Test
    fun deveLancarExcecaoAoCriarTarefaComCategoriaInexistente() {
        val categoria = Categoria(id = 99L, descricao = "Desconhecida")
        val tarefa = Tarefa(titulo = "Fazer algo", categoria = categoria)

        `when`(categoriaRepository.findById(99L)).thenReturn(Optional.empty())

        val exception = assertThrows<ResponseStatusException> {
            tarefaController.criar(tarefa)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
        assertEquals("Categoria não encontrada", exception.reason)
        verify(tarefaRepository, never()).save(any(Tarefa::class.java))
    }

    @Test
    fun deveLancarExcecaoAoCriarTarefaSemCategoria() {
        val tarefa = Tarefa(titulo = "Sem Categoria")

        val exception = assertThrows<ResponseStatusException> {
            tarefaController.criar(tarefa)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
        assertEquals("Categoria é obrigatória", exception.reason)
        verify(categoriaRepository, never()).findById(anyLong())
        verify(tarefaRepository, never()).save(any(Tarefa::class.java))
    }

    @Test
    fun deveDeletarTarefaComSucesso() {
        `when`(tarefaRepository.existsById(1L)).thenReturn(true)
        doNothing().`when`(tarefaRepository).deleteById(1L)

        assertDoesNotThrow { tarefaController.deletar(1L) }

        verify(tarefaRepository, times(1)).existsById(1L)
        verify(tarefaRepository, times(1)).deleteById(1L)
    }
}
