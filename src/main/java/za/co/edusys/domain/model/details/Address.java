package za.co.edusys.domain.model.details;

/**
 * Created by marc.marais on 2017/03/10.
 */
public class Address {
    private String addressLine1;
    private String addressLine2;
    private String suburb;
    private String city;
    private String country;
    private String zipCode;

    public Address() {
    }

    public Address(String addressLine1, String addressLine2, String suburb, String city, String country, String zipCode) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.suburb = suburb;
        this.city = city;
        this.country = country;
        this.zipCode = zipCode;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
