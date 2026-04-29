package com.example.tarefas.controller;

import com.example.tarefas.model.Categoria;
import com.example.tarefas.model.Tarefa;
import com.example.tarefas.repository.CategoriaRepository;
import com.example.tarefas.repository.TarefaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarefaControllerTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private TarefaController tarefaController;

    @Test
    void deveCriarTarefaComSucesso() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Estudar JUnit");
        tarefa.setCategoria(categoria);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        Tarefa resultado = tarefaController.criar(tarefa);

        assertNotNull(resultado);
        assertEquals("Estudar JUnit", resultado.getTitulo());
        verify(categoriaRepository, times(1)).findById(1L);
        verify(tarefaRepository, times(1)).save(tarefa);
    }

    @Test
    void deveLancarExcecaoAoCriarTarefaComCategoriaInexistente() {
        Categoria categoria = new Categoria();
        categoria.setId(99L);

        Tarefa tarefa = new Tarefa();
        tarefa.setCategoria(categoria);

        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            tarefaController.criar(tarefa);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Categoria não encontrada", exception.getReason());
        verify(tarefaRepository, never()).save(any(Tarefa.class));
    }

    @Test
    void deveLancarExcecaoAoCriarTarefaSemCategoria() {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Sem Categoria");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            tarefaController.criar(tarefa);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Categoria é obrigatória", exception.getReason());
        verify(categoriaRepository, never()).findById(anyLong());
        verify(tarefaRepository, never()).save(any(Tarefa.class));
    }

    @Test
    void deveDeletarTarefaComSucesso() {
        when(tarefaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(tarefaRepository).deleteById(1L);

        assertDoesNotThrow(() -> tarefaController.deletar(1L));

        verify(tarefaRepository, times(1)).existsById(1L);
        verify(tarefaRepository, times(1)).deleteById(1L);
    }
}
