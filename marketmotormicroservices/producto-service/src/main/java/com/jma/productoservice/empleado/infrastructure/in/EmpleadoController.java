package com.jma.productoservice.empleado.infrastructure.in;


import com.jma.productoservice.empleado.domain.command.EmpleadoCommandInsert;
import com.jma.productoservice.empleado.domain.command.EmpleadoCommandUpdate;
import com.jma.productoservice.empleado.domain.dto.EmpleadoDto;
import com.jma.productoservice.empleado.domain.response.EmpleadoResponse;
import com.jma.productoservice.usuario.domain.dto.UsuarioDto;
import com.jma.productoservice.empleado.application.mapper.EmpleadoMapper;
import com.jma.productoservice.empleado.application.service.EmpleadoService;
import com.jma.productoservice.usuario.application.service.UsuarioService;
import com.jma.productoservice.utils.ConstantsService;
import com.jma.productoservice.utils.EstadoD;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/empleados")
@Validated
@AllArgsConstructor
public class EmpleadoController {

    private final EmpleadoService<EmpleadoDto> empleadoService;
    private final UsuarioService<UsuarioDto> usuarioService;


    /*
    @Autowired
    public EmpleadoController(EmpleadoService<EmpleadoDto> empleadoService, UsuarioService<UsuarioDto> usuarioService){
        this.empleadoService = empleadoService;
        this.usuarioService = usuarioService;
    }
*/

    @GetMapping
    public ResponseEntity<List<EmpleadoDto>> obtenerTodos(){
        return ResponseEntity.ok(empleadoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDto> obtenerPorId(@PathVariable("id") Long id){
            EmpleadoDto empleado = empleadoService.obtenerPorId(id);
            if(empleado == null)
                return ResponseEntity.notFound().build();

            return ResponseEntity.ok(empleado);
    }
    @GetMapping("/usuario/{alias}")
    public ResponseEntity<EmpleadoDto> obtenerPorUsuarioAlias(@PathVariable("alias") String alias){
        EmpleadoDto empleado = empleadoService.obtenerEmpleadoPorUsuarioAlias(alias);
        if(empleado == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(empleado);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Boolean> desactivar(@PathVariable("id") Long id){

        try{
            EmpleadoDto empleado = empleadoService.obtenerPorId(id);
            if(empleado == null)
                return ResponseEntity.notFound().build();

            empleado.setId(id);
            if(empleado.getUsuarioDto()!=null){
                System.out.println(empleado.getUsuarioDto().getId());
            }

            empleado.declararDisponibilidad(EstadoD.INACTIVO);
            empleadoService.guardar(empleado);
            empleado.getUsuarioDto().declararDisponibilidad(EstadoD.INACTIVO);
            usuarioService.cambiarEstadoInactivo(empleado.getUsuarioDto());
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/activar/{id}")
    public ResponseEntity<Boolean> activar(@PathVariable("id") Long id){

        try{
            EmpleadoDto empleado = empleadoService.obtenerPorId(id);
            if(empleado == null)
                return ResponseEntity.notFound().build();

            empleado.setId(id);
            empleado.declararDisponibilidad(EstadoD.ACTIVO);
            empleadoService.guardar(empleado);
            usuarioService.cambiarEstadoActivo(empleado.getUsuarioDto());
            //usuarioService.guardar(empleado.getUsuarioDto());
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") Long id){
        try{
            String respuesta = empleadoService.eliminar(id);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<EmpleadoDto> guardar(@RequestBody @Valid EmpleadoCommandInsert empleadoCommandInsert){
        try{
            UsuarioDto usuarioDto = new UsuarioDto();
            System.out.println("Llego aqui " + empleadoCommandInsert.getIdUsuario() + " " + empleadoCommandInsert.getNombre() + " "+ empleadoCommandInsert.getCorreo());
            usuarioDto.setId(empleadoCommandInsert.getIdUsuario());
            EmpleadoDto empleadoToSave = EmpleadoMapper.mapFromCommandInsertToDto(empleadoCommandInsert);
            empleadoToSave.setUsuarioDto(usuarioDto);
            EmpleadoDto empleadoGuardado = empleadoService.guardar(empleadoToSave);
            return ResponseEntity.ok(empleadoGuardado);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/guardarTodos")
    public ResponseEntity<List<EmpleadoDto>> guardarTodos(@RequestBody @Valid List<EmpleadoCommandInsert> empleadoscommand){
        try{
            List<UsuarioDto> usuariosObtenidosPorId = empleadoscommand.stream().map(e -> usuarioService.obtenerPorId(e.getIdUsuario())).toList();

            List<EmpleadoDto> empleadosMapeados = empleadoscommand.stream().map(EmpleadoMapper::mapFromCommandInsertToDto).toList();

            Iterator<UsuarioDto> usuariosIterator = usuariosObtenidosPorId.iterator();
            Iterator<EmpleadoDto> empleadosIterator = empleadosMapeados.iterator();

            List<EmpleadoDto> empleadosToTransferData = new ArrayList<>();

           while (usuariosIterator.hasNext() && empleadosIterator.hasNext()) {
               EmpleadoDto empleadoToList = empleadosIterator.next();
               empleadoToList.setUsuarioDto(usuariosIterator.next());
               empleadosToTransferData.add(empleadoToList);
            }


            List<EmpleadoDto> empleadosGuardados = empleadoService.guardarTodos(empleadosToTransferData);
            return ResponseEntity.ok(empleadosGuardados);

        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pagination")
    public ResponseEntity<EmpleadoResponse> obtenerTodosPaginados(
            @RequestParam(value = "pageNo", defaultValue = ConstantsService.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ConstantsService.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ConstantsService.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ConstantsService.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return ResponseEntity.ok(empleadoService.obtenerTodosPaginados(pageNo-1, pageSize, sortBy, sortDir));
    }

    @PutMapping
    public ResponseEntity<EmpleadoDto> actualizar(@RequestBody @Valid EmpleadoCommandUpdate empleadoCommandUpdate){

        EmpleadoDto empleadoDto = EmpleadoMapper.mapFromCommandUpdateToDto(empleadoCommandUpdate);
        EmpleadoDto empleadoActualizado = empleadoService.actualizar(empleadoDto);

        return ResponseEntity.ok(empleadoActualizado);
    }


}
