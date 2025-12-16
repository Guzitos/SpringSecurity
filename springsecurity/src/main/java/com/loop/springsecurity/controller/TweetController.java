// Define o pacote onde este controller está localizado
package com.loop.springsecurity.controller;

// DTO usado para receber os dados necessários para criar um tweet
import com.loop.springsecurity.controller.dto.CreateTweetDto;

// DTO que representa a resposta do feed (lista de tweets + paginação)
import com.loop.springsecurity.controller.dto.FeedDto;

// DTO que representa cada item individual do feed
import com.loop.springsecurity.controller.dto.FeedItemDto;

// Entidade Role, usada para verificar permissões (ex: ADMIN)
import com.loop.springsecurity.entities.Role;

// Entidade Tweet, que representa a tabela de tweets no banco
import com.loop.springsecurity.entities.Tweet;

// Repositório responsável por acessar os tweets no banco de dados
import com.loop.springsecurity.repository.TweetRepository;

// Repositório responsável por acessar os usuários no banco de dados
import com.loop.springsecurity.repository.UserRepository;

// Interface do Spring Data que representa uma página de resultados
import org.springframework.data.domain.Page;

// Classe usada para criar requisições de paginação
import org.springframework.data.domain.PageRequest;

// Classe usada para definir ordenação (ASC, DESC)
import org.springframework.data.domain.Sort;

// Enum com códigos HTTP (200, 403, 404, etc)
import org.springframework.http.HttpStatus;

// Classe usada para montar respostas HTTP
import org.springframework.http.ResponseEntity;

// Representa o usuário autenticado via JWT
// Contém as informações extraídas do token
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

// Anotações para mapear endpoints REST
import org.springframework.web.bind.annotation.*;

// Exceção usada para retornar erros HTTP automaticamente
import org.springframework.web.server.ResponseStatusException;

// Classe usada para trabalhar com UUID
import java.util.UUID;

// Marca essa classe como um Controller REST
@RestController
public class TweetController {

    // Repositório de tweets (injeção de dependência)
    private final TweetRepository tweetRepository;

    // Repositório de usuários (injeção de dependência)
    private final UserRepository userRepository;

    // Construtor onde o Spring injeta os repositórios automaticamente
    public TweetController(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    // Endpoint GET /feed
    // Retorna o feed de tweets com paginação
    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(

            // Parâmetro de query "page"
            // Caso não seja enviado, o valor padrão será 0
            @RequestParam(value = "page", defaultValue = "0") int page,

            // Parâmetro de query "pageSize"
            // Caso não seja enviado, o valor padrão será 10
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {

        // Busca os tweets no banco de forma paginada
        // Ordena do mais recente para o mais antigo usando creationTimestamp
        var tweets = tweetRepository.findAll(
                        PageRequest.of(
                                page,                 // número da página
                                pageSize,             // tamanho da página
                                Sort.Direction.DESC,  // ordem decrescente
                                "creationTimestamp"   // campo usado na ordenação
                        )
                )

                // Converte cada Tweet em um FeedItemDto
                .map(tweet -> new FeedItemDto(
                        tweet.getTweetId(),               // ID do tweet
                        tweet.getContent(),               // conteúdo do tweet
                        tweet.getUser().getUsername()     // username do autor
                ));

        // Retorna HTTP 200 com o FeedDto
        // Inclui: lista de tweets, página atual, tamanho da página,
        // total de páginas e total de elementos
        return ResponseEntity.ok(
                new FeedDto(
                        tweets.getContent(),        // lista de tweets da página atual
                        page,                        // página atual
                        pageSize,                    // tamanho da página
                        tweets.getTotalPages(),      // total de páginas
                        tweets.getTotalElements()    // total de tweets
                )
        );
    }

    // Endpoint POST /tweets
    // Cria um novo tweet
    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(

            // Corpo da requisição contendo o conteúdo do tweet
            @RequestBody CreateTweetDto dto,

            // Token JWT do usuário autenticado
            JwtAuthenticationToken token
    ) {

        // Recupera o usuário logado a partir do ID presente no token (subject)
        var user = userRepository.findById(
                UUID.fromString(token.getName())
        );

        // Cria uma nova entidade Tweet
        var tweet = new Tweet();

        // Define o usuário dono do tweet
        tweet.setUser(user.get());

        // Define o conteúdo do tweet
        tweet.setContent(dto.content());

        // Salva o tweet no banco de dados
        tweetRepository.save(tweet);

        // Retorna HTTP 200 sem corpo
        return ResponseEntity.ok().build();
    }

    // Endpoint DELETE /tweets/{id}
    // Remove um tweet pelo ID
    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<Void> deleteTweet(

            // ID do tweet vindo da URL
            @PathVariable("id") Long tweetId,

            // Token JWT do usuário autenticado
            JwtAuthenticationToken token
    ) {

        // Busca o usuário logado pelo ID presente no token
        var user = userRepository.findById(
                UUID.fromString(token.getName())
        );

        // Busca o tweet pelo ID
        // Caso não exista, retorna HTTP 404
        var tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND)
                );

        // Verifica se o usuário possui a role ADMIN
        var isAdmin = user.get()
                .getRoles()
                .stream()
                .anyMatch(role ->
                        role.getName().equalsIgnoreCase(
                                Role.Values.ADMIN.name()
                        )
                );

        // Permite deletar o tweet se:
        // 1) O usuário for ADMIN
        // OU
        // 2) O tweet pertencer ao próprio usuário logado
        if (isAdmin ||
                tweet.getUser()
                        .getUserID()
                        .equals(UUID.fromString(token.getName()))
        ) {

            // Remove o tweet do banco
            tweetRepository.deleteById(tweetId);

        } else {

            // Caso o usuário não tenha permissão,
            // retorna HTTP 403 (Forbidden)
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        // Se tudo deu certo, retorna HTTP 200
        return ResponseEntity.ok().build();
    }
}
