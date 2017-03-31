package za.co.edusys.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.edusys.domain.model.Class;
import za.co.edusys.domain.model.event.Event;

import java.util.stream.Stream;

/**
 * Created by marc.marais on 2017/02/27.
 */
public interface EventRepository extends JpaRepository<Event, Long> {
    Stream<Event> findAllByClasses(Class aClass);
}
