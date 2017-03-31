package za.co.edusys.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.edusys.domain.model.MenuItem;
import za.co.edusys.domain.model.Role;
import za.co.edusys.domain.model.School;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    Stream<MenuItem> findAllByRoles(Role role);
}
