package com.example.tarefas.controller

import com.example.tarefas.model.Categoria
import com.example.tarefas.repository.CategoriaRepository
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/categorias")
@Tag(name = "Categorias", description = "Endpoints de categorias")
class CategoriaController(private val repository: CategoriaRepository) {

    @GetMapping
    fun listarTodas(): List<Categoria> = repository.findAll()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): Categoria =
        repository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada") }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun criar(@Valid @RequestBody categoria: Categoria): Categoria = repository.save(categoria)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @Valid @RequestBody categoria: Categoria): Categoria {
        val existente = buscarPorId(id)
        existente.descricao = categoria.descricao
        return repository.save(existente)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletar(@PathVariable id: Long) {
        if (!repository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada")
        }
        repository.deleteById(id)
    }
}
