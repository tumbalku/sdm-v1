package com.sdm.app.controller;

import com.sdm.app.entity.User;
import com.sdm.app.model.req.create.CreateSipRequest;
import com.sdm.app.model.req.search.SearchSipRequest;
import com.sdm.app.model.req.update.UpdateSipRequest;
import com.sdm.app.model.res.*;
import com.sdm.app.service.impl.SipServiceImpl;
import com.sdm.app.utils.FileHelper;
import com.sdm.app.utils.ResponseConverter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/sip")
@AllArgsConstructor
public class SipController {

  private final SipServiceImpl sipService;

  @GetMapping("/test")
  public WebResponse<List<SipLiteResponse>> search(){
    List<SipLiteResponse> response = sipService.findAllSipLite();
    return WebResponse.<List<SipLiteResponse>>builder()
            .data(response)
            .message("oke")
            .build();
  }
  @GetMapping("current")
  public WebResponse<List<SipResponse>> getCurrentSips(User user){
    List<SipResponse> response = sipService.findCurrentSips(user);
    return WebResponse.<List<SipResponse>>builder()
            .data(response)
            .message("Success get current sips")
            .build();
  }
  @GetMapping("/{id}")
  public WebResponse<SipResponse> getById(@PathVariable("id") String id){
    SipResponse response = sipService.getById(id);
    return WebResponse.<SipResponse>builder()
            .data(response)
            .message("Success get sip")
            .build();
  }

  @GetMapping
  public WebResponseWithPaging<List<SipResponse>> search(
          @RequestParam(name = "identity", required = false) String identity,
          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
          @RequestParam(name = "size", required = false, defaultValue = "10") Integer size){

    SearchSipRequest request = new SearchSipRequest();
    request.setSize(size);
    request.setPage(page);
    request.setIdentity(identity);

    Page<SipResponse> responses = sipService.searchSip(request);

    return WebResponseWithPaging.<List<SipResponse>>builder()
            .data(responses.getContent())
            .message("Search Success")
            .pagination(ResponseConverter.getPagingResponse(responses))
            .build();
  }

  @GetMapping("pdf/{id}")
  public ResponseEntity<byte[]> getPdf(@PathVariable("id") String id) {
    // Ambil file dari database atau file system
    SipDocResponse response = sipService.getSipDoc(id);

    // Set headers untuk pengiriman PDF
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("inline", "file.pdf");

    return new ResponseEntity<>(response.getData(), headers, HttpStatus.OK);
  }

  @GetMapping("/download/{id}")
  public ResponseEntity<byte[]> downloadFile(@PathVariable String id) throws IOException {
    SipDocResponse response = sipService.getSipDoc(id);

    String extension = FileHelper.getExtensionFromMediaType(response.getType());
    String filename = response.getFilename() + extension;

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.parseMediaType(response.getType()))
            .body(response.getData());
  }

  @PostMapping("/upload")
  public WebResponse<SipResponse> save(User user,
                                          @RequestParam(name = "file", required = false) MultipartFile file,
                                          @RequestParam(name = "userId", required = false) String userId,
                                          @RequestParam(name = "num", required = false) String num,
                                          @RequestParam(name = "expiredAt", required = false) LocalDate expiredAt){

    CreateSipRequest request = new CreateSipRequest();
    request.setUserId(userId);
    request.setFile(file);
    request.setNum(num);
    request.setExpiredAt(expiredAt);

    SipResponse response = sipService.create(user, request);

    return WebResponse.<SipResponse>builder()
            .data(response)
            .message("Success add sip")
            .build();
  }

  @PatchMapping("/{id}")
  public WebResponse<SipResponse> update(User user, @PathVariable("id") String id, @RequestBody UpdateSipRequest request){

    request.setId(id);
    SipResponse response = sipService.update(user, request);

    return WebResponse.<SipResponse>builder()
            .data(response)
            .message("Success update")
            .build();
  }

  @DeleteMapping("/{id}")
  public WebResponse<SipResponse> delete(User user, @PathVariable("id") String id){
    SipResponse response = sipService.delete(user, id);
    return WebResponse.<SipResponse>builder()
            .data(response)
            .message("Remove has been success")
            .build();

  }

}
