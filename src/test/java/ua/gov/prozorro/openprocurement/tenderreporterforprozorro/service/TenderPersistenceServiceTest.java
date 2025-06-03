package ua.gov.prozorro.openprocurement.tenderreporterforprozorro.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.entitys.TenderRepository;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.records.*;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TenderPersistenceServiceTest {

    @Autowired
    private TenderPersistenceService tenderPersistenceService;

    @Autowired
    private TenderRepository tenderRepository;

    @Test
    void testSaveTenders_savesDataCorrectly() {
        TenderRecord record = new TenderRecord(
                "test-tender-id",
                OffsetDateTime.now(),
                new ProcuringEntity(
                        "Test Procuring Entity",
                        new Identifier("UA-EDR", "12345678", "Legal Name"),
                        new Address("Main St 1", "Kyiv", "Kyivska", "01001", "Ukraine"),
                        new ContactPoint("Ivan Ivanov", "ivan@example.com", "+380123456789"),
                        "general"
                )
        );

        // When: we save it
        tenderPersistenceService.saveTenders(List.of(record));

        // Then: it should be persisted
        var saved = tenderRepository.findById("test-tender-id");

        assertThat(saved).isPresent();
        assertThat(saved.get().getProcuringEntity().getName()).isEqualTo("Test Procuring Entity");
        assertThat(saved.get().getProcuringEntity().getContactPoint().getEmail()).isEqualTo("ivan@example.com");
        assertThat(saved.get().getProcuringEntity().getAddress().getLocality()).isEqualTo("Kyiv");
    }
}
