package com.drogueria.ventas.controller;

import com.drogueria.ventas.entity.Venta;
import com.drogueria.ventas.service.VentaService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class VentaControllerTest {
    @InjectMocks
    private VentaController ventaController;

    @Mock
    private VentaService medicamentoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    void testGetAllVentas() {

        Venta venta1 = new Venta();
        Venta venta2 = new Venta();

        venta1.setId("Test1");
        venta2.setId("Test2");

        List<Venta> mockVentas = new ArrayList<>();
        mockVentas.add(venta1);
        mockVentas.add(venta2);

        when(medicamentoService.listarVentas()).thenReturn(mockVentas);

        List<Venta> result = ventaController.getAll();

        log.info("Result: {}", result);

        verify(medicamentoService, times(1)).listarVentas();

        Assert.assertEquals(2, result.size());
        Assert.assertEquals(mockVentas.get(0).getId(), result.get(0).getId());
        Assert.assertEquals(mockVentas.get(1).getId(), result.get(1).getId());

    }


    @Test
    @Order(2)
    void testSaveVenta() {
        Venta mockVenta = new Venta();
        mockVenta.setId("Test");
        mockVenta.setMedicamento("1234");
        mockVenta.setCantidad(2);
        mockVenta.setValor_unitario(12000);
        mockVenta.setValor_total(24000);

        ResponseEntity<?> responseEntity = ventaController.saveVenta(mockVenta);

        log.info("Result: {}",responseEntity.getBody());

        verify(medicamentoService, times(1)).guardar(mockVenta);

        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

    }

    @Test
    @Order(3)
    void testGetVenta() {
        String mockId = "1234";
        Venta mockVenta = new Venta();

        when(medicamentoService.encontrarVenta(mockId)).thenReturn(mockVenta);

        Venta result = ventaController.getVenta(mockId);

        log.info("Result: {}",result.toString());

        verify(medicamentoService, times(1)).encontrarVenta(mockId);
    }

    @Test
    @Order(4)
    void testDeleteVenta() {
        String mockId = "1234";

        ResponseEntity<?> responseEntity = ventaController.deleteVenta(mockId);

        log.info("Result: {}",responseEntity.getBody());


        verify(medicamentoService, times(1)).eliminar(mockId);
    }

    //USANDO REST TEMPLATE
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Order(5)
    void testListarVentas() {
        ResponseEntity<List<Venta>> response = testRestTemplate.exchange(
                "/api/ventas",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
        });

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

    }
}
