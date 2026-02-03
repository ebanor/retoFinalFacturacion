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
            host='db',              # ← IMPORTANTE: 'db' en Docker, 'localhost' en local
            port=3306,
            database='reto_final',
            user='root',
            password='root'
        )
        return connection
    except Error as e:
        print(json.dumps({"exitosos": 0, "errores": [f"Error al conectar a MySQL: {e}"]}))
        return None

# ... resto del código
