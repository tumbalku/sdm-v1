package com.sdm.app.controller;

import com.sdm.app.entity.User;
import com.sdm.app.model.req.update.UpdateSipReport;
import com.sdm.app.model.res.*;
import com.sdm.app.service.impl.SipReportServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/sip/report")
@AllArgsConstructor
public class SipRoportController {

  private final SipReportServiceImpl reportService;

  @GetMapping("/{id}")
  public WebResponse<SipReportResponse> getById(@PathVariable("id") Long id){
    SipReportResponse response = reportService.getById(id);
    return WebResponse.<SipReportResponse>builder()
            .data(response)
            .message("Success get report")
            .build();
  }


  @PostMapping("/{id}")
  public WebResponse<SipReportResponse> create(User user, @PathVariable("id") String id){

    SipReportResponse response = reportService.create(user, id);

    return WebResponse.<SipReportResponse>builder()
            .data(response)
            .message("Success add report to sip")
            .build();
  }

  @PatchMapping("/{id}")
  public WebResponse<SipReportResponse> update(User user, @PathVariable("id") Long id, @RequestBody UpdateSipReport request){
    request.setId(id);
    SipReportResponse response = reportService.update(user, request);

    return WebResponse.<SipReportResponse>builder()
            .data(response)
            .message("Success update report")
            .build();
  }

  @DeleteMapping("/{id}")
  public WebResponse<SipReportResponse> delete(User user, @PathVariable("id") Long id){
    SipReportResponse response = reportService.delete(user, id);
    return WebResponse.<SipReportResponse>builder()
            .data(response)
            .message("Report remove has been successfully")
            .build();
  }

}
