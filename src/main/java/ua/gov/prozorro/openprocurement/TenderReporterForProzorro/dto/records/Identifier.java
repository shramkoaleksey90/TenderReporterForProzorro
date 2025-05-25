package ua.gov.prozorro.openprocurement.TenderReporterForProzorro.dto.records;

public record Identifier(
        String scheme,
        String id,
        String legalName
) {}
