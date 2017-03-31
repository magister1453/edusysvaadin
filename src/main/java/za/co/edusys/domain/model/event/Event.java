package za.co.edusys.domain.model.event;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import za.co.edusys.domain.model.Class;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by marc.marais on 2017/02/25.
 */
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection(targetClass = EventStatus.class)
    @CollectionTable(name = "eventstatus", joinColumns = @JoinColumn(name = "event_id"))
    @Enumerated(EnumType.STRING)
    private List<EventStatus> statuses;
    @Enumerated(EnumType.STRING)
    private EventStatus visibleStatus;
    private String description;
    private Integer totalMarks;
    @OneToMany(mappedBy = "event")
    private List<EventItem> itemList;
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    private List<Class> classes;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Event(){}

    public Event(String name, EventType eventType, List<EventStatus> statuses, EventStatus visibleStatus, String description, List<Class> classes,
                 LocalDateTime startDate, LocalDateTime endDate, Integer totalMarks) {
        this.name = name;
        this.eventType = eventType;
        this.statuses = statuses;
        this.visibleStatus = visibleStatus;
        this.description = description;
        this.classes = classes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalMarks = totalMarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public List<EventStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<EventStatus> statuses) {
        this.statuses = statuses;
    }

    public EventStatus getVisibleStatus() {
        return visibleStatus;
    }

    public void setVisibleStatus(EventStatus visibleStatus) {
        this.visibleStatus = visibleStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }

    public List<EventItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<EventItem> itemList) {
        this.itemList = itemList;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
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
}
