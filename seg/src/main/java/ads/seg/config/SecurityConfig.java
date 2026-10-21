package ads.seg.config;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * Classe de configuração do Spring Security
 */
@Controller
public class SecurityConfig {
    /**
     * Configuração do filtro de segurança para habilitar o login OAuth2
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
// Autoriza qualquer requisição autenticada
        http.authorizeHttpRequests(
                authorizeRequests -> authorizeRequests.anyRequest().authenticated()
        ).oauth2Login(Customizer.withDefaults()); // Habilita o login OAuth2
        return http.build();
    }
    /**
     * Método que retorna a página inicial da aplicação com o nome do usuário autenticado.
     *
     * @param model Objeto Model para adicionar atributos à página HTML
     * @param principal Objeto OAuth2User com os dados do usuário autenticado
     * @return String com o nome da página HTML a ser exibida na aplicação web
     */
    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            model.addAttribute("nome", principal.getAttribute("name"));
        }
        return "index";
    }
}