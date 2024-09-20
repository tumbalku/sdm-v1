package com.sdm.app.model.res;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SipLiteResponse {
  private String num;
  private LocalDate expiredAt;
  private UserLite user;
  private List<SipReportResponse> reports;
}
