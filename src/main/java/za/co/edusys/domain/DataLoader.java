package za.co.edusys.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import za.co.edusys.domain.model.*;
import za.co.edusys.domain.model.Class;
import za.co.edusys.domain.repository.*;

import java.util.*;

@Component
public class DataLoader implements ApplicationRunner {

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private SchoolRepository schoolRepository;
    private MenuItemRepository menuItemRepository;
    private ClassRepository classRepository;

    @Autowired
    public DataLoader(UserRepository userRepository, AuthorityRepository authorityRepository, SchoolRepository schoolRepository, MenuItemRepository menuItemRepository,
                      ClassRepository classRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.schoolRepository = schoolRepository;
        this.menuItemRepository = menuItemRepository;
        this.classRepository = classRepository;
    }


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        authorityRepository.save(new Authorities("ADMIN"));
        List<Subject> subjectList = Arrays.asList(Subject.values());
        menuItemRepository.save(new MenuItem("User Admin", "user", Arrays.asList(Role.ADMIN, Role.SUPERADMIN)));
        menuItemRepository.save(new MenuItem("School Admin", "school", Arrays.asList(Role.SUPERADMIN)));
        menuItemRepository.save(new MenuItem("Main Admin", "", Arrays.asList(Role.ADMIN, Role.SUPERADMIN)));
        menuItemRepository.save(new MenuItem("Class Admin", "class", Arrays.asList(Role.ADMIN, Role.SUPERADMIN)));
        schoolRepository.save(new School("RedHill High School", true, Grade.getHighSchool(), subjectList));
        schoolRepository.save(new School("Crawford College Benmore", true, Grade.getHighSchool(), subjectList));
        userRepository.save(new User("admin", "admin", "admin", "admin", Role.ADMIN, Optional.of(schoolRepository.findOne(1L)), Optional.empty()));
        userRepository.save(new User("superadmin", "superadmin", "superadmin", "superadmin", Role.SUPERADMIN, Optional.empty(), Optional.empty()));
        userRepository.save(new User("MarcTest", "marctest", "Marc", "Marais", Role.ADMIN, Optional.of(schoolRepository.findOne(1L)), Optional.empty()));
        userRepository.save(new User("Test1", "1", "Test1", "Test1", Role.TEACHER, Optional.of(schoolRepository.findOne(1L)), Optional.empty()));
        userRepository.save(new User("Test2", "2", "Test2", "Test2", Role.PARENT, Optional.empty(), Optional.empty()));
        userRepository.save(new User("Test3", "3", "Test3", "Test3", Role.PUPIL, Optional.of(schoolRepository.findOne(1L)), Optional.of(Grade.Grade_10)));
        userRepository.save(new User("Test4", "4", "Test4", "Test4", Role.PUPIL, Optional.of(schoolRepository.findOne(1L)), Optional.of(Grade.Grade_10)));
        classRepository.save(new Class("Class A", schoolRepository.findOne(1L), userRepository.findOne(4L), Subject.ENGLISH, Grade.Grade_10,
                Arrays.asList(userRepository.findOne(6L), userRepository.findOne(7L))));
    }
}
