package za.co.edusys.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.edusys.domain.model.School;

import java.util.List;

/**
 * Created by marc.marais on 2017/02/10.
 */
@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    List<School> findByNameContainingIgnoreCase(String name, Pageable pageable);
    School findOneByName(String name);
}
