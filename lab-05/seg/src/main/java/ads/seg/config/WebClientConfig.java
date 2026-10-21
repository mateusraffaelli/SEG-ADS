package ads.seg.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
/**
 * Classe de configuração do WebClient para realizar chamadas à API do GitHub.
 */
@Configuration
public class WebClientConfig {
    /**
     * Método que retorna um objeto WebClient.Builder para realizar chamadas à API do GitHub.
     */
    @Bean
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}