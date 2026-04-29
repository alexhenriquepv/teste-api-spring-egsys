package com.example.tarefas.repository

import com.example.tarefas.model.Tarefa
import org.springframework.data.jpa.repository.JpaRepository

interface TarefaRepository : JpaRepository<Tarefa, Long>
