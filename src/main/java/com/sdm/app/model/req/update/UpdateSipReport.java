package com.sdm.app.model.req.update;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.app.enumrated.SipReportStatus;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSipReport {

  @JsonIgnore
  private Long id;
  private SipReportStatus status;

}
