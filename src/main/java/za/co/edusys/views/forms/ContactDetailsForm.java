package za.co.edusys.views.forms;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import org.vaadin.viritin.fields.EmailField;
import org.vaadin.viritin.fields.MTextField;
import za.co.edusys.domain.model.User;
import za.co.edusys.domain.model.details.ContactDetails;

/**
 * Created by marc.marais on 2017/03/10.
 */
public class ContactDetailsForm extends GridLayout {
    private MTextField workNumberField;
    private MTextField homeNumberField;
    private MTextField cellNumberField;
    private EmailField emailAddressField;

    public ContactDetailsForm() {
        setDescription("Contact Details");
        setColumns(2);
        setRows(1);
        this.workNumberField = new MTextField("Work Number");
        this.homeNumberField = new MTextField("Home Number");
        this.cellNumberField = new MTextField("Cell Number").withRequired(true);
        this.emailAddressField = new EmailField("Email Address");
        addComponent(new FormLayout(this.workNumberField, this.homeNumberField));
        addComponent(new FormLayout(this.cellNumberField, this.emailAddressField));
    }

    public void saveContactDetails(User user){
        ContactDetails contactDetails = new ContactDetails(workNumberField.getValue(), homeNumberField.getValue(), cellNumberField.getValue(), emailAddressField.getValue());
        user.setContactDetails(contactDetails);
        clear();
    }

    public void setValue(User user){
        workNumberField.setValue(user != null ? user.getContactDetails().getWorkTelNumber() : "");
        homeNumberField.setValue(user != null ? user.getContactDetails().getHomeTelNumber() : "");
        cellNumberField.setValue(user != null ? user.getContactDetails().getCellNumber() : "");
        emailAddressField.setValue(user != null ? user.getContactDetails().getEmailAddress() : "");
    }

    private void clear(){
        workNumberField.clear();
        homeNumberField.clear();
        cellNumberField.clear();
        emailAddressField.clear();
    }

    public MTextField getWorkNumberField() {
        return workNumberField;
    }

    public void setWorkNumberField(MTextField workNumberField) {
        this.workNumberField = workNumberField;
    }

    public MTextField getHomeNumberField() {
        return homeNumberField;
    }

    public void setHomeNumberField(MTextField homeNumberField) {
        this.homeNumberField = homeNumberField;
    }

    public MTextField getCellNumberField() {
        return cellNumberField;
    }

    public void setCellNumberField(MTextField cellNumberField) {
        this.cellNumberField = cellNumberField;
    }

    public EmailField getEmailAddressField() {
        return emailAddressField;
    }

    public void setEmailAddressField(EmailField emailAddressField) {
        this.emailAddressField = emailAddressField;
    }
}
