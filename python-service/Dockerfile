FROM python:3.11-slim

WORKDIR /app

# Instalar dependencias
RUN pip install mysql-connector-python watchdog

# Copiar scripts
COPY procesar_facturas.py /app/procesar_facturas.py
COPY monitor.py /app/monitor.py

# Ejecutar monitor
CMD ["python3", "/app/monitor.py"]
