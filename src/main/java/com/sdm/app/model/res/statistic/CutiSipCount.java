package com.sdm.app.model.res.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CutiSipCount {

  private Long cuti;
  private Long sip;
}
