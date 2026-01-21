package com.mikeldi.demo.service;

import com.mikeldi.demo.entity.Cliente;
import com.mikeldi.demo.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public ResultadoProcesamiento procesarCSV(MultipartFile file) {
        List<String> errores = new ArrayList<>();
        int registrosExitosos = 0;
        int lineaActual = 0;
        
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            
            String linea;
            boolean esPrimeraLinea = true;
            
            while ((linea = br.readLine()) != null) {
                lineaActual++;
                
                // Saltar la cabecera (primera línea)
                if (esPrimeraLinea) {
                    esPrimeraLinea = false;
                    continue;
                }
                
                // Saltar líneas vacías
                if (linea.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    // Dividir la línea por comas
                    String[] datos = linea.split(",");
                    
                    // Validar que tenga el número correcto de columnas
                    if (datos.length != 5) {
                        errores.add("Línea " + lineaActual + ": Número incorrecto de columnas (esperadas: 5, encontradas: " + datos.length + ")");
                        continue;
                    }
                    
                    // Extraer datos
                    String nombre = datos[0].trim();
                    String email = datos[1].trim();
                    String telefono = datos[2].trim();
                    String direccion = datos[3].trim();
                    String empresa = datos[4].trim();
                    
                    // Validaciones básicas
                    if (nombre.isEmpty()) {
                        errores.add("Línea " + lineaActual + ": El nombre es obligatorio");
                        continue;
                    }
                    
                    if (email.isEmpty() || !email.contains("@")) {
                        errores.add("Línea " + lineaActual + ": Email inválido (" + email + ")");
                        continue;
                    }
                    
                    if (empresa.isEmpty()) {
                        errores.add("Línea " + lineaActual + ": La empresa es obligatoria");
                        continue;
                    }
                    
                    // Verificar si el email ya existe en la BD
                    if (clienteRepository.existsByEmail(email)) {
                        errores.add("Línea " + lineaActual + ": El email " + email + " ya existe en la base de datos");
                        continue;
                    }
                    
                    // Crear y guardar el cliente
                    Cliente cliente = new Cliente(nombre, email, telefono, direccion, empresa);
                    clienteRepository.save(cliente);
                    registrosExitosos++;
                    
                } catch (Exception e) {
                    errores.add("Línea " + lineaActual + ": Error al procesar - " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            errores.add("Error al leer el archivo: " + e.getMessage());
        }
        
        return new ResultadoProcesamiento(registrosExitosos, errores);
    }
    
    // Clase interna para devolver resultados
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
