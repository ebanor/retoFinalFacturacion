package com.mikeldi.demo.service;

import com.mikeldi.demo.repository.FacturaVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FacturaVentaService {
    
    @Autowired
    private FacturaVentaRepository facturaVentaRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String INPUT_DIR = "/shared/input";
    private static final String OUTPUT_DIR = "/shared/output";
    
    public ResultadoProcesamiento procesarCSV(MultipartFile file) {
        List<String> errores = new ArrayList<>();
        int registrosExitosos = 0;
        
        try {
            // Generar ID único para este archivo
            String uniqueId = UUID.randomUUID().toString();
            Path inputFile = Paths.get(INPUT_DIR, uniqueId + ".csv");
            Path outputFile = Paths.get(OUTPUT_DIR, uniqueId + "_result.json");
            
            // Crear directorios si no existen
            Files.createDirectories(inputFile.getParent());
            Files.createDirectories(outputFile.getParent());
            
            // Guardar CSV en carpeta compartida
            Files.copy(file.getInputStream(), inputFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ CSV guardado en: " + inputFile);
            
            // Esperar a que Python procese (máximo 30 segundos)
            int attempts = 0;
            while (!Files.exists(outputFile) && attempts < 30) {
                Thread.sleep(1000);
                attempts++;
                System.out.println("⏳ Esperando resultado... (" + attempts + "/30)");
            }
            
            if (!Files.exists(outputFile)) {
                errores.add("Timeout: El servicio Python no procesó el archivo en 30 segundos");
                // Limpiar archivo de entrada
                Files.deleteIfExists(inputFile);
                return new ResultadoProcesamiento(0, errores);
            }
            
            // Leer resultado JSON
            String jsonResult = Files.readString(outputFile);
            System.out.println("📄 Resultado JSON: " + jsonResult);
            
            if (jsonResult.trim().isEmpty()) {
                errores.add("El script Python devolvió un resultado vacío");
                return new ResultadoProcesamiento(0, errores);
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> resultado = objectMapper.readValue(jsonResult, Map.class);
            
            registrosExitosos = (Integer) resultado.get("exitosos");
            
            @SuppressWarnings("unchecked")
            List<String> erroresPython = (List<String>) resultado.get("errores");
            if (erroresPython != null) {
                errores.addAll(erroresPython);
            }
            
            // Limpiar archivos temporales
            Files.deleteIfExists(outputFile);
            System.out.println("✅ Procesamiento completado: " + registrosExitosos + " registros");
            
        } catch (InterruptedException e) {
            errores.add("Proceso interrumpido: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            errores.add("Error al procesar archivo: " + e.getMessage());
            e.printStackTrace();
        }
        
        return new ResultadoProcesamiento(registrosExitosos, errores);
    }
    
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
