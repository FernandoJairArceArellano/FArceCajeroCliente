package com.Digis01.FArceCajeroCliente.Controller;

import com.Digis01.FArceCajeroCliente.ML.Result;
import com.Digis01.FArceCajeroCliente.ML.Usuario;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/Usuario")
public class UsuarioController {

    String urlBase = "http://localhost:8081/usuarioapi/v1";

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public String mostrarLogin(@RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("alertMessage", "❌ Usuario o contraseña inválidos.");
        }
        if (logout != null) {
            model.addAttribute("alertMessage", "👋 Has cerrado sesión.");
        }
        return "iniciarSesion";
    }

    @GetMapping("/index")
    public String mostrarUsuarios(HttpServletRequest request, Model model) {
        // Obtén la cookie JSESSIONID
        Cookie[] cookies = request.getCookies();
        String jSessionId = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    jSessionId = cookie.getValue();
                    break;
                }
            }
        }

        HttpHeaders headers = new HttpHeaders();
        if (jSessionId != null) {
            headers.add("Cookie", "JSESSIONID=" + jSessionId);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(
                urlBase,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Result<Usuario>>() {
        }
        );

        Result respuestaUsuario = responseEntity.getBody();

        model.addAttribute("usuarios", respuestaUsuario.objects);

        return "usuarioIndex";
    }

    @GetMapping("/access-denied")
    public String accesoDenegado(@RequestParam String mensaje,
            @RequestParam String rutaIntentada,
            Model model) {
        model.addAttribute("mensaje", mensaje);
        model.addAttribute("rutaIntentada", rutaIntentada);
        return "access-denied"; // Asegúrate que access-denied.html esté en /templates/
    }
}
