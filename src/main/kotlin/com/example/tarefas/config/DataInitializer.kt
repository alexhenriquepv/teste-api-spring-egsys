package com.example.tarefas.config

import com.example.tarefas.model.Categoria
import com.example.tarefas.model.Tarefa
import com.example.tarefas.model.Usuario
import com.example.tarefas.repository.CategoriaRepository
import com.example.tarefas.repository.TarefaRepository
import com.example.tarefas.repository.UsuarioRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val categoriaRepository: CategoriaRepository,
    private val tarefaRepository: TarefaRepository
) : CommandLineRunner {

    override fun run(vararg args: String) {
        if (usuarioRepository.count() == 0L) {
            val admin = Usuario(login = "admin", senha = passwordEncoder.encode("admin") ?: "")
            usuarioRepository.save(admin)
            println("Usuário padrão criado: admin / admin")

            val c1 = Categoria(descricao = "Trabalho")
            val c2 = Categoria(descricao = "Estudos")
            val c3 = Categoria(descricao = "Casa")

            categoriaRepository.saveAll(listOf(c1, c2, c3))
            println("Categorias padrão criadas: Trabalho, Estudos, Casa")

            val t = Tarefa(
                titulo = "Tarefa padrão",
                descricao = "Descrição da tarefa padrão",
                categoria = c1
            )
            tarefaRepository.save(t)
            println("Tarefa padrão criada: Tarefa padrão")
        }
    }
}
