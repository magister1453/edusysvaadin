package za.co.edusys;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import za.co.edusys.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marc.marais on 2017/01/25.
 */
public class Utils {

    public static boolean listStringEqualsIgnoreCase(String toFind, List<String> possibleList){
        for(String listItem: possibleList){
            if(listItem.equalsIgnoreCase(toFind))
                return true;
        }
        return false;
    }

    public static List getDataForGenericRepo(JpaRepository repository, String searchParam, PageRequest paginationParam){
        if(repository instanceof UserRepository)
            return ((UserRepository) repository).findBySurnameContainingOrFirstNameContaining(searchParam, searchParam, paginationParam);
        else
            return new ArrayList();
    }
}
