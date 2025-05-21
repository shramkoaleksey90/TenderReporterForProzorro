package ua.gov.prozorro.openprocurement.TenderReporterForProzorro.dto;

import java.time.OffsetDateTime;

public record Tender(
        String id,
        OffsetDateTime dateModified
) {
}
