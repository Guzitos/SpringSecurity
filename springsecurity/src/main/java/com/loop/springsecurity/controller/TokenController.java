// Define o pacote onde esse controller está localizado
// Normalmente segue a estrutura base do projeto
package com.loop.springsecurity.controller;

// Importa o DTO que representa o corpo da requisição de login (username e password)
import com.loop.springsecurity.controller.dto.LoginRequest;

// Importa o DTO que representa a resposta do login (token JWT e tempo de expiração)
import com.loop.springsecurity.controller.dto.LoginResponse;

// Importa a entidade Role, usada para extrair as permissões do usuário
import com.loop.springsecurity.entities.Role;

// Importa o repositório responsável por acessar os dados do usuário no banco
import com.loop.springsecurity.repository.UserRepository;

// Classe do Spring usada para construir respostas HTTP de forma flexível
import org.springframework.http.ResponseEntity;

// Exceção lançada quando usuário ou senha são inválidos
import org.springframework.security.authentication.BadCredentialsException;

// Encoder de senha que usa o algoritmo BCrypt
// Serve para comparar a senha digitada com a senha criptografada no banco
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Classe que representa o conjunto de claims (informações) do JWT
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

// Interface responsável por gerar (assinar) o token JWT
import org.springframework.security.oauth2.jwt.JwtEncoder;

// Classe que empacota os parâmetros necessários para o encoder gerar o token
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

// Anotação que indica que a classe é um controller REST
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Classe usada para trabalhar com data e hora no formato UTC
import java.time.Instant;

// Utilitário para transformar streams em Strings (join, map, etc)
import java.util.stream.Collectors;

// Marca essa classe como um Controller REST
// Isso permite que ela receba requisições HTTP e retorne JSON
@RestController
public class TokenController {

    // Responsável por gerar o JWT
    private final JwtEncoder jwtEncoder;

    // Repositório para buscar usuários no banco de dados
    private final UserRepository userRepository;

    // Encoder usado para validar a senha do usuário
    private final BCryptPasswordEncoder passwordEncoder;

    // Construtor com injeção de dependências
    // O Spring injeta automaticamente essas dependências em tempo de execução
    public TokenController(
            JwtEncoder jwtEncoder,
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Mapeia requisições HTTP POST para o endpoint /login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            // Indica que o corpo da requisição será convertido para LoginRequest
            @RequestBody LoginRequest loginRequest
    ) {

        // Busca o usuário no banco pelo username informado
        var user = userRepository.findByUsername(loginRequest.username());

        // Verifica duas coisas:
        // 1) Se o usuário não existe
        // 2) Se a senha informada não corresponde à senha criptografada no banco
        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            // Caso alguma das validações falhe, lança exceção de credenciais inválidas
            throw new BadCredentialsException("user or password invalid!");
        }

        // Captura o instante atual (hora atual em UTC)
        var now = Instant.now();

        // Define o tempo de expiração do token em segundos (300s = 5 minutos)
        var expiresIn = 300L;

        // Extrai as roles do usuário
        // Converte cada Role em String (nome da role)
        // Junta tudo em uma única String separada por espaço
        // Exemplo: "ROLE_ADMIN ROLE_USER"
        var scopes = user.get()
                .getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        // Cria o conjunto de informações (claims) que irão dentro do JWT
        var claims = JwtClaimsSet.builder()

                // Define quem emitiu o token
                .issuer("mybackend")

                // Define o "dono" do token (normalmente o ID do usuário)
                .subject(user.get().getUserID().toString())

                // Define quando o token irá expirar
                .expiresAt(now.plusSeconds(expiresIn))

                // Define quando o token foi emitido
                .issuedAt(now)

                // Claim customizada que armazena as permissões do usuário
                .claim("scope", scopes)

                // Finaliza a construção do objeto de claims
                .build();

        // Gera o token JWT a partir das claims
        // O encoder assina o token usando a chave configurada no Spring Security
        var jwtValue = jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();

        // Retorna HTTP 200 (OK) com o token e o tempo de expiração no corpo da resposta
        return ResponseEntity.ok(
                new LoginResponse(jwtValue, expiresIn)
        );
    }
}
