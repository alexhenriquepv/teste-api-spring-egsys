package com.example.tarefas.controller;

import com.example.tarefas.model.Categoria;
import com.example.tarefas.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaControllerTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaController categoriaController;

    @Test
    void deveListarTodasAsCategorias() {
        Categoria c1 = new Categoria();
        c1.setDescricao("Trabalho");
        Categoria c2 = new Categoria();
        c2.setDescricao("Estudos");

        when(categoriaRepository.findAll()).thenReturn(List.of(c1, c2));

        List<Categoria> resultado = categoriaController.listarTodas();

        assertEquals(2, resultado.size());
        assertEquals("Trabalho", resultado.get(0).getDescricao());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void deveCriarCategoriaComSucesso() {
        Categoria categoria = new Categoria();
        categoria.setDescricao("Nova Categoria");

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);
        Categoria resultado = categoriaController.criar(categoria);

        assertNotNull(resultado);
        assertEquals("Nova Categoria", resultado.getDescricao());
        verify(categoriaRepository, times(1)).save(categoria);
    }
}
