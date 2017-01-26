package za.co.edusys.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import za.co.edusys.domain.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByUserName(String userName);
}
