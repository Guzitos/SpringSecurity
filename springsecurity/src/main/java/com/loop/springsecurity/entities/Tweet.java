// Define o pacote onde esta entidade está localizada
package com.loop.springsecurity.entities;

// Importações do Jakarta Persistence (JPA)
// Usadas para mapear a classe para uma tabela no banco de dados
import jakarta.persistence.*;

// Anotações do Lombok para gerar automaticamente getters e setters
import lombok.Getter;
import lombok.Setter;

// Anotação do Hibernate que preenche automaticamente a data de criação
import org.hibernate.annotations.CreationTimestamp;

// Classe usada para representar data e hora em UTC
import java.time.Instant;

// Marca esta classe como uma entidade JPA
// Indica que ela será persistida no banco de dados
@Entity

// Define explicitamente o nome da tabela no banco de dados
@Table(name = "tb_tweets")

// Lombok: gera automaticamente os getters
@Getter

// Lombok: gera automaticamente os setters
@Setter
public class Tweet {

    // Marca este atributo como a chave primária da tabela
    @Id

    // Define a estratégia de geração do ID
    // SEQUENCE usa uma sequência do banco de dados
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    // Define o nome da coluna no banco de dados
    @Column(name = "tweet_id")
    private Long tweetId;

    // Define um relacionamento muitos-para-um (ManyToOne)
    // Muitos tweets podem pertencer a um único usuário
    @ManyToOne

    // Define a coluna que faz a ligação com a tabela de usuários
    // user_id será a chave estrangeira (FK)
    @JoinColumn(name = "user_id")
    private User user;

    // Conteúdo textual do tweet
    private String content;

    // Campo preenchido automaticamente no momento da criação do registro
    // Não precisa ser definido manualmente no código
    @CreationTimestamp
    private Instant creationTimestamp;
}
