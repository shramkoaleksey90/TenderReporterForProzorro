package ua.gov.prozorro.openprocurement.tenderreporterforprozorro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.records.TenderRecord;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.service.TenderFetchService;

import java.util.List;

@RestController
@RequestMapping("/api/tenders")
public class TenderController {
    private final TenderFetchService tenderFetchService;

    public TenderController(TenderFetchService tenderFetchService) {
        this.tenderFetchService = tenderFetchService;
    }

    @GetMapping
    public List<TenderRecord> getAllTenders() {
        return tenderFetchService.fetchAllTenders();
    }
}
