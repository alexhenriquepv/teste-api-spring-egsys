package com.example.tarefas.security

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider {

    @Value("\${jwt.secret:SuperSecretKeyForJwtAuthenticationThatMustBeLongEnough123456789}")
    private lateinit var jwtSecret: String

    @Value("\${jwt.expiration:86400000}")
    private var jwtExpirationInMs: Int = 86400000

    private val key by lazy { Keys.hmacShaKeyFor(jwtSecret.toByteArray()) }

    fun generateToken(authentication: Authentication): String {
        val username = authentication.name
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs)

        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }

    fun getUsernameFromJWT(token: String): String {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(authToken)
            return true
        } catch (ex: Exception) {
            println("Token JWT inválido: \${ex.message}")
        }
        return false
    }
}
