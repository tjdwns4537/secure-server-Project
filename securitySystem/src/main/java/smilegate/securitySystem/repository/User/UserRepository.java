package smilegate.securitySystem.repository.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import smilegate.securitySystem.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Lazy가 아닌 Eager조회로 권한 정보도 같이 가져옴
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
