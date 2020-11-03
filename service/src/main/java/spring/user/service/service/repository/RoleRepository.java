package spring.user.service.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.user.service.service.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
