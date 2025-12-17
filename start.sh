#!/bin/bash

echo "======================================"
echo "Sistema de Gestión de Clínica"
echo "======================================"
echo ""

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Compilando el proyecto...${NC}"
./mvnw clean package -DskipTests

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Compilación exitosa${NC}"
    echo ""
    echo -e "${YELLOW}Iniciando la aplicación...${NC}"
    echo ""
    
    ./mvnw spring-boot:run &
    
    # Esperar a que la aplicación inicie
    sleep 10
    
    echo ""
    echo "======================================"
    echo -e "${GREEN}Aplicación iniciada correctamente${NC}"
    echo "======================================"
    echo ""
    echo "Endpoints disponibles:"
    echo "• API REST:        http://localhost:8080/clinica/api/v1"
    echo "• Swagger UI:      http://localhost:8080/clinica/swagger-ui.html"
    echo "• H2 Console:      http://localhost:8080/clinica/h2-console"
    echo "• Health Check:    http://localhost:8080/clinica/api/v1/health"
    echo "• SOAP WSDL:       http://localhost:8080/clinica/ws/pacientes.wsdl"
    echo ""
    echo "Para detener: Ctrl+C"
    echo "======================================"
    
    # Mantener el script en ejecución
    wait
else
    echo -e "${RED}✗ Error en la compilación${NC}"
    exit 1
fi
