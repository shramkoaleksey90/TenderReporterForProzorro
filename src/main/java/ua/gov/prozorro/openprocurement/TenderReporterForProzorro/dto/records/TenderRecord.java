package ua.gov.prozorro.openprocurement.TenderReporterForProzorro.dto.records;

import java.time.OffsetDateTime;

public record TenderRecord(
        String id,
        OffsetDateTime dateModified,
        ProcuringEntity procuringEntity
) {}
