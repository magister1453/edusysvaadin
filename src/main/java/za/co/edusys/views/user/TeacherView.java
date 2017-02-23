package za.co.edusys.views.user;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.grid.MGrid;
import za.co.edusys.MainUI;
import za.co.edusys.PageableComponent;
import za.co.edusys.domain.model.Class;

import javax.annotation.PostConstruct;

/**
 * Created by marc.marais on 2017/02/22.
 */
@SpringView(name = "teacher")
public class TeacherView extends VerticalLayout implements View {

    @PostConstruct
    void init() {
        ((MainUI)getUI()).getParameters();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
