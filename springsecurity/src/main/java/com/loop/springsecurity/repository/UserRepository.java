// Define o pacote onde este repositório está localizado
package com.loop.springsecurity.repository;

// Importa a entidade User
// Representa a tabela de usuários no banco de dados
import com.loop.springsecurity.entities.User;

// Interface base do Spring Data JPA
// Fornece automaticamente métodos CRUD
import org.springframework.data.jpa.repository.JpaRepository;

// Anotação que indica que esta interface é um repositório Spring
import org.springframework.stereotype.Repository;

// Classe usada para representar valores que podem ou não existir
import java.util.Optional;

// Classe usada para representar identificadores no formato UUID
import java.util.UUID;

// Marca esta interface como um repositório Spring
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Método de consulta customizado
    // O Spring Data JPA cria automaticamente a query com base no nome do método
    //
    // Busca um usuário pelo campo "username"
    // Retorna Optional para evitar NullPointerException
    Optional<User> findByUsername(String username);
}
