package com.jma.productoservice.repository;

import com.jma.productoservice.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductoRepository extends JpaRepository<ProductoEntity,Long> {
}
