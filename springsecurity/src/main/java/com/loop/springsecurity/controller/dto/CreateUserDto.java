// Define o pacote onde este DTO está localizado
package com.loop.springsecurity.controller.dto;

// DTO (Data Transfer Object) usado para receber dados
// durante a criação de um novo usuário
public record CreateUserDto(

        // Nome de usuário informado no cadastro
        // Exemplo de JSON:
        // {
        //   "username": "joao",
        //   "password": "123456"
        // }
        String username,

        // Senha informada no cadastro
        // Essa senha ainda NÃO está criptografada
        // A criptografia acontece no backend antes de salvar no banco
        String password
) {
}
