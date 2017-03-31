package za.co.edusys.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.components.calendar.event.BasicEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import za.co.edusys.domain.model.User;
import za.co.edusys.domain.repository.EventItemRepository;
import za.co.edusys.domain.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * Created by marc.marais on 2017/02/26.
 */
@SpringView(name = "calendar")
public class CalendarView extends HorizontalLayout implements View {

    private TabSheet tabSheet;

    @Autowired
    EventItemRepository eventItemRepository;

    @Autowired
    UserRepository userRepository;

    @PostConstruct
    void init(){
        tabSheet = new TabSheet();
        tabSheet.addTab(initDailyTab(), "Daily");
        tabSheet.addTab(initWeeklyTab(), "Weekly");
        tabSheet.addTab(initMonthlyTab(), "Monthly");
//        User teacher = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        List<Class> classList = classRepository.findAllByGradeAndSubjectAndTeacher(grade, subject, teacher);
//        for(Class aClass: classList){
//            tabSheet.addTab(initClassTab(aClass), aClass.getName());
//        }
        addComponent(initEventPanel());
        addComponent(tabSheet);
    }

    private Panel initEventPanel(){
        ListSelect eventList = new ListSelect("");
        final DragAndDropWrapper listWrap = new DragAndDropWrapper(eventList);
        listWrap.setDragStartMode(DragAndDropWrapper.DragStartMode.COMPONENT);
        Panel eventPanel = new Panel(listWrap);
        return eventPanel;
    }

    private VerticalLayout initDailyTab(){
        Calendar calendar = new Calendar("");
        calendar.setTimeFormat(Calendar.TimeFormat.Format24H);
        calendar.setStartDate(Date.valueOf(LocalDate.now()));
        calendar.setEndDate(Date.valueOf(LocalDate.now()));
        addEvents(calendar);
        return new VerticalLayout(calendar);
    }

    private void addEvents(Calendar calendar) {
        User user = userRepository.findOne(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        eventItemRepository.findByUser(user).forEach(eventItem -> {
            calendar.addEvent(new BasicEvent(eventItem.getEvent().getName(), eventItem.getEvent().getDescription(),
                    Date.from(eventItem.getStartDate().toInstant(ZoneOffset.ofHours(2))),
                    Date.from(eventItem.getEndDate().toInstant(ZoneOffset.ofHours(2)))));
        });
    }

    private VerticalLayout initWeeklyTab(){
        VerticalLayout layout = new VerticalLayout();
        Calendar calendar = new Calendar("");
        calendar.setTimeFormat(Calendar.TimeFormat.Format24H);
        calendar.setStartDate(Date.valueOf(LocalDate.now()));
        calendar.setEndDate(Date.valueOf(LocalDate.now().plusDays(5)));
        addEvents(calendar);
        layout.addComponent(calendar);
        return layout;
    }

    private VerticalLayout initMonthlyTab(){
        VerticalLayout layout = new VerticalLayout();
        Calendar calendar = new Calendar("");
        calendar.setTimeFormat(Calendar.TimeFormat.Format24H);
        calendar.setStartDate(Date.valueOf(LocalDate.now()));
        calendar.setEndDate(Date.valueOf(LocalDate.now().plusDays(59)));
        addEvents(calendar);
        layout.addComponent(calendar);
        return layout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
