//package com.Digis01.FArceCajeroCliente.Controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class ErrorController {
//
//    @GetMapping("/access-denied")
//    public String accesoDenegado(@RequestParam String mensaje,
//            @RequestParam String rutaIntentada,
//            Model model) {
//        model.addAttribute("mensaje", mensaje);
//        model.addAttribute("rutaIntentada", rutaIntentada);
//        return "access-denied"; // Asegúrate que access-denied.html esté en /templates/
//    }
//}
