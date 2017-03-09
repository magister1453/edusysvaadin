package za.co.edusys.domain.model;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by marc.marais on 2017/03/04.
 */
@Entity
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    LocalDate startDate;
    LocalDate endDate;
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    School school;

    public Term() {
    }

    public Term(String name, LocalDate startDate, LocalDate endDate, School school) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.school = school;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
