package com.sdm.app.service.impl;

import com.sdm.app.entity.Sip;
import com.sdm.app.entity.User;
import com.sdm.app.model.req.create.CreateSipRequest;
import com.sdm.app.model.req.search.SearchSipRequest;
import com.sdm.app.model.req.update.UpdateSipRequest;
import com.sdm.app.model.res.FileResponse;
import com.sdm.app.model.res.SipDocResponse;
import com.sdm.app.model.res.SipResponse;
import com.sdm.app.repository.SipReportRepository;
import com.sdm.app.repository.SipRepository;
import com.sdm.app.utils.GeneralHelper;
import com.sdm.app.utils.ResponseConverter;
import jakarta.persistence.criteria.Predicate;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SipServiceImpl {

  private final SipRepository sipRepository;
  private final FileServiceImpl fileService;
  private final UserServiceImpl userService;

  @Transactional(readOnly = true)
  public Page<SipResponse> searchSip(SearchSipRequest request){

    int page = request.getPage() - 1;

    Specification<Sip> specification = (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if(Objects.nonNull(request.getIdentity())){
        predicates.add(builder.or(
                builder.equal(root.get("num"), request.getIdentity()),
                builder.like(root.get("name"), "%" + request.getIdentity() + "%")));
      }

      return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
    };

    Pageable pageable = PageRequest.of(page, request.getSize());
    Page<Sip> users = sipRepository.findAll(specification, pageable);
    List<SipResponse> userResponse = users.getContent().stream()
            .map(ResponseConverter::sipToResponse)
            .collect(Collectors.toList());

    return new PageImpl<>(userResponse, pageable, users.getTotalElements());
  }

  @Transactional(readOnly = true)
  public List<SipResponse> findCurrentSips(User user){
    return sipRepository.findByUser(user).stream().map(ResponseConverter::sipToResponse).collect(Collectors.toList());
  }

  @Transactional
  public SipResponse update(User admin, UpdateSipRequest request){
    GeneralHelper.isAdmin(admin);
    Sip sip = getSip(request.getId());
    Optional.ofNullable(request.getNum()).filter(StringUtils::hasText).ifPresent(sip::setNum);
    Optional.ofNullable(request.getName()).filter(StringUtils::hasText).ifPresent(sip::setName);

    sipRepository.save(sip);
    return ResponseConverter.sipToResponse(sip);
  }

  @Transactional
  public SipResponse create(User admin, CreateSipRequest request){
    GeneralHelper.isAdmin(admin);
    Sip sip = new Sip();
    sip.setId(UUID.randomUUID().toString());
    sip.setName(request.getName());
    sip.setUploadedAt(LocalDateTime.now());
    sip.setUpdatedAt(LocalDateTime.now());
    sip.setExpiredAt(request.getExpiredAt());
    Optional.ofNullable(request.getNum()).filter(StringUtils::hasText).ifPresent(sip::setNum);
    User user = userService.getUser(request.getNip());
    sip.setUser(user);

    if(Objects.nonNull(request.getFile())){
      FileResponse file = fileService.saveFile(request.getFile());
      sip.setFileType(file.getContentType());
      sip.setPath(file.getURL());
      sip.setSize(file.getSize());
    }

    sipRepository.save(sip);

    return ResponseConverter.sipToResponse(sip);
  }

  @Transactional
  public SipResponse delete(User admin, String id){
    GeneralHelper.isAdmin(admin);
    Sip sip = getSip(id);
    if(Objects.nonNull(sip.getPath())){
      fileService.removePrevFile(sip.getPath());
    }
    sipRepository.delete(sip);
    return ResponseConverter.sipToResponse(sip);
  }

  @Transactional
  public SipDocResponse getSipDoc(String id){
    Sip sip = getSip(id);
    SipDocResponse response = new SipDocResponse();
    response.setFilename(sip.getName());
    response.setType(sip.getFileType());
    byte[] data = fileService.getFile(sip.getPath());
    response.setData(data);

    return response;
  }

  @Transactional(readOnly = true)
  public SipResponse getById(String id){
    Sip sip = getSip(id);
    return ResponseConverter.sipToResponse(sip);
  }

  public Sip getSip(String id){
    return sipRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sip not found!"));
  }
}
