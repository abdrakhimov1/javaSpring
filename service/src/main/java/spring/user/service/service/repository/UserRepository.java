package spring.user.service.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.user.service.service.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);

}
