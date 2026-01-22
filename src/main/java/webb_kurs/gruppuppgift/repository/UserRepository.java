package webb_kurs.gruppuppgift.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import webb_kurs.gruppuppgift.model.UserModel;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
}
