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
        var procuring = tenderRecord.procuringEntity();

        ContactPointEntity contact = new ContactPointEntity();
        contact.setName(procuring.contactPoint().name());
        contact.setEmail(procuring.contactPoint().email());
        contact.setPhone(procuring.contactPoint().telephone());

        AddressEntity address = new AddressEntity();
        address.setStreetAddress(procuring.address().streetAddress());
        address.setLocality(procuring.address().locality());
        address.setRegion(procuring.address().region());

        ProcuringEntityEntity procuringEntity = new ProcuringEntityEntity();
        procuringEntity.setName(procuring.name());
        procuringEntity.setIdentifierId(procuring.identifier().id());
        procuringEntity.setContactPoint(contact);
        procuringEntity.setAddress(address);

        TenderEntity entity = new TenderEntity();
        entity.setTenderId(tenderRecord.id());
        entity.setDateModified(tenderRecord.dateModified());
        entity.setProcuringEntity(procuringEntity);

        return entity;
    }
}

