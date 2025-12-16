// Define o pacote de configuração da aplicação
package com.loop.springsecurity.config;

// Importa a entidade Role
// Usada para criar ou buscar permissões no banco
import com.loop.springsecurity.entities.Role;

// Importa a entidade User
// Usada para criar o usuário administrador
import com.loop.springsecurity.entities.User;

// Repositório responsável por acessar as roles no banco de dados
import com.loop.springsecurity.repository.RoleRepository;

// Repositório responsável por acessar os usuários no banco de dados
import com.loop.springsecurity.repository.UserRepository;

// Anotação que indica que o método roda dentro de uma transação
import jakarta.transaction.Transactional;

// Interface usada para executar código automaticamente ao subir a aplicação
import org.springframework.boot.CommandLineRunner;

// Marca esta classe como uma classe de configuração do Spring
import org.springframework.context.annotation.Configuration;

// Encoder usado para criptografar a senha do usuário admin
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Estrutura de dados usada para definir o conjunto de roles do usuário
import java.util.Set;

// Indica que esta classe é uma configuração do Spring
@Configuration

// Implementa CommandLineRunner
// Isso faz com que o método run seja executado automaticamente
// quando a aplicação Spring Boot inicia
public class AdminUserConfig implements CommandLineRunner {

    // Repositório de roles
    private final RoleRepository roleRepository;

    // Repositório de usuários
    private final UserRepository userRepository;

    // Encoder de senha (BCrypt)
    private final BCryptPasswordEncoder passwordEncoder;

    // Construtor com injeção de dependências
    public AdminUserConfig(
            RoleRepository roleRepository,
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Método executado automaticamente ao iniciar a aplicação
    @Override

    // Garante que todas as operações com banco
    // aconteçam dentro de uma única transação
    @Transactional
    public void run(String... args) {

        // =========================
        // CRIA OU BUSCA ROLE ADMIN
        // =========================
        Role adminRole = roleRepository
                .findByName(Role.Values.ADMIN.name())

                // Caso não exista no banco, cria a role ADMIN
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(Role.Values.ADMIN.name());
                    return roleRepository.save(role);
                });

        // =========================
        // CRIA OU BUSCA ROLE BASIC
        // =========================
        Role basicRole = roleRepository
                .findByName(Role.Values.BASIC.name())

                // Caso não exista no banco, cria a role BASIC
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(Role.Values.BASIC.name());
                    return roleRepository.save(role);
                });

        // =========================
        // CRIA USUÁRIO ADMIN PADRÃO
        // =========================
        userRepository
                .findByUsername("admin")

                // Se o usuário existir
                .ifPresentOrElse(

                        // Caso exista, apenas exibe uma mensagem no console
                        user -> System.out.println("Usuário admin já existe"),

                        // Caso NÃO exista, cria o usuário admin
                        () -> {
                            User user = new User();

                            // Define o username do admin
                            user.setUsername("admin");

                            // Criptografa a senha antes de salvar
                            user.setPassword(
                                    passwordEncoder.encode("123")
                            );

                            // Define a role ADMIN para o usuário
                            user.setRoles(Set.of(adminRole));

                            // Salva o usuário no banco
                            userRepository.save(user);
                        }
                );
    }
}
