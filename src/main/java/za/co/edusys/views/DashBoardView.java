package za.co.edusys.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import za.co.edusys.MainUI;

/**
 * Created by marc.marais on 2017/01/08.
 */
@SpringView(name = "")
public class DashBoardView extends VerticalLayout implements View{
    private Navigator navigator = MainUI.navigator;

    public DashBoardView() {
        setSizeFull();
        addComponent(new Label("This is the main view"));
        addComponent(new Button("User", this::userButtonClick));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    public void userButtonClick(Button.ClickEvent e) {
        navigator.navigateTo(MainUI.USER_VIEW);
    }
}
