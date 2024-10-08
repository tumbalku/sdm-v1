package com.sdm.app.controller;


import com.sdm.app.entity.File;
import com.sdm.app.model.res.DocResponse;
import com.sdm.app.model.res.SipDocResponse;
import com.sdm.app.model.res.WebResponse;
import com.sdm.app.service.impl.FileServiceImpl;
import com.sdm.app.utils.FileHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;


@RestController
@RequestMapping("api/v1/file")
@AllArgsConstructor
public class FileController {

  private final FileServiceImpl fileService;

  @GetMapping(path = "/image/{path}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<?> getImage(@PathVariable("path") String path){
    if(!Objects.nonNull(path)){
      return null;
    }
    return ResponseEntity.status(HttpStatus.OK)
            .body(fileService.getFile(path));
  }

  @GetMapping("/view/pdf/{id}")
  public ResponseEntity<byte[]> getFile(@PathVariable("id") String id) {
    DocResponse response = fileService.getDoc(id);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(response.getType()));
    headers.setContentDispositionFormData("inline", response.getFilename());
    return new ResponseEntity<>(response.getData(), headers, HttpStatus.OK);
  }

  @GetMapping("/download/{id}")
  public ResponseEntity<byte[]> downloadFile(@PathVariable String id){
    DocResponse response = fileService.getDoc(id);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + response.getFilename() + "\"")
            .contentType(MediaType.parseMediaType(response.getType()))
            .body(response.getData());
  }

  @DeleteMapping(path = "/{path}")
  public WebResponse<String> removeFile(@PathVariable("path") String path){
    fileService.removePrevFile(path);
    return WebResponse.<String>builder()
            .data("OK")
            .message("file has been removed")
            .build();
  }
}
