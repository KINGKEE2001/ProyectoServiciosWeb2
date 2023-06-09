package com.jma.productoservice.ordenCompra.infrastructure.in;

import com.jma.productoservice.detalleOrdenCompra.application.mapper.DetalleOrdenCompraMapper;
import com.jma.productoservice.detalleOrdenCompra.domain.dto.DetalleOrdenCompraDto;
import com.jma.productoservice.detalleVenta.application.mapper.DetalleVentaMapper;
import com.jma.productoservice.empleado.domain.dto.EmpleadoDto;
import com.jma.productoservice.ordenCompra.domain.command.OrdenCompraCommandInsert;
import com.jma.productoservice.ordenCompra.domain.command.OrdenCompraCommandUpdate;
import com.jma.productoservice.ordenCompra.domain.command.OrdenCompraTransactionCommandInsert;
import com.jma.productoservice.ordenCompra.domain.dto.OrdenCompraDto;
import com.jma.productoservice.ordenCompra.domain.response.OrdenCompraResponse;
import com.jma.productoservice.proveedor.domain.dto.ProveedorDto;
import com.jma.productoservice.ordenCompra.application.mapper.OrdenCompraMapper;
import com.jma.productoservice.ordenCompra.application.service.OrdenCompraService;
import com.jma.productoservice.utils.ConstantsService;
import com.jma.productoservice.utils.EstadoD;
import com.jma.productoservice.venta.application.mapper.VentaMapper;
import com.jma.productoservice.venta.domain.command.VentaTransactionCommandInsert;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ordencompra")
@Validated
@AllArgsConstructor
public class OrdenCompraController {

    private final OrdenCompraService<OrdenCompraDto> ordenCompraService;

    /*
    @Autowired
    public OrdenCompraController(OrdenCompraService<OrdenCompraDto> ordenCompraService){
        this.ordenCompraService = ordenCompraService;
    }
*/

    @GetMapping
    public ResponseEntity<List<OrdenCompraDto>> obtenerTodos(){
        return ResponseEntity.ok(ordenCompraService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompraDto> obtenerPorId(@PathVariable("id") Long id){
        OrdenCompraDto ordenCompraDto = ordenCompraService.obtenerPorId(id);
        if(ordenCompraDto == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(ordenCompraDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> desactivar(@PathVariable("id") Long id){

        try{
            OrdenCompraDto ordenCompraDto = ordenCompraService.obtenerPorId(id);
            if(ordenCompraDto == null)
                return ResponseEntity.notFound().build();

            ordenCompraDto.declararDisponibilidad(EstadoD.INACTIVO);
            ordenCompraService.guardar(ordenCompraDto);
            return ResponseEntity.ok("Se desactivó correctamente");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/confirmar")
    public ResponseEntity<Boolean> confirmar(@RequestParam(value = "id", defaultValue = "0", required = false) Long id,@Valid @RequestBody List<DetalleOrdenCompraDto> detalles){

        try{
            ordenCompraService.confirmarCompra(id, detalles);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }






    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") Long id){
        try{
            String respuesta = ordenCompraService.eliminar(id);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<OrdenCompraDto> guardar(@RequestBody @Valid OrdenCompraCommandInsert ordenCompraCommandInsert){
        try{
            ProveedorDto proveedorDto = new ProveedorDto();
            proveedorDto.setId(ordenCompraCommandInsert.getIdProveedor());

            EmpleadoDto empleadoDto = new EmpleadoDto();
            empleadoDto.setId(ordenCompraCommandInsert.getIdEmpleado());


            OrdenCompraDto ordenToSave = OrdenCompraMapper.mapFromCommandInsertToDto(ordenCompraCommandInsert);
            ordenToSave.setProveedor(proveedorDto);
            ordenToSave.setEmpleado(empleadoDto);
            OrdenCompraDto ordenGuardado = ordenCompraService.guardar(ordenToSave);
            return ResponseEntity.ok(ordenGuardado);
        } catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/realizarOrdenCompra")
    public ResponseEntity<Boolean> realizarTransaccionOrdenCompra(@RequestBody @Valid OrdenCompraTransactionCommandInsert ordenCompraTransactionCommandInsert) {

        try{
            boolean result = ordenCompraService.realizarOrdenCompra(OrdenCompraMapper.mapFromCommandInsertToDto(ordenCompraTransactionCommandInsert.getOrdenCompra()),ordenCompraTransactionCommandInsert.getDetallesOrdenCompra().stream().map(DetalleOrdenCompraMapper::mapFromCommandInsertToDto).collect(Collectors.toList()));
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(false);
    }


    @GetMapping("/pagination")
    public ResponseEntity<OrdenCompraResponse> obtenerTodosPaginados(
            @RequestParam(value = "pageNo", defaultValue = ConstantsService.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ConstantsService.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ConstantsService.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ConstantsService.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return ResponseEntity.ok(ordenCompraService.obtenerTodosPaginados(pageNo, pageSize, sortBy, sortDir));
    }


    @GetMapping("/pendientes/pagination")
    public ResponseEntity<OrdenCompraResponse> obtenerPendientesPaginados(
            @RequestParam(value = "pageNo", defaultValue = ConstantsService.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ConstantsService.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ConstantsService.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ConstantsService.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return ResponseEntity.ok(ordenCompraService.obtenerPendientesPaginados(false,pageNo-1, pageSize, sortBy, sortDir));
    }


    @PutMapping
    public ResponseEntity<OrdenCompraDto> actualizar(@RequestBody @Valid OrdenCompraCommandUpdate ordenCompraCommandUpdate){

        OrdenCompraDto ordenCompraDto = OrdenCompraMapper.mapFromCommandUpdateToDto(ordenCompraCommandUpdate);
        OrdenCompraDto ordenActualizado = ordenCompraService.actualizar(ordenCompraDto);

        return ResponseEntity.ok(ordenActualizado);
    }

}
