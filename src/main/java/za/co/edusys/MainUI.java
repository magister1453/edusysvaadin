package za.co.edusys;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;
import za.co.edusys.domain.model.User;
import za.co.edusys.views.DashBoardView;
import za.co.edusys.views.UserView;


@SpringUI(path = "/main")
@Title("MainPage")
@Theme("valo")
public class MainUI extends UI{

    public static Navigator navigator;
    public static final String USER_VIEW = "/user";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout components = new MVerticalLayout(initHeader(), initMain(), initFooter());
//        navigator = new Navigator(this, dataPanel);
//        navigator.addView(USER_VIEW, new UserView());
//        navigator.addView("", new DashBoardView());
//        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Label label = new Label("Thank you " + user.getFirstName() + " " + user.getSurname() + " " + user.getAuthorities().get(0).getAuthority() + ", you have logged in");
        setContent(components);
    }

    public VerticalLayout initHeader(){
        for(Window window : this.getWindows()) {
            System.out.println(window.getHeight());
        }

        Label headerLabel = new MLabel("This is the Header").withHeight("70%").withFullWidth();
        Button logoutButton = new MButton("Logout", this::logoutButtonClick).withHeight("30%").withWidth("10%");
        VerticalLayout headerLayout = new MVerticalLayout(headerLabel, logoutButton).withHeight("240px").withFullWidth();
        headerLayout.setComponentAlignment(logoutButton, Alignment.BOTTOM_RIGHT);
        return headerLayout;
    }

    public HorizontalLayout initMain(){
        Label accordianLabel = new MLabel("This is the accordian label").withWidth("20%").withFullHeight();
        Label mainLabel = new MLabel("This is the main label").withWidth("60%").withFullHeight();
        Label rightLabel = new MLabel("This is the right label").withWidth("20%").withFullHeight();
        HorizontalLayout horizontalLayout = new MHorizontalLayout(accordianLabel, mainLabel, rightLabel).withHeight("600px").withFullWidth();
        horizontalLayout.setComponentAlignment(accordianLabel, Alignment.MIDDLE_CENTER);
        horizontalLayout.setComponentAlignment(mainLabel, Alignment.MIDDLE_CENTER);
        horizontalLayout.setComponentAlignment(rightLabel, Alignment.MIDDLE_CENTER);
        return horizontalLayout;
    }

    public VerticalLayout initFooter(){
        VerticalLayout verticalLayout = new MVerticalLayout(new MLabel("This is the footer").withFullHeight()).withHeight("240px").withFullWidth();
        
        return ;
    }

    public void logoutButtonClick(Button.ClickEvent e) {
        SecurityContextHolder.clearContext();
        this.getSession().close();
        this.getPage().setLocation("/login");
    }


}
