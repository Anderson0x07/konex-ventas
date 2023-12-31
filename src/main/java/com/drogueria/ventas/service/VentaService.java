package com.drogueria.ventas.service;

import com.drogueria.ventas.entity.Venta;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VentaService {

    public List<Venta> listarVentas();
    public void guardar(Venta venta);

    public void eliminar(String id);

    public Venta encontrarVenta(String id);

    public List<Venta> ventasPorMedicamento(String id);

}
