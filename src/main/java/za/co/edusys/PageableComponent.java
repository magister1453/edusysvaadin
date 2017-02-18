package za.co.edusys;

import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.vaadin.ui.themes.ValoTheme.BUTTON_LINK;

/**
 * Created by marc.marais on 2017/02/03.
 */
public class PageableComponent extends VerticalLayout{
    private Button firstButton;
    private Button previousButton;
    private Button nextButton;
    private Button lastButton;
    private ComboBox pageComboBox;
    private TextField searchField;
    private int numPerPage = 5;
    private int thisPage = 0;
    private Component dataComponent;
    private JpaRepository repository;

    public PageableComponent(Component dataComponent, JpaRepository repository){
        this.dataComponent = dataComponent;
        this.repository = repository;
        searchField = new MTextField("").withCaption("Search");
        searchField.addValueChangeListener(valueChangeEvent -> {
            thisPage = 0;
            if(((String)valueChangeEvent.getProperty().getValue()).length() >= 3){
                ((MGrid)this.dataComponent).setRows(Utils.getDataForGenericRepo(repository, new PageRequest(thisPage, numPerPage), searchField.getValue()));
            } else {
                ((MGrid)this.dataComponent).setRows(Utils.getDataForGenericRepo(repository, new PageRequest(thisPage, numPerPage), ""));
            }
        });
        pageComboBox = new ComboBox("Number of items per page", Arrays.asList(5,10,20,50));
        pageComboBox.setValue(numPerPage);
        pageComboBox.addValueChangeListener( valueChangeEvent -> {
            thisPage = 0;
            numPerPage = (int)valueChangeEvent.getProperty().getValue();
            refreshData();
        });
        refreshData();
        addComponent(searchField);
        addComponent(dataComponent);
        addComponent(initDirectionalButtons());
    }

    private HorizontalLayout initDirectionalButtons(){
        HorizontalLayout paginationLayout = new HorizontalLayout();
        this.nextButton = new MButton("Next").withStyleName(BUTTON_LINK);
        this.nextButton.addClickListener(clickEvent -> {
            List list =  Utils.getDataForGenericRepo(repository, new PageRequest(++thisPage, numPerPage), searchField.getValue());
            if(list.size() != 0)
                ((MGrid)this.dataComponent).setRows(list);
            else
                thisPage--;
        });
        this.previousButton = new MButton("Previous").withStyleName(BUTTON_LINK);
        this.previousButton.addClickListener(clickEvent -> {
            if(thisPage > 0) {
                ((MGrid) this.dataComponent).setRows(Utils.getDataForGenericRepo(repository, new PageRequest(--thisPage, numPerPage), searchField.getValue()));
            }
        });
        this.firstButton = new MButton("First").withStyleName(BUTTON_LINK);
        this.firstButton.addClickListener(clickEvent -> {
            thisPage = 0;
            ((MGrid)this.dataComponent).setRows(Utils.getDataForGenericRepo(repository, new PageRequest(thisPage, numPerPage), searchField.getValue()));
        });
        this.lastButton = new MButton("Last").withStyleName(BUTTON_LINK);
        this.lastButton.addClickListener(clickEvent -> {
            //TODO fix bug with last on search
            thisPage = (int)(this.repository.count()/numPerPage);
            ((MGrid)this.dataComponent).setRows(Utils.getDataForGenericRepo(repository, new PageRequest(thisPage, numPerPage), searchField.getValue()));
        });
        paginationLayout.addComponent(pageComboBox);
        paginationLayout.addComponent(firstButton);
        paginationLayout.addComponent(previousButton);
        paginationLayout.addComponent(nextButton);
        paginationLayout.addComponent(lastButton);
        return paginationLayout;
    }

    public void refreshData() {
        if(this.dataComponent instanceof MGrid)
            ((MGrid)this.dataComponent).setRows(Utils.getDataForGenericRepo(repository, new PageRequest(thisPage, numPerPage), searchField.getValue()));
    }
}
