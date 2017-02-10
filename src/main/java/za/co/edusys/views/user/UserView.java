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
import za.co.edusys.domain.model.User;
import za.co.edusys.domain.repository.AuthorityRepository;
import za.co.edusys.domain.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Arrays;


@SpringView(name = "userList")
public class UserView extends VerticalLayout implements View {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authRepository;

    private FormLayout userForm;
    private User selectedUser;
    private MGrid<User> userListGrid;
    private PageableComponent pageableComponent;
    private TextField firstNameField;
    private TextField surnameField;
    private TextField userNameField;
    private ComboBox roleComboBox;

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
        if(user != null) {
            firstNameField.setValue(user.getFirstName());
            surnameField.setValue(user.getSurname());
            userNameField.setValue(user.getUsername());
        } else {
            firstNameField.setValue("");
            surnameField.setValue("");
            userNameField.setValue("");
        }
    }

    private FormLayout initUserForm(){
        firstNameField = new MTextField("First Name:").withRequired(true);
        surnameField = new MTextField("Surname:").withRequired(true);
        userNameField = new MTextField("User Name:").withRequired(true);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        roleComboBox = new ComboBox("Role", Arrays.asList(Role.values()));
        if(!user.getRole().equals(Role.SUPERADMIN)){
            roleComboBox.removeItem(Role.SUPERADMIN);
        }
        userForm = new MFormLayout(firstNameField, surnameField, userNameField, roleComboBox, new MButton("Save",this::addEditUser), new MButton("Cancel", this::cancelUser)).withVisible(false);
        return userForm;
    }

    private void createUser(Button.ClickEvent e){
        selectedUser = null;
        showUserDetails(null);
    }

    private void cancelUser(Button.ClickEvent e){
        userForm.setVisible(false);
        pageableComponent.setVisible(true);
    }

    private void addEditUser(Button.ClickEvent e){
        if(selectedUser == null){
            User newUser = new User(
                    userNameField.getValue(),
                    firstNameField.getValue() + 123,
                    firstNameField.getValue(),
                    surnameField.getValue(),
                    (Role)roleComboBox.getValue()
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
        return updateUser;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
