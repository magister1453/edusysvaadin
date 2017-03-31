package za.co.edusys.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.edusys.domain.model.User;
import za.co.edusys.domain.model.event.Event;
import za.co.edusys.domain.model.event.EventItem;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by marc.marais on 2017/02/27.
 */
public interface EventItemRepository extends JpaRepository<EventItem, Long> {
    Stream<EventItem> findByUserAndEvent(User user, Event event);
    Stream<EventItem> findByUser(User user);
}
