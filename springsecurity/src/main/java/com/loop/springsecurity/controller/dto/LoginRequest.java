// Define o pacote onde este DTO está localizado
package com.loop.springsecurity.controller.dto;

// DTO (Data Transfer Object) usado para receber os dados de login
// Enviado no corpo da requisição para o endpoint /login
public record LoginRequest(

        // Nome de usuário informado no login
        // Exemplo de JSON:
        // {
        //   "username": "admin",
        //   "password": "123"
        // }
        String username,

        // Senha informada pelo usuário
        // Essa senha será comparada com a senha criptografada no banco
        String password
) {
}
