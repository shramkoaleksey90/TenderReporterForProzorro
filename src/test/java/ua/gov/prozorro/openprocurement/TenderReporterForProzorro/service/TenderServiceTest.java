package ua.gov.prozorro.openprocurement.TenderReporterForProzorro.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.gov.prozorro.openprocurement.TenderReporterForProzorro.dto.Tender;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TenderServiceTest {
    @Autowired
    private TenderService tenderService;

    @Test
    void testFetchAllTenders_ReturnsNonEmptyList() {
        List<Tender> tenders = tenderService.fetchAllTenders();
        assertThat(tenders).hasSizeGreaterThan(10); // Verify multi-batch
        assertThat(tenders.get(0).id()).isNotNull();
    }
}