package za.co.edusys.domain.model;

import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.List;

@Entity
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    @ManyToOne
    School school;
    @ManyToOne
    User teacher;
    @Enumerated(EnumType.STRING)
    Subject subject;
    @Enumerated(EnumType.STRING)
    Grade grade;
    @ManyToMany(fetch = FetchType.EAGER)
    List<User> pupils;

    public Class(){}

    public Class(String name, School school, User teacher, Subject subject, Grade grade, List<User> pupils){
        this.name = name;
        this.school = school;
        this.teacher = teacher;
        this.subject = subject;
        this.grade = grade;
        this.pupils = pupils;
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

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public List<User> getPupils() {
        return pupils;
    }

    public void setPupils(List<User> pupils) {
        this.pupils = pupils;
    }
}

