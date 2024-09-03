package com.sdm.app.controller;

import com.sdm.app.entity.User;
import com.sdm.app.enumrated.KopType;
import com.sdm.app.model.req.create.CreateCutiRequest;
import com.sdm.app.model.req.search.SearchCutiRequest;
import com.sdm.app.model.res.*;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/cuti")
@AllArgsConstructor
public class CutiController {

  private final CutiServiceImpl cutiService;

  @GetMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponseWithPagingReport<List<CutiResponse>, Map<KopType, Long>> search (User user,
                                                                 @RequestParam(name = "name", required = false) String name,
                                                                 @RequestParam(name = "type", required = false) String type,
                                                                 @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                                 @RequestParam(name = "size", required = false, defaultValue = "10") Integer size){

    SearchCutiRequest request = new SearchCutiRequest();
    request.setName(name);
    request.setType(type);
    request.setPage(page);
    request.setSize(size);

    DataReportResponse<Page<CutiResponse>, Map<KopType, Long>> response = cutiService.search(request);
    return WebResponseWithPagingReport.<List<CutiResponse>, Map<KopType, Long>>builder()
            .data(response.getData().getContent())
            .report(response.getReport())
            .message("Search Success")
            .pagination(ResponseConverter.getPagingResponse(response.getData()))
            .build();

//    Page<CutiResponse> responses = cutiService.search(request);

//    return WebResponseWithPaging.<List<CutiResponse>>builder()
//            .data(responses.getContent())
//            .message("Search Success")
//            .pagination(ResponseConverter.getPagingResponse(responses))
//            .build();
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


  @PostMapping
  public WebResponse<CutiResponse> create(User user, @RequestBody CreateCutiRequest request)
          throws MalformedURLException, FileNotFoundException {
    CutiResponse response = cutiService.create(user, request);
    return WebResponse.<CutiResponse>builder()
            .data(response)
            .message("Cuti has been requested, Please be patient!")
            .build();
  }

  @PatchMapping("/{id}")
  public WebResponse<CutiResponse> update(User user, @PathVariable("id") String id,
                                          @RequestBody CreateCutiRequest request) {
    request.setId(id);
    CutiResponse response = cutiService.update(user, request);
    return WebResponse.<CutiResponse>builder()
            .data(response)
            .message("Cuti has been updated!")
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
