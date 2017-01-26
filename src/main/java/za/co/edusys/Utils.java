package za.co.edusys;

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
}
