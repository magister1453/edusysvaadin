package za.co.edusys.domain.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    String route;
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "rolemenu", joinColumns = @JoinColumn(name = "menu_id"))
    @Enumerated(EnumType.STRING)
    List<Role> roles;

    public MenuItem(){};

    public MenuItem(String name, String route, List<Role> roles){
        this.name = name;
        this.route = route;
        this.roles = roles;
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

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
