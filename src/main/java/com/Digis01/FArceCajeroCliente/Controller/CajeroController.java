package com.Digis01.FArceCajeroCliente.Controller;

import com.Digis01.FArceCajeroCliente.ML.Cajero;
import com.Digis01.FArceCajeroCliente.ML.Result;
import com.Digis01.FArceCajeroCliente.ML.CajeroInventario;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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

        model.addAttribute("cajeros", respuestaCajero.objects);

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

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable int id, Model model) {
        ResponseEntity<CajeroInventario[]> response = restTemplate.getForEntity(
                urlBase + "/getbyid/" + id,
                CajeroInventario[].class
        );

        CajeroInventario[] datos = response.getBody();
        model.addAttribute("datos", datos);

        return "detalleCajero";
    }

    @GetMapping("/retirar/{idcajero}")
    public String mostrarFormularioRetiro(@PathVariable int idcajero, Model model) {
        model.addAttribute("idcajero", idcajero);
        return "retiro";
    }

    @PostMapping("/retirar")
    public String procesarRetiro(@RequestParam int idcajero, @RequestParam double monto, Model model) {
        try {
            ResponseEntity<Result> response = restTemplate.postForEntity(
                    urlBase + "/retirar/" + idcajero + "/" + monto, null, Result.class);
            Result resultado = response.getBody();

            if (resultado != null && resultado.correct) {
                model.addAttribute("resultado", resultado);
            } else {
                model.addAttribute("mensaje", resultado != null ? resultado.errorMessasge : "Ocurrió un error desconocido.");
            }
        } catch (HttpClientErrorException.BadRequest e) {
            // Aquí capturas solo el mensaje plano de error
            String mensajeError = e.getResponseBodyAsString();
            model.addAttribute("mensaje", mensajeError);
        } catch (Exception e) {
            model.addAttribute("mensaje", "Error inesperado: " + e.getMessage());
        }

        return "resultado-retiro";
    }

    @GetMapping("/cliente/simularCambioDia")
    public String simularCambioDiaDesdeCliente(Model model) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(urlBase + "/simularCambioDia", String.class);
            model.addAttribute("mensaje", response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            model.addAttribute("mensaje", "Error: " + ex.getResponseBodyAsString());
        } catch (Exception e) {
            model.addAttribute("mensaje", "Error inesperado: " + e.getMessage());
        }

        return "index"; // la vista HTML que muestra el resultado
    }
}
