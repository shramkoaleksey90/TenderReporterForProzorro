package ua.gov.prozorro.openprocurement.TenderReporterForProzorro.dto.entitys;

import jakarta.persistence.*;

@Entity
public class ProcuringEntityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String identifierId;

    @OneToOne(cascade = CascadeType.ALL)
    private ContactPointEntity contactPoint;

    @OneToOne(cascade = CascadeType.ALL)
    private AddressEntity address;

    public void setName(String name) {
        this.name = name;
    }

    public void setIdentifierId(String identifierId) {
        this.identifierId = identifierId;
    }

    public void setContactPoint(ContactPointEntity contactPoint) {
        this.contactPoint = contactPoint;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getIdentifierId() {
        return identifierId;
    }

    public ContactPointEntity getContactPoint() {
        return contactPoint;
    }

    public AddressEntity getAddress() {
        return address;
    }
}
