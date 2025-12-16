// Define o pacote onde este controller está localizado
package com.loop.springsecurity.controller;

// DTO usado para receber os dados necessários para criar um novo usuário
import com.loop.springsecurity.controller.dto.CreateUserDto;

// Entidade Role, que representa as permissões do sistema
import com.loop.springsecurity.entities.Role;

// Entidade User, que representa o usuário no banco de dados
import com.loop.springsecurity.entities.User;

// Repositório responsável por acessar as roles no banco
import com.loop.springsecurity.repository.RoleRepository;

// Repositório responsável por acessar os usuários no banco
import com.loop.springsecurity.repository.UserRepository;

// Enum que contém os códigos HTTP (201, 422, 500, etc)
import org.springframework.http.HttpStatus;

// Classe usada para montar respostas HTTP
import org.springframework.http.ResponseEntity;

// Anotação usada para restringir acesso a métodos com base em permissões
import org.springframework.security.access.prepost.PreAuthorize;

// Encoder que usa BCrypt para criptografar senhas
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Anotações do Spring para mapear endpoints REST
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Exceção usada para retornar erros HTTP automaticamente
import org.springframework.web.server.ResponseStatusException;

// Estruturas de dados usadas para listas e conjuntos
import java.util.List;
import java.util.Set;

// Marca essa classe como um Controller REST
@RestController
public class UserController {

    // Repositório de usuários (injeção de dependência)
    private final UserRepository userRepository;

    // Repositório de roles (injeção de dependência)
    private final RoleRepository roleRepository;

    // Encoder responsável por criptografar a senha do usuário
    private final BCryptPasswordEncoder passwordEncoder;

    // Construtor onde o Spring injeta automaticamente as dependências
    public UserController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Endpoint POST /users
    // Responsável por criar um novo usuário
    @PostMapping("/users")
    public ResponseEntity<Void> newUser(

            // Corpo da requisição contendo username e password
            @RequestBody CreateUserDto dto
    ) {

        // 1️⃣ Busca a role BASIC no banco de dados
        // Essa role será atribuída por padrão ao novo usuário
        Role basicRole = roleRepository
                .findByName(Role.Values.BASIC.name())

                // Caso a role não exista, retorna erro 500
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Role BASIC não encontrada"
                        )
                );

        // Verifica se já existe um usuário com o mesmo username
        if (userRepository.findByUsername(dto.username()).isPresent()) {

            // Se existir, retorna HTTP 422 (Unprocessable Entity)
            // Indica que os dados enviados são inválidos para criação
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Cria a entidade User
        User user = new User();

        // Define o username informado no DTO
        user.setUsername(dto.username());

        // Criptografa a senha antes de salvar no banco
        user.setPassword(
                passwordEncoder.encode(dto.password())
        );

        // Define a role padrão do usuário como BASIC
        user.setRoles(Set.of(basicRole));

        // Salva o usuário no banco de dados
        userRepository.save(user);

        // Retorna HTTP 201 (Created) sem corpo
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    // Endpoint GET /users
    // Retorna a lista de todos os usuários
    @GetMapping("/users")

    // Restringe o acesso apenas a usuários com autoridade ADMIN
    // "SCOPE_ADMIN" vem da claim "scope" presente no JWT
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> listUsers() {

        // Busca todos os usuários no banco
        var users = userRepository.findAll();

        // Retorna HTTP 200 com a lista de usuários
        return ResponseEntity.ok(users);
    }
}
