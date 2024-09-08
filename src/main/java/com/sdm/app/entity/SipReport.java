package com.sdm.app.entity;

import com.sdm.app.enumrated.SipReportStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sip_reports")
public class SipReport {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime sentDate;

  @Enumerated(value = EnumType.STRING)
  private SipReportStatus status;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "sip_id", referencedColumnName = "id",  nullable = false)
  private Sip sip;

}
