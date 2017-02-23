package za.co.edusys.views.user;

import com.vaadin.data.Property;
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
import za.co.edusys.Utils;
import za.co.edusys.domain.model.Grade;
import za.co.edusys.domain.model.Role;
import za.co.edusys.domain.model.School;
import za.co.edusys.domain.model.User;
import za.co.edusys.domain.repository.AuthorityRepository;
import za.co.edusys.domain.repository.SchoolRepository;
import za.co.edusys.domain.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;


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
    private ComboBox gradeComboBox;
    private MButton createUser = new MButton("Create User", this::createUser);

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
        addComponent(createUser);
        addComponent(initUserForm());
    }

    private void showUserDetails(User user){
        Utils.switchVisible(Arrays.asList(userForm, pageableComponent, createUser));
        getUI().setFocusedComponent(firstNameField);
        User loggedInUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        firstNameField.setValue(user != null ? user.getFirstName() : "");
        surnameField.setValue(user != null ? user.getSurname() : "");
        userNameField.setValue(user != null ? user.getUsername() : "");
        gradeComboBox.select(user != null ? user.getGrade() : null);
        gradeComboBox.setVisible(false);
        roleComboBox.select(user != null ? user.getRole() : null);
        if(loggedInUser.getRole().equals(Role.SUPERADMIN))
            schoolComboBox.select(user != null ? user.getSchool() : null);
    }

    private FormLayout initUserForm(){
        firstNameField = new MTextField("First Name:").withRequired(true);
        surnameField = new MTextField("Surname:").withRequired(true);
        userNameField = new MTextField("User Name:").withRequired(true);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        gradeComboBox = new ComboBox("Grade");
        gradeComboBox.setVisible(false);
        gradeComboBox.setTextInputAllowed(false);
        roleComboBox = new ComboBox("Role", Arrays.asList(Role.values()));
        roleComboBox.setTextInputAllowed(false);
        roleComboBox.addValueChangeListener(this::changeRoleCombobox);
        if(user.getRole().equals(Role.ADMIN)){
            gradeComboBox = new ComboBox("Grade", user.getSchool().getGrades());
            roleComboBox.removeItem(Role.SUPERADMIN);
            userForm = new MFormLayout(new Label(user.getSchool().getName()), firstNameField, surnameField, userNameField, roleComboBox, gradeComboBox,
                    new MButton("Save",this::addEditUser), new MButton("Cancel", this::cancelUser)).withVisible(false);
        } else if(user.getRole().equals(Role.SUPERADMIN)) {
            schoolComboBox = new ComboBox("School", schoolRepository.findAll().stream().map(school -> school.getName()).collect(Collectors.toList()));
            schoolComboBox.setTextInputAllowed(false);
            schoolComboBox.addValueChangeListener(this::changeSchoolCombobox);
            userForm = new MFormLayout(schoolComboBox, firstNameField, surnameField, userNameField, roleComboBox, gradeComboBox,
                    new MButton("Save",this::addEditUser), new MButton("Cancel", this::cancelUser)).withVisible(false);
        }
        return userForm;
    }

    private void changeRoleCombobox(Property.ValueChangeEvent valueChangeEvent) {
        Role selectedRole = (Role)roleComboBox.getValue();
        if(selectedRole != null && selectedRole.equals(Role.PUPIL) && !gradeComboBox.isVisible())
            gradeComboBox.setVisible(true);
        else if(selectedRole != null && !selectedRole.equals(Role.PUPIL) && gradeComboBox.isVisible())
            gradeComboBox.setVisible(false);
    }

    private void changeSchoolCombobox(Property.ValueChangeEvent valueChangeEvent) {
        gradeComboBox.removeAllItems();
        List<Grade> gradeList = new ArrayList<Grade>();
        if(valueChangeEvent.getProperty().getValue() != null)
            gradeList = schoolRepository.findOneByName((String)valueChangeEvent.getProperty().getValue()).getGrades();
        Collections.sort(gradeList);
        gradeComboBox.addItems(gradeList);
    }

    private void createUser(Button.ClickEvent e){
        selectedUser = null;
        showUserDetails(null);
    }

    private void cancelUser(Button.ClickEvent e){
        Utils.switchVisible(Arrays.asList(userForm, pageableComponent, createUser));
        userListGrid.deselectAll();
    }

    private void addEditUser(Button.ClickEvent e){
        if(selectedUser == null){
            Optional<School> userSchool;
            Optional<Grade> selectedGrade = Optional.empty();
            User loggedInUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            if(loggedInUser.getRole().equals(Role.ADMIN))
                userSchool = Optional.of(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSchool());
            else
                userSchool = Optional.of(this.schoolRepository.findOneByName((String)schoolComboBox.getValue()));
            if(gradeComboBox.isVisible())
                selectedGrade = Optional.of((Grade)gradeComboBox.getValue());
            User newUser = new User(userNameField.getValue(), firstNameField.getValue() + 123, firstNameField.getValue(), surnameField.getValue(),
                    (Role)roleComboBox.getValue(), userSchool, selectedGrade);
            userRepository.save(newUser);
            Notification.show("User " + newUser.getFirstName() + " " + newUser.getSurname() + " succesfully created.");
        } else {
            User updatedUser = userRepository.findOne(selectedUser.getId());
            updatedUser = updateUser(updatedUser);
            userRepository.save(updatedUser);
            Notification.show("User " + updatedUser.getFirstName() + " " + updatedUser.getSurname() + " succesfully updated.");
        }
        pageableComponent.refreshData();
        Utils.switchVisible(Arrays.asList(userForm, pageableComponent, createUser));
    }

    private User updateUser(User updateUser){
        updateUser.setFirstName(firstNameField.getValue());
        updateUser.setSurname(surnameField.getValue());
        updateUser.setUserName(userNameField.getValue());
        updateUser.setRole((Role)roleComboBox.getValue());
        updateUser.setSchool(this.schoolRepository.findOneByName((String)schoolComboBox.getValue()));
        if(gradeComboBox.isVisible())
            updateUser.setGrade((Grade)gradeComboBox.getValue());
        return updateUser;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
