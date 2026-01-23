package webb_kurs.gruppuppgift.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webb_kurs.gruppuppgift.model.GameModel;

import java.util.UUID;

@Repository
public interface IGameRepository extends JpaRepository<GameModel, UUID> {
}
