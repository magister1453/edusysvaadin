package za.co.edusys.domain.repository;

import org.springframework.data.repository.CrudRepository;
import za.co.edusys.domain.model.Authorities;

public interface AuthorityRepository extends CrudRepository<Authorities, Long> {
}
