package com.example.tarefas.config;

import com.example.tarefas.model.Categoria;
import com.example.tarefas.model.Tarefa;
import com.example.tarefas.model.Usuario;
import com.example.tarefas.repository.CategoriaRepository;
import com.example.tarefas.repository.TarefaRepository;
import com.example.tarefas.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoriaRepository categoriaRepository;
    private final TarefaRepository tarefaRepository;

    public DataInitializer(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            CategoriaRepository categoriaRepository,
            TarefaRepository tarefaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoriaRepository = categoriaRepository;
        this.tarefaRepository = tarefaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario("admin", passwordEncoder.encode("admin"));
            usuarioRepository.save(admin);
            System.out.println("Usuário padrão criado: admin / admin");

            Categoria c = new Categoria();
            c.setDescricao("Categoria padrão");
            categoriaRepository.save(c);
            System.out.println("Categoria padrão criada: Categoria padrão");

            Tarefa t = new Tarefa();
            t.setTitulo("Tarefa padrão");
            t.setDescricao("Descrição da tarefa padrão");
            t.setCategoria(c);
            tarefaRepository.save(t);
            System.out.println("Tarefa padrão criada: Tarefa padrão");

        }
    }
}
