package com.sdm.app.controller;

import com.sdm.app.entity.User;
import com.sdm.app.enumrated.CutiStatus;
import com.sdm.app.enumrated.KopType;
import com.sdm.app.model.req.RemoveFileCutiRequest;
import com.sdm.app.model.req.create.CreateCutiRequest;
import com.sdm.app.model.req.create.UserCreateCutiRequest;
import com.sdm.app.model.req.search.SearchCutiRequest;
import com.sdm.app.model.req.update.DecitionCutiRequest;
import com.sdm.app.model.req.update.UpdateCutiRequest;
import com.sdm.app.model.res.CutiResponse;
import com.sdm.app.model.res.DataReportResponse;
import com.sdm.app.model.res.WebResponse;
import com.sdm.app.model.res.WebResponseWithPagingReport;
import com.sdm.app.service.impl.CutiServiceImpl;
import com.sdm.app.utils.ResponseConverter;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/cuti")
@AllArgsConstructor
public class CutiController {

  private final CutiServiceImpl cutiService;

  @GetMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponseWithPagingReport<List<CutiResponse>, Map<KopType, Long>> search(
          @RequestParam(name = "name", required = false) String name,
          @RequestParam(name = "type", required = false) String type,
          @RequestParam(name = "statuses", required = false) List<String> statuses,
          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
          @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

    SearchCutiRequest request = new SearchCutiRequest();
    request.setName(name);
    request.setType(type);
    request.setPage(page);
    request.setSize(size);
    request.setStatuses(statuses);

    DataReportResponse<Page<CutiResponse>, Map<KopType, Long>> response = cutiService.search(request);
    return WebResponseWithPagingReport.<List<CutiResponse>, Map<KopType, Long>>builder()
            .data(response.getData().getContent())
            .report(response.getReport())
            .message("Search Success")
            .pagination(ResponseConverter.getPagingResponse(response.getData()))
            .build();
  }

  //  Download
  @GetMapping("/download/blanko")
  public ResponseEntity<byte[]> downloadBlanko(User user) throws IOException {

    Resource resource = cutiService.getBlanko(user);

    byte[] data = IOUtils.toByteArray(resource.getInputStream());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "my-cuti-report.pdf");

