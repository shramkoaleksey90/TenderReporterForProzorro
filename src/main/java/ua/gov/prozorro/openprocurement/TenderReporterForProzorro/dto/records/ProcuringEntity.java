package ua.gov.prozorro.openprocurement.TenderReporterForProzorro.dto.records;

public record ProcuringEntity(
        String name,
        Identifier identifier,
        Address address,
        ContactPoint contactPoint,
        String kind
) {}
