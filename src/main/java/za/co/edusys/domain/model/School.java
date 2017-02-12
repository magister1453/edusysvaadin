package za.co.edusys.domain.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by marc.marais on 2017/02/10.
 */
@Entity
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    boolean enabled;
    @OneToMany(mappedBy = "school")
    Set<User> userList;
    @OneToMany(mappedBy = "grade")
    Set<Grade> gradeList;


    public School(){}

    public School(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<User> getUserList() {
        return userList;
    }

    public void setUserList(Set<User> userList) {
        this.userList = userList;
    }

    public Set<Grade> getGradeList() {
        return gradeList;
    }

    public void setGradeList(Set<Grade> gradeList) {
        this.gradeList = gradeList;
    }
}
