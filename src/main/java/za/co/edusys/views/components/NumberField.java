package za.co.edusys.views.components;

import com.vaadin.ui.AbstractTextField;
import org.vaadin.viritin.fields.MTextField;

/**
 * Created by marc.marais on 2017/03/10.
 */
public class NumberField extends MTextField {
    public NumberField(String caption) {
        super(caption);
        addTextChangeListener( textChangeEvent -> {
            String value = textChangeEvent.getText();
            if(!value.chars().allMatch(Character::isDigit))
                this.setValue(this.getValue().substring(0, this.getValue().length() - 1));
        });
        setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
    }
}
