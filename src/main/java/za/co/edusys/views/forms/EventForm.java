package za.co.edusys.views.forms;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import za.co.edusys.domain.model.Class;
import za.co.edusys.domain.model.Grade;
import za.co.edusys.domain.model.User;
import za.co.edusys.domain.model.event.Event;
import za.co.edusys.domain.model.event.EventItem;
import za.co.edusys.domain.model.event.EventStatus;
import za.co.edusys.domain.model.event.EventType;
import za.co.edusys.domain.repository.ClassRepository;
import za.co.edusys.domain.repository.EventItemRepository;
import za.co.edusys.domain.repository.EventRepository;
import za.co.edusys.domain.repository.SchoolRepository;
import za.co.edusys.views.TeacherView;
import za.co.edusys.views.components.NumberField;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by marc.marais on 2017/03/08.
 */
public class EventForm extends FormLayout {
    private ComboBox eventTypeCombo;
    private TextField nameField;
    private RichTextArea description;
    private DateField startDate;
    private DateField endDate;
    private Button addButton;
    private Button cancelButton;
    private TwinColSelect classTwinColSelect;
    private TextField markTextField;

    public EventForm() {
    }

    public EventForm(List<Class> classes, EventRepository eventRepository, EventItemRepository eventItemRepository, ClassRepository classRepository, SchoolRepository schoolRepository){
        initEventComboBox();
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        nameField = new MTextField("Name");
        description = new RichTextArea("Description");
        startDate = new MDateField("Start Date").withResolution(Resolution.MINUTE);
        endDate = new MDateField("End Date").withResolution(Resolution.MINUTE);
        cancelButton = new MButton("Cancel");
        classTwinColSelect = new TwinColSelect();
        classTwinColSelect.addItems(classes.stream().map(Class::getName).collect(Collectors.toList()).toArray());
        addButton = new MButton("Add Event");
        setAddButton(eventRepository, eventItemRepository, classRepository, schoolRepository, currentUser);
        cancelButton.addClickListener(this::resetView);
        addComponents(eventTypeCombo, classTwinColSelect, nameField, description, startDate, endDate, addButton, cancelButton);
    }

    public void updateEvent(za.co.edusys.domain.model.event.Event event, EventRepository eventRepository){
        removeComponent(eventTypeCombo);
        removeComponent(classTwinColSelect);
        addComponent(new Label(event.getEventType().getName()), 0);
        addComponent(new HorizontalLayout((Label[])event.getClasses().stream().map(aClass -> new Label(aClass.getName())).collect(Collectors.toList()).toArray()), 1);
        nameField.setValue(event.getName());
        description.setValue(event.getDescription());
        startDate.setValue(Date.from(event.getStartDate().toInstant(ZoneOffset.ofHours(2))));
        endDate.setValue(Date.from(event.getEndDate().toInstant(ZoneOffset.ofHours(2))));
        classTwinColSelect.addItems(event.getClasses().stream().map(Class::getName).collect(Collectors.toList()).toArray());

        if(event.getTotalMarks() != null){
            markTextField = new NumberField("Total Marks");
            markTextField.setValue(event.getTotalMarks().toString());
        }
        addButton = new MButton("Update Event");
        addButton.addClickListener(clickEvent -> {
            za.co.edusys.domain.model.event.Event updatedEvent = eventRepository.findOne(event.getId());
            updatedEvent.setName(nameField.getValue());
            updatedEvent.setDescription(description.getValue());
            updatedEvent.setStartDate(LocalDateTime.ofInstant(startDate.getValue().toInstant(), ZoneId.systemDefault()));
            updatedEvent.setEndDate(LocalDateTime.ofInstant(startDate.getValue().toInstant(), ZoneId.systemDefault()));
            updatedEvent.getItemList().forEach(eventItem -> {
                eventItem.setStartDate(updatedEvent.getStartDate());
                eventItem.setEndDate(updatedEvent.getEndDate());
            });
            eventRepository.save(updatedEvent);
            resetView(clickEvent);
        });
    }

    private void setAddButton(EventRepository eventRepository, EventItemRepository eventItemRepository, ClassRepository classRepository, SchoolRepository schoolRepository, User currentUser) {
        addButton.addClickListener(clickEvent -> {
            List<Class> classList = new ArrayList<>();
            ((Collection) classTwinColSelect.getValue()).forEach(o -> classList.add(classRepository.findOneBySchoolAndName(schoolRepository.findOne(currentUser.getSchool().getId()), (String)o)));
            za.co.edusys.domain.model.event.Event event = eventRepository.save(new za.co.edusys.domain.model.event.Event(nameField.getValue(),
                    (EventType)eventTypeCombo.getValue(), Arrays.asList(EventStatus.values()), EventStatus.IN_PROGRESS, description.getValue(),
                    classList, LocalDateTime.ofInstant(startDate.getValue().toInstant(), ZoneId.systemDefault()),
                    LocalDateTime.ofInstant(endDate.getValue().toInstant(), ZoneId.systemDefault()),
                    markTextField != null ? Integer.parseInt(markTextField.getValue()) : null));
            classList.forEach(aClass -> aClass.getPupils().forEach(user -> {
                eventItemRepository.save(new EventItem(EventStatus.OPEN, event, user,
                        LocalDateTime.ofInstant(startDate.getValue().toInstant(), ZoneId.systemDefault()),
                        LocalDateTime.ofInstant(endDate.getValue().toInstant(), ZoneId.systemDefault()), ""));
            }));
            resetView(clickEvent);
        });
    }

    private void resetView(Button.ClickEvent clickEvent) {
        TeacherView view = (TeacherView)(clickEvent.getButton().findAncestor(VerticalLayout.class));
        view.removeAllComponents();
        view.initView();
    }

    private void initEventComboBox() {
        eventTypeCombo = new ComboBox("Event Type");
        eventTypeCombo.addItems(Arrays.asList(EventType.values()));
        eventTypeCombo.setTextInputAllowed(false);
        eventTypeCombo.addValueChangeListener(valueChangeEvent -> {
            if (valueChangeEvent.getProperty().getValue() != EventType.HOMEWORK && !getComponent(4).getCaption().equals("Total Marks")){
                markTextField = new NumberField("Total Marks");
                addComponent(markTextField, getComponentIndex(startDate) - 1);
            } else if(valueChangeEvent.getProperty().getValue() == EventType.HOMEWORK) {
                if(markTextField != null)
                    removeComponent(markTextField);
            }
        });
    }
}
