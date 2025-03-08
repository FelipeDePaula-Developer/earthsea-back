package com.earthsea.ia_dev.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { } // Habilita o CORS usando a configuração abaixo
            .csrf { it.disable() } // Desativa CSRF para evitar bloqueios em requisições POST
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/chat/generate/*", "/cad/*").permitAll() // Libera esses endpoints
                    .anyRequest().authenticated() // Protege os demais endpoints
            }

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        // Se quiser permitir qualquer origem sem credenciais, defina allowCredentials = false e allowedOrigins = listOf("*")
        // Caso precise enviar cookies/credenciais, substitua "*" por domínios específicos e mantenha allowCredentials = true
        configuration.allowedOrigins = listOf("http://localhost:3000", "http://172.18.0.4:3000")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
