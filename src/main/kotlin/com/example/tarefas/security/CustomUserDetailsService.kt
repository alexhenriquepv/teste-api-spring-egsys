package com.example.tarefas.security

import com.example.tarefas.repository.UsuarioRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val usuarioRepository: UsuarioRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val usuario = usuarioRepository.findByLogin(username)
            ?: throw UsernameNotFoundException("Usuário não encontrado: \$username")

        return User.builder()
            .username(usuario.login)
            .password(usuario.senha)
            .roles("USER")
            .build()
    }
}
