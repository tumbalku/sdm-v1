package com.sdm.app.controller;

import com.sdm.app.entity.User;
import com.sdm.app.model.req.create.CreateDocumentRequest;
import com.sdm.app.model.req.search.SearchDocumentRequest;
import com.sdm.app.model.req.update.PinPriorityRequest;
import com.sdm.app.model.req.update.UpdateDocumentRequest;
import com.sdm.app.model.res.DocumentResponse;
import com.sdm.app.model.res.WebResponse;
import com.sdm.app.model.res.WebResponseWithPaging;
import com.sdm.app.service.impl.DocumentServiceImpl;
import com.sdm.app.utils.ResponseConverter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/documents")
@AllArgsConstructor
public class DocumentController {

  private final DocumentServiceImpl service;

  @PatchMapping(path = "priority/{id}",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<DocumentResponse> pinPriority(User user,
                                                   @PathVariable("id") String id,
                                                   @RequestBody PinPriorityRequest request) {
    request.setId(id);
    DocumentResponse response = service.pinPriority(user, request);

    return WebResponse.<DocumentResponse>builder()
            .data(response)
            .message("Success pin")
            .build();
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<DocumentResponse> getById(@PathVariable("id") String id) {

    DocumentResponse response = service.getById(id);
    return WebResponse.<DocumentResponse>builder()
            .data(response)
            .message("Success get document")
            .build();
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponseWithPaging<List<DocumentResponse>> search(@RequestParam(name = "content", required = false) String content,
                                                              @RequestParam(name = "priority", required = false) Integer priority,
                                                              @RequestParam(name = "dateSortBy", required = false, defaultValue = "latest") String dateSortBy,
                                                              @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                              @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

    SearchDocumentRequest request = SearchDocumentRequest.builder()
            .priority(priority)
            .content(content)
            .dateSortBy(dateSortBy)
            .size(size)
            .page(page)
            .build();

    Page<DocumentResponse> responses = service.searchDocuments(request);

    return WebResponseWithPaging.<List<DocumentResponse>>builder()
            .data(responses.getContent())
            .pagination(ResponseConverter.getPagingResponse(responses))
            .message("Success get all documents")
            .build();
  }

  @PatchMapping(path = "/{id}",
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<DocumentResponse> update(User user,
                                              @PathVariable("id") String id,
                                              @RequestBody UpdateDocumentRequest request) {

    request.setId(id);
    DocumentResponse response = service.update(user, request);
    return WebResponse.<DocumentResponse>builder()
            .data(response)
            .message("Berhasil update document")
            .build();
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public WebResponse<DocumentResponse> create(User user,
                                              @RequestParam(name = "name") String name,
                                              @RequestParam(name = "description") String description,
                                              @RequestParam(name = "file") MultipartFile file) {

    CreateDocumentRequest request = new CreateDocumentRequest();
    request.setDescription(description);
    request.setName(name);
    request.setFile(file);

    DocumentResponse response = service.create(user, request);
    return WebResponse.<DocumentResponse>builder()
            .data(response)
            .message("Document berhasil dibuat")
            .build();
  }

  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> delete(User user, @PathVariable("id") String id) {

    String response = service.delete(user, id);
    return WebResponse.<String>builder()
            .data(response)
            .message("OK")
            .build();
  }
}
