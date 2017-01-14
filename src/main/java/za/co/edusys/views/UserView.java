package za.co.edusys.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by marc.marais on 2017/01/08.
 */
@SpringView
public class UserView extends VerticalLayout implements View {

    public UserView() {
        setSizeFull();
        addComponent(new Label("This is the user view"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
