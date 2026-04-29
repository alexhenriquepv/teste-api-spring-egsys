package com.example.tarefas.repository

import com.example.tarefas.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface UsuarioRepository : JpaRepository<Usuario, Long> {
    fun findByLogin(login: String): Usuario?
}
