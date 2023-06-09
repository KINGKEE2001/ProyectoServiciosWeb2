package com.jma.productoservice.cliente.application.impl;

import com.jma.productoservice.cliente.domain.dto.ClienteDto;
import com.jma.productoservice.cliente.domain.entity.ClienteEntity;
import com.jma.productoservice.cliente.application.mapper.ClienteMapper;
import com.jma.productoservice.cliente.application.service.ClienteService;
import com.jma.productoservice.cliente.domain.response.ClienteResponse;
import com.jma.productoservice.cliente.infrastructure.out.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClienteServiceImpl implements ClienteService<ClienteDto> {


    private final ClienteRepository clienteRepository;

    /*
    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }
    */
    @Override
    public ClienteDto guardar(ClienteDto object) {
        ClienteEntity clienteEntity = ClienteMapper.mapToEntity(object);

        if(object.getId()!= null){
            clienteEntity.setId(object.getId());
        }

        return ClienteMapper.mapToDto(clienteRepository.save(clienteEntity));
    }

    @Override
    public List<ClienteDto> obtenerTodos() {
        return clienteRepository.findAll().stream().map(ClienteMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public String eliminar(Object id) {
        try{
            clienteRepository.deleteById((Long)id);
            return "Fue eliminado con éxito";
        }catch (Exception ex){
            return "No se pudo eliminar, se encontró un error";
        }
    }

    @Override
    public ClienteDto obtenerPorId(Object id) {
        return clienteRepository.findById((Long)id).map(ClienteMapper::mapToDto).orElse(null);
    }

    @Override
    public ClienteDto actualizar(ClienteDto object) {
        ClienteEntity clienteEntity = ClienteMapper.mapToEntity(object);
        clienteEntity.setId(object.getId());

        return ClienteMapper.mapToDto(clienteRepository.save(clienteEntity));
    }

    @Override
    public ClienteResponse obtenerTodosPaginados(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<ClienteEntity> clientesPageable = clienteRepository.findAll(pageable);

        List<ClienteEntity> clientes = clientesPageable.getContent();

        List<ClienteDto> content = clientes.stream().map(ClienteMapper::mapToDto).collect(Collectors.toList());

        ClienteResponse clienteResponse = new ClienteResponse();

        clienteResponse.setContent(content);
        clienteResponse.setTotalElements(clientesPageable.getTotalElements());
        clienteResponse.setPageSize(clientesPageable.getSize());
        clienteResponse.setPageNo(clientesPageable.getNumber());
        clienteResponse.setTotalPages(clientesPageable.getTotalPages());
        clienteResponse.setLast(clientesPageable.isLast());
        return clienteResponse;
    }








}
