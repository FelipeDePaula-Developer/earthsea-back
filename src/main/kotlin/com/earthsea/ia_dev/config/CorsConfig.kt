package com.earthsea.ia_dev.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() } // ⚠️ Desabilita CSRF para evitar bloqueios em requisições POST
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/chat/generate", "/cad/client").permitAll() // Libera esses endpoints
                    .anyRequest().authenticated() // Protege os demais endpoints
            }
//            .formLogin { it.disable() } // Remove login padrão
//            .httpBasic { it.disable() } // Remove autenticação básica

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        //configuration.allowedOrigins = listOf("http://172.18.0.4:3000", "http://localhost:3000")
        configuration.allowedOrigins = listOf("*") // 🔧 Permite qualquer origem temporariamente para testar
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
