package com.mikeldi.demo.service;

import com.mikeldi.demo.repository.FacturaVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Servicio para procesar facturas desde archivos CSV usando Python
 */
@Service
public class FacturaVentaService {
    
    @Autowired
    private FacturaVentaRepository facturaVentaRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public ResultadoProcesamiento procesarCSV(MultipartFile file) {
        List<String> errores = new ArrayList<>();
        int registrosExitosos = 0;
        
        try {
            // Guardar archivo temporalmente
            Path tempFile = Files.createTempFile("upload_", ".csv");
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
            
            // Obtener ruta del script Python
            ClassPathResource scriptResource = new ClassPathResource("scripts/procesar_facturas.py");
            String scriptPath = scriptResource.getFile().getAbsolutePath();
            
            // Ejecutar script Python
            ProcessBuilder processBuilder = new ProcessBuilder(
                "python", 
                scriptPath, 
                tempFile.toString()
            );
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            
            // Leer salida JSON del script
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                // Parsear resultado JSON
                @SuppressWarnings("unchecked")
                Map<String, Object> resultado = objectMapper.readValue(
                    output.toString(), 
                    Map.class
                );
                
                registrosExitosos = (Integer) resultado.get("exitosos");
                
                @SuppressWarnings("unchecked")
                List<String> erroresPython = (List<String>) resultado.get("errores");
                if (erroresPython != null) {
                    errores.addAll(erroresPython);
                }
            } else {
                errores.add("Error al ejecutar script Python (código: " + exitCode + ")");
                errores.add("Salida: " + output.toString());
            }
            
            // Eliminar archivo temporal
            Files.deleteIfExists(tempFile);
            
        } catch (Exception e) {
            errores.add("Error al procesar archivo: " + e.getMessage());
            e.printStackTrace();
        }
        
        return new ResultadoProcesamiento(registrosExitosos, errores);
    }
    
    /**
     * Clase para encapsular el resultado del procesamiento
     */
    public static class ResultadoProcesamiento {
        private int registrosExitosos;
        private List<String> errores;
        
        public ResultadoProcesamiento(int registrosExitosos, List<String> errores) {
            this.registrosExitosos = registrosExitosos;
            this.errores = errores;
        }
        
        public int getRegistrosExitosos() {
            return registrosExitosos;
        }
        
        public List<String> getErrores() {
            return errores;
        }
        
        public boolean tieneErrores() {
            return !errores.isEmpty();
        }
    }
}
