package api.repositories;

import api.models.Categoria;
import api.models.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {}
