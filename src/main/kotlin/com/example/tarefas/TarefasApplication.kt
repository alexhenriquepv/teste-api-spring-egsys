package com.example.tarefas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.slf4j.LoggerFactory

@SpringBootApplication
class TarefasApplication {
    private val log = LoggerFactory.getLogger(TarefasApplication::class.java)

    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReady() {
        log.info("=========================================================")
        log.info("🚀 APLICAÇÃO RODANDO COM SUCESSO! ")
        log.info("🔗 Link da aplicação: http://localhost:8080")
        log.info("📄 Swagger UI:        http://localhost:8080/swagger-ui.html")
        log.info("=========================================================")
    }
}

fun main(args: Array<String>) {
    runApplication<TarefasApplication>(*args)
}
