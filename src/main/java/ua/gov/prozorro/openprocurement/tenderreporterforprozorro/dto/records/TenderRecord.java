package ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.records;

import java.time.OffsetDateTime;

public record TenderRecord(
        String id,
        OffsetDateTime dateModified,
        ProcuringEntity procuringEntity
) {}
