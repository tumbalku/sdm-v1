package com.sdm.app.controller;

import com.sdm.app.entity.User;
import com.sdm.app.model.req.create.CreateLetterRequest;
import com.sdm.app.model.req.search.SearchFileRequest;
import com.sdm.app.model.req.update.UpdateLetterRequest;
import com.sdm.app.model.res.*;
import com.sdm.app.service.impl.LetterServiceImpl;
import com.sdm.app.utils.FileHelper;
import com.sdm.app.utils.ResponseConverter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/letter")
@AllArgsConstructor
public class LetterController {

  private final LetterServiceImpl letterService;

  @GetMapping("current")
  public WebResponse<List<LetterResponse>> getCurrentLetters(User user){
    List<LetterResponse> response = letterService.findCurrentLetters(user);
    return WebResponse.<List<LetterResponse>>builder()
            .data(response)
            .message("Success get current letters")
            .build();
  }
  @GetMapping("/{id}")
  public WebResponse<LetterResponse> getById(@PathVariable("id") Long id){
    LetterResponse response = letterService.getById(id);
    return WebResponse.<LetterResponse>builder()
            .data(response)
            .message("Success get document")
            .build();
  }

  @GetMapping
  public WebResponseWithPaging<List<LetterResponse>> search(
          @RequestParam(name = "filename", required = false) String filename,
          @RequestParam(name = "fileType", required = false) String fileType,
          @RequestParam(name = "type", required = false) String type,
          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
          @RequestParam(name = "size", required = false, defaultValue = "10") Integer size){

    SearchFileRequest request = new SearchFileRequest();
    request.setSize(size);
    request.setPage(page);
    request.setFileType(fileType);
    request.setType(type);
    request.setFilename(filename);

    Page<LetterResponse> responses = letterService.searchLetter(request);

    return WebResponseWithPaging.<List<LetterResponse>>builder()
            .data(responses.getContent())
            .message("Search Success")
            .pagination(ResponseConverter.getPagingResponse(responses))
            .build();
  }

  @GetMapping("/download/{id}")
  public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) throws IOException {
    LetterDocResponse response = letterService.getLetterDoc(id);

    String extension = FileHelper.getExtensionFromMediaType(response.getType());
    String filename = response.getFilename() + extension;

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.parseMediaType(response.getType()))
            .body(response.getData());
  }

  @PostMapping("/upload")
  public WebResponse<LetterResponse> save(User user,
                                          @RequestParam(name = "file", required = false) MultipartFile file,
                                          @RequestParam(name = "nip", required = false) String nip,
                                          @RequestParam(name = "name", required = false) String name,
                                          @RequestParam(name = "num", required = false) String num,
                                          @RequestParam(name = "expiredAt", required = false) LocalDate expiredAt,
                                          @RequestParam(name = "docType", required = false) String docType){

    CreateLetterRequest request = new CreateLetterRequest();
    request.setName(name);
    request.setType(docType);
    request.setNip(nip);
    request.setFile(file);
    request.setNum(num);
    request.setExpiredAt(expiredAt);

    LetterResponse response = letterService.create(user, request);

    return WebResponse.<LetterResponse>builder()
            .data(response)
            .message("Success add new file")
            .build();
  }

  @PatchMapping("/{id}")
  public WebResponse<LetterResponse> update(User user, @PathVariable("id") Long id, @RequestBody UpdateLetterRequest request){

    request.setId(id);
    LetterResponse response = letterService.update(user, request);

    return WebResponse.<LetterResponse>builder()
            .data(response)
            .message("Success update")
            .build();
  }

  @DeleteMapping("/{id}")
  public WebResponse<LetterResponse> delete(User user, @PathVariable("id") Long id){
    LetterResponse response = letterService.delete(user, id);
    return WebResponse.<LetterResponse>builder()
            .data(response)
            .message("Remove has been success")
            .build();

  }

}
