package za.co.edusys.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.edusys.domain.model.*;
import za.co.edusys.domain.model.Class;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserName(String userName);
    User findUserByFirstNameAndSurname(String firstName,String surname);
    List<User> findAllByRoleAndSchool(Role role, School school);
    List<User> findBySurnameContainingOrFirstNameContaining(String surname, String firstName, Pageable pageable);
    List<User> findByRoleAndGradeAndSchool(Role role, Grade grade, School school);
    Stream<User> findByClasses(Class aClass);
    List<User> findAllByClasses(Class aClass);
    @Query("SELECT u from User u left join u.school school where school = :school and (u.surname like :surname or u.firstName like :firstName)")
    List<User> getAdminUsers(@Param("school") School school, @Param("surname") String surname, @Param("firstName") String firstName, Pageable pageable);
}
