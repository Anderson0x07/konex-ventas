package com.drogueria.ventas.controller;

import com.drogueria.ventas.entity.Venta;
import com.drogueria.ventas.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin
public class VentaController {

    @Autowired
    private VentaService ventaService;

    private Map<String,Object> response = new HashMap<>();


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Venta> getAll() {
        return ventaService.listarVentas();
    }

    @PostMapping
    public ResponseEntity<?> saveVenta(@RequestBody Venta venta) {
        LocalDateTime now = LocalDateTime.now(); // fecha y hora actual
        venta.setFecha_venta(now);
        ventaService.guardar(venta);
        response.clear();
        response.put("message", "Venta registrada exitosamente");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Venta updateVenta(@PathVariable String id, @RequestBody Venta updatedVenta) {
        return ventaService.editarVenta(id, updatedVenta);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Venta getVenta(@PathVariable String id) {
        return ventaService.encontrarVenta(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVenta(@PathVariable String id) {
        ventaService.eliminar(id);
        response.clear();
        response.put("message", "Venta eliminada exitosamente");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
