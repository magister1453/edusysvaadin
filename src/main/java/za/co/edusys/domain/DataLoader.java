package za.co.edusys.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import za.co.edusys.domain.model.Authorities;
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
        userRepository.save(new User("admin", "$2a$06$U8xL2XI7.xPhJmKfl7/BVu4XcOplsSjbSG5W85U.fNdhfPJy6M1NO", "admin", "admin", authorityRepository.findOne(1L)));
    }
}
