package com.sdm.app.service.impl;

import com.sdm.app.model.res.statistic.CutiSipCount;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatisticsService {

  private final SipServiceImpl sipService;
  private final CutiServiceImpl cutiService;

  public CutiSipCount getCutiSipCount() {
    long sipCount = sipService.countAll();
    long cutiCount = cutiService.countCutiAll();
    return CutiSipCount.builder()
            .sip(sipCount)
            .cuti(cutiCount)
            .build();
  }
}
