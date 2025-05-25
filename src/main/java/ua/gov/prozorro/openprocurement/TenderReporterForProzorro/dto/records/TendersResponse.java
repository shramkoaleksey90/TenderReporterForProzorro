package ua.gov.prozorro.openprocurement.TenderReporterForProzorro.dto.records;

import java.util.List;

public record TendersResponse(
        List<TenderRecord> data
) {
}
