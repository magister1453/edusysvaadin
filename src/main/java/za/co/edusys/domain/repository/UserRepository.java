package za.co.edusys.domain.repository;

import org.springframework.data.repository.CrudRepository;
import za.co.edusys.domain.model.User;

/**
 * Created by marc.marais on 2017/01/07.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByUserName(String userName);
}
