package ua.gov.prozorro.openprocurement.TenderReporterForProzorro.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.gov.prozorro.openprocurement.TenderReporterForProzorro.dto.records.TenderRecord;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TenderRecordServiceTest {
    @Autowired
    private TenderService tenderService;

    @Test
    void testFetchAllTenders_ReturnsNonEmptyList() {
        List<TenderRecord> tenderRecords = tenderService.fetchAllTenders();
        assertThat(tenderRecords).hasSizeGreaterThan(10); // Verify multi-batch
        assertThat(tenderRecords.get(0).id()).isNotNull();
    }
}