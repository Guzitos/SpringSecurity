// Define o pacote onde este repositório está localizado
package com.loop.springsecurity.repository;

// Importa a entidade Tweet
// Representa a tabela de tweets no banco de dados
import com.loop.springsecurity.entities.Tweet;

// Interface do Spring Data que representa resultados paginados
import org.springframework.data.domain.Page;

// Interface base do Spring Data JPA
// Fornece automaticamente operações CRUD
import org.springframework.data.jpa.repository.JpaRepository;

// Anotação que indica que esta interface é um repositório Spring
import org.springframework.stereotype.Repository;

// Marca esta interface como um repositório
@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    // Nenhum método customizado é definido aqui
    // Todos os métodos básicos já são fornecidos pelo JpaRepository, como:
    // - save(Tweet tweet)
    // - findById(Long id)
    // - findAll()
    // - findAll(Pageable pageable)
    // - deleteById(Long id)
    // - delete(Tweet tweet)

}
