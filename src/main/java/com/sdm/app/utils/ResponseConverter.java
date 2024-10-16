package com.sdm.app.utils;

import com.sdm.app.entity.*;
import com.sdm.app.model.res.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ResponseConverter {

  public static PinUserResponse pinUserToResponse(User user) {
    return PinUserResponse.builder()
            .position(user.getPosition())
            .workUnit(user.getWorkUnit())
            .avatar(user.getAvatar())
            .name(user.getName())
            .id(user.getId())
            .build();
  }

  public static UserResponse userToResponse(User user) {
    UserResponse response = UserResponse.builder()
            .id(user.getId())
            .priority(user.getPriority())
            .nip(user.getNip())
            .name(user.getName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .avatar(user.getAvatar())
            .gender(user.getGender())
            .status(user.getStatus())
            .pangkat(user.getPangkat())
            .username(user.getUsername())
            .golongan(user.getGolongan())
            .position(user.getPosition())
            .workUnit(user.getWorkUnit())
            .instagram(user.getInstagram())
            .linkedin(user.getLinkedin())
            .facebook(user.getFacebook())
            .twitter(user.getTwitter())
            .build();

    if (Objects.nonNull(user.getAddress())) {
      response.setAddress(user.getAddress().getName());
    }

    if (Objects.nonNull(user.getRoles()) && user.getRoles().size() != 0) {
      response.setRoles(user.getRoles().stream()
              .map(Role::getName)
              .collect(Collectors.toList()));
    }

    return response;
  }

  public static UserLite userToLiteResponse(User user) {
    return UserLite.builder()
            .id(user.getId())
            .nip(user.getNip())
            .avatar(user.getAvatar())
            .name(user.getName())
            .workUnit(user.getWorkUnit())
            .address(user.getAddress().getName())
            .build();
  }

  public static SimpleUserResponse userToSimpleResponse(User user) {
    SimpleUserResponse response = SimpleUserResponse.builder()
            .id(user.getId())
            .priority(user.getPriority())
            .nip(user.getNip())
            .name(user.getName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .gender(user.getGender())
            .position(user.getPosition())
            .status(user.getStatus())
            .avatar(user.getAvatar())
            .address(user.getAddress().getName())
            .workUnit(user.getWorkUnit())
            .instagram(user.getInstagram())
            .linkedin(user.getLinkedin())
            .facebook(user.getFacebook())
            .twitter(user.getTwitter())
            .build();

    if (Objects.nonNull(user.getRoles())) {
      response.setRoles(user.getRoles().stream()
              .map(Role::getName)
              .collect(Collectors.toList()));
    }

    return response;
  }

  public static LetterResponse letterToResponse(Letter letter) {
    return LetterResponse.builder()
            .id(letter.getId())
            .size(letter.getSize())
            .type(letter.getType())
            .name(letter.getName())
            .num(letter.getNum())
            .path(letter.getPath())
            .uploadedAt(letter.getUploadedAt())
            .expiredAt(letter.getExpiredAt())
            .updatedAt(letter.getUpdatedAt())
            .fileType(letter.getFileType())
            .user(userToLiteResponse(letter.getUser()))
            .build();
  }

  public static SipReportResponse sipReportToResponse(SipReport report) {
    return SipReportResponse.builder()
            .sentDate(report.getSentDate())
            .status(report.getStatus())
            .id(report.getId())
            .build();
  }

  public static SipResponse sipToResponse(Sip sip) {
    SipResponse response = SipResponse.builder()
            .id(sip.getId())
            .size(sip.getSize())
            .name(sip.getName())
            .num(sip.getNum())
            .path(sip.getPath())
            .uploadedAt(sip.getUploadedAt())
            .expiredAt(sip.getExpiredAt())
            .updatedAt(sip.getUpdatedAt())
            .fileType(sip.getFileType())
            .user(userToSimpleResponse(sip.getUser()))
            .build();

    if (Objects.nonNull(sip.getReports()) && sip.getReports().size() != 0) {
      response.setReports(sip.getReports().stream().map(ResponseConverter::sipReportToResponse).collect(Collectors.toList()));
    }

    return response;
  }

  public static AddressResponse addressToResponse(Address address) {
    return AddressResponse.builder()
            .id(address.getId())
            .name(address.getName())
            .build();
  }

  public static KopResponse kopToResponse(Kop kop) {

    return KopResponse.builder()
            .id(kop.getId())
            .name(kop.getType().getDescription())
            .type(kop.getType())
            .romawi(kop.getRomawi())
            .uniKop(kop.getUniKop())
            .year(kop.getYear())
            .build();
  }

  public static RoleResponse roleToResponse(Role role) {
    RoleResponse response = new RoleResponse();
    response.setId(role.getId());
    response.setName(role.getName());

    return response;
  }

  public static CutiResponse cutiToResponse(Cuti cuti) {
    CutiResponse response = CutiResponse.builder()
            .total(cuti.getTotal())
            .status(cuti.getStatus().getDescription())
            .kop(kopToResponse(cuti.getKop()))
            .dateEnd(cuti.getDateEnd())
            .message(cuti.getMessage())
            .document(cuti.getDocument())
            .workUnit(cuti.getWorkUnit())
            .reason(cuti.getReason())
            .dateStart(cuti.getDateStart())
            .signedBy(cuti.getSignedBy())
            .mark(cuti.getMark())
            .forYear(cuti.getForYear())
            .address(cuti.getAddress())
            .user(userToLiteResponse(cuti.getUser()))
            .number(cuti.getNumber())
            .id(cuti.getId())
            .createdAt(cuti.getCreatedAt())
            .updatedAt(cuti.getUpdatedAt())
            .build();

    if (Objects.nonNull(cuti.getPeople())) {
      response.setPeople(cuti.getPeople().stream()
              .map(People::getName)
              .collect(Collectors.toList()));
    }
    return response;
  }

  public static PagingResponse getPagingResponse(Page<?> page) {
    int pageNumber = page.getNumber() + 1;
    return PagingResponse.builder()
            .page(pageNumber) // current page
            .totalItems(page.getContent().size()) // get total items
            .pageSize(page.getTotalPages()) // total page keseluruhan
            .size(page.getSize())
            .build();
  }

  public static PostResponse postToResponse(Post post) {
    return PostResponse.builder()
            .id(post.getId())
            .title(post.getTitle())
            .user(userToLiteResponse(post.getUser()))
            .priority(post.getPriority())
            .imageUrl(post.getImage())
            .content(post.getContent())
            .createdAt(post.getCreatedAt())
            .updatedAt(post.getUpdatedAt())
            .build();
  }

  public static SipLiteResponse sipLiteToResponse(Sip sip) {
    SipLiteResponse response = SipLiteResponse.builder()
            .num(sip.getNum())
            .expiredAt(sip.getExpiredAt())
            .user(userToLiteResponse(sip.getUser()))
            .build();
    if (Objects.nonNull(sip.getReports()) && sip.getReports().size() != 0) {
      response.setReports(sip.getReports().stream().map(ResponseConverter::sipReportToResponse).collect(Collectors.toList()));
    }
    return response;
  }

  public static DocumentResponse documentToResponse(Document document) {
    return DocumentResponse.builder()
            .id(document.getId())
            .size(document.getSize())
            .name(document.getName())
            .path(document.getPath())
            .type(document.getType())
            .priority(document.getPriority())
            .updatedAt(document.getUpdatedAt())
            .uploadedAt(document.getUploadedAt())
            .description(document.getDescription())
            .build();
  }
}
