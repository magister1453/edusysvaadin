package za.co.edusys.domain.model;

/**
 * Created by marc.marais on 2017/02/20.
 */
public enum Subject {
    MATHS("Mathematics"),
    PHYSICAL_SCIENCE("Physical Science"),
    ENGLISH("English");

    Subject(String name){
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
