package za.co.edusys.views;

import com.vaadin.event.Action;
import com.vaadin.event.MouseEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.alump.labelbutton.LabelButton;
import org.vaadin.alump.labelbutton.LabelClickEvent;
import org.vaadin.alump.labelbutton.LabelClickListener;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MGridLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import za.co.edusys.domain.model.*;
import za.co.edusys.domain.model.Class;
import za.co.edusys.domain.repository.*;
import za.co.edusys.views.forms.EventForm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.vaadin.ui.themes.ValoTheme.BUTTON_LINK;

/**
 * Created by marc.marais on 2017/02/22.
 */
@SpringView(name = "teacher")
public class TeacherView extends VerticalLayout implements View {

    private Grade grade;
    private Subject subject;
    private User teacher;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventItemRepository eventItemRepository;

    @Autowired
    private UserRepository userRepository;

    public void initView() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(initAdminTab(), "General");
        teacher = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Class> classList = classRepository.findAllByGradeAndSubjectAndTeacher(grade, subject, teacher);
        for(Class aClass: classList){
            tabSheet.addTab(initClassTab(aClass), aClass.getName());
        }
        Button addEventButton = new MButton("Create Event");
        addEventButton.addClickListener(clickEvent -> {
            createEventForm(classList, clickEvent);
        });
        HorizontalLayout buttonLayout = new MHorizontalLayout(addEventButton);
        buttonLayout.setComponentAlignment(addEventButton, Alignment.MIDDLE_CENTER);
        addComponents(tabSheet,buttonLayout);
    }

    private void createEventForm(List<Class> classList, Button.ClickEvent clickEvent) {
        clickEvent.getButton().findAncestor(VerticalLayout.class).getComponent(0).setVisible(false);
        clickEvent.getButton().findAncestor(VerticalLayout.class).getComponent(1).setVisible(false);
        clickEvent.getButton().findAncestor(VerticalLayout.class).addComponent(new EventForm(classList, eventRepository, eventItemRepository, classRepository, schoolRepository));
    }

    private VerticalLayout initClassTab(Class aClass) {

        Term currentTerm = termRepository.findBySchool(schoolRepository.findOne(teacher.getSchool().getId()))
                .filter(term -> LocalDate.now().isAfter(term.getStartDate()) && LocalDate.now().isBefore(term.getEndDate())).findFirst().get();
        List<za.co.edusys.domain.model.event.Event> eventList = eventRepository.findAllByClasses(aClass).
                filter(event -> event.getEndDate().isBefore(LocalDateTime.of(currentTerm.getEndDate(), LocalTime.MIDNIGHT))
                && event.getStartDate().isAfter(LocalDateTime.of(currentTerm.getStartDate(), LocalTime.MIDNIGHT))).collect(Collectors.toList());
        MGridLayout markGrid = new MGridLayout(eventList.size() + 1, aClass.getPupils().size() + 1).withFullWidth().withFullHeight();
        markGrid.add(new Label());
        eventList.forEach(event -> {
            Button headerButton = new MButton(event.getName()).withStyleName(BUTTON_LINK);
            markGrid.add(headerButton);
            headerButton.addClickListener(clickEvent -> {
                EventForm eventForm = new EventForm(event.getClasses(), eventRepository, eventItemRepository, classRepository, schoolRepository);
                clickEvent.getButton().findAncestor(VerticalLayout.class).getComponent(0).setVisible(false);
                clickEvent.getButton().findAncestor(VerticalLayout.class).addComponent(eventForm);
                eventForm.updateEvent(event, eventRepository);
            });
        });

        aClass.getPupils().forEach(user -> {
            markGrid.add(new MLabel(user.getFullName()));
            eventList.forEach(event -> {
                Label resultLabel = new MLabel(eventItemRepository.findByUserAndEvent(user, event).findAny().orElse(null).getResult());
                markGrid.addComponent(resultLabel);
                markGrid.setComponentAlignment(resultLabel, Alignment.MIDDLE_CENTER);
            });
        });

        return new VerticalLayout(markGrid);
    }



    private VerticalLayout initAdminTab() {
        return new VerticalLayout(new Label("General Tab"));
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if(viewChangeEvent.getParameters() != null){
            String[] parameters = viewChangeEvent.getParameters().split("&");
            subject = Subject.valueOf(parameters[0].split("=")[1].toUpperCase().replace(" ", "_"));
            grade = Grade.valueOf(parameters[1].split("=")[1].replace(" ", "_"));
        }
        initView();
    }
}
