package za.co.edusys.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.edusys.domain.model.School;
import za.co.edusys.domain.model.Term;

import java.util.stream.Stream;

/**
 * Created by marc.marais on 2017/03/04.
 */
public interface TermRepository  extends JpaRepository<Term, Long> {
    Stream<Term> findBySchool(School school);
}
