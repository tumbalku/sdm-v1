package com.sdm.app.controller;

import com.sdm.app.model.res.WebResponse;
import com.sdm.app.model.res.statistic.CutiSipCount;
import com.sdm.app.service.impl.StatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/stats")
@AllArgsConstructor
public class StatisticController {

  private final StatisticsService service;

  @GetMapping("count/cuti-and-sip")
  public WebResponse<CutiSipCount> getCutiStatusCount() {
    CutiSipCount response = service.getCutiSipCount();
    return WebResponse.<CutiSipCount>builder()
            .data(response)
            .message("success get count cuti and sip")
            .build();
  }
}
