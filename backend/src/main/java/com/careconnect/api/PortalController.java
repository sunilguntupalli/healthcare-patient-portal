package com.careconnect.api;
import com.careconnect.appointment.*; import com.careconnect.record.*; import com.careconnect.user.*; import jakarta.validation.Valid; import jakarta.validation.constraints.Future; import jakarta.validation.constraints.NotBlank; import java.time.LocalDate; import java.time.LocalDateTime; import java.util.List; import org.springframework.http.*; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api") @CrossOrigin(origins="http://localhost:5174") public class PortalController {
 private final UserRepository users;private final AppointmentRepository appointments;private final MedicalRecordRepository records;
 public PortalController(UserRepository users,AppointmentRepository appointments,MedicalRecordRepository records){this.users=users;this.appointments=appointments;this.records=records;}
 private User me(Authentication a){return users.findByEmail(a.getName()).orElseThrow();}
 record AppointmentInput(@NotBlank String provider,@NotBlank String specialty,@Future LocalDateTime scheduledAt,String reason){}
 record RescheduleInput(@Future LocalDateTime scheduledAt){}
 record AppointmentView(Long id,String provider,String specialty,LocalDateTime scheduledAt,String status,String reason){}
 record ProfileView(String firstName,String lastName,String email,LocalDate dateOfBirth){}
 record ProfileInput(@NotBlank String firstName,@NotBlank String lastName,LocalDate dateOfBirth){}
 private AppointmentView view(Appointment appointment){return new AppointmentView(appointment.getId(),appointment.getProvider(),appointment.getSpecialty(),appointment.getScheduledAt(),appointment.getStatus(),appointment.getReason());}
 private ProfileView profile(User user){return new ProfileView(user.getFirstName(),user.getLastName(),user.getEmail(),user.getDateOfBirth());}
 @GetMapping("/profile") ProfileView profile(Authentication a){return profile(me(a));}
 @PutMapping("/profile") ProfileView updateProfile(@Valid @RequestBody ProfileInput input,Authentication a){User user=me(a);user.updateProfile(input.firstName(),input.lastName(),input.dateOfBirth());return profile(users.save(user));}
 @GetMapping("/appointments") List<AppointmentView> appointments(Authentication a){return appointments.findByPatientIdOrderByScheduledAtAsc(me(a).getId()).stream().map(this::view).toList();}
 @PostMapping("/appointments") ResponseEntity<AppointmentView> create(@Valid @RequestBody AppointmentInput i,Authentication a){return ResponseEntity.status(201).body(view(appointments.save(new Appointment(me(a),i.provider(),i.specialty(),i.scheduledAt(),i.reason()))));}
 @PatchMapping("/appointments/{id}/reschedule") ResponseEntity<AppointmentView> reschedule(@PathVariable Long id,@Valid @RequestBody RescheduleInput input,Authentication a){User patient=me(a);return appointments.findById(id).filter(appointment->appointment.belongsTo(patient.getId())).map(appointment->{appointment.reschedule(input.scheduledAt());return ResponseEntity.ok(view(appointments.save(appointment)));}).orElse(ResponseEntity.notFound().build());}
 @PatchMapping("/appointments/{id}/cancel") ResponseEntity<AppointmentView> cancel(@PathVariable Long id,Authentication a){User patient=me(a);return appointments.findById(id).filter(appointment->appointment.belongsTo(patient.getId())).map(appointment->{appointment.cancel();return ResponseEntity.ok(view(appointments.save(appointment)));}).orElse(ResponseEntity.notFound().build());}
 @GetMapping("/records") List<MedicalRecord> records(Authentication a){return records.findByPatientIdOrderByRecordedOnDesc(me(a).getId());}
}
