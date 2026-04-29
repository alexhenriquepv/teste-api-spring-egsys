package com.example.tarefas.controller

import com.example.tarefas.model.Tarefa
import com.example.tarefas.repository.CategoriaRepository
import com.example.tarefas.repository.TarefaRepository
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
@RequestMapping("/tarefas")
@Tag(name = "Tarefas", description = "Endpoints para gerenciamento de tarefas")
class TarefaController(
    private val tarefaRepository: TarefaRepository,
    private val categoriaRepository: CategoriaRepository
) {

    @GetMapping
    fun listarTodas(): List<Tarefa> = tarefaRepository.findAll()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): Tarefa =
        tarefaRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada") }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun criar(@Valid @RequestBody tarefa: Tarefa): Tarefa {
        if (tarefa.categoria?.id == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria é obrigatória")
        }

        val categoria = categoriaRepository.findById(tarefa.categoria!!.id!!)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não encontrada") }

        tarefa.categoria = categoria
        return tarefaRepository.save(tarefa)
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @Valid @RequestBody tarefaAtualizada: Tarefa): Tarefa {
        val existente = buscarPorId(id)

        if (tarefaAtualizada.categoria?.id == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria é obrigatória")
        }

        val categoria = categoriaRepository.findById(tarefaAtualizada.categoria!!.id!!)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não encontrada") }

        existente.titulo = tarefaAtualizada.titulo
        existente.descricao = tarefaAtualizada.descricao
        existente.categoria = categoria

        return tarefaRepository.save(existente)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletar(@PathVariable id: Long) {
        if (!tarefaRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada")
        }
        tarefaRepository.deleteById(id)
    }
}
