package com.drogueria.ventas.implement;

import com.drogueria.ventas.dto.MedicamentoDto;
import com.drogueria.ventas.entity.Venta;
import com.drogueria.ventas.exception.NotFoundException;
import com.drogueria.ventas.repository.VentaRepository;
import com.drogueria.ventas.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class VentaImplement implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Venta> listarVentas() {

        List<Venta> ventas = ventaRepository.findAll();

        if (ventas.isEmpty()) {
            throw new NotFoundException("No hay ventas registradas");
        }
        return ventas;
    }

    @Transactional
    @Override
    public void guardar(Venta venta) {

        MedicamentoDto medicamentoDto = restTemplate.getForObject(
                "http://medicamentos-service/medicamentos/" + venta.getMedicamento(),
                MedicamentoDto.class
        );

        medicamentoDto.setStock(medicamentoDto.getStock() - venta.getCantidad()); // Se realiza la resta de stock cuando se genera la venta

        //Actualizar el medicamento en el inventario
        restTemplate.put(
                "http://medicamentos-service/medicamentos",
                medicamentoDto,
                MedicamentoDto.class
        );

        ventaRepository.save(venta);
    }

    @Override
    public void eliminar(String id) {
        ventaRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Venta no encontrada")
        );
        ventaRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Venta editarVenta(String id, Venta updatedVenta) {
        Venta existingVenta = ventaRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Venta no encontrada")
        );

        if (existingVenta.getFecha_venta() != null)
            existingVenta.setFecha_venta(updatedVenta.getFecha_venta());

        if (existingVenta.getMedicamento() != null)
            existingVenta.setMedicamento(updatedVenta.getMedicamento());

        if (existingVenta.getCantidad() != 0)
            existingVenta.setCantidad(updatedVenta.getCantidad());

        if (existingVenta.getValor_unitario() != 0)
            existingVenta.setValor_unitario(updatedVenta.getValor_unitario());

        if (existingVenta.getValor_total() != 0)
            existingVenta.setValor_total(updatedVenta.getValor_total());

        return ventaRepository.save(existingVenta);
    }

    @Override
    public Venta encontrarVenta(String id) {


        Venta venta = ventaRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Venta no encontrada")
        );;

        MedicamentoDto medicamentoDto = restTemplate.getForObject(
                "http://medicamentos-service/api/medicamentos/" + venta.getMedicamento(),
                MedicamentoDto.class
        );

        venta.setMedicamento(medicamentoDto);

        return venta;
    }
}
