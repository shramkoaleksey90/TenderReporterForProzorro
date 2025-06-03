package ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.records;

public record Identifier(
        String scheme,
        String id,
        String legalName
) {}
