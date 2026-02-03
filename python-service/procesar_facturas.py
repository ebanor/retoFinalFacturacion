import sys
import csv
import json
import mysql.connector
from mysql.connector import Error
from decimal import Decimal

def conectar_bd():
    """Conecta a la base de datos MySQL"""
    try:
        connection = mysql.connector.connect(
            host='db',
            port=3306,
            database='reto_final',
            user='root',
            password='root'
        )
        return connection
    except Error as e:
        print(json.dumps({"exitosos": 0, "errores": [f"Error al conectar a MySQL: {e}"]}))
        return None

def numero_factura_existe(cursor, numeroFactura):
    """Verifica si el número de factura ya existe en la BD"""
    query = "SELECT COUNT(*) FROM facturaventa WHERE numero_factura = %s"
    cursor.execute(query, (numeroFactura,))
    result = cursor.fetchone()
    return result[0] > 0

def guardar_factura(cursor, datos):
    """Inserta la factura en la base de datos"""
    query = """
        INSERT INTO facturaventa 
        (numero_factura, serie, pedido_id, cliente_id, fecha_emision, fecha_vencimiento, 
         estado_pago, base_imponible, iva_total, total_factura, moneda, usuario_emisor_id) 
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    """
    valores = (
        datos['numeroFactura'],
        datos['serie'],
        datos['pedidoId'],
        datos['clienteId'],
        datos['fechaEmision'],
        datos['fechaVencimiento'],
        datos['estadoPago'],
        datos['baseImponible'],
        datos['ivaTotal'],
        datos['totalFactura'],
        datos['moneda'],
        datos['usuarioEmisorId']
    )
    cursor.execute(query, valores)

def formatear_linea_csv(row):
    """Formatea una línea del CSV para mostrarla en los errores"""
    return (f"{row.get('numeroFactura', '')},{row.get('serie', '')},{row.get('pedidoId', '')},"
            f"{row.get('clienteId', '')},{row.get('fechaEmision', '')},{row.get('fechaVencimiento', '')},"
            f"{row.get('estadoPago', '')},{row.get('baseImponible', '')},{row.get('ivaTotal', '')},"
            f"{row.get('totalFactura', '')},{row.get('moneda', '')},{row.get('usuarioEmisorId', '')}")

def validar_decimal(valor, nombre_campo):
    """Valida que el valor sea un decimal válido y positivo"""
    if not valor or valor.strip() == '':
        return None, f"{nombre_campo} es obligatorio"
    try:
        decimal_val = Decimal(valor.strip())
        if decimal_val < 0:
            return None, f"{nombre_campo} no puede ser negativo"
        return decimal_val, None
    except:
        return None, f"{nombre_campo} debe ser un número válido"

def validar_entero(valor, nombre_campo, obligatorio=False):
    """Valida que el valor sea un entero válido"""
    if not valor or valor.strip() == '':
        if obligatorio:
            return None, f"{nombre_campo} es obligatorio"
        return None, None
    try:
        return int(valor.strip()), None
    except:
        return None, f"{nombre_campo} debe ser un número entero"

