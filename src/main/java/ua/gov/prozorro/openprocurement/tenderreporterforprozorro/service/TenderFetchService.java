package ua.gov.prozorro.openprocurement.tenderreporterforprozorro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.TenderFetchProperties;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.records.TenderRecord;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.records.TendersResponse;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TenderFetchService {
    private final WebClient webClient;

    private final Logger logger = LoggerFactory.getLogger(TenderFetchService.class);

    private final TenderFetchProperties tenderFetchProperties;

    public TenderFetchService(WebClient.Builder webClientBuilder, TenderFetchProperties tenderFetchProperties) {
        this.webClient = webClientBuilder
                .baseUrl("https://public.api.openprocurement.org/api/2.5")
                .build();
        this.tenderFetchProperties = tenderFetchProperties;
    }

    public List<TenderRecord> fetchAllTenders() {
        ZonedDateTime startDate = tenderFetchProperties.startDate().atStartOfDay(ZoneOffset.UTC);
        logger.info("Start Date in ZonedDateTime: {}", startDate);
        ZonedDateTime endDate = tenderFetchProperties.endDate().atStartOfDay(ZoneOffset.UTC);
        logger.info("End Date in ZonedDateTime: {}", endDate);
        int maxRecords = tenderFetchProperties.maxRecords();
        logger.info("maxRecords: {}", maxRecords);

        List<TenderRecord> allTenderRecords = new ArrayList<>();
        // Use startDate + 1 day as the offset to ensure we include tenders modified exactly at startDate
        // (due to descending order and exclusive offset behavior)
        String lastDateModified = String.valueOf(startDate.plusDays(1).toEpochSecond());
        logger.info("lastDateModified: {}", lastDateModified);
        int totalFetched = 0;

        while (totalFetched < maxRecords) {
            List<TenderRecord> batch = fetchBatch(lastDateModified);
            if (batch.isEmpty()) break;

            for (TenderRecord tenderRecord : batch) {
                ZonedDateTime tenderDate = tenderRecord.dateModified().atZoneSameInstant(ZoneOffset.UTC);
                if (tenderDate.isBefore(startDate)) {
                    return allTenderRecords;
                }
                if (!tenderDate.isBefore(startDate) && tenderDate.isBefore(endDate)) {
                    allTenderRecords.add(tenderRecord);
                    totalFetched++;
                }
            }
            lastDateModified = batch.get(batch.size() - 1).dateModified().toString();
        }
        logger.info("List of tenders: {}", allTenderRecords);
        return allTenderRecords;
    }

    private List<TenderRecord> fetchBatch(String offsetTimestamp) {
        logger.info("Fetching batch with offset: {}", offsetTimestamp);

        return webClient.get()
                .uri(uriBuilder -> {
                    var uri = uriBuilder.path("/tenders")
                            .queryParam("descending", "1")
                            .queryParam("limit", tenderFetchProperties.pageSize());

                    if (offsetTimestamp != null) {
                        uri.queryParam("offset", offsetTimestamp);
                    }
                    uri.queryParam("opt_fields", "procuringEntity");
                    var builtUri = uri.build();
                    logger.info("Built URI for request: {}", builtUri);
                    return builtUri;
                })
                .retrieve()
                .bodyToMono(TendersResponse.class)
                .blockOptional()
                .map(TendersResponse::data)
                .orElse(List.of());
    }
}
