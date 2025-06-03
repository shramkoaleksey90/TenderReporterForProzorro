package ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.records;

import java.util.List;

public record TendersResponse(
        List<TenderRecord> data
) {
}
