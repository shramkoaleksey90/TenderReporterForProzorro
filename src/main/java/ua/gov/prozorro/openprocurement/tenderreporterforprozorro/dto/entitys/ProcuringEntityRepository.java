package ua.gov.prozorro.openprocurement.tenderreporterforprozorro.dto.entitys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcuringEntityRepository extends JpaRepository<ProcuringEntityEntity, Long> {}
