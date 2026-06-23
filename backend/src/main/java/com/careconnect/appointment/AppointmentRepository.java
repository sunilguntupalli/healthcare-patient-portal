package com.careconnect.appointment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AppointmentRepository extends JpaRepository<Appointment,Long>{ List<Appointment> findByPatientIdOrderByScheduledAtAsc(Long patientId); }
