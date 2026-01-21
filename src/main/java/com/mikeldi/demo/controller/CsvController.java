package com.mikeldi.demo.controller;

import com.mikeldi.demo.service.ClienteService;
import com.mikeldi.demo.service.ClienteService.ResultadoProcesamiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CsvController {
    
    @Autowired
    private ClienteService clienteService;
    
    // Mostrar el formulario inicial
    @GetMapping("/")
    public String mostrarFormulario(Model model) {
        return "index";
    }
    
    // Procesar el archivo CSV subido
    @PostMapping("/upload-csv")
    public String subirCSV(@RequestParam("file") MultipartFile file, Model model) {
        
        // Validación 1: Verificar que se subió un archivo
        if (file.isEmpty()) {
            model.addAttribute("error", "Por favor, selecciona un archivo");
            return "index";
        }
        
        // Validación 2: Verificar que sea un archivo .csv
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
            model.addAttribute("error", "Error: Solo se permiten archivos con extensión .csv");
            return "index";
        }
        
        try {
            // Procesar el archivo CSV
            ResultadoProcesamiento resultado = clienteService.procesarCSV(file);
            
            // Preparar datos para mostrar en la vista
            model.addAttribute("registrosExitosos", resultado.getRegistrosExitosos());
            model.addAttribute("totalErrores", resultado.getErrores().size());
            model.addAttribute("errores", resultado.getErrores());
            
            // Mensaje de éxito
            if (resultado.getRegistrosExitosos() > 0) {
                model.addAttribute("exito", "Se han almacenado correctamente " + 
                    resultado.getRegistrosExitosos() + " registros en la base de datos");
            }
            
            // Si hay errores, agregar advertencia
            if (resultado.tieneErrores()) {
                model.addAttribute("advertencia", "Se encontraron " + 
                    resultado.getErrores().size() + " errores durante el procesamiento");
            }
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar el archivo: " + e.getMessage());
        }
        
        return "index";
    }
}
