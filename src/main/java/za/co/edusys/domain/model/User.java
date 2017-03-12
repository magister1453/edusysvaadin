package za.co.edusys.domain.model;

import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import za.co.edusys.domain.model.details.Address;
import za.co.edusys.domain.model.details.ContactDetails;
import za.co.edusys.domain.model.event.EventItem;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Entity
@Component
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Authorities> authorities;
    private String password;
    private String userName;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;
    private String firstName;
    private String surname;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private School school;
    @Enumerated(EnumType.STRING)
    private Grade grade;
    @OneToMany(mappedBy = "user")
    private List<EventItem> eventList;
    @ManyToMany
    private List<Class> classes;
    @Embedded
    private ContactDetails contactDetails;

    public User(String password, String userName, String firstName, String surname, Role role, School school, Grade grade, ContactDetails contactDetails, Address address) {
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
        this.userName = userName;
        this.firstName = firstName;
        this.surname = surname;
        this.role = role;
        this.school = school;
        this.grade = grade;
        this.contactDetails = contactDetails;
        this.address = address;
    }

    @Embedded
    private Address address;

    public List<EventItem> getEventList() {
        return eventList;
    }

    public void setEventList(List<EventItem> eventList) {
        this.eventList = eventList;
    }

    public User() {};

    public User(String userName, String password, String firstName, String surname, Role role, School school, Grade grade){
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.userName = userName;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
        this.firstName = firstName;
        this.surname = surname;
        this.role = role;
        this.school = school;
        this.grade = grade;
        contactDetails = new ContactDetails("","","","");
        address = new Address("","","","","","");
    }

    @Autowired
    @Transient
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setAuthorities(List<Authorities> authorities) {
        this.authorities = authorities;
    }

    public void setPassword(String password) {
        this.password = passwordEncoder.encode(password);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUserName() {
        return userName;
    }

    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public BCryptPasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public String getFullName() {
        return surname + "," + firstName;
    }

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ContactDetails contactDetails) {
        this.contactDetails = contactDetails;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

