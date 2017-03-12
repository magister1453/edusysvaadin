package za.co.edusys.domain.model.details;

import javax.persistence.Embeddable;

/**
 * Created by marc.marais on 2017/03/10.
 */
@Embeddable
public class ContactDetails {
    private String workTelNumber;
    private String homeTelNumber;
    private String cellNumber;
    private String emailAddress;

    public ContactDetails() {
    }

    public ContactDetails(String workTelNumber, String homeTelNumber, String cellNumber, String emailAddress) {
        this.workTelNumber = workTelNumber;
        this.homeTelNumber = homeTelNumber;
        this.cellNumber = cellNumber;
        this.emailAddress = emailAddress;
    }

    public String getWorkTelNumber() {
        return workTelNumber;
    }

    public void setWorkTelNumber(String workTelNumber) {
        this.workTelNumber = workTelNumber;
    }

    public String getHomeTelNumber() {
        return homeTelNumber;
    }

    public void setHomeTelNumber(String homeTelNumber) {
        this.homeTelNumber = homeTelNumber;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
