package ua.gov.prozorro.openprocurement.TenderReporterForProzorro.dto;

import java.util.List;

public record TendersResponse(
        List<Tender> data
) {
}
