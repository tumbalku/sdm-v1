package com.sdm.app.service.impl;

import com.sdm.app.entity.Kop;
import com.sdm.app.entity.User;
import com.sdm.app.enumrated.KopType;
import com.sdm.app.model.req.create.CreateKopRequest;
import com.sdm.app.model.res.KopResponse;
import com.sdm.app.repository.KopRepository;
import com.sdm.app.utils.GeneralHelper;
import com.sdm.app.utils.ResponseConverter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class KopServiceImpl {

  private final KopRepository repository;

  @Transactional
  public KopResponse update(User admin, CreateKopRequest request){

    GeneralHelper.isAdmin(admin);
    Kop kop = getKop(request.getId());
    Optional.ofNullable(request.getUniKop()).filter(StringUtils::hasText).ifPresent(kop::setUniKop);
    Optional.ofNullable(request.getType()).filter(StringUtils::hasText)
            .ifPresent(type -> kop.setType(KopType.valueOf(type)));
    Optional.ofNullable(request.getYear()).ifPresent(year -> kop.setYear(Year.of(year)));
    Optional.ofNullable(request.getRomawi()).filter(StringUtils::hasText).ifPresent(kop::setRomawi);

    repository.save(kop);
    return ResponseConverter.kopToResponse(kop);
  }


  @Transactional
  public KopResponse create(User admin, CreateKopRequest request){
    GeneralHelper.isAdmin(admin);
    Kop kop = new Kop();
    kop.setUniKop(request.getUniKop());
    kop.setType(KopType.valueOf(request.getType()));
    kop.setYear(Year.of(request.getYear()));
    kop.setRomawi(request.getRomawi());

    repository.save(kop);
    return ResponseConverter.kopToResponse(kop);
  }

  @Transactional
  public KopResponse delete(User admin, Long id){
    GeneralHelper.isAdmin(admin);
    Kop kop = getKop(id);
    repository.delete(kop);
    return ResponseConverter.kopToResponse(kop);
  }

  @Transactional(readOnly = true)
  public List<KopResponse> findAll(){
    return repository.findAll().stream().map(ResponseConverter::kopToResponse).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public KopResponse getById(Long id){
    Kop kop = getKop(id);
    return ResponseConverter.kopToResponse(kop);
  }

  @Transactional(readOnly = true)
  public Kop getKop(Long id){
    return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "KOP not found!"));
  }
}
