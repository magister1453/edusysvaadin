package za.co.edusys.views.user;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MPasswordField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MFormLayout;
import za.co.edusys.domain.model.User;
import za.co.edusys.domain.repository.AuthorityRepository;
import za.co.edusys.domain.repository.UserRepository;
import za.co.edusys.views.LimitedGrid;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@SpringView(name = "userList")
public class UserListView extends VerticalLayout implements View {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authRepository;

    private FormLayout userForm;
    private User selectedUser;
    private LimitedGrid<User> userListGrid;

    @PostConstruct
    void init() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(user));
        userListGrid = new LimitedGrid<>();
        userListGrid.setLimitedRows(User.class, users, "firstName", "surname", "enabled");
        userListGrid.addSelectionListener(selectionEvent -> { // Java 8
            // Get selection from the selection model
            selectedUser = (User)((Grid.SingleSelectionModel)
                    userListGrid.getSelectionModel()).getSelectedRow();

            if (selectedUser != null) {
                showUserDetails(selectedUser);
            }
            else
                Notification.show("Nothing selected");
        });
        userListGrid.setHeightByRows(users.size());
        addComponent(userListGrid);
        addComponent(new Button("Create User", this::createUser));
        addComponent(initUserForm());
    }
    void showUserDetails(User user){
        userForm.setVisible(true);
        TextField firstNameField = (MTextField) userForm.getComponent(0);
        TextField surnameField = (MTextField) userForm.getComponent(1);
        TextField userNameField = (MTextField) userForm.getComponent(2);
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

    FormLayout initUserForm(){
        TextField firstNameField = new MTextField("First Name:").withRequired(true);
        TextField surnameField = new MTextField("Surname:").withRequired(true);
        TextField userNameField = new MTextField("User Name:").withRequired(true);
        userForm = new MFormLayout(firstNameField, surnameField, userNameField, new MButton("Save",this::addEditUser)).withVisible(false);
        return userForm;
    }

    public void createUser(Button.ClickEvent e){
        selectedUser = null;
        showUserDetails(null);
    }

    public void addEditUser(Button.ClickEvent e){
        if(selectedUser == null){
            User newUser = new User(
                    ((MTextField) userForm.getComponent(2)).getValue(),
                    ((MTextField) userForm.getComponent(0)).getValue() + 123,
                    ((MTextField) userForm.getComponent(0)).getValue(),
                    ((MTextField) userForm.getComponent(1)).getValue(),
                    authRepository.findOne(3L)
            );
            userRepository.save(newUser);
            refreshData();
            Notification.show("User " + newUser.getFirstName() + " " + newUser.getSurname() + " succesfully created.");
            userForm.setVisible(false);
        } else {
            User updatedUser = userRepository.findOne(selectedUser.getId());
            updatedUser = updateUser(updatedUser);
            userRepository.save(updatedUser);
            refreshData();
            Notification.show("User " + updatedUser.getFirstName() + " " + updatedUser.getSurname() + " succesfully updated.");
            userForm.setVisible(false);
        }
    }

    private void refreshData() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(user));
        userListGrid.setLimitedRows(User.class, users, "firstName", "surname", "enabled");
    }

    private User updateUser(User updateUser){
        updateUser.setFirstName(((MTextField) userForm.getComponent(0)).getValue());
        updateUser.setSurname(((MTextField) userForm.getComponent(1)).getValue());
        updateUser.setUserName(((MTextField) userForm.getComponent(2)).getValue());
        return updateUser;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
