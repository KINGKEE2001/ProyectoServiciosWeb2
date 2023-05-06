package com.jma.productoservice.repository;


import com.jma.productoservice.entity.PermisoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermisoRepository extends JpaRepository<PermisoEntity,Long> {
    @Query(value = "SELECT t1 FROM PermisoEntity t1 join fetch t1.roles t2 WHERE t2.id = ?1 ")
    List<PermisoEntity> findPermisoEntitiesByRolesId(Long rolId);
}
