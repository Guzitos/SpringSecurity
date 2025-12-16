// Define o pacote de configuração de segurança da aplicação
package com.loop.springsecurity.config;

// Importações da biblioteca Nimbus JOSE + JWT
// Usadas para criação e manipulação de chaves criptográficas e JWT
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

// Importações do Spring
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Importações de chaves RSA
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

// Marca esta classe como uma classe de configuração do Spring
@Configuration

// Habilita a configuração de segurança web do Spring Security
@EnableWebSecurity

// Habilita segurança em nível de método
// Permite usar anotações como @PreAuthorize
@EnableMethodSecurity
public class SecurityConfig {

    // Injeta a chave pública RSA a partir do application.properties / application.yml
    // Usada para VALIDAR tokens JWT
    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    // Injeta a chave privada RSA a partir do application.properties / application.yml
    // Usada para ASSINAR tokens JWT
    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    // Define a cadeia de filtros de segurança do Spring Security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Desabilita proteção CSRF
                // Normalmente desabilitado em APIs stateless com JWT
                .csrf(csrf -> csrf.disable())

                // Configura a política de sessão como STATELESS
                // O servidor não mantém sessão do usuário
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Define as regras de autorização das requisições HTTP
                .authorizeHttpRequests(auth -> auth

                        // Permite criar usuários sem autenticação
                        .requestMatchers(HttpMethod.POST, "/users/**").permitAll()

                        // Permite login sem autenticação
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()

                        // Permite acesso à rota de erro
                        .requestMatchers("/error").permitAll()

                        // Qualquer outra requisição exige autenticação
                        .anyRequest().authenticated()
                )

                // Configura o servidor como Resource Server OAuth2
                // Indica que a autenticação será feita via JWT
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                );

        // Constrói e retorna o filtro de segurança configurado
        return http.build();
    }

    // Bean responsável por GERAR (assinar) tokens JWT
    @Bean
    public JwtEncoder jwtEncoder() {

        // Cria um JWK (JSON Web Key) usando a chave pública e privada RSA
        JWK jwk = new RSAKey.Builder(this.publicKey)
                .privateKey(privateKey)
                .build();

        // Cria um conjunto imutável de chaves JWK
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        // Retorna o encoder que assina o JWT usando RSA
        return new NimbusJwtEncoder(jwks);
    }

    // Bean responsável por VALIDAR tokens JWT
    @Bean
    public JwtDecoder jwtDecoder() {

        // Cria um decoder que valida o JWT usando a chave pública RSA
        return NimbusJwtDecoder
                .withPublicKey(publicKey)
                .build();
    }

    // Bean do BCryptPasswordEncoder
    // Usado para criptografar e validar senhas
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
