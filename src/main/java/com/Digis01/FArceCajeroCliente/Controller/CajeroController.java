package com.Digis01.FArceCajeroCliente.Controller;

import com.Digis01.FArceCajeroCliente.ML.Cajero;
import com.Digis01.FArceCajeroCliente.ML.Result;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/cajero")
public class CajeroController {

    String urlBase = "http://localhost:8081/cajeroapi/v1";

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public String Index(Model model) {
        ResponseEntity<Result<Cajero>> responseEntity = restTemplate.exchange(
                urlBase + "/todos",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Cajero>>() {

        });
        Result respuestaCajero = responseEntity.getBody();

        model.addAttribute("cajeros", respuestaCajero);

        return "index";
    }

    @GetMapping("/nuevoCajeroForm")
    public String mostrarFormulario() {
        return "CajeroNuevo"; // Vista del formulario
    }

    @PostMapping("/nuevoCajero")
    public String nuevoCajeroPost(@RequestParam("ubicacion") String ubicacion) {
        String endpoint = urlBase + "/add/" + ubicacion;
        restTemplate.postForObject(endpoint, null, String.class);

        return "redirect:/cajero"; // Redirige a la vista principal o una de confirmación
    }

}
