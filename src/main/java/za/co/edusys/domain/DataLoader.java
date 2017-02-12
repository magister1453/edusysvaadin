package za.co.edusys.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import za.co.edusys.domain.model.Authorities;
import za.co.edusys.domain.model.Role;
import za.co.edusys.domain.model.School;
import za.co.edusys.domain.model.User;
import za.co.edusys.domain.repository.AuthorityRepository;
import za.co.edusys.domain.repository.SchoolRepository;
import za.co.edusys.domain.repository.UserRepository;

import java.util.Optional;

@Component
public class DataLoader implements ApplicationRunner {

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private SchoolRepository schoolRepository;

    @Autowired
    public DataLoader(UserRepository userRepository, AuthorityRepository authorityRepository, SchoolRepository schoolRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.schoolRepository = schoolRepository;
    }


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        authorityRepository.save(new Authorities("ADMIN"));
        schoolRepository.save(new School("RedHill High School", true));
        schoolRepository.save(new School("Crawford College Benmore", true));
        userRepository.save(new User("admin", "admin", "admin", "admin", Role.ADMIN, Optional.of(schoolRepository.findOne(1L))));
        userRepository.save(new User("superadmin", "superadmin", "superadmin", "superadmin", Role.SUPERADMIN, Optional.empty()));
        userRepository.save(new User("MarcTest", "marctest", "Marc", "Marais", Role.ADMIN, Optional.of(schoolRepository.findOne(2L))));
        userRepository.save(new User("Test1", "1", "Test1", "Test1", Role.TEACHER, Optional.of(schoolRepository.findOne(1L))));
        userRepository.save(new User("Test2", "2", "Test2", "Test2", Role.PARENT, Optional.empty()));
        userRepository.save(new User("Test3", "3", "Test3", "Test3", Role.PUPIL, Optional.of(schoolRepository.findOne(1L))));
        userRepository.save(new User("Test4", "4", "Test4", "Test4", Role.PUPIL, Optional.of(schoolRepository.findOne(2L))));
    }
}
