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
import za.co.edusys.domain.repository.SchoolRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by marc.marais on 2017/02/10.
 */
@SpringView(name = "school")
public class SchoolView extends VerticalLayout implements View {

    @Autowired
    SchoolRepository schoolRepository;

    private FormLayout schoolForm;
    private School selectedSchool;
    private MGrid<School> schoolListGrid;
    private PageableComponent pageableComponent;
    private MTextField nameField;

    @PostConstruct
    void init() {
        schoolListGrid = new MGrid<School>().withProperties("name", "enabled")
                .withColumnHeaders("Name", "Enabled");

        schoolListGrid.addSelectionListener(selectionEvent -> {
            selectedSchool = (School)((Grid.SingleSelectionModel)
                    schoolListGrid.getSelectionModel()).getSelectedRow();

            if (selectedSchool != null) {
                showSchoolDetails(selectedSchool);
            }
            else
                Notification.show("Nothing selected");
        });
        pageableComponent = new PageableComponent(schoolListGrid, schoolRepository);
        addComponent(pageableComponent);
        addComponent(new Button("Create School", this::createSchool));
        addComponent(initUserForm());
    }

    private void showSchoolDetails(School school){
        schoolForm.setVisible(true);
        getUI().setFocusedComponent(nameField);
        if(school != null) {
            nameField.setValue(school.getName());
        } else {
            nameField.setValue("");
        }
    }

    private void createSchool(Button.ClickEvent e){
        selectedSchool = null;
        showSchoolDetails(null);
    }

    private FormLayout initUserForm(){
        nameField = new MTextField("Name:").withRequired(true);
        schoolForm = new MFormLayout(nameField, new MButton("Save",this::addEditSchool), new MButton("Cancel", this::cancelSchool)).withVisible(false);
        return schoolForm;
    }

    private void addEditSchool(Button.ClickEvent e){
        if(selectedSchool == null){
            School newSchool = new School(nameField.getValue(), true);
            schoolRepository.save(newSchool);
            Notification.show("School " + newSchool.getName() + " succesfully created.");
        } else {
            School updatedSchool = schoolRepository.findOne(selectedSchool.getId());
            updatedSchool.setName(nameField.getValue());
            schoolRepository.save(updatedSchool);
            Notification.show("School " + updatedSchool.getName() + " succesfully updated.");
        }
        pageableComponent.refreshData();
        schoolForm.setVisible(false);
    }

    private void cancelSchool(Button.ClickEvent e){
        schoolForm.setVisible(false);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
