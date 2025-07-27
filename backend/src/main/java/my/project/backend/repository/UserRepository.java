package my.project.backend.repository;

import my.project.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
     Optional<UserEntity> findByEmail(String email);

     boolean existsByEmail(String email);
}
