package com.sdm.app.controller;

import com.sdm.app.model.req.create.CreateKopRequest;
import com.sdm.app.model.res.KopResponse;
import com.sdm.app.model.res.WebResponse;
import com.sdm.app.entity.User;
import com.sdm.app.service.impl.KopServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.itextpdf.kernel.pdf.PdfName.User;

@RestController
@RequestMapping("api/v1/kops")
@AllArgsConstructor
public class KopController {

  private final KopServiceImpl kopService;

  @GetMapping
  public WebResponse<List<KopResponse>> findAll(){

    List<KopResponse> response = kopService.findAll();
    return WebResponse.<List<KopResponse>>builder()
            .data(response)
            .message("Success get all kop")
            .build();
  }

  @GetMapping("/{id}")
  public WebResponse<KopResponse> getById(@PathVariable("id") Long id){
    KopResponse response = kopService.getById(id);
    return WebResponse.<KopResponse>builder()
            .data(response)
            .message("Success get kop")
            .build();
  }

  @DeleteMapping("/{id}")
  public WebResponse<KopResponse> delete(User user, @PathVariable("id") Long id){

    KopResponse response = kopService.delete(user, id);
    return WebResponse.<KopResponse>builder()
            .data(response)
            .message("Success delete kop")
            .build();
  }

  @PatchMapping("/{id}")
  public WebResponse<KopResponse> update(User user, @PathVariable("id") Long id, @RequestBody CreateKopRequest request){
    request.setId(id);
    KopResponse response = kopService.update(user, request);
    return WebResponse.<KopResponse>builder()
            .data(response)
            .message("Kop has been updated")
            .build();
  }

  @PostMapping
  public WebResponse<KopResponse> create(User user, @RequestBody CreateKopRequest request){
    KopResponse response = kopService.create(user, request);
    return WebResponse.<KopResponse>builder()
            .data(response)
            .message("Kop has been updated")
            .build();
  }

}
