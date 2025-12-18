@echo off
REM Script para generar automáticamente todos los archivos BPMN para Bonita

echo.
echo ================================================================
echo Generando archivos BPMN para Bonita desde DTOs...
echo ================================================================
echo.

REM Compilar y ejecutar generación de BPMN
call mvnw.cmd spring-boot:run -DskipTests -q

echo.
echo ================================================================
echo ✓ Archivos BPMN generados en: target/bpmn/
echo.
echo Próximos pasos:
echo 1. Los archivos BPMN están listos para importar en Bonita
echo 2. Abre Bonita Studio y selecciona File > Import Process
echo 3. Selecciona los archivos .bpmn20.xml de target/bpmn/
echo 4. Configura los conectores REST en cada ServiceTask
echo.
echo URL Base REST: http://localhost:8080/clinica/api/v1
echo ================================================================
echo.
pause
