import time
import os
import subprocess
import json
from pathlib import Path

INPUT_DIR = "/shared/input"
OUTPUT_DIR = "/shared/output"

# Crear directorios
Path(INPUT_DIR).mkdir(parents=True, exist_ok=True)
Path(OUTPUT_DIR).mkdir(parents=True, exist_ok=True)

print("🐍 Monitor Python iniciado. Esperando archivos CSV...")

while True:
    try:
        for csv_file in Path(INPUT_DIR).glob("*.csv"):
            print(f"📄 Procesando: {csv_file.name}")
            
            result = subprocess.run(
                ["python3", "/app/procesar_facturas.py", str(csv_file)],
                capture_output=True,
                text=True
            )
            
            output_file = Path(OUTPUT_DIR) / f"{csv_file.stem}_result.json"
            
            if result.returncode == 0:
                output_file.write_text(result.stdout)
                print(f"✅ Resultado: {output_file.name}")
            else:
                error_result = {"exitosos": 0, "errores": [f"Error: {result.stderr}"]}
                output_file.write_text(json.dumps(error_result))
                print(f"❌ Error: {result.stderr}")
            
            csv_file.unlink()
            
    except Exception as e:
        print(f"⚠️ Error: {e}")
    
    time.sleep(2)
