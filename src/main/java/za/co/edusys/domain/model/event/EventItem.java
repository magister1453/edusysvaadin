package za.co.edusys.domain.model.event;

import za.co.edusys.domain.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by marc.marais on 2017/02/25.
 */
@Entity
public class EventItem{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Enumerated(EnumType.STRING)
    EventStatus eventStatus;
    String result;
    @ManyToOne(fetch = FetchType.EAGER)
    Event event;
    @ManyToOne(fetch = FetchType.EAGER)
    User user;
    LocalDateTime startDate;
    LocalDateTime endDate;

    public EventItem() {
    }

    public EventItem(EventStatus eventStatus, Event event, User user, LocalDateTime startDate, LocalDateTime endDate, String result) {
        this.eventStatus = eventStatus;
        this.event = event;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
