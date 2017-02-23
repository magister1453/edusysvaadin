package za.co.edusys.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import za.co.edusys.domain.model.Class;
import za.co.edusys.domain.model.School;
import za.co.edusys.domain.model.User;

import java.util.List;

public interface ClassRepository extends JpaRepository<Class, Long> {
    List<Class> findAllBySchool(School school, Pageable pageable);
    List<Class> findAllByTeacher(User teacher);
}
