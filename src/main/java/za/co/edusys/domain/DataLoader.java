package za.co.edusys.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import za.co.edusys.domain.model.*;
import za.co.edusys.domain.model.Class;
import za.co.edusys.domain.model.details.Address;
import za.co.edusys.domain.model.details.ContactDetails;
import za.co.edusys.domain.model.event.Event;
import za.co.edusys.domain.model.event.EventItem;
import za.co.edusys.domain.model.event.EventStatus;
import za.co.edusys.domain.model.event.EventType;
import za.co.edusys.domain.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataLoader implements ApplicationRunner {

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private SchoolRepository schoolRepository;
    private MenuItemRepository menuItemRepository;
    private ClassRepository classRepository;
    private TermRepository termRepository;
    private EventRepository eventRepository;
    private EventItemRepository eventItemRepository;

    @Autowired
    public DataLoader(UserRepository userRepository, AuthorityRepository authorityRepository, SchoolRepository schoolRepository, MenuItemRepository menuItemRepository,
                      ClassRepository classRepository, TermRepository termRepository, EventRepository eventRepository, EventItemRepository eventItemRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.schoolRepository = schoolRepository;
        this.menuItemRepository = menuItemRepository;
        this.classRepository = classRepository;
        this.termRepository = termRepository;
        this.eventRepository = eventRepository;
        this.eventItemRepository = eventItemRepository;
    }


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        authorityRepository.save(new Authorities("ADMIN"));
        menuItemRepository.save(new MenuItem("User Admin", "user", Arrays.asList(Role.ADMIN, Role.SUPERADMIN)));
        menuItemRepository.save(new MenuItem("School Admin", "school", Arrays.asList(Role.SUPERADMIN)));
        menuItemRepository.save(new MenuItem("Main Admin", "", Arrays.asList(Role.ADMIN, Role.SUPERADMIN)));
        menuItemRepository.save(new MenuItem("Class Admin", "class", Arrays.asList(Role.ADMIN, Role.SUPERADMIN)));
        schoolRepository.save(new School("RedHill High School", true, Grade.getHighSchool(), Arrays.asList(Subject.values())));
        schoolRepository.save(new School("Crawford College Benmore", true, Grade.getHighSchool(), Arrays.asList(Subject.values())));
        termRepository.save(new Term("Term 1", LocalDate.of(2017,2,1), LocalDate.of(2017,4,1), schoolRepository.findOneByName("RedHill High School")));
        termRepository.save(new Term("Term 1", LocalDate.of(2017,2,1), LocalDate.of(2017,4,1), schoolRepository.findOneByName("Crawford College Benmore")));
        userRepository.save(new User("admin", "admin", "admin", "admin", Role.ADMIN, schoolRepository.findOne(1L), null,
                new ContactDetails("0116781000","0116781000","0116781000","0116781000"),
                new Address("137A Cedar Place", null, "Northcliff", "Randburg", "South Africa", "2196")));
        userRepository.save(new User("superadmin", "superadmin", "superadmin", "superadmin", Role.SUPERADMIN, null, null));
        userRepository.save(new User("MarcTest", "marctest", "Marc", "Marais", Role.ADMIN, schoolRepository.findOne(1L), null));
        userRepository.save(new User("Test1", "1", "Test1", "Test1", Role.TEACHER, schoolRepository.findOne(1L), null));
        userRepository.save(new User("Test2", "2", "Test2", "Test2", Role.PARENT, null, null));
        userRepository.save(new User("Test3", "3", "Test3", "Test3", Role.PUPIL, schoolRepository.findOne(1L), Grade.Grade_10));
        userRepository.save(new User("Test4", "4", "Test4", "Test4", Role.PUPIL, schoolRepository.findOne(1L), Grade.Grade_10));
        classRepository.save(new Class("Class A", schoolRepository.findOne(1L), userRepository.findOne(4L), Subject.ENGLISH, Grade.Grade_10,
                Arrays.asList(userRepository.findOne(6L), userRepository.findOne(7L)), null));
        eventRepository.save(new Event("Test 1", EventType.TEST, Arrays.asList(EventStatus.values()), EventStatus.CLOSED, "Test 1",
                Arrays.asList(classRepository.findOne(1L)), LocalDateTime.of(2017,3,6,11,30), LocalDateTime.of(2017,3,6,1,30), 80));
        eventItemRepository.save(new EventItem(EventStatus.OPEN, eventRepository.findOne(1L), userRepository.findOne(6L), LocalDateTime.of(2017,3,9,11,30), LocalDateTime.of(2017,3,9,12,30), "40"));
        eventItemRepository.save(new EventItem(EventStatus.OPEN, eventRepository.findOne(1L), userRepository.findOne(7L), LocalDateTime.of(2017,3,9,11,30), LocalDateTime.of(2017,3,9,12,30), "70"));
        eventItemRepository.save(new EventItem(EventStatus.OPEN, eventRepository.findOne(1L), userRepository.findOne(4L), LocalDateTime.of(2017,3,9,11,30), LocalDateTime.of(2017,3,9,12,30), null));
    }
}
