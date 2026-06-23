package com.careconnect.api;
import com.careconnect.appointment.*; import com.careconnect.record.*; import com.careconnect.user.*; import jakarta.validation.constraints.NotBlank; import java.time.LocalDateTime; import java.util.List; import org.springframework.http.*; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api") @CrossOrigin(origins="http://localhost:5174") public class PortalController {
 private final UserRepository users;private final AppointmentRepository appointments;private final MedicalRecordRepository records;
 public PortalController(UserRepository users,AppointmentRepository appointments,MedicalRecordRepository records){this.users=users;this.appointments=appointments;this.records=records;}
 private User me(Authentication a){return users.findByEmail(a.getName()).orElseThrow();}
 record AppointmentInput(@NotBlank String provider,@NotBlank String specialty,LocalDateTime scheduledAt,String reason){}
 @GetMapping("/appointments") List<Appointment> appointments(Authentication a){return appointments.findByPatientIdOrderByScheduledAtAsc(me(a).getId());}
 @PostMapping("/appointments") ResponseEntity<Appointment> create(@RequestBody AppointmentInput i,Authentication a){return ResponseEntity.status(201).body(appointments.save(new Appointment(me(a),i.provider(),i.specialty(),i.scheduledAt(),i.reason())));}
 @GetMapping("/records") List<MedicalRecord> records(Authentication a){return records.findByPatientIdOrderByRecordedOnDesc(me(a).getId());}
}
