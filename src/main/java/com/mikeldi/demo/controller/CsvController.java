package com.mikeldi.demo.controller;

import com.mikeldi.demo.service.FacturaVentaService;
import com.mikeldi.demo.service.FacturaVentaService.ResultadoProcesamiento;
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
    private FacturaVentaService facturaVentaService;
    
    @GetMapping("/")
    public String mostrarFormulario(Model model) {
        return "index";
    }
    
    @PostMapping("/upload-csv")
    public String subirCSV(@RequestParam("file") MultipartFile file, Model model) {
        
        if (file.isEmpty()) {
            model.addAttribute("error", "Por favor, selecciona un archivo");
            return "index";
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
            model.addAttribute("error", "Error: Solo se permiten archivos con extensión .csv");
            return "index";
        }
        
        try {
            ResultadoProcesamiento resultado = facturaVentaService.procesarCSV(file);
            
            model.addAttribute("registrosExitosos", resultado.getRegistrosExitosos());
            model.addAttribute("totalErrores", resultado.getErrores().size());
            model.addAttribute("errores", resultado.getErrores());
            
            if (resultado.getRegistrosExitosos() > 0) {
                model.addAttribute("exito", "Se han almacenado correctamente " + 
                    resultado.getRegistrosExitosos() + " facturas en la base de datos");
            }
            
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
