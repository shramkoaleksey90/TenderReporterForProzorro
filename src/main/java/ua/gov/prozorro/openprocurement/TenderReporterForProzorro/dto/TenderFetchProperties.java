package ua.gov.prozorro.openprocurement.TenderReporterForProzorro.dto;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
@ConfigurationProperties(prefix = "tender.fetch")
public record TenderFetchProperties(
        LocalDate startDate,
        LocalDate endDate,
        int maxRecords,
        int pageSize
) {}