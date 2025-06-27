package com.Digis01.FArceCajeroCliente.Controller;

import com.Digis01.FArceCajeroCliente.ML.Result;
import com.Digis01.FArceCajeroCliente.ML.Usuario;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/Usuario")
public class UsuarioController {

    String urlBase = "http://localhost:8081/usuarioapi/v1";

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public String mostrarLogin() {
        return "iniciarSesion";
    }

    @GetMapping("/index")
    public String mostrarUsuarios(Model model) {
        ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(
                urlBase,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Usuario>>() {
        });

        Result respuestaUsuario = responseEntity.getBody();

        model.addAttribute("usuarios", respuestaUsuario.objects);

        return "usuarioIndex";
    }

//    @RequestMapping("/iniciarSesion")
//    public String login(@RequestParam(required = false) String error, Model model) {
//        if (error != null) {
//            model.addAttribute("alertType", "danger");
//            model.addAttribute("alertMessage", "Credenciales incorrectas. Por favor, inténtalo de nuevo.");
//        }
//        return "login";
//    }
}
