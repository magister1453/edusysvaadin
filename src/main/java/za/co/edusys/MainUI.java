package za.co.edusys;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.edusys.domain.repository.UserRepository;
import za.co.edusys.views.DashBoardView;
import za.co.edusys.views.user.UserListView;

import static com.vaadin.ui.themes.ValoTheme.BUTTON_LINK;


@SpringUI(path = "/main")
@Title("MainPage")
@Theme("valo")
public class MainUI extends UI{

    public static Navigator navigator;
    public static final String USER_VIEW = "/userlist";
    @Autowired
    SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout components = new MVerticalLayout(initHeader(), initMain(), initFooter());
//        navigator.addView(USER_VIEW, new UserView());
//        navigator.addView("", new DashBoardView());
//        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Label label = new Label("Thank you " + user.getFirstName() + " " + user.getSurname() + " " + user.getAuthorities().get(0).getAuthority() + ", you have logged in");
        setContent(components);
    }

    public VerticalLayout initHeader(){
        Label headerLabel = new MLabel("This is the Header").withHeight("70%").withFullWidth();
        Button logoutButton = new MButton("Logout", this::logoutButtonClick).withHeight("30%").withWidth("10%");
        VerticalLayout headerLayout = new MVerticalLayout(headerLabel, logoutButton).withHeight("240px").withFullWidth();
        headerLayout.setComponentAlignment(logoutButton, Alignment.BOTTOM_RIGHT);
        return headerLayout;
    }

    public HorizontalLayout initMain(){
        Accordion accordionMenu = initAccordianMenu();
        VerticalLayout dataLayout = new MVerticalLayout().withFullHeight().withFullWidth();
        UserListView userView = new UserListView();
        dataLayout.addComponent(userView);
        navigator = new Navigator(this, dataLayout);
        navigator.addProvider(viewProvider);
        Label rightLabel = new MLabel("This is the right label").withWidth("20%").withFullHeight();
        HorizontalLayout horizontalLayout = new MHorizontalLayout(accordionMenu, dataLayout, rightLabel).withHeight("600px").withFullWidth();
        horizontalLayout.setComponentAlignment(accordionMenu, Alignment.TOP_LEFT);
        horizontalLayout.setComponentAlignment(dataLayout, Alignment.MIDDLE_CENTER);
        horizontalLayout.setComponentAlignment(rightLabel, Alignment.MIDDLE_CENTER);
        return horizontalLayout;
    }

    private Accordion initAccordianMenu() {
        Accordion accordion = new Accordion();
        Layout tab1 = new VerticalLayout(
                new MButton("User Admin", (e -> getUI().getNavigator().navigateTo("userList"))).withStyleName(BUTTON_LINK),
                new MButton("Main Admin", (e -> getUI().getNavigator().navigateTo(""))).withStyleName(BUTTON_LINK));
        accordion.addTab(tab1, "Main Tab");
        accordion.setWidth("20%");
        accordion.setResponsive(true);
        return accordion;
    }

    public VerticalLayout initFooter(){
        VerticalLayout verticalLayout = new MVerticalLayout(new MLabel("This is the footer").withFullHeight()).withHeight("240px").withFullWidth();

        return verticalLayout;
    }

    public void logoutButtonClick(Button.ClickEvent e) {
        SecurityContextHolder.clearContext();
        this.getSession().close();
        this.getPage().setLocation("/login");
    }


}
