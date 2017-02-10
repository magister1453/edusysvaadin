package za.co.edusys.views.user;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by marc.marais on 2017/02/10.
 */
@SpringView(name = "userList")
public class SchoolView extends VerticalLayout implements View {
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
