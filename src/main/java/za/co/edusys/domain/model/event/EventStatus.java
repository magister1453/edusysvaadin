package za.co.edusys.domain.model.event;

/**
 * Created by marc.marais on 2017/02/25.
 */
public enum EventStatus {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    CLOSED("Closed");

    private String name;

    EventStatus(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
