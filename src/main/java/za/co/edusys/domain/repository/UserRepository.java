package za.co.edusys.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.edusys.domain.model.School;
import za.co.edusys.domain.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserName(String userName);
    List<User> findBySurnameContainingOrFirstNameContaining(String surname, String firstName, Pageable pageable);
    @Query("SELECT u from User u left join u.school school where school = :school and (u.surname like :surname or u.firstName like :firstName)")
    List<User> getAdminUsers(@Param("school") School school, @Param("surname") String surname, @Param("firstName") String firstName, Pageable pageable);
}
