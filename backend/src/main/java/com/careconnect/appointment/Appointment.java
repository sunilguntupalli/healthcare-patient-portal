package com.careconnect.appointment;
import com.careconnect.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Appointment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @ManyToOne(optional = false) private User patient;
  @Column(nullable = false) private String provider;
  @Column(nullable = false) private String specialty;
  @Column(nullable = false) private LocalDateTime scheduledAt;
  @Column(nullable = false) private String status = "CONFIRMED";
  private String reason;
  protected Appointment() {}
  public Appointment(User patient,String provider,String specialty,LocalDateTime scheduledAt,String reason){this.patient=patient;this.provider=provider;this.specialty=specialty;this.scheduledAt=scheduledAt;this.reason=reason;}
  public void cancel(){ this.status = "CANCELLED"; }
  public void reschedule(LocalDateTime scheduledAt){this.scheduledAt=scheduledAt;this.status="CONFIRMED";}
  public boolean belongsTo(Long patientId){ return patient.getId().equals(patientId); }
  public Long getId(){return id;} public String getProvider(){return provider;} public String getSpecialty(){return specialty;} public LocalDateTime getScheduledAt(){return scheduledAt;} public String getStatus(){return status;} public String getReason(){return reason;}
}
