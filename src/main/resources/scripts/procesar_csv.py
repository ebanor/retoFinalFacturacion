import sys
import csv
import json
import mysql.connector
from mysql.connector import Error

def conectar_bd():
    """Conectar a la base de datos MySQL"""
    try:
        connection = mysql.connector.connect(
            host='localhost',
            port=3306,
            database='reto_final',
            user='root',
            password='root'
        )
        return connection
    except Error as e:
        print(f"Error al conectar a MySQL: {e}")
        return None

def email_existe(cursor, email):
    """Verificar si el email ya existe en la BD"""
    query = "SELECT COUNT(*) FROM clientes WHERE email = %s"
    cursor.execute(query, (email,))
    result = cursor.fetchone()
    return result[0] > 0

def guardar_cliente(cursor, datos):
    """Insertar cliente en la base de datos"""
    query = """
        INSERT INTO clientes (nombre, email, telefono, direccion, empresa) 
        VALUES (%s, %s, %s, %s, %s)
    """
    valores = (
        datos['nombre'],
        datos['email'],
        datos['telefono'],
        datos['direccion'],
        datos['empresa']
    )
    cursor.execute(query, valores)

def formatear_linea_csv(row):
    """Formatear un registro CSV como texto para mostrar en errores"""
    return f"{row.get('nombre', '')},{row.get('email', '')},{row.get('telefono', '')},{row.get('direccion', '')},{row.get('empresa', '')}"

def procesar_csv(file_path):
    resultados = {
        "exitosos": 0,
        "errores": []
    }
    
    # Conectar a la base de datos
    connection = conectar_bd()
    if not connection:
        resultados['errores'].append("No se pudo conectar a la base de datos")
        return resultados
    
    cursor = connection.cursor()
    
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            reader = csv.DictReader(file)
            linea = 1
            
            for row in reader:
                linea += 1
                linea_texto = formatear_linea_csv(row)
                
                # Saltar líneas vacías
                if not any(row.values()):
                    continue
                
                # Validación: nombre obligatorio
                nombre = row.get('nombre', '').strip()
                if not nombre:
                    resultados['errores'].append(
                        f"Línea {linea}: El nombre es obligatorio\n→ {linea_texto}"
                    )
                    continue
                
                # Validación: email obligatorio y formato
                email = row.get('email', '').strip()
                if not email or '@' not in email:
                    resultados['errores'].append(
                        f"Línea {linea}: Email inválido ({email})\n→ {linea_texto}"
                    )
                    continue
                
                # Validación: empresa obligatoria
                empresa = row.get('empresa', '').strip()
                if not empresa:
                    resultados['errores'].append(
                        f"Línea {linea}: La empresa es obligatoria\n→ {linea_texto}"
                    )
                    continue
                
                # Verificar si el email ya existe en la BD
                if email_existe(cursor, email):
                    resultados['errores'].append(
                        f"Línea {linea}: El email {email} ya existe en la base de datos\n→ {linea_texto}"
                    )
                    continue
                
                # Preparar datos para insertar
                datos = {
                    'nombre': nombre,
                    'email': email,
                    'telefono': row.get('telefono', '').strip(),
                    'direccion': row.get('direccion', '').strip(),
                    'empresa': empresa
                }
                
                # Guardar en la base de datos
                try:
                    guardar_cliente(cursor, datos)
                    connection.commit()
                    resultados['exitosos'] += 1
                except Error as e:
                    resultados['errores'].append(
                        f"Línea {linea}: Error al guardar - {str(e)}\n→ {linea_texto}"
                    )
                    connection.rollback()
                    
    except FileNotFoundError:
        resultados['errores'].append(f"Archivo no encontrado: {file_path}")
    except Exception as e:
        resultados['errores'].append(f"Error al leer archivo: {str(e)}")
    finally:
        if cursor:
            cursor.close()
        if connection and connection.is_connected():
            connection.close()
    
    return resultados

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print(json.dumps({"exitosos": 0, "errores": ["Se requiere la ruta del archivo CSV"]}))
        sys.exit(1)
    
    resultado = procesar_csv(sys.argv[1])
    print(json.dumps(resultado))
