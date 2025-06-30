package ua.gov.prozorro.openprocurement.tenderreporterforprozorro.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.records.TenderRecord;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TenderFetchServiceTest {
    @Autowired
    private TenderFetchService tenderFetchService;

    @Test
    void testFetchAllTenders_ReturnsNonEmptyList() {
        List<TenderRecord> tenderRecords = tenderFetchService.fetchAllTenders();
        assertThat(tenderRecords).hasSizeGreaterThan(10); // Verify multi-batch
        assertThat(tenderRecords.get(0).id()).isNotNull();
    }
}