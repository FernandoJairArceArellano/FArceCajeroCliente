package com.Digis01.FArceCajeroCliente.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Usuario")
public class UsuarioController {

    @GetMapping("/iniciarSesion")
    public String mostrarLogin() {
        return "iniciarSesion";
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
