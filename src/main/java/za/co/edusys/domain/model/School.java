package za.co.edusys.domain.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
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
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection(targetClass = Grade.class)
    @CollectionTable(name = "schoolgrade", joinColumns = @JoinColumn(name = "school_id"))
    @Enumerated(EnumType.STRING)
    List<Grade> grades;
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection(targetClass = Subject.class)
    @CollectionTable(name = "schoolsubject", joinColumns = @JoinColumn(name = "school_id"))
    @Enumerated(EnumType.STRING)
    List<Subject> subjects;


    public School(){}

    public School(String name, boolean enabled, List<Grade> grades, List<Subject> subjects) {
        this.name = name;
        this.enabled = enabled;
        this.grades = grades;
        this.subjects = subjects;
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

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