    return new ResponseEntity<>(data, headers, HttpStatus.OK);
  }

  @GetMapping("/download/reports")
  public ResponseEntity<byte[]> downloadReports(User user,
                                                @RequestParam(name = "year") int year) throws IOException {

    Resource resource = cutiService.cutiReportYear(user, year);

    byte[] data = IOUtils.toByteArray(resource.getInputStream());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", String.format("cuti-reports-%d.pdf", year));

    return new ResponseEntity<>(data, headers, HttpStatus.OK);
  }

  @GetMapping("/download/{id}")
  public ResponseEntity<byte[]> downloadFile(User user,
                                             @PathVariable("id") String id) throws IOException {

    Resource resource = cutiService.download(id);

    byte[] data = IOUtils.toByteArray(resource.getInputStream());


    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "cuti-document.pdf");

    return new ResponseEntity<>(data, headers, HttpStatus.OK);
  }

  @PatchMapping("/decision/{id}")
  public WebResponse<CutiResponse> makeDecisionCuti(User user,
                                                    @PathVariable("id") String id,
                                                    @RequestBody DecitionCutiRequest request) {
    request.setId(id);
    CutiResponse response = cutiService.makeDecisionCuti(user, request);
    return WebResponse.<CutiResponse>builder()
            .message("Cuti has been " + response.getMessage())
            .data(response)
            .build();
  }

  @PostMapping(path = "/request", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public WebResponse<CutiResponse> createRequest(User user,
                                                 @RequestParam(name = "dateStart", required = false) LocalDate dateStart,
                                                 @RequestParam(name = "dateEnd", required = false) LocalDate dateEnd,
                                                 @RequestParam(name = "address", required = false) String address,
                                                 @RequestParam(name = "reason", required = false) String reason,
                                                 @RequestParam(name = "workUnit", required = false) String workUnit,
                                                 @RequestParam(name = "forYear", required = false) String forYear,
                                                 @RequestParam(name = "kop", required = false) Long kop,
                                                 @RequestParam(name = "total", required = false) Integer total,
                                                 @RequestParam(name = "file", required = false) MultipartFile file) {
    UserCreateCutiRequest request = new UserCreateCutiRequest();
    request.setAddress(address);
    request.setReason(reason);
    request.setKop(kop);
    request.setDateEnd(dateEnd);
    request.setDateStart(dateStart);
    request.setWorkUnit(workUnit);
    request.setTotal(total);
    request.setFile(file);
    request.setForYear(forYear);

    CutiResponse response = cutiService.userCreateCuti(user, request);
    return WebResponse.<CutiResponse>builder()
            .data(response)
            .message("Cuti has been requested, please be patient!")
            .build();
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public WebResponse<CutiResponse> create(User user,
                                          @RequestParam(name = "dateStart", required = false) LocalDate dateStart,
                                          @RequestParam(name = "dateEnd", required = false) LocalDate dateEnd,
                                          @RequestParam(name = "number", required = false) String number,
                                          @RequestParam(name = "people", required = false) List<String> people,
                                          @RequestParam(name = "kop", required = false) Long kop,
                                          @RequestParam(name = "userId", required = false) String userId,
                                          @RequestParam(name = "address", required = false) String address,
                                          @RequestParam(name = "mark", required = false) String mark,
                                          @RequestParam(name = "message", required = false) String message,
                                          @RequestParam(name = "forYear", required = false) String forYear,
                                          @RequestParam(name = "reason", required = false) String reason,
                                          @RequestParam(name = "status", required = false) CutiStatus status,
                                          @RequestParam(name = "workUnit", required = false) String workUnit,
                                          @RequestParam(name = "signedBy", required = false) String signedBy,
                                          @RequestParam(name = "total", required = false) Integer total,
                                          @RequestParam(name = "file", required = false) MultipartFile file
  ) throws MalformedURLException, FileNotFoundException {

    CreateCutiRequest request = new CreateCutiRequest();
    request.setDateStart(dateStart);
    request.setDateEnd(dateEnd);
    request.setNumber(number);
    request.setPeople(people);
    request.setKop(kop);
    request.setUser(userId);
    request.setAddress(address);
    request.setMark(mark);
    request.setStatus(status);
    request.setWorkUnit(workUnit);
    request.setMessage(message);
    request.setReason(reason);
    request.setSignedBy(signedBy);
    request.setTotal(total);
    request.setFile(file);
    request.setForYear(forYear);

    CutiResponse response = cutiService.create(user, request);
    return WebResponse.<CutiResponse>builder()
            .data(response)
            .message("Cuti has been created")
            .build();
  }

  @PatchMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public WebResponse<CutiResponse> update(User user,
                                          @PathVariable("id") String id,
                                          @RequestParam(name = "dateStart", required = false) LocalDate dateStart,
                                          @RequestParam(name = "dateEnd", required = false) LocalDate dateEnd,
                                          @RequestParam(name = "number", required = false) String number,
                                          @RequestParam(name = "people", required = false) List<String> people,
                                          @RequestParam(name = "kop", required = false) Long kop,
                                          @RequestParam(name = "userId", required = false) String userId,
                                          @RequestParam(name = "address", required = false) String address,
                                          @RequestParam(name = "mark", required = false) String mark,
                                          @RequestParam(name = "message", required = false) String message,
                                          @RequestParam(name = "forYear", required = false) String forYear,
                                          @RequestParam(name = "status", required = false) CutiStatus status,
                                          @RequestParam(name = "workUnit", required = false) String workUnit,
                                          @RequestParam(name = "signedBy", required = false) String signedBy,
                                          @RequestParam(name = "total", required = false) Integer total,
                                          @RequestParam(name = "file", required = false) MultipartFile file) {

    UpdateCutiRequest request = new UpdateCutiRequest();
    request.setId(id);
    request.setDateStart(dateStart);
    request.setDateEnd(dateEnd);
    request.setNumber(number);
    request.setPeople(people);
    request.setKop(kop);
    request.setUser(userId);
    request.setAddress(address);
    request.setMark(mark);
    request.setStatus(status);
    request.setWorkUnit(workUnit);
    request.setMessage(message);
    request.setSignedBy(signedBy);
    request.setTotal(total);
    request.setFile(file);
    request.setForYear(forYear);

    CutiResponse response = cutiService.update(user, request);
    return WebResponse.<CutiResponse>builder()
            .data(response)
            .message("Cuti has been updated!")
            .build();
  }

  @DeleteMapping("/remove/doc/{id}")
  public WebResponse<String> deleteDoc(User user,
                                       @PathVariable("id") String id,
                                       @RequestBody RemoveFileCutiRequest request) {
    request.setId(id);
    System.out.println(request.getPath());
    String response = cutiService.removeFile(user, request);
    return WebResponse.<String>builder()
            .message(response)
            .build();
  }

  @DeleteMapping("/junk")
  public WebResponse<String> deleteJunk(User user) {
    cutiService.delete();
    return WebResponse.<String>builder()
            .data("OK")
            .message("Old cuti has been removed")
            .build();
  }

  @DeleteMapping("/{id}")
  public WebResponse<CutiResponse> delete(User user, @PathVariable("id") String id) {
    CutiResponse response = cutiService.deleteById(user, id);
    return WebResponse.<CutiResponse>builder()
            .data(response)
            .message("Cuti has been deleted!")
            .build();
  }

  @GetMapping("/current")
  public WebResponse<List<CutiResponse>> findAllCurrentCuties(User user) {
    List<CutiResponse> response = cutiService.findAllCurrentCuties(user);
    return WebResponse.<List<CutiResponse>>builder()
            .data(response)
            .message("Success get all current cuti")
            .build();
  }

  @GetMapping("/{id}")
  public WebResponse<CutiResponse> find(@PathVariable("id") String id) {
    CutiResponse response = cutiService.getById(id);
    return WebResponse.<CutiResponse>builder()
            .data(response)
            .message("Success find cuti!")
            .build();
  }
}
