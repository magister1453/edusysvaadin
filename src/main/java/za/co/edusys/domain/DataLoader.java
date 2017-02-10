package za.co.edusys.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import za.co.edusys.domain.model.Authorities;
import za.co.edusys.domain.model.Role;
import za.co.edusys.domain.model.User;
import za.co.edusys.domain.repository.AuthorityRepository;
import za.co.edusys.domain.repository.UserRepository;

@Component
public class DataLoader implements ApplicationRunner {

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;

    @Autowired
    public DataLoader(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        authorityRepository.save(new Authorities("ADMIN"));
        userRepository.save(new User("admin", "admin", "admin", "admin", Role.ADMIN));
        userRepository.save(new User("superadmin", "superadmin", "superadmin", "superadmin", Role.SUPERADMIN));
        userRepository.save(new User("MarcTest", "marctest", "Marc", "Marais", Role.PARENT));
        userRepository.save(new User("Test1", "1", "Test1", "Test1", Role.TEACHER));
        userRepository.save(new User("Test2", "2", "Test2", "Test2", Role.PARENT));
        userRepository.save(new User("Test3", "3", "Test3", "Test3", Role.PARENT));
        userRepository.save(new User("Test4", "4", "Test4", "Test4", Role.PARENT));
    }
}
