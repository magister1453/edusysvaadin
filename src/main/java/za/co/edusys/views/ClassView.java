package za.co.edusys.views;

import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import za.co.edusys.PageableComponent;
import za.co.edusys.Utils;
import za.co.edusys.domain.model.*;
import za.co.edusys.domain.model.Class;
import za.co.edusys.domain.model.event.Event;
import za.co.edusys.domain.repository.ClassRepository;
import za.co.edusys.domain.repository.SchoolRepository;
import za.co.edusys.domain.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@SpringView(name = "class")
public class ClassView extends VerticalLayout implements View {

    @Autowired
    ClassRepository classRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SchoolRepository schoolRepository;

    private FormLayout classForm;
    private Class selectedClass;
    private MGrid<Class> classListGrid;
    private PageableComponent pageableComponent;
    private MTextField nameField;
    private ComboBox schoolComboBox;
    private ComboBox gradeComboBox;
    private ComboBox subjectComboBox;
    private ComboBox teacherComboBox;
    private TwinColSelect pupilTwinColSelect;
    private List<za.co.edusys.domain.model.event.Event> eventList = new ArrayList<>();
    private MHorizontalLayout eventLayout = new MHorizontalLayout();
    private DateField startTime;
    private DateField endTime;
    private ComboBox days;
    private MButton createClass = new MButton("Create Class", this::createClass);

    @PostConstruct
    void init() {
        classListGrid = new MGrid<Class>().withProperties("name", "grade", "subject")
                .withColumnHeaders("Name", "Grade", "Subject");

        classListGrid.addSelectionListener(selectionEvent -> {
            selectedClass = (Class)((Grid.SingleSelectionModel)
                    classListGrid.getSelectionModel()).getSelectedRow();
            if (selectedClass != null) {
                showClassDetails(selectedClass);
            }
        });
        pageableComponent = new PageableComponent(classListGrid, classRepository);
        addComponent(pageableComponent);
        addComponent(createClass);
        addComponent(initClassForm());
    }

    private FormLayout initClassForm() {
        User loggedInUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        initFields();
        eventLayout.add(startTime, endTime, days, new MButton("Add", this::addClassTime));
        if(loggedInUser.getRole().equals(Role.SUPERADMIN)) {
            schoolComboBox = new ComboBox("School", schoolRepository.findAll().stream().map(School::getName).collect(Collectors.toList()));
            schoolComboBox.addValueChangeListener(this::setComboBoxesForSchool);
            classForm = new MFormLayout(schoolComboBox, nameField, gradeComboBox, subjectComboBox, teacherComboBox, pupilTwinColSelect,
                    new MButton("Save", this::addEditClass), new MButton("Cancel", this::cancelClass)).withVisible(false);
        } else {
            gradeComboBox.addItems(loggedInUser.getSchool().getGrades());
            subjectComboBox.addItems(loggedInUser.getSchool().getSubjects());
            teacherComboBox.addItems(userRepository.findAllByRoleAndSchool(
                    Role.TEACHER, loggedInUser.getSchool()).stream().map(user -> user.getFirstName() + " " + user.getSurname()).collect(Collectors.toList()));
            classForm = new MFormLayout(new Label(loggedInUser.getSchool().getName()), nameField, gradeComboBox, subjectComboBox, teacherComboBox, pupilTwinColSelect,
                    new MButton("Save", this::addEditClass), new MButton("Cancel", this::cancelClass)).withVisible(false);
        }
        return classForm;
    }

    private void initFields() {
        nameField = new MTextField("Name:").withRequired(true);
        gradeComboBox = new ComboBox("Grade:");
        gradeComboBox.setTextInputAllowed(false);
        gradeComboBox.setRequired(true);
        gradeComboBox.addValueChangeListener(this::setPupilsForGrade);
        subjectComboBox = new ComboBox("Subject:");
        subjectComboBox.setTextInputAllowed(false);
        subjectComboBox.setRequired(true);
        teacherComboBox = new ComboBox("Teacher:");
        teacherComboBox.setTextInputAllowed(false);
        teacherComboBox.setRequired(true);
        pupilTwinColSelect = new TwinColSelect("Pupil:");
        startTime = new MDateField().withResolution(Resolution.HOUR).withRequired(true).withVisible(false);
        endTime = new MDateField().withResolution(Resolution.HOUR).withRequired(true).withVisible(false);
        days.addItems(Arrays.asList(DayOfWeek.values()));
        days.removeItem(DayOfWeek.SATURDAY);
        days.removeItem(DayOfWeek.SUNDAY);
        days.setRequired(true);
        days.setVisible(false);
    }

    private void addClassTime(Button.ClickEvent e){
        za.co.edusys.domain.model.event.Event event = new za.co.edusys.domain.model.event.Event();

    }

    private void setComboBoxesForSchool(Property.ValueChangeEvent valueChangeEvent) {
        User loggedInUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        gradeComboBox.removeAllItems();
        subjectComboBox.removeAllItems();
        teacherComboBox.removeAllItems();
        if(valueChangeEvent.getProperty().getValue() != null) {
            School school;
            if(loggedInUser.getRole().equals(Role.SUPERADMIN))
                school = schoolRepository.findOneByName((String) valueChangeEvent.getProperty().getValue());
            else
                school = schoolRepository.findOne(loggedInUser.getSchool().getId());
            gradeComboBox.addItems(school.getGrades());
            subjectComboBox.addItems(school.getSubjects());
            teacherComboBox.addItems(userRepository.findAllByRoleAndSchool(Role.TEACHER, school).stream()
                    .map(user -> user.getFirstName() + " " + user.getSurname()).collect(Collectors.toList()));
        }
    }

