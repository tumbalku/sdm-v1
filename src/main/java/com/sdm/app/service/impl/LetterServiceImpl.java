package com.sdm.app.service.impl;

import com.sdm.app.entity.Letter;
import com.sdm.app.entity.User;
import com.sdm.app.enumrated.LetterType;
import com.sdm.app.model.req.create.CreateLetterRequest;
import com.sdm.app.model.req.search.SearchFileRequest;
import com.sdm.app.model.req.update.UpdateLetterRequest;
import com.sdm.app.model.res.FileResponse;
import com.sdm.app.model.res.LetterDocResponse;
import com.sdm.app.model.res.LetterResponse;
import com.sdm.app.repository.LetterRepository;
import com.sdm.app.utils.GeneralHelper;
import com.sdm.app.utils.ResponseConverter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LetterServiceImpl {

  private final LetterRepository letterRepository;
  private final UserServiceImpl userService;
  private final FileServiceImpl fileService;

  @Transactional(readOnly = true)
  public Page<LetterResponse> searchLetter(SearchFileRequest request){

    int page = request.getPage() - 1;

    Specification<Letter> specification = (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if(Objects.nonNull(request.getFilename())){
        predicates.add(builder.like(root.get("name"), "%" + request.getFilename() + "%"));
      }

      if(Objects.nonNull(request.getFileType())){
        predicates.add(builder.equal(root.get("fileType"), request.getFileType()));
      }

      if(Objects.nonNull(request.getType())) {
        predicates.add(builder.equal(root.get("type"), LetterType.valueOf(request.getType())));
      }

      return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
    };

    Pageable pageable = PageRequest.of(page, request.getSize());
    Page<Letter> users = letterRepository.findAll(specification, pageable);
    List<LetterResponse> userResponse = users.getContent().stream()
            .map(ResponseConverter::letterToResponse)
            .collect(Collectors.toList());

    return new PageImpl<>(userResponse, pageable, users.getTotalElements());
  }

  @Transactional(readOnly = true)
  public List<LetterResponse> findCurrentLetters(User user){
    return letterRepository.findByUser(user).stream().map(ResponseConverter::letterToResponse).collect(Collectors.toList());
  }

  @Transactional
  public LetterResponse update(User admin, UpdateLetterRequest request){
    GeneralHelper.isAdmin(admin);
    Letter letter = getLetter(request.getId());
    Optional.ofNullable(request.getNum()).filter(StringUtils::hasText).ifPresent(letter::setNum);
    Optional.ofNullable(request.getName()).filter(StringUtils::hasText).ifPresent(letter::setName);
    Optional.ofNullable(request.getType()).filter(StringUtils::hasText)
            .ifPresent(type -> letter.setType(LetterType.valueOf(type)));

    letterRepository.save(letter);
    return ResponseConverter.letterToResponse(letter);
  }

  @Transactional
  public LetterResponse create(User admin, CreateLetterRequest request){
    GeneralHelper.isAdmin(admin);
    Letter letter = new Letter();
    letter.setName(request.getName());
    letter.setType(LetterType.valueOf(request.getType()));
    letter.setNum(request.getNum());
    letter.setUploadedAt(LocalDateTime.now());
    letter.setUpdatedAt(LocalDateTime.now());
    letter.setExpiredAt(request.getExpiredAt());
    Optional.ofNullable(request.getNum()).filter(StringUtils::hasText).ifPresent(letter::setNum);
    User user = userService.getUser(request.getNip());
    letter.setUser(user);

    if(Objects.nonNull(request.getFile())){
      FileResponse file = fileService.saveFile(request.getFile());
      letter.setFileType(file.getContentType());
      letter.setPath(file.getURL());
      letter.setSize(file.getSize());
    }

    letterRepository.save(letter);

    return ResponseConverter.letterToResponse(letter);
  }

  @Transactional
  public LetterResponse delete(User admin, Long id){
    GeneralHelper.isAdmin(admin);
    Letter letter = getLetter(id);
    fileService.removePrevFile(letter.getPath());
    letterRepository.delete(letter);
    return ResponseConverter.letterToResponse(letter);
  }

  @Transactional
  public LetterDocResponse getLetterDoc(Long id){
    Letter letter = getLetter(id);
    LetterDocResponse response = new LetterDocResponse();
    response.setFilename(letter.getName());
    response.setType(letter.getFileType());
    byte[] data = fileService.getFile(letter.getPath());
    response.setData(data);

    return response;
  }

  @Transactional(readOnly = true)
  public LetterResponse getById(Long id){
    Letter letter = getLetter(id);
    return ResponseConverter.letterToResponse(letter);
  }

  public Letter getLetter(Long id){
    return letterRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Letter not found!"));
  }
}
