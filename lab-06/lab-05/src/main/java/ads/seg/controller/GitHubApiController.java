package ads.seg.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
/**
 * Classe de controle para realizar chamadas à API do GitHub.
 */
@Controller
public class GitHubApiController {
    @Autowired
    private WebClient.Builder webClientBuilder;
    /**
     * Método que retorna a página 'repositorios.html' com a lista de repositórios do usuário autenticado.
     * @param model Objeto Model para adicionar atributos à página HTML
     * @param authorizedClient Objeto OAuth2AuthorizedClient com as informações do cliente autorizado
     * @return String com o nome da página HTML a ser exibida na aplicação web
     */
    @GetMapping("/repos")
    public String getRepos(Model model, @RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient
            authorizedClient) {
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
// Chamada à API do GitHub para obter os repositórios
        List<Object> repos = webClientBuilder.build()
                .get()
                .uri("https://api.github.com/user/repos")
                .headers(headers -> headers.setBearerAuth(accessToken.getTokenValue()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
// Adiciona a lista de repositórios ao modelo
        model.addAttribute("repos", repos);
        return "repositorios"; // Retorna a página 'repositorios.html'
    }
}