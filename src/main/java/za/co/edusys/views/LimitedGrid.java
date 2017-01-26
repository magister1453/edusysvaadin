package za.co.edusys.views;

import com.vaadin.data.sort.SortOrder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.vaadin.viritin.grid.MGrid;
import za.co.edusys.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by marc.marais on 2017/01/24.
 *
 */
public class LimitedGrid<T> extends MGrid{

    public boolean setLimitedRows(Class clazz, List<T> dataList, String... columns){
        try {
            this.setRows(dataList);
            FieldUtils.getAllFieldsList(clazz).forEach(field -> {
                if (!Utils.listStringEqualsIgnoreCase(field.getName(), Arrays.asList(columns))) {
                    for(Column column: this.getColumns()) {
                        if (column.getPropertyId().toString().equalsIgnoreCase(field.getName()))
                            this.removeColumn(column.getPropertyId());
                    }
                }
            });
            this.setColumnOrder(columns);
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
