// Define o pacote onde este DTO está localizado
package com.loop.springsecurity.controller.dto;

// DTO (Data Transfer Object) usado como resposta do endpoint de login
// Retorna as informações necessárias para o cliente se autenticar
public record LoginResponse(

        // Token JWT gerado após o login bem-sucedido
        // Esse token deve ser enviado nas próximas requisições
        // no header: Authorization: Bearer <token>
        String accessToken,

        // Tempo de expiração do token em segundos
        // Exemplo: 300 = 5 minutos
        Long expiresIn
) {
}
