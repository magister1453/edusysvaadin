package za.co.edusys.views.user;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MFormLayout;
import za.co.edusys.PageableComponent;
import za.co.edusys.domain.model.Role;
import za.co.edusys.domain.model.School;
import za.co.edusys.domain.model.User;
import za.co.edusys.domain.repository.AuthorityRepository;
import za.co.edusys.domain.repository.SchoolRepository;
import za.co.edusys.domain.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@SpringView(name = "user")
public class UserView extends VerticalLayout implements View {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authRepository;

    @Autowired
    SchoolRepository schoolRepository;

    private FormLayout userForm;
    private User selectedUser;
    private MGrid<User> userListGrid;
    private PageableComponent pageableComponent;
    private TextField firstNameField;
    private TextField surnameField;
    private TextField userNameField;
    private ComboBox roleComboBox;
    private ComboBox schoolComboBox;

    @PostConstruct
    void init() {
        userListGrid = new MGrid<User>().withProperties("firstName", "surname", "enabled")
                .withColumnHeaders("First Name", "Surname", "Enabled");

        userListGrid.addSelectionListener(selectionEvent -> {
            selectedUser = (User)((Grid.SingleSelectionModel)
                    userListGrid.getSelectionModel()).getSelectedRow();

            if (selectedUser != null) {
                showUserDetails(selectedUser);
            }
            else
                Notification.show("Nothing selected");
        });
        pageableComponent = new PageableComponent(userListGrid, userRepository);
        addComponent(pageableComponent);
        addComponent(new Button("Create User", this::createUser));
        addComponent(initUserForm());
    }

    private void showUserDetails(User user){
        userForm.setVisible(true);
        getUI().setFocusedComponent(firstNameField);
        User loggedInUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if(user != null) {
            firstNameField.setValue(user.getFirstName());
            surnameField.setValue(user.getSurname());
            userNameField.setValue(user.getUsername());
            roleComboBox.setValue((user.getRole()));
            if(loggedInUser.getRole().equals(Role.SUPERADMIN))
                schoolComboBox.setValue((user.getSchool()));
        } else {
            firstNameField.setValue("");
            surnameField.setValue("");
            userNameField.setValue("");
            roleComboBox.setValue("");
            if(loggedInUser.getRole().equals(Role.SUPERADMIN))
                schoolComboBox.setValue("");
        }
    }

    private FormLayout initUserForm(){
        firstNameField = new MTextField("First Name:").withRequired(true);
        surnameField = new MTextField("Surname:").withRequired(true);
        userNameField = new MTextField("User Name:").withRequired(true);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        roleComboBox = new ComboBox("Role", Arrays.asList(Role.values()));
        roleComboBox.setTextInputAllowed(false);
        if(user.getRole().equals(Role.ADMIN)){
            roleComboBox.removeItem(Role.SUPERADMIN);
            userForm = new MFormLayout(new Label(user.getSchool().getName()), firstNameField, surnameField, userNameField, roleComboBox,
                    new MButton("Save",this::addEditUser), new MButton("Cancel", this::cancelUser)).withVisible(false);
        } else if(user.getRole().equals(Role.SUPERADMIN)) {
            List<String> schoolNames = new ArrayList<>();
            schoolRepository.findAll().stream().forEach(school -> schoolNames.add(school.getName()));
            schoolComboBox = new ComboBox("School", schoolNames);
            schoolComboBox.setTextInputAllowed(false);
            userForm = new MFormLayout(schoolComboBox, firstNameField, surnameField, userNameField, roleComboBox,
                    new MButton("Save",this::addEditUser), new MButton("Cancel", this::cancelUser)).withVisible(false);
        }

        return userForm;
    }

    private void createUser(Button.ClickEvent e){
        selectedUser = null;
        showUserDetails(null);
    }

    private void cancelUser(Button.ClickEvent e){
        userForm.setVisible(false);
    }

    private void addEditUser(Button.ClickEvent e){
        if(selectedUser == null){
            Optional<School> userSchool;
            User loggedInUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            if(loggedInUser.getRole().equals(Role.ADMIN))
                userSchool = Optional.of(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSchool());
            else
                userSchool = Optional.of(this.schoolRepository.findOneByName((String)schoolComboBox.getValue()));
            User newUser = new User(
                    userNameField.getValue(),
                    firstNameField.getValue() + 123,
                    firstNameField.getValue(),
                    surnameField.getValue(),
                    (Role)roleComboBox.getValue(),
                    userSchool,
                    Optional.empty()
            );
            userRepository.save(newUser);
            Notification.show("User " + newUser.getFirstName() + " " + newUser.getSurname() + " succesfully created.");
        } else {
            User updatedUser = userRepository.findOne(selectedUser.getId());
            updatedUser = updateUser(updatedUser);
            userRepository.save(updatedUser);
            Notification.show("User " + updatedUser.getFirstName() + " " + updatedUser.getSurname() + " succesfully updated.");
        }
        pageableComponent.refreshData();
        userForm.setVisible(false);
    }

    private User updateUser(User updateUser){
        updateUser.setFirstName(firstNameField.getValue());
        updateUser.setSurname(surnameField.getValue());
        updateUser.setUserName(userNameField.getValue());
        updateUser.setRole((Role)roleComboBox.getValue());
        updateUser.setSchool(this.schoolRepository.findOneByName((String)schoolComboBox.getValue()));
        return updateUser;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
