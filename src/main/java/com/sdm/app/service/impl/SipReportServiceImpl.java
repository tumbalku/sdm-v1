package com.sdm.app.service.impl;

import com.sdm.app.entity.Sip;
import com.sdm.app.entity.SipReport;
import com.sdm.app.entity.User;
import com.sdm.app.enumrated.SipReportStatus;
import com.sdm.app.model.req.update.UpdateSipReport;
import com.sdm.app.model.res.SipReportResponse;
import com.sdm.app.repository.SipReportRepository;
import com.sdm.app.utils.GeneralHelper;
import com.sdm.app.utils.ResponseConverter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SipReportServiceImpl {

  private final SipReportRepository reportRepository;
  private final SipServiceImpl sipService;

  @Transactional
  public SipReportResponse create(User admin, String id){
    GeneralHelper.isAdmin(admin);

    Sip sip = sipService.getSip(id);
    SipReport report = new SipReport();
    report.setSip(sip);
    report.setStatus(SipReportStatus.TERKIRIM);
    report.setSentDate(LocalDateTime.now());

    reportRepository.save(report);
    return ResponseConverter.sipReportToResponse(report);
  }

  @Transactional
  public SipReportResponse update(User admin, UpdateSipReport request){
    GeneralHelper.isAdmin(admin);

    SipReport report = getReport(request.getId());
    report.setStatus(request.getStatus());

    reportRepository.save(report);
    return ResponseConverter.sipReportToResponse(report);
  }

  @Transactional
  public SipReportResponse delete(User admin, Long id){
    GeneralHelper.isAdmin(admin);
    SipReport report = getReport(id);
    reportRepository.delete(report);
    return ResponseConverter.sipReportToResponse(report);
  }

  public SipReportResponse getById(Long id){
    SipReport report =  getReport(id);
    return ResponseConverter.sipReportToResponse(report);
  }

  private SipReport getReport(Long id){
    return reportRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found!"));
  }
}
