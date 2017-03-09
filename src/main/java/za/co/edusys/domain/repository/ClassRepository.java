package za.co.edusys.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import za.co.edusys.domain.model.*;
import za.co.edusys.domain.model.Class;

import java.util.List;

public interface ClassRepository extends JpaRepository<Class, Long> {
    List<Class> findAllBySchool(School school, Pageable pageable);
    List<Class> findAllByTeacher(User teacher);
    List<Class> findAllByGradeAndSubjectAndTeacher(Grade grade, Subject subject, User teacher);
    Class findOneBySchoolAndName(School school, String name);
}
