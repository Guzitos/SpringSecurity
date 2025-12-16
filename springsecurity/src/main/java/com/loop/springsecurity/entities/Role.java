// Define o pacote onde esta entidade está localizada
package com.loop.springsecurity.entities;

// Importações do Jakarta Persistence (JPA)
// Usadas para mapear a classe para uma tabela no banco de dados
import jakarta.persistence.*;

// Anotações do Lombok para gerar automaticamente getters e setters
import lombok.Getter;
import lombok.Setter;

// Marca esta classe como uma entidade JPA
// Isso indica que ela será mapeada para uma tabela no banco de dados
@Entity

// Define explicitamente o nome da tabela no banco
@Table(name = "tb_roles")

// Lombok: gera automaticamente os métodos getter
@Getter

// Lombok: gera automaticamente os métodos setter
@Setter
public class Role {

    // Marca este atributo como a chave primária da tabela
    @Id

    // Define que o valor da chave primária será gerado automaticamente
    // IDENTITY geralmente usa auto-increment do banco
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // Define o nome da coluna no banco de dados
    @Column(name = "role_id")
    private Long roleId;

    // Nome da role (ex: ADMIN, BASIC)
    // Será usado no controle de autorização
    private String name;

    // Enum interno que representa os valores possíveis de Role
    // Serve como uma forma segura e centralizada de referenciar roles no código
    public enum Values {

        // Role de administrador
        // Geralmente possui permissões mais altas
        ADMIN(1L),

        // Role básica
        // Geralmente atribuída a usuários comuns
        BASIC(2L);

        // ID da role no banco de dados
        // Mantém o vínculo entre o enum e o registro da tabela
        long roleId;

        // Construtor do enum
        // Associa cada valor do enum a um ID específico
        Values(long roleId) {
            this.roleId = roleId;
        }
    }
}
