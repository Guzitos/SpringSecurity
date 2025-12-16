// Define o pacote onde este DTO está localizado
package com.loop.springsecurity.controller.dto;

// Estrutura de dados usada para armazenar listas
import java.util.List;

// DTO (Data Transfer Object) que representa a resposta do feed de tweets
// Ele encapsula tanto os dados quanto as informações de paginação
public record FeedDto(

        // Lista de itens do feed (cada item representa um tweet)
        List<FeedItemDto> feedItens,

        // Número da página atual (começa normalmente em 0)
        int page,

        // Quantidade de itens por página
        int pageSize,

        // Total de páginas disponíveis
        int totalPages,

        // Total de elementos existentes (todos os tweets)
        long totalElements
) {
}
