package com.jma.productoservice.token.infrastructure.out;

import com.jma.productoservice.token.domain.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

  @Query(value = """
      select t from TokenEntity t inner join UsuarioEntity u
      on t.usuario.id = u.id
      where u.id = :id and (t.expirado = false or t.revocado = false)\s
      """)
  List<TokenEntity> findAllValidTokenByUser(Long id);

  Optional<TokenEntity> findByToken(String token);
}
