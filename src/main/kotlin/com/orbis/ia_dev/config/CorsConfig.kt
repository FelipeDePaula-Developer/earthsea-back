import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig() : WebMvcConfigurer{
    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**") // Permite todas as rotas
                    .allowedOrigins("http://172.20.0.4:3000") // Permite requisições do frontend
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
                    .allowedHeaders("*") // Permite todos os headers
                    .allowCredentials(true) // Permite envio de cookies (se necessário)
            }
        }
    }
}