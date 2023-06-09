package com.jma.productoservice.producto.application.service;

import com.jma.productoservice.producto.domain.response.ProductoResponse;
import com.jma.productoservice.utils.ICrudCommon;

import java.util.List;

public interface ProductoService<T> extends ICrudCommon<T> {

    List<T> guardarTodos(List<T> list);

    ProductoResponse obtenerTodosPaginados(int pageNo, int pageSize, String sortBy, String sortDir);


    ProductoResponse obtenerPFiltradosPorDescripcion(String descripcion,int pageNo, int pageSize, String sortBy, String sortDir);


    List<T> obtenerProductosDisponibles();
}
