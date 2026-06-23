package com.careconnect.record;
import com.careconnect.user.User;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class MedicalRecord {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @ManyToOne(optional=false) private User patient;
  @Column(nullable=false) private LocalDate recordedOn;
  @Column(nullable=false) private String title;
  @Column(length=2000) private String summary;
  protected MedicalRecord(){}
  public MedicalRecord(User patient,LocalDate recordedOn,String title,String summary){this.patient=patient;this.recordedOn=recordedOn;this.title=title;this.summary=summary;}
  public Long getId(){return id;} public LocalDate getRecordedOn(){return recordedOn;} public String getTitle(){return title;} public String getSummary(){return summary;}
}
