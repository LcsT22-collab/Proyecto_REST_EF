package com.clinica.soap;

import com.clinica.model.dto.PagoDTO;
import com.clinica.service.impl.PagoServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * Endpoint SOAP para Pagos
 */
@Endpoint
@Slf4j
@RequiredArgsConstructor
public class PagoSOAPEndpoint {
    
    private static final String NAMESPACE_URI = "http://clinica.com/soap/pago";
    private final PagoServiceImpl pagoService;
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CrearPagoRequest")
    @ResponsePayload
    public CrearPagoResponse crearPago(@RequestPayload CrearPagoRequest request) {
        log.info("SOAP: Creando pago para cita {}", request.getCitaId());
        
        CrearPagoResponse response = new CrearPagoResponse();
        try {
            PagoDTO pagoDTO = new PagoDTO();
            pagoDTO.setCitaId(request.getCitaId());
            pagoDTO.setMonto(request.getMonto());
            pagoDTO.setMetodoPago(request.getMetodoPago());
            
            PagoDTO creado = pagoService.crearPago(pagoDTO);
            response.setIdPago(creado.getIdPago());
            response.setMensaje("Pago registrado exitosamente");
            response.setExitoso(true);
        } catch (Exception e) {
            log.error("Error creando pago SOAP", e);
            response.setExitoso(false);
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ProcesarPagoRequest")
    @ResponsePayload
    public ProcesarPagoResponse procesarPago(@RequestPayload ProcesarPagoRequest request) {
        log.info("SOAP: Procesando pago {}", request.getIdPago());
        
        ProcesarPagoResponse response = new ProcesarPagoResponse();
        try {
            PagoDTO procesado = pagoService.procesarPago(request.getIdPago());
            response.setPago(procesado);
            response.setExitoso(true);
        } catch (Exception e) {
            log.error("Error procesando pago SOAP", e);
            response.setExitoso(false);
            response.setMensaje("Error: " + e.getMessage());
        }
        return response;
    }
}

class CrearPagoRequest {
    private Long citaId;
    private String monto;
    private String metodoPago;
    
    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }
    public String getMonto() { return monto; }
    public void setMonto(String monto) { this.monto = monto; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}

class CrearPagoResponse {
    private Long idPago;
    private String mensaje;
    private Boolean exitoso;
    
    public Long getIdPago() { return idPago; }
    public void setIdPago(Long idPago) { this.idPago = idPago; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Boolean getExitoso() { return exitoso; }
    public void setExitoso(Boolean exitoso) { this.exitoso = exitoso; }
}

class ProcesarPagoRequest {
    private Long idPago;
    
    public Long getIdPago() { return idPago; }
    public void setIdPago(Long idPago) { this.idPago = idPago; }
}

class ProcesarPagoResponse {
    private PagoDTO pago;
    private Boolean exitoso;
    private String mensaje;
    
    public PagoDTO getPago() { return pago; }
    public void setPago(PagoDTO pago) { this.pago = pago; }
    public Boolean getExitoso() { return exitoso; }
    public void setExitoso(Boolean exitoso) { this.exitoso = exitoso; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
