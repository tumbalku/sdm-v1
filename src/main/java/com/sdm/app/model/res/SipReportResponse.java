package com.sdm.app.model.res;

import com.sdm.app.enumrated.SipReportStatus;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SipReportResponse {

  private Long id;

  private LocalDateTime sentDate;

  private SipReportStatus status;
}
