// Define o pacote onde esta entidade está localizada
package com.loop.springsecurity.entities;

// Importa o DTO usado no processo de login
// Ele contém os dados enviados pelo usuário (username e password)
import com.loop.springsecurity.controller.dto.LoginRequest;

// Importações do Jakarta Persistence (JPA)
// Usadas para mapear a classe para uma tabela no banco de dados
import jakarta.persistence.*;

// Anotações do Lombok para gerar automaticamente getters e setters
import lombok.Getter;
import lombok.Setter;

// Interface do Spring Security usada para validar senhas
import org.springframework.security.crypto.password.PasswordEncoder;

// Estruturas de dados usadas para armazenar roles e UUID
import java.util.Set;
import java.util.UUID;

// Marca esta classe como uma entidade JPA
@Entity

// Define explicitamente o nome da tabela no banco de dados
@Table(name = "tb_users")

// Lombok: gera automaticamente os getters
@Getter

// Lombok: gera automaticamente os setters
@Setter
public class User {

    // Marca este atributo como a chave primária da tabela
    @Id

    // Define que o ID será gerado automaticamente no formato UUID
    @GeneratedValue(strategy = GenerationType.UUID)

    // Define o nome da coluna no banco de dados
    @Column(name = "user_id")
    private UUID userID;

    // Username do usuário
    // A anotação unique = true garante que não haverá dois usuários com o mesmo username
    @Column(unique = true)
    private String username;

    // Senha do usuário
    // Este valor será armazenado já criptografado no banco
    private String password;

    // Define um relacionamento muitos-para-muitos (ManyToMany)
    // Um usuário pode ter várias roles
    // Uma role pode pertencer a vários usuários
    @ManyToMany(

            // CascadeType.ALL propaga operações (save, delete, etc)
            // para a tabela de relacionamento
            cascade = CascadeType.ALL,

            // FetchType.EAGER indica que as roles serão carregadas
            // automaticamente junto com o usuário
            fetch = FetchType.EAGER
    )

    // Define a tabela intermediária que liga usuários e roles
    @JoinTable(
            name = "tb_users_roles",

            // Coluna que referencia o usuário
            joinColumns = @JoinColumn(name = "user_id"),

            // Coluna que referencia a role
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // Método responsável por validar se a senha informada no login está correta
    public boolean isLoginCorrect(

            // DTO contendo a senha digitada pelo usuário
            LoginRequest loginRequest,

            // Encoder usado para comparar a senha digitada
            // com a senha criptografada armazenada no banco
            PasswordEncoder passwordEncoder
    ) {

        // Compara a senha em texto puro (digitada)
        // com a senha criptografada salva no banco
        return passwordEncoder.matches(
                loginRequest.password(),
                this.password
        );
    }
}
