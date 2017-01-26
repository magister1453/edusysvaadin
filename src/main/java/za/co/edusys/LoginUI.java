package za.co.edusys;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.viritin.fields.MPasswordField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.edusys.domain.repository.UserRepository;

@SpringUI(path = "/login")
@Title("LoginPage")
@Theme("valo")
public class LoginUI extends UI {

    TextField user;
    PasswordField password;
    Button loginButton = new Button("Login", this::loginButtonClick);

    @Autowired
    public DaoAuthenticationProvider daoAuthenticationProvider;

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();
        user = new MTextField("User:").withWidth("300px").withRequired(true).withInputPrompt("Your username");
        password = new MPasswordField("Password:").withWidth("300px").withRequired(true).withNullRepresentation("");
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        VerticalLayout fields = new MVerticalLayout(user, password, loginButton)
                .withCaption("Please login to access the application").withSpacing(true).withMargin(new MarginInfo(true, true, true, false)).withSizeUndefined();

        VerticalLayout uiLayout = new VerticalLayout(fields);
        uiLayout.setSizeFull();
        uiLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        setStyleName(Reindeer.LAYOUT_BLUE);
        setFocusedComponent(user);

        setContent(uiLayout);
    }

    public void loginButtonClick(Button.ClickEvent e) {
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getValue(),password.getValue());
        Authentication authenticated = daoAuthenticationProvider.authenticate(auth);
        SecurityContextHolder.getContext().setAuthentication(authenticated);
        this.getPage().setLocation("/main");
    }
}
