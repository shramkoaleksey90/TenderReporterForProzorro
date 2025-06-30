package ua.gov.prozorro.openprocurement.tenderreporterforprozorro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.service.ExcelExportService;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.service.TenderFetchService;
import ua.gov.prozorro.openprocurement.tenderreporterforprozorro.service.TenderPersistenceService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@Profile("!test")
public class BatchJobRunner implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(BatchJobRunner.class);

    private final TenderFetchService fetchService;
    private final TenderPersistenceService persistenceService;
    private final ExcelExportService exportService;
    private final ApplicationContext context;

    public BatchJobRunner(
            TenderFetchService fetchService,
            TenderPersistenceService persistenceService,
            ExcelExportService exportService,
            ApplicationContext context
    ) {
        this.fetchService = fetchService;
        this.persistenceService = persistenceService;
        this.exportService = exportService;
        this.context = context;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            logger.info("Starting data fetch...");
            var fetchedTenders = fetchService.fetchAllTenders();

            logger.info("Fetched: {}  tenders.", fetchedTenders.size());
            logger.info("Saving to database...");
            persistenceService.saveTenders(fetchedTenders);

            logger.info("Exporting to Excel...");
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            exportService.exportToExcel("report" + timestamp + ".xlsx");

            logger.info("Batch job completed.");
        } catch (Exception e) {
            logger.warn("Error during batch job: {}", e.getMessage());
        } finally {
            // clean shutdown after work is done
            int exitCode = SpringApplication.exit(context, () -> 0);
            System.exit(exitCode);
        }
    }
}
