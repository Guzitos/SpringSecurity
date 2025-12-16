// Define o pacote onde este repositório está localizado
package com.loop.springsecurity.repository;

// Importa a entidade Role
// Essa entidade representa a tabela de roles no banco de dados
import com.loop.springsecurity.entities.Role;

// Interface base do Spring Data JPA
// Fornece automaticamente métodos CRUD (save, findById, findAll, delete, etc)
import org.springframework.data.jpa.repository.JpaRepository;

// Anotação que indica que esta interface é um repositório Spring
import org.springframework.stereotype.Repository;

// Classe usada para representar valores que podem ou não existir
import java.util.Optional;

// Marca esta interface como um repositório
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Método de consulta customizado
    // O Spring Data JPA gera automaticamente a query com base no nome do método
    //
    // Busca uma Role pelo campo "name"
    // Retorna Optional para evitar NullPointerException
    Optional<Role> findByName(String name);

}
