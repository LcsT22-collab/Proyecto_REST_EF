#!/bin/bash
# Script de pruebas rápidas para el Sistema de Gestión de Clínica

BASE_URL="http://localhost:8080/clinica/api/v1"

echo "======================================"
echo "PRUEBAS RÁPIDAS - Sistema de Clínica"
echo "======================================"
echo ""

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

# 1. Health Check
echo -e "${CYAN}1. Verificando estado del sistema...${NC}"
curl -s "$BASE_URL/health" | jq '.'
echo ""

# 2. Listar pacientes
echo -e "${CYAN}2. Listando pacientes...${NC}"
curl -s "$BASE_URL/pacientes" | jq '.'
echo ""

# 3. Crear paciente
echo -e "${CYAN}3. Creando nuevo paciente...${NC}"
curl -s -X POST "$BASE_URL/pacientes" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test Paciente API",
    "fechaNacimiento": "2010-01-01",
    "nombreTutor": "Tutor Test",
    "telefono": "555-TEST",
    "correo": "test@api.com"
  }' | jq '.'
echo ""

# 4. Listar terapeutas
echo -e "${CYAN}4. Listando terapeutas...${NC}"
curl -s "$BASE_URL/terapeutas" | jq '.'
echo ""

# 5. Iniciar proceso BPM
echo -e "${CYAN}5. Iniciando proceso BPM para crear cita...${NC}"
curl -s -X POST "$BASE_URL/bpm/cita?pacienteId=1&terapeutaId=1&tipoCita=CONSULTA" | jq '.'
echo ""

# 6. Verificar BPM status
echo -e "${CYAN}6. Verificando estado del BPM...${NC}"
curl -s "$BASE_URL/bpm/status"
echo ""
echo ""

# 7. Listar disciplinas
echo -e "${CYAN}7. Listando disciplinas...${NC}"
curl -s "$BASE_URL/disciplinas" | jq '.'
echo ""

# 8. System Info
echo -e "${CYAN}8. Información del sistema...${NC}"
curl -s "$BASE_URL/health/info" | jq '.'
echo ""

echo "======================================"
echo -e "${GREEN}✓ Pruebas completadas${NC}"
echo "======================================"
echo ""
echo "Para más pruebas, ver: TESTING_GUIDE.md"
echo ""
