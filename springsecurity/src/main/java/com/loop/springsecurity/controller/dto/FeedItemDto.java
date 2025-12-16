// Define o pacote onde este DTO está localizado
package com.loop.springsecurity.controller.dto;

// DTO (Data Transfer Object) que representa um único item do feed
// Cada instância corresponde a um tweet individual
public record FeedItemDto(

        // Identificador único do tweet
        long tweetId,

        // Conteúdo textual do tweet
        String content,

        // Nome do usuário que criou o tweet
        // Esse valor normalmente vem de tweet.getUser().getUsername()
        String usernas
) {
}
