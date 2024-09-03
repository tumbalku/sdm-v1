package com.sdm.app.controller;


import com.sdm.app.entity.File;
import com.sdm.app.model.res.WebResponse;
import com.sdm.app.service.impl.FileServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/file")
@AllArgsConstructor
public class FileController {

  private final FileServiceImpl fileService;

  @GetMapping(path = "/image/{path}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<?> getImage(@PathVariable("path") String path){

    return ResponseEntity.status(HttpStatus.OK)
            .body(fileService.getFile(path));
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
