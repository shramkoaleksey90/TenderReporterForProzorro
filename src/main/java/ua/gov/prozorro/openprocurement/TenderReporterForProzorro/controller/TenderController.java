package ua.gov.prozorro.openprocurement.TenderReporterForProzorro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.gov.prozorro.openprocurement.TenderReporterForProzorro.dto.Tender;
import ua.gov.prozorro.openprocurement.TenderReporterForProzorro.service.TenderService;

import java.util.List;

@RestController
@RequestMapping("/api/tenders")
public class TenderController {
    private final TenderService tenderService;

    public TenderController(TenderService tenderService) {
        this.tenderService = tenderService;
    }

    @GetMapping
    public List<Tender> getAllTenders() {
        return tenderService.fetchAllTenders();
    }
}
