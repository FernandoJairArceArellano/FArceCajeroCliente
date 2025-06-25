package com.Digis01.FArceCajeroCliente.Controller;

import com.Digis01.FArceCajeroCliente.ML.Cajero;
import com.Digis01.FArceCajeroCliente.ML.Result;
import com.Digis01.FArceCajeroCliente.ML.CajeroInventario;
import com.Digis01.FArceCajeroCliente.ML.ItemEntregado;
import java.util.ArrayList;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/nuevoCajero")
    public String nuevoCajeroPost(@RequestParam("ubicacion") String ubicacion, RedirectAttributes redirectAttributes) {
        try {
            String nuevoCajero = urlBase + "/add/" + ubicacion;
            String response = restTemplate.postForObject(nuevoCajero, null, String.class);

            redirectAttributes.addFlashAttribute("alertMessage", response);
            redirectAttributes.addFlashAttribute("alertType", "success");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("alertMessage", "Error al crear el cajero: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("alertType", "danger");
        }

        return "redirect:/cajero";
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
            ResponseEntity<Result<ItemEntregado>> response = restTemplate.exchange(
                    urlBase + "/retirar/" + idcajero + "/" + monto,
                    HttpMethod.POST,
                    null,
                    new ParameterizedTypeReference<Result<ItemEntregado>>() {
            });

            Result<ItemEntregado> resultado = response.getBody();

            if (resultado != null && resultado.correct) {
                model.addAttribute("resultado", resultado);
            } else {
                model.addAttribute("mensaje", resultado != null ? resultado.errorMessasge : "Ocurrió un error desconocido.");
            }
        } catch (HttpClientErrorException.BadRequest ex) {
            String mensajeError = ex.getResponseBodyAsString();
            model.addAttribute("mensaje", mensajeError);
        } catch (Exception ex) {
            model.addAttribute("mensaje", "Error inesperado: " + ex.getMessage());
        }

        return "resultado-retiro";
    }

    @GetMapping("/simularCambioDia")
    public String simularCambioDiaDesdeCliente(Model model) {
        // Simulación del cambio de día
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(urlBase + "/simularCambioDia", String.class);
            model.addAttribute("alertMessage", response.getBody());
            model.addAttribute("alertType", "success");
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            model.addAttribute("alertMessage", "Error: " + ex.getResponseBodyAsString());
            model.addAttribute("alertType", "danger");
        } catch (Exception e) {
            model.addAttribute("alertMessage", "Error inesperado: " + e.getMessage());
            model.addAttribute("alertType", "danger");
        }

        // Cargar lista de cajeros
        try {
            ResponseEntity<Result<Cajero>> responseEntity = restTemplate.exchange(
                    urlBase + "/todos",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<Cajero>>() {
            });
            Result<Cajero> respuestaCajero = responseEntity.getBody();
            model.addAttribute("cajeros", respuestaCajero.objects);
        } catch (Exception e) {
            model.addAttribute("cajeros", new ArrayList<>());
            model.addAttribute("alertMessage", "Error al cargar cajeros: " + e.getMessage());
            model.addAttribute("alertType", "danger");
        }

        return "index";
    }

    @GetMapping("/detalle/simularCambioDia/{id}")
    public String simularCambioDiaParaCajero(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(urlBase + "/simularCambioDia/" + id, String.class);
            redirectAttributes.addFlashAttribute("alertMessage", response.getBody());
            redirectAttributes.addFlashAttribute("alertType", "success");
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            redirectAttributes.addFlashAttribute("alertMessage", "Error: " + ex.getResponseBodyAsString());
            redirectAttributes.addFlashAttribute("alertType", "danger");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("alertMessage", "Error inesperado: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("alertType", "danger");
        }

        return "redirect:/cajero/detalle/" + id;
    }

}
