package za.co.edusys.views.forms;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import org.vaadin.viritin.fields.MTextField;
import za.co.edusys.domain.model.User;
import za.co.edusys.domain.model.details.Address;

public class AddressForm extends GridLayout {
    private MTextField addressLine1Field;
    private MTextField addressLine2Field;
    private MTextField suburb;
    private MTextField city;
    private ComboBox country;
    private MTextField zipCode;

    public AddressForm() {
        setRows(1);
        setColumns(2);
        setDescription("Address");
        this.addressLine1Field = new MTextField("Address Line 1").withRequired(true);
        this.addressLine2Field = new MTextField("Address Line 2");
        this.suburb = new MTextField("Suburb");
        this.city = new MTextField("City");
        this.country = new ComboBox("County");
        country.setTextInputAllowed(false);
        country.addItem("South Africa");
        this.zipCode = new MTextField("Zip Code");
        addComponents(new FormLayout(addressLine1Field, suburb, country), new FormLayout(addressLine2Field, city, zipCode));

    }

    public void saveAddress(User user){
        Address address = new Address(addressLine1Field.getValue(), addressLine2Field.getValue(), suburb.getValue(), city.getValue(), (String)country.getValue(), zipCode.getValue());
        user.setAddress(address);
        clear();
    }

    public void setValue(User user){
        addressLine1Field.setValue(user != null ? user.getAddress().getAddressLine1() : "");
        addressLine2Field.setValue(user != null ? user.getAddress().getAddressLine2() : "");
        suburb.setValue(user != null ? user.getAddress().getSuburb() : "");
        city.setValue(user != null ? user.getAddress().getCity() : "");
        country.select(user != null ? user.getAddress().getCountry() : "");
        zipCode.setValue(user != null ? user.getAddress().getZipCode() : "");
    }

    private void clear(){
        addressLine1Field.clear();
        addressLine2Field.clear();
        suburb.clear();
        city.clear();
        country.clear();
        zipCode.clear();
    }

    public MTextField getAddressLine1Field() {
        return addressLine1Field;
    }

    public void setAddressLine1Field(MTextField addressLine1Field) {
        this.addressLine1Field = addressLine1Field;
    }

    public MTextField getAddressLine2Field() {
        return addressLine2Field;
    }

    public void setAddressLine2Field(MTextField addressLine2Field) {
        this.addressLine2Field = addressLine2Field;
    }

    public MTextField getSuburb() {
        return suburb;
    }

    public void setSuburb(MTextField suburb) {
        this.suburb = suburb;
    }

    public MTextField getCity() {
        return city;
    }

    public void setCity(MTextField city) {
        this.city = city;
    }

    public ComboBox getCountry() {
        return country;
    }

    public void setCountry(ComboBox country) {
        this.country = country;
    }

    public MTextField getZipCode() {
        return zipCode;
    }

    public void setZipCode(MTextField zipCode) {
        this.zipCode = zipCode;
    }
}
