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

/**
 * Controlador para la carga de facturas desde CSV
 */
@Controller
public class CsvController {
    
    @Autowired
    private FacturaVentaService facturaVentaService;
    
    // Muestra la página principal
    @GetMapping("/")
    public String mostrarFormulario(Model model) {
        return "index";
    }
    
    // Procesa el archivo CSV subido
    @PostMapping("/upload-csv")
    public String subirCSV(@RequestParam("file") MultipartFile file, Model model) {
        
        // Validar que hay archivo
        if (file.isEmpty()) {
            model.addAttribute("error", "Por favor, selecciona un archivo");
            return "index";
        }
        
        // Validar extensión .csv
        String nombreArchivo = file.getOriginalFilename();
        if (nombreArchivo == null || !nombreArchivo.toLowerCase().endsWith(".csv")) {
            model.addAttribute("error", "Error: Solo se permiten archivos con extensión .csv");
            return "index";
        }
        
        try {
            // Procesar el CSV con el servicio
            ResultadoProcesamiento resultado = facturaVentaService.procesarCSV(file);
            
            int facturasGuardadas = resultado.getRegistrosExitosos();
            int cantidadErrores = resultado.getErrores().size();
            
            model.addAttribute("registrosExitosos", facturasGuardadas);
            model.addAttribute("totalErrores", cantidadErrores);
            model.addAttribute("errores", resultado.getErrores());
            
            if (facturasGuardadas > 0) {
                model.addAttribute("exito", "Se han almacenado correctamente " + 
                    facturasGuardadas + " facturas en la base de datos");
            }
            
            if (resultado.tieneErrores()) {
                model.addAttribute("advertencia", "Se encontraron " + 
                    cantidadErrores + " errores durante el procesamiento");
            }
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar el archivo: " + e.getMessage());
        }
        
        return "index";
    }
}
