package com.careconnect.record;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord,Long>{ List<MedicalRecord> findByPatientIdOrderByRecordedOnDesc(Long patientId); }
