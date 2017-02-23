package za.co.edusys;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.edusys.domain.model.*;
import za.co.edusys.domain.model.Class;
import za.co.edusys.domain.repository.ClassRepository;
import za.co.edusys.domain.repository.MenuItemRepository;
import za.co.edusys.domain.repository.UserRepository;
import za.co.edusys.views.user.ClassView;
import za.co.edusys.views.user.SchoolView;
import za.co.edusys.views.user.TeacherView;
import za.co.edusys.views.user.UserView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import static com.vaadin.ui.themes.ValoTheme.BUTTON_LINK;


@SpringUI(path = "/main")
@Title("MainPage")
@Theme("valo")
public class MainUI extends UI{

    public static Navigator navigator;
    public static final String USER_VIEW = "/user";
    public static final String SCHOOL_VIEW = "/school";
    public static final String CLASS_VIEW = "/class";
    public static final String TEACHER_VIEW = "/teacher";
    private Grade grade = Grade.Grade_0;
    private Subject subject = Subject.MATHS;
    public VerticalLayout dataLayout;
    @Autowired
    SpringViewProvider viewProvider;
    @Autowired
    MenuItemRepository menuItemRepository;
    @Autowired
    ClassRepository classRepository;
    @Autowired
    UserRepository userRepository;

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
        dataLayout = new MVerticalLayout().withHeight("3000px").withFullWidth();
        UserView userView = new UserView();
        dataLayout.addComponent(userView);
        navigator = new Navigator(this, dataLayout);
        navigator.addView(USER_VIEW, new UserView());
        navigator.addView(SCHOOL_VIEW, new SchoolView());
        navigator.addView(CLASS_VIEW, new ClassView());
        navigator.addView(TEACHER_VIEW, new TeacherView());
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
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Layout tab1 = new VerticalLayout();
        menuItemRepository.findAllByRoles(user.getRole()).forEach(
                menuItem -> tab1.addComponent(new MButton(menuItem.getName(), (e -> getUI().getNavigator().navigateTo(menuItem.getRoute()))).withStyleName(BUTTON_LINK)));
        accordion.addTab(tab1, "Admin");
        if(user.getRole().equals(Role.TEACHER)){
            List<Class> teacherClasses = classRepository.findAllByTeacher(userRepository.findOne(user.getId()));
            HashMap<String, List<String>> menuMap = new HashMap<>();
            for(Class aClass : teacherClasses){
                if(menuMap.containsKey(aClass.getSubject())){
                    List<String> grades = menuMap.get(aClass.getSubject());
                    grades.add(aClass.getGrade().getName());
                } else {
                    List<String> grades = new ArrayList<>();
                    grades.add(aClass.getGrade().getName());
                    menuMap.put(aClass.getSubject().getName(), grades);
                }
            }
            for(String subject: menuMap.keySet()){
                List<String> grades = menuMap.get(subject);
                Layout tab = new VerticalLayout();

                grades.stream().forEach(s -> tab.addComponent(new MButton(s, (e -> {
                    this.subject.setName(subject);
                    this.grade.setName(s);
                    getUI().getNavigator().navigateTo("teacher");
                })).withStyleName(BUTTON_LINK)));
                accordion.addTab(tab, subject);
            }
        }
        accordion.setWidth("20%");
        accordion.setResponsive(true);
        return accordion;
    }

    public HashMap<String, String> getParameters(){
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(this.subject.getName(), this.grade.getName());
        return parameters;
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
