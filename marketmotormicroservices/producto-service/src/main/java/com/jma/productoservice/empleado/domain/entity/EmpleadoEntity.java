package com.jma.productoservice.empleado.domain.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jma.productoservice.ordenCompra.domain.entity.OrdenCompraEntity;
import com.jma.productoservice.usuario.domain.entity.UsuarioEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tb_empleado")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class EmpleadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Long id;

    @Column(name = "nombre_empleado")
    @NotBlank
    private String nombre;

    @Column(name = "apellidoPat_empleado")
    @NotBlank
    private String apellidoPaterno;

    @Column(name = "apellidoMat_empleado")
    @NotBlank
    private String apellidoMaterno;

    @Column(name = "telefono_empleado")
    @NotBlank
    private String telefono;

    @Column(name = "correo_empleado", unique = true)
    @Email
    private String correo;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "actualizado_en")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime actualizadoEn;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "creado_en",updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime creadoEn;

    @Column(name = "estado")
    private boolean estado;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    @NotNull
    private UsuarioEntity usuario;


    @OneToMany(mappedBy = "empleado")
    @JsonIgnore
    private Set<OrdenCompraEntity> ordenesCompra;


}
