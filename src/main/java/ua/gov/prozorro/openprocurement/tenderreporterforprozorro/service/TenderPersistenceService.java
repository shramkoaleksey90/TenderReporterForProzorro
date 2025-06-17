package ua.gov.prozorro.openprocurement.tenderreporterforprozorro.service;

import org.springframework.stereotype.Service;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.entitys.*;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.records.TenderRecord;

import java.util.List;

@Service
public class TenderPersistenceService {

    private final TenderRepository tenderRepository;

    public TenderPersistenceService(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    public void saveTenders(List<TenderRecord> records) {
        for (TenderRecord tenderRecord : records) {
            TenderEntity entity = mapToEntity(tenderRecord);
            tenderRepository.save(entity);
        }
    }

    private TenderEntity mapToEntity(TenderRecord tenderRecord) {
        if (tenderRecord == null) {
            throw new IllegalArgumentException("TenderRecord cannot be null");
        }

        TenderEntity entity = new TenderEntity();
        entity.setTenderId(tenderRecord.id());
        entity.setDateModified(tenderRecord.dateModified());

        var procuring = tenderRecord.procuringEntity();
        if (procuring == null) {
            throw new IllegalArgumentException("ProcuringEntity cannot be null");
        }

        ContactPointEntity contact = new ContactPointEntity();
        if (procuring.contactPoint() != null) {
            var contactPoint = procuring.contactPoint();
            contact.setName(contactPoint.name());
            contact.setEmail(contactPoint.email());
            contact.setPhone(contactPoint.telephone());
        }

        AddressEntity address = new AddressEntity();
        if (procuring.address() != null) {
            var addressData = procuring.address();
            address.setStreetAddress(addressData.streetAddress());
            address.setLocality(addressData.locality());
            address.setRegion(addressData.region());
        }

        ProcuringEntityEntity procuringEntity = new ProcuringEntityEntity();
        procuringEntity.setName(procuring.name());
        if (procuring.identifier() != null) {
            procuringEntity.setIdentifierId(procuring.identifier().id());
        }
        procuringEntity.setContactPoint(contact);
        procuringEntity.setAddress(address);

        entity.setProcuringEntity(procuringEntity);

        return entity;
    }
}

