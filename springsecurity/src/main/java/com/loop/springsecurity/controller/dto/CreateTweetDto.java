// Define o pacote onde este DTO está localizado
package com.loop.springsecurity.controller.dto;

// DTO (Data Transfer Object) usado para receber dados
// durante a criação de um novo tweet
public record CreateTweetDto(

        // Conteúdo do tweet enviado no corpo da requisição
        // Exemplo de JSON:
        // {
        //   "content": "Meu primeiro tweet!"
        // }
        String content
) {
}