def procesar_csv(file_path):
    """Procesa el archivo CSV y guarda las facturas en la BD"""
    resultados = {
        "exitosos": 0,
        "errores": []
    }
    
    connection = conectar_bd()
    if not connection:
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
                
                # Validar numeroFactura
                numeroFactura = row.get('numeroFactura', '').strip()
                if not numeroFactura:
                    resultados['errores'].append(
                        f"Línea {linea}: El número de factura es obligatorio\n→ {linea_texto}"
                    )
                    continue
                
                if numero_factura_existe(cursor, numeroFactura):
                    resultados['errores'].append(
                        f"Línea {linea}: El número de factura {numeroFactura} ya existe\n→ {linea_texto}"
                    )
                    continue
                
                # Validar serie
                serie = row.get('serie', '').strip()
                if not serie:
                    resultados['errores'].append(
                        f"Línea {linea}: La serie es obligatoria\n→ {linea_texto}"
                    )
                    continue
                
                # Validar clienteId
                clienteId, error = validar_entero(row.get('clienteId', ''), 'clienteId', obligatorio=True)
                if error:
                    resultados['errores'].append(f"Línea {linea}: {error}\n→ {linea_texto}")
                    continue
                
                # Validar pedidoId (opcional)
                pedidoId, error = validar_entero(row.get('pedidoId', ''), 'pedidoId', obligatorio=False)
                if error:
                    resultados['errores'].append(f"Línea {linea}: {error}\n→ {linea_texto}")
                    continue
                
                # Validar fechaEmision
                fechaEmision = row.get('fechaEmision', '').strip()
                if not fechaEmision:
                    resultados['errores'].append(
                        f"Línea {linea}: La fecha de emisión es obligatoria\n→ {linea_texto}"
                    )
                    continue
                
                # Validar fechaVencimiento (opcional)
                fechaVencimiento = row.get('fechaVencimiento', '').strip() or None
                
                # Validar estadoPago
                estadoPago = row.get('estadoPago', '').strip()
                if not estadoPago:
                    resultados['errores'].append(
                        f"Línea {linea}: El estado de pago es obligatorio\n→ {linea_texto}"
                    )
                    continue
                
                estados_validos = ['pendiente', 'pagada', 'parcial', 'vencida', 'anulada']
                if estadoPago.lower() not in estados_validos:
                    resultados['errores'].append(
                        f"Línea {linea}: Estado '{estadoPago}' no válido (use: {', '.join(estados_validos)})\n→ {linea_texto}"
                    )
                    continue
                
                # Validar baseImponible
                baseImponible, error = validar_decimal(row.get('baseImponible', ''), 'baseImponible')
                if error:
                    resultados['errores'].append(f"Línea {linea}: {error}\n→ {linea_texto}")
                    continue
                
                # Validar ivaTotal
                ivaTotal, error = validar_decimal(row.get('ivaTotal', ''), 'ivaTotal')
                if error:
                    resultados['errores'].append(f"Línea {linea}: {error}\n→ {linea_texto}")
                    continue
                
                # Validar totalFactura
                totalFactura, error = validar_decimal(row.get('totalFactura', ''), 'totalFactura')
                if error:
                    resultados['errores'].append(f"Línea {linea}: {error}\n→ {linea_texto}")
                    continue
                
                # Verificar coherencia: total = base + iva
                total_calculado = baseImponible + ivaTotal
                if abs(total_calculado - totalFactura) > Decimal('0.01'):
                    resultados['errores'].append(
                        f"Línea {linea}: totalFactura ({totalFactura}) no coincide con baseImponible + ivaTotal ({total_calculado})\n→ {linea_texto}"
                    )
                    continue
                
                # Validar moneda
                moneda = row.get('moneda', '').strip()
                if not moneda:
                    resultados['errores'].append(
                        f"Línea {linea}: La moneda es obligatoria\n→ {linea_texto}"
                    )
                    continue
                
                # Validar usuarioEmisorId (opcional)
                usuarioEmisorId, error = validar_entero(row.get('usuarioEmisorId', ''), 'usuarioEmisorId', obligatorio=False)
                if error:
                    resultados['errores'].append(f"Línea {linea}: {error}\n→ {linea_texto}")
                    continue
                
                # Preparar datos para insertar
                datos = {
                    'numeroFactura': numeroFactura,
                    'serie': serie,
                    'pedidoId': pedidoId,
                    'clienteId': clienteId,
                    'fechaEmision': fechaEmision,
                    'fechaVencimiento': fechaVencimiento,
                    'estadoPago': estadoPago,
                    'baseImponible': str(baseImponible),
                    'ivaTotal': str(ivaTotal),
                    'totalFactura': str(totalFactura),
                    'moneda': moneda,
                    'usuarioEmisorId': usuarioEmisorId
                }
                
                # Guardar en la BD
                try:
                    guardar_factura(cursor, datos)
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
