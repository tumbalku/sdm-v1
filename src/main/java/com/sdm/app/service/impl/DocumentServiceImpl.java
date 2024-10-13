package com.sdm.app.service.impl;

import com.sdm.app.entity.Document;
import com.sdm.app.entity.User;
import com.sdm.app.model.req.create.CreateDocumentRequest;
import com.sdm.app.model.req.search.SearchDocumentRequest;
import com.sdm.app.model.req.update.PinPriorityRequest;
import com.sdm.app.model.req.update.UpdateDocumentRequest;
import com.sdm.app.model.res.DocumentResponse;
import com.sdm.app.model.res.FileResponse;
import com.sdm.app.repository.DocumentRepository;
import com.sdm.app.utils.GeneralHelper;
import com.sdm.app.utils.ResponseConverter;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DocumentServiceImpl {

  private final DocumentRepository repository;
  private final FileServiceImpl fileService;

  @Transactional(readOnly = true)
  private Document getDocument(String id) {
    return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document tidak ditemukan"));
  }

  @Transactional
  public DocumentResponse pinPriority(User admin, PinPriorityRequest request) {
    GeneralHelper.isAdmin(admin);
    Document document = getDocument(request.getId());
    document.setPriority(request.getPriority());
    repository.save(document);
    return ResponseConverter.documentToResponse(document);
  }

  @Transactional
  public DocumentResponse create(User admin, CreateDocumentRequest request) {
    GeneralHelper.isAdmin(admin);
    Document document = new Document();
    document.setId(UUID.randomUUID().toString());
    document.setName(request.getName());
    document.setDescription(request.getDescription());
    document.setUploadedAt(LocalDateTime.now());
    document.setUpdatedAt(LocalDateTime.now());

    if (Objects.nonNull(request.getFile())) {
      if (request.getFile().getSize() != 0) {
        FileResponse file = fileService.saveFile(request.getFile());
        document.setPath(file.getURL());
        document.setSize(file.getSize());
        document.setType(file.getContentType());
      } else {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Harus mengupload file!");
      }
    } else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Harus mengupload file!");
    }

    repository.save(document);
    return ResponseConverter.documentToResponse(document);
  }

  @Transactional
  public DocumentResponse update(User admin, UpdateDocumentRequest request) {
    GeneralHelper.isAdmin(admin);
    Document document = getDocument(request.getId());
    Optional.ofNullable(request.getName()).ifPresent(document::setName);
    Optional.ofNullable(request.getDescription()).ifPresent(document::setDescription);
    document.setUpdatedAt(LocalDateTime.now());

    repository.save(document);
    return ResponseConverter.documentToResponse(document);
  }

  @Transactional
  public String delete(User admin, String id) {
    GeneralHelper.isAdmin(admin);
    Document document = getDocument(id);
    fileService.removePrevFile(document.getPath());
    repository.delete(document);
    return "Document berhasil dihapus!";
  }

  @Transactional(readOnly = true)
  public DocumentResponse getById(String id) {
    Document document = getDocument(id);
    return ResponseConverter.documentToResponse(document);
  }

  @Transactional(readOnly = true)
  public Page<DocumentResponse> searchDocuments(SearchDocumentRequest request) {

    int page = request.getPage() - 1;

    Specification<Document> specification = (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (Objects.nonNull(request.getContent())) {
        predicates.add(builder.or(builder.like(root.get("name"), "%" + request.getContent() + "%"), builder.like(root.get("description"), "%" + request.getContent() + "%")));
      }

      if (Objects.nonNull(request.getPriority())) {
        predicates.add(builder.equal(root.get("priority"), request.getPriority()));
      }

      return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
    };


    Sort.Direction sortDirection = "latest".equalsIgnoreCase(request.getDateSortBy()) ? Sort.Direction.DESC : Sort.Direction.ASC;

    Sort sort = Sort.by(Sort.Order.desc("priority"), new Sort.Order(sortDirection, "uploadedAt"));

    Pageable pageable = PageRequest.of(page, request.getSize(), sort);
    Page<Document> documents = repository.findAll(specification, pageable);
    List<DocumentResponse> postResponse = documents.getContent().stream().map(ResponseConverter::documentToResponse).collect(Collectors.toList());

    return new PageImpl<>(postResponse, pageable, documents.getTotalElements());
  }

}
