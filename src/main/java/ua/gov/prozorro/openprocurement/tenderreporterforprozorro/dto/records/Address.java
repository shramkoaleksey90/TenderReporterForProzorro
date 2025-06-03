package ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.records;

public record Address(
        String streetAddress,
        String locality,
        String region,
        String postalCode,
        String countryName
) {}
