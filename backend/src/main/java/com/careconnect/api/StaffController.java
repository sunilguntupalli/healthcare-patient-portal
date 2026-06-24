package com.careconnect.api;

import com.careconnect.appointment.*;
import com.careconnect.record.*;
import com.careconnect.user.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api") @CrossOrigin(origins="http://localhost:5174")
public class StaffController {
  private final UserRepository users; private final AppointmentRepository appointments; private final MedicalRecordRepository records;
  public StaffController(UserRepository users,AppointmentRepository appointments,MedicalRecordRepository records){this.users=users;this.appointments=appointments;this.records=records;}
  private User me(Authentication authentication){return users.findByEmail(authentication.getName()).orElseThrow();}
  private User role(Authentication authentication,String role){User user=me(authentication);if(!role.equals(user.getRole()))throw new AccessDeniedException("This workspace is restricted.");return user;}
  private String provider(User doctor){return "Dr. "+doctor.getFirstName()+" "+doctor.getLastName();}
  record ClinicianAppointment(Long id,String patientName,String patientEmail,String specialty,String scheduledAt,String status,String reason){}
  record NoteInput(@NotBlank String summary){}
  record AdminOverview(long patients,long clinicians,long appointments,long upcoming){}
  record UserView(Long id,String fullName,String email,String role){}
  private ClinicianAppointment view(Appointment appointment){User patient=appointment.getPatient();return new ClinicianAppointment(appointment.getId(),patient.getFirstName()+" "+patient.getLastName(),patient.getEmail(),appointment.getSpecialty(),appointment.getScheduledAt().toString(),appointment.getStatus(),appointment.getReason());}
  @GetMapping("/doctor/appointments") List<ClinicianAppointment> doctorAppointments(Authentication authentication){User doctor=role(authentication,"DOCTOR");return appointments.findByProviderOrderByScheduledAtAsc(provider(doctor)).stream().map(this::view).toList();}
  @PostMapping("/doctor/appointments/{id}/notes") ResponseEntity<?> addNote(@PathVariable Long id,@Valid @RequestBody NoteInput input,Authentication authentication){User doctor=role(authentication,"DOCTOR");return appointments.findById(id).filter(a->a.getProvider().equals(provider(doctor))).<ResponseEntity<?>>map(a->{MedicalRecord record=records.save(new MedicalRecord(a.getPatient(),LocalDate.now(),"Visit note · "+provider(doctor),input.summary()));return ResponseEntity.status(HttpStatus.CREATED).body(record);}).orElseGet(()->ResponseEntity.notFound().build());}
  @GetMapping("/admin/overview") AdminOverview overview(Authentication authentication){role(authentication,"ADMIN");List<User> all=users.findAll();long patients=all.stream().filter(u->"PATIENT".equals(u.getRole())).count();long clinicians=all.stream().filter(u->"DOCTOR".equals(u.getRole())).count();long upcoming=appointments.findAll().stream().filter(a->"CONFIRMED".equals(a.getStatus())).count();return new AdminOverview(patients,clinicians,appointments.count(),upcoming);}
  @GetMapping("/admin/users") List<UserView> users(Authentication authentication){role(authentication,"ADMIN");return users.findAll().stream().map(u->new UserView(u.getId(),u.getFirstName()+" "+u.getLastName(),u.getEmail(),u.getRole())).toList();}
}
