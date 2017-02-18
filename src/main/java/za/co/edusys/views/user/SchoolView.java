package za.co.edusys.views.user;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MFormLayout;
import za.co.edusys.PageableComponent;
import za.co.edusys.Utils;
import za.co.edusys.domain.model.Grade;
import za.co.edusys.domain.model.School;
import za.co.edusys.domain.repository.SchoolRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
    private TwinColSelect gradeTwinColSelect;
    private MButton createSchool = new MButton("Create School", this::createSchool);

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
        addComponent(createSchool);
        addComponent(initSchoolForm());
    }

    private void showSchoolDetails(School school){
        Utils.switchVisible(Arrays.asList(schoolForm, pageableComponent, createSchool));
        getUI().setFocusedComponent(nameField);
        nameField.setValue(school != null ? school.getName() : "");
        gradeTwinColSelect.removeAllItems();
        gradeTwinColSelect.addItems(Grade.values());
        gradeTwinColSelect.setValue(school != null ? school.getGrades() : "");
    }

    private void createSchool(Button.ClickEvent e){
        selectedSchool = null;
        showSchoolDetails(null);
    }

    private FormLayout initSchoolForm(){
        nameField = new MTextField("Name:").withRequired(true);
        gradeTwinColSelect = new TwinColSelect("Select Grades:");
        gradeTwinColSelect.addItems(Grade.values());
        schoolForm = new MFormLayout(nameField, gradeTwinColSelect, new MButton("Save",this::addEditSchool), new MButton("Cancel", this::cancelSchool)).withVisible(false);
        return schoolForm;
    }

    private void addEditSchool(Button.ClickEvent e){
        if(selectedSchool == null){
            List<Grade> gradeList = new ArrayList<>();
            gradeList.addAll((Collection) gradeTwinColSelect.getValue());
            School newSchool = new School(nameField.getValue(), true, gradeList);
            schoolRepository.save(newSchool);
            Notification.show("School " + newSchool.getName() + " succesfully created.");
        } else {
            School updatedSchool = schoolRepository.findOne(selectedSchool.getId());
            updatedSchool.setName(nameField.getValue());
            List<Grade> gradeList = new ArrayList<>();
            gradeList.addAll((Collection) gradeTwinColSelect.getValue());
            updatedSchool.setGrades(gradeList);
            schoolRepository.save(updatedSchool);
            Notification.show("School " + updatedSchool.getName() + " succesfully updated.");
        }
        pageableComponent.refreshData();
        Utils.switchVisible(Arrays.asList(schoolForm, pageableComponent, createSchool));
    }

    private void cancelSchool(Button.ClickEvent e){
        Utils.switchVisible(Arrays.asList(schoolForm, pageableComponent, createSchool));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
