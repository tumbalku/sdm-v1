package com.sdm.app.service.impl;

import com.sdm.app.entity.*;
import com.sdm.app.enumrated.CutiStatus;
import com.sdm.app.enumrated.KopType;
import com.sdm.app.model.req.create.CreateCutiRequest;
import com.sdm.app.model.req.create.EmailRequest;
import com.sdm.app.model.req.create.UserCreateCutiRequest;
import com.sdm.app.model.req.search.SearchCutiRequest;
import com.sdm.app.model.req.update.DecitionCutiRequest;
import com.sdm.app.model.res.CutiResponse;
import com.sdm.app.model.res.CutiTypeCount;
import com.sdm.app.model.res.DataReportResponse;
import com.sdm.app.repository.CutiRepository;
import com.sdm.app.repository.PeopleRepository;
import com.sdm.app.repository.UserRepository;
import com.sdm.app.service.text.CutiPdfService;
import com.sdm.app.utils.GeneralHelper;
import com.sdm.app.utils.ResponseConverter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CutiServiceImpl {

  private final CutiRepository cutiRepository;
  private final KopServiceImpl kopService;
  private final UserServiceImpl userService;
  private final PeopleRepository peopleRepository;
  private final CutiPdfService cutiPdfService;
  private final EmailService emailService;

  @Transactional
  public CutiResponse makeDecisionCuti(User admin, DecitionCutiRequest request){
    GeneralHelper.isAdmin(admin);

    Cuti cuti = getCuti(request.getId());
    cuti.setStatus(request.getStatus());
    cuti.setMessage(request.getMessage());
    cuti.setUpdatedAt(LocalDateTime.now());

    if(!request.getStatus().equals(CutiStatus.REJECT)){
      Optional.ofNullable(request.getNumber()).ifPresent(cuti::setNumber);
      Optional.ofNullable(request.getSignedBy()).filter(StringUtils::hasText).ifPresent(cuti::setSignedBy);

      if(Objects.nonNull(request.getPeople()) && request.getPeople().size() != 0) {
        cuti.getPeople().clear();
        for (String people : request.getPeople()) {
          People exsistingPeople = peopleRepository.findByNameIgnoreCase(people).orElse(null);
          if(Objects.nonNull(exsistingPeople)){
            cuti.getPeople().add(exsistingPeople);
          }else{
            People newPep = new People();
            newPep.setName(people);
            peopleRepository.save(newPep);
            cuti.getPeople().add(newPep);
          }
        }
      }
    }

    cutiRepository.save(cuti);
    return ResponseConverter.cutiToResponse(cuti);
  }

  @Transactional
  public CutiResponse userCreateCuti(User user, UserCreateCutiRequest request){
    Cuti cuti = new Cuti();
    String requestId = UUID.randomUUID().toString();
    cuti.setId(requestId);
    cuti.setUser(user);
    cuti.setAddress(request.getAddress());
    cuti.setStatus(CutiStatus.PENDING);
    cuti.setReason(request.getReason());
    cuti.setDateEnd(request.getDateEnd());
    cuti.setDateStart(request.getDateStart());

    cuti.setCreatedAt(LocalDateTime.now());
    cuti.setUpdatedAt(LocalDateTime.now());

    Kop kop = kopService.getKop(request.getKop());
    cuti.setKop(kop);
    cuti.setRomawi(kop.getRomawi());
    cuti.setYear(kop.getYear());

    cutiRepository.save(cuti);

    String start = GeneralHelper.dateFormatter().format(request.getDateStart());
    String end = GeneralHelper.dateFormatter().format(request.getDateEnd());
    String address = Objects.nonNull(request.getAddress()) ? request.getAddress() : user.getAddress().getName();
    String nip = Objects.nonNull(user.getNip()) ? user.getNip() : null;
    EmailRequest emailRequest = EmailRequest.builder()
            .nip(nip)
            .address(address)
            .type(kop.getType())
            .reason(request.getReason())
            .name(user.getName())
            .token(requestId)
            .endDate(end)
            .startDate(start)
            .build();

    emailService.sendEmailHTMLFormat(emailRequest);
    return ResponseConverter.cutiToResponse(cuti);
  }

  public Resource download(String id) throws MalformedURLException, FileNotFoundException {

    Cuti cuti = getCuti(id);
    cutiPdfService.makeAnCutiReport(cuti);

    Path filePath = Path.of("temp-pdf/output.pdf");
    if(!Files.exists(filePath)) {
      throw new FileNotFoundException("file was not found on the server");
    }

    return new UrlResource(filePath.toUri());
  }

  @Transactional(readOnly = true)
  public List<CutiResponse> findAllCurrentCuties(User current) {
    return cutiRepository.findByUser(current).stream().map(ResponseConverter::cutiToResponse).collect(Collectors.toList());
  }

  public Map<KopType, Long> getCountByType(List<CutiStatus> statuses) {
    List<CutiTypeCount> results;

    if (Objects.nonNull(statuses) && !statuses.isEmpty()) {
      results = cutiRepository.countByType(statuses); // Panggil query dengan list status
    } else {
      results = cutiRepository.countByAllTypes(); // Jika status tidak disediakan, hitung semua tipe
    }


    return results.stream()
            .collect(Collectors.toMap(
                    CutiTypeCount::getKop, // Mengambil tipe cuti sebagai key
                    CutiTypeCount::getCount // Mengambil jumlah cuti sebagai value
            ));
  }
  @Transactional(readOnly = true)
  public DataReportResponse<Page<CutiResponse>, Map<KopType, Long>> search(SearchCutiRequest request){
    int page = request.getPage() - 1;

    Specification<Cuti> specification = (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      Join<Cuti, User> userJoin = root.join("user", JoinType.LEFT);
      if (Objects.nonNull(request.getName())) {
        predicates.add(builder.like(userJoin.get("name"), "%" + request.getName() + "%"));
      }

      if(Objects.nonNull(request.getType())){
        predicates.add(builder.equal(root.get("kop").get("type"), KopType.valueOf(request.getType())));
      }

      if (Objects.nonNull(request.getStatuses()) && !request.getStatuses().isEmpty()) {
        List<CutiStatus> statusList = request.getStatuses().stream()
                .map(CutiStatus::valueOf)
                .collect(Collectors.toList());
        predicates.add(root.get("status").in(statusList));
      }

      return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
    };

    Pageable pageable = PageRequest.of(page, request.getSize());
    Page<Cuti> users = cutiRepository.findAll(specification, pageable);
    List<CutiResponse> userResponse = users.getContent().stream()
            .map(ResponseConverter::cutiToResponse)
            .collect(Collectors.toList());

    Map<KopType, Long> counts = getCountByType(
            Objects.nonNull(request.getStatuses()) && !request.getStatuses().isEmpty() ?
                    request.getStatuses().stream().map(CutiStatus::valueOf).collect(Collectors.toList()) :
                    null
    );

    DataReportResponse<Page<CutiResponse>, Map<KopType, Long>> data = new DataReportResponse<>();
    data.setData(new PageImpl<>(userResponse, pageable, users.getTotalElements()));
    data.setReport(counts);
    return data;
  }

  @Transactional
  public void delete() {
    LocalDate thresholdDate = LocalDate.now().minusDays(360);
    List<Cuti> expiredLeaves = cutiRepository.findByDateEndBefore(thresholdDate);
    cutiRepository.deleteAll(expiredLeaves);
    System.out.println("Expired leave records deleted: " + expiredLeaves.size());
  }

  @Transactional
  public CutiResponse create(User admin, CreateCutiRequest request){
    GeneralHelper.isAdmin(admin);

    Cuti cuti = new Cuti();
    cuti.setId(UUID.randomUUID().toString());
    cuti.setNumber(request.getNumber());
    cuti.setDateEnd(request.getDateEnd());
    cuti.setDateStart(request.getDateStart());
    cuti.setSignedBy(request.getSignedBy());
    cuti.setCreatedAt(LocalDateTime.now());
    cuti.setUpdatedAt(LocalDateTime.now());
    cuti.setAddress(request.getAddress());
    cuti.setStatus(CutiStatus.APPROVE);

    if(Objects.nonNull(request.getPeople()) && request.getPeople().size() != 0){
      for (String people : request.getPeople()) {
        People exsistingPeople = peopleRepository.findByNameIgnoreCase(people).orElse(null);
        if(Objects.nonNull(exsistingPeople)){
          cuti.getPeople().add(exsistingPeople);
        }else{
          People newPep = new People();
          newPep.setName(people);
          peopleRepository.save(newPep);
          cuti.getPeople().add(newPep);
        }
      }
    }

    Kop kop = kopService.getKop(request.getKop());
    cuti.setKop(kop);
    cuti.setRomawi(kop.getRomawi());
    cuti.setYear(kop.getYear());

    User user = userService.getUser(request.getUser());
    cuti.setUser(user);

    cutiRepository.save(cuti);
    return ResponseConverter.cutiToResponse(cuti);
  }

  @Transactional
  public CutiResponse update(User admin, CreateCutiRequest request){
    GeneralHelper.isAdmin(admin);
    System.out.println("get id from service = " + request.getId());
    Cuti cuti = getCuti(request.getId());

    Optional.ofNullable(request.getNumber()).ifPresent(cuti::setNumber);
    Optional.ofNullable(request.getDateEnd()).ifPresent(cuti::setDateEnd);
    Optional.ofNullable(request.getDateStart()).ifPresent(cuti::setDateStart);
    Optional.ofNullable(request.getAddress()).filter(StringUtils::hasText).ifPresent(cuti::setAddress);
    Optional.ofNullable(request.getSignedBy()).filter(StringUtils::hasText).ifPresent(cuti::setSignedBy);

    cuti.setUpdatedAt(LocalDateTime.now());

    if(Objects.nonNull(request.getPeople()) && request.getPeople().size() != 0) {
      cuti.getPeople().clear();
      for (String people : request.getPeople()) {
        People exsistingPeople = peopleRepository.findByNameIgnoreCase(people).orElse(null);
        if(Objects.nonNull(exsistingPeople)){
          cuti.getPeople().add(exsistingPeople);
        }else{
          People newPep = new People();
          newPep.setName(people);
          peopleRepository.save(newPep);
          cuti.getPeople().add(newPep);
        }
      }
    }

    Kop kop = kopService.getKop(request.getKop());
    cuti.setKop(kop);

    User user = userService.getUser(request.getUser());
    cuti.setUser(user);

    cutiRepository.save(cuti);
    return ResponseConverter.cutiToResponse(cuti);
  }


  @Transactional
  public CutiResponse deleteById(User admin, String id){
    GeneralHelper.isAdmin(admin);
    Cuti cuti = getCuti(id);
    cutiRepository.delete(cuti);
    return ResponseConverter.cutiToResponse(cuti);
  }

  @Transactional(readOnly = true)
  public CutiResponse getById(String id){
    Cuti cuti = getCuti(id);
    return ResponseConverter.cutiToResponse(cuti);
  }

  public Cuti getCuti(String id){
    return cutiRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuti not found!"));
  }
}
