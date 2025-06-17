package ua.gov.prozorro.openprocurement.tenderreporterforprozorro.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.entitys.TenderRepository;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.records.*;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

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

        tenderPersistenceService.saveTenders(List.of(record));

        var saved = tenderRepository.findById("test-tender-id");

        assertThat(saved).isPresent();
        assertThat(saved.get().getProcuringEntity().getName()).isEqualTo("Test Procuring Entity");
        assertThat(saved.get().getProcuringEntity().getContactPoint().getEmail()).isEqualTo("ivan@example.com");
        assertThat(saved.get().getProcuringEntity().getAddress().getLocality()).isEqualTo("Kyiv");
    }

    @Test
    void testSaveTenders_handlesNullTenderRecord() {
        // Test that null tender record is handled gracefully
        assertThatIllegalArgumentException().isThrownBy(
                        () -> tenderPersistenceService.saveTenders(Arrays.asList((TenderRecord) null)))
                .isInstanceOf(Exception.class);
    }

    @Test
    void testSaveTenders_handlesNullProcuringEntity() {
        TenderRecord record = new TenderRecord(
                "test-tender-null-procuring",
                OffsetDateTime.now(),
                null // null procuring entity
        );

        assertThatIllegalArgumentException().isThrownBy(
                        () -> tenderPersistenceService.saveTenders(List.of(record)))
                .isInstanceOf(IllegalArgumentException.class).withMessage("ProcuringEntity cannot be null");

    }

    @Test
    void testSaveTenders_handlesNullContactPoint() {
        TenderRecord record = new TenderRecord(
                "test-tender-null-contact",
                OffsetDateTime.now(),
                new ProcuringEntity(
                        "Test Entity",
                        new Identifier("UA-EDR", "12345678", "Legal Name"),
                        new Address("Main St 1", "Kyiv", "Kyivska", "01001", "Ukraine"),
                        null, // null contact point
                        "general"
                )
        );

        tenderPersistenceService.saveTenders(List.of(record));
        var saved = tenderRepository.findById("test-tender-null-contact");

        assertThat(saved).isPresent();
        assertThat(saved.get().getProcuringEntity().getContactPoint()).isNotNull();
        assertThat(saved.get().getProcuringEntity().getContactPoint().getName()).isNull();
        assertThat(saved.get().getProcuringEntity().getContactPoint().getEmail()).isNull();
        assertThat(saved.get().getProcuringEntity().getContactPoint().getPhone()).isNull();
    }

    @Test
    void testSaveTenders_handlesNullAddress() {
        TenderRecord record = new TenderRecord(
                "test-tender-null-address",
                OffsetDateTime.now(),
                new ProcuringEntity(
                        "Test Entity",
                        new Identifier("UA-EDR", "12345678", "Legal Name"),
                        null, // null address
                        new ContactPoint("Ivan Ivanov", "ivan@example.com", "+380123456789"),
                        "general"
                )
        );

        tenderPersistenceService.saveTenders(List.of(record));
        var saved = tenderRepository.findById("test-tender-null-address");

        assertThat(saved).isPresent();
        assertThat(saved.get().getProcuringEntity().getAddress()).isNotNull();
        assertThat(saved.get().getProcuringEntity().getAddress().getStreetAddress()).isNull();
        assertThat(saved.get().getProcuringEntity().getAddress().getLocality()).isNull();
        assertThat(saved.get().getProcuringEntity().getAddress().getRegion()).isNull();
    }

    @Test
    void testSaveTenders_handlesNullIdentifier() {
        TenderRecord record = new TenderRecord(
                "test-tender-null-identifier",
                OffsetDateTime.now(),
                new ProcuringEntity(
                        "Test Entity",
                        null, // null identifier
                        new Address("Main St 1", "Kyiv", "Kyivska", "01001", "Ukraine"),
                        new ContactPoint("Ivan Ivanov", "ivan@example.com", "+380123456789"),
                        "general"
                )
        );

        tenderPersistenceService.saveTenders(List.of(record));
        var saved = tenderRepository.findById("test-tender-null-identifier");

        assertThat(saved).isPresent();
        assertThat(saved.get().getProcuringEntity().getIdentifierId()).isNull();
    }

    @Test
    void testSaveTenders_handlesPartiallyNullContactFields() {
        TenderRecord record = new TenderRecord(
                "test-tender-partial-contact",
                OffsetDateTime.now(),
                new ProcuringEntity(
                        "Test Entity",
                        new Identifier("UA-EDR", "12345678", "Legal Name"),
                        new Address("Main St 1", "Kyiv", "Kyivska", "01001", "Ukraine"),
                        new ContactPoint("Ivan Ivanov", null, null), // only name provided
                        "general"
                )
        );

        tenderPersistenceService.saveTenders(List.of(record));
        var saved = tenderRepository.findById("test-tender-partial-contact");

        assertThat(saved).isPresent();
        assertThat(saved.get().getProcuringEntity().getContactPoint().getName()).isEqualTo("Ivan Ivanov");
        assertThat(saved.get().getProcuringEntity().getContactPoint().getEmail()).isNull();
        assertThat(saved.get().getProcuringEntity().getContactPoint().getPhone()).isNull();
    }

    @Test
    void testSaveTenders_handlesPartiallyNullAddressFields() {
        TenderRecord record = new TenderRecord(
                "test-tender-partial-address",
                OffsetDateTime.now(),
                new ProcuringEntity(
                        "Test Entity",
                        new Identifier("UA-EDR", "12345678", "Legal Name"),
                        new Address("Main St 1", null, null, "01001", "Ukraine"), // only street and postal code
                        new ContactPoint("Ivan Ivanov", "ivan@example.com", "+380123456789"),
                        "general"
                )
        );

        tenderPersistenceService.saveTenders(List.of(record));
        var saved = tenderRepository.findById("test-tender-partial-address");

        assertThat(saved).isPresent();
        assertThat(saved.get().getProcuringEntity().getAddress().getStreetAddress()).isEqualTo("Main St 1");
        assertThat(saved.get().getProcuringEntity().getAddress().getLocality()).isNull();
        assertThat(saved.get().getProcuringEntity().getAddress().getRegion()).isNull();
    }
}
