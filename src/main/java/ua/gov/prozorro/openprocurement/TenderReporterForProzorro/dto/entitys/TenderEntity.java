package ua.gov.prozorro.openprocurement.TenderReporterForProzorro.dto.entitys;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.OffsetDateTime;

@Entity
public class TenderEntity {
    @Id
    private String tenderId;
    private OffsetDateTime dateModified;
    @ManyToOne(cascade = CascadeType.ALL)
    private ProcuringEntityEntity procuringEntity;

    public void setTenderId(String tenderId) {
        this.tenderId = tenderId;
    }

    public void setDateModified(OffsetDateTime dateModified) {
        this.dateModified = dateModified;
    }

    public void setProcuringEntity(ProcuringEntityEntity procuringEntity) {
        this.procuringEntity = procuringEntity;
    }

    public String getTenderId() {
        return tenderId;
    }

    public OffsetDateTime getDateModified() {
        return dateModified;
    }

    public ProcuringEntityEntity getProcuringEntity() {
        return procuringEntity;
    }
}
