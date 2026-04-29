package com.example.tarefas.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@Entity
class Tarefa(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @NotBlank(message = "O título é obrigatório")
    var titulo: String? = null,

    var descricao: String? = null,

    @NotNull(message = "A categoria é obrigatória")
    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    var categoria: Categoria? = null,

    var dataHora: LocalDateTime? = null
) {
    @PrePersist
    fun prePersist() {
        dataHora = LocalDateTime.now()
    }

    @PreUpdate
    fun preUpdate() {
        dataHora = LocalDateTime.now()
    }
}
