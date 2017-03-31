package za.co.edusys;

import com.vaadin.ui.Component;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import za.co.edusys.domain.model.Role;
import za.co.edusys.domain.model.School;
import za.co.edusys.domain.model.User;
import za.co.edusys.domain.repository.ClassRepository;
import za.co.edusys.domain.repository.SchoolRepository;
import za.co.edusys.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marc.marais on 2017/01/25.
 */
public class Utils {

    public static List getDataForGenericRepo(JpaRepository repository, PageRequest paginationParam, Object... searchParam ){
        if(repository instanceof UserRepository) {
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(loggedInUser.getRole().equals(Role.SUPERADMIN))
                return ((UserRepository) repository).findBySurnameContainingOrFirstNameContaining((String)searchParam[0], (String)searchParam[0], paginationParam);
            else
                return ((UserRepository) repository).getAdminUsers(loggedInUser.getSchool(), "%" + searchParam[0] + "%", "%" + searchParam[0] + "%",
                        paginationParam);
        }
        else if (repository instanceof SchoolRepository) {
            return ((SchoolRepository) repository).findByNameContainingIgnoreCase((String)searchParam[0], paginationParam);
        }
        else if (repository instanceof ClassRepository) {
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(loggedInUser.getRole().equals(Role.SUPERADMIN))
                return ((ClassRepository)repository).findAll();
            else
                return ((ClassRepository)repository).findAllBySchool(loggedInUser.getSchool(), paginationParam);
        }
        else
            return new ArrayList();
    }

    public static void switchVisible(List<Component> components){
        components.stream().forEach(component -> component.setVisible(!component.isVisible()));
    }
}
