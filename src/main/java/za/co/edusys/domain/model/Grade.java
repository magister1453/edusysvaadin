package za.co.edusys.domain.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by marc.marais on 2017/02/13.
 */
public enum Grade {
    Grade_RR("Grade RR"),
    Grade_R("Grade R"),
    Grade_0("Grade 0"),
    Grade_1("Grade 1"),
    Grade_2("Grade 2"),
    Grade_3("Grade 3"),
    Grade_4("Grade 4"),
    Grade_5("Grade 5"),
    Grade_6("Grade 6"),
    Grade_7("Grade 7"),
    Grade_8("Grade 8"),
    Grade_9("Grade 9"),
    Grade_10("Grade 10"),
    Grade_11("Grade 11"),
    Grade_12("Grade 12");

    Grade(String name){
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<Grade> getHighSchool() {
        return Arrays.asList(Grade_8, Grade_9, Grade_10, Grade_11, Grade_12);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
