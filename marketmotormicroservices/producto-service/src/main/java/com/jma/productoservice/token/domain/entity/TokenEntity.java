package com.jma.productoservice.token.domain.entity;

import com.jma.productoservice.usuario.domain.entity.UsuarioEntity;
import com.jma.productoservice.utils.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tipoToken = TokenType.BEARER;

    public boolean revocado;

    public boolean expirado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    public UsuarioEntity usuario;

}
