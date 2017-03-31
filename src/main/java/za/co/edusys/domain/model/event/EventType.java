package za.co.edusys.domain.model.event;

/**
 * Created by marc.marais on 2017/02/25.
 */
public enum EventType {
    HOMEWORK("Homework"),
    ASSIGNMENT("Assignment"),
    TEST("Test"),
    CLASS("Class"),
    EXAM("Exam");

    private String name;

    EventType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