    private void setPupilsForGrade(Property.ValueChangeEvent valueChangeEvent) {
        User loggedInUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if(valueChangeEvent.getProperty().getValue() != null) {
            pupilTwinColSelect.setVisible(true);
            pupilTwinColSelect.removeAllItems();
            List<String> pupilNames = new ArrayList<>();
            if(loggedInUser.getRole().equals(Role.SUPERADMIN))
                userRepository.findByRoleAndGradeAndSchool(Role.PUPIL, (Grade) gradeComboBox.getValue(), schoolRepository.findOneByName((String) schoolComboBox.getValue())).forEach(user -> pupilNames.add(user.getFirstName() + " " + user.getSurname()));
            else
                userRepository.findByRoleAndGradeAndSchool(Role.PUPIL, (Grade) gradeComboBox.getValue(), schoolRepository.findOne(loggedInUser.getSchool().getId())).forEach(user -> pupilNames.add(user.getFirstName() + " " + user.getSurname()));
            pupilTwinColSelect.addItems(pupilNames);
        } else {
            pupilTwinColSelect.removeAllItems();
            pupilTwinColSelect.setVisible(false);
        }
    }

    private void showClassDetails(Class selectedClass) {
        User loggedInUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Utils.switchVisible(Arrays.asList(classForm, pageableComponent, createClass));
        getUI().setFocusedComponent(nameField);
        nameField.setValue(selectedClass != null ? selectedClass.getName() : "");
        if(loggedInUser.getRole().equals(Role.SUPERADMIN))
            schoolComboBox.select(selectedClass != null ? selectedClass.getSchool().getName() : null);
        gradeComboBox.select(selectedClass != null ? selectedClass.getGrade() : null);
        subjectComboBox.select(selectedClass != null ? selectedClass.getSubject() : null);
        teacherComboBox.select(selectedClass != null ? selectedClass.getTeacher().getFirstName() + " " + selectedClass.getTeacher().getSurname() : null);
        pupilTwinColSelect.removeAllItems();
        if(selectedClass == null)
            pupilTwinColSelect.setVisible(false);
        else {
            List<String> pupilNames = new ArrayList<>();
            userRepository.findByRoleAndGradeAndSchool(Role.PUPIL, selectedClass.getGrade(), selectedClass.getSchool()).forEach(user -> pupilNames.add(user.getFirstName() + " " + user.getSurname()));
            pupilTwinColSelect.addItems(pupilNames);
            // FIXME: 2017/02/27
            pupilTwinColSelect.setValue(selectedClass != null ? pupilNames : null);
        }

    }

    private void createClass(Button.ClickEvent e){
        showClassDetails(null);
    }

    private void cancelClass(Button.ClickEvent e){
        classListGrid.deselectAll();
        Utils.switchVisible(Arrays.asList(classForm, pageableComponent, createClass));
    }

    private void addEditClass(Button.ClickEvent e){
        School classSchool = null;
        User loggedInUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if(loggedInUser.getRole().equals(Role.ADMIN))
            classSchool = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSchool();
        else
            classSchool = this.schoolRepository.findOneByName((String)schoolComboBox.getValue());
        if(selectedClass == null){
            List<User> pupilList = new ArrayList();
            ((Collection)pupilTwinColSelect.getValue()).forEach(o -> {
                pupilList.add(userRepository.findUserByFirstNameAndSurname(((String)o).split(" ")[0], ((String)o).split(" ")[1]));
            });
            Class newClass = new Class(nameField.getValue(), classSchool, userRepository.findUserByFirstNameAndSurname(((String)teacherComboBox.getValue()).split(" ")[0], ((String)teacherComboBox.getValue()).split(" ")[1]),
                    (Subject)subjectComboBox.getValue(), (Grade)gradeComboBox.getValue(), pupilList, eventList);
            classRepository.save(newClass);
            Notification.show("Class " + newClass.getName() + " succesfully created.");
        } else {
            Class updatedClass = classRepository.findOne(selectedClass.getId());
            updatedClass.setSchool(classSchool);
            updatedClass = updateClass(updatedClass);
            classRepository.save(updatedClass);
            Notification.show("Class " + updatedClass.getName() + " succesfully updated.");
        }
        pageableComponent.refreshData();
        Utils.switchVisible(Arrays.asList(classForm, pageableComponent, createClass));
    }

    private MHorizontalLayout addClassTimes(Class selectedClass) {

//        eventList =
        return null;
    }

    private Class updateClass(Class updatedClass) {
        updatedClass.setName(nameField.getValue());
        updatedClass.setTeacher(userRepository.findUserByFirstNameAndSurname(((String)teacherComboBox.getValue()).split(" ")[0], ((String)teacherComboBox.getValue()).split(" ")[1]));
        updatedClass.setSubject((Subject)subjectComboBox.getValue());
        updatedClass.setGrade((Grade)gradeComboBox.getValue());
        updatedClass.setEvents(eventList);
        List<User> pupilList = new ArrayList();
        ((Collection)pupilTwinColSelect.getValue()).forEach(o -> {
            pupilList.add(userRepository.findUserByFirstNameAndSurname(((String)o).split(" ")[0], ((String)o).split(" ")[1]));
        });
        updatedClass.setPupils(pupilList);
        return updatedClass;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
