package com.sdm.app.service.impl;

import com.sdm.app.entity.Address;
import com.sdm.app.entity.Role;
import com.sdm.app.entity.User;
import com.sdm.app.enumrated.Gender;
import com.sdm.app.enumrated.UserStatus;
import com.sdm.app.model.req.create.CreateUserRequest;
import com.sdm.app.model.req.search.UserSearchPin;
import com.sdm.app.model.req.search.UserSearchRequest;
import com.sdm.app.model.req.update.PinPriorityRequest;
import com.sdm.app.model.req.update.UpdatePasswordRequest;
import com.sdm.app.model.req.update.UpdateUserRequest;
import com.sdm.app.model.res.PinUserResponse;
import com.sdm.app.model.res.SimpleUserResponse;
import com.sdm.app.model.res.UserResponse;
import com.sdm.app.repository.RoleRepository;
import com.sdm.app.repository.UserRepository;
import com.sdm.app.security.BCrypt;
import com.sdm.app.service.UserService;
import com.sdm.app.utils.GeneralHelper;
import com.sdm.app.utils.ResponseConverter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final FileServiceImpl fileService;
  private final AddressServiceImpl addressService;

  @Transactional
  public UserResponse pinPriority(User admin, PinPriorityRequest request) {
    GeneralHelper.isAdmin(admin);
    User user = getUser(request.getId());
    user.setPriority(request.getPriority());
    userRepository.save(user);
    return ResponseConverter.userToResponse(user);
  }

  @Transactional(readOnly = true)
  public PinUserResponse getPinUser(String id) {
    User user = getUser(id);
    return ResponseConverter.pinUserToResponse(user);
  }

  @Transactional(readOnly = true)
  public Page<PinUserResponse> searchPin(UserSearchPin request) {

    int page = request.getPage() - 1;

    Specification<User> specification = (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      predicates.add(builder.equal(root.get("priority"), 1));

      if (Objects.nonNull(request.getWorkUnit())) {
        predicates.add(builder.equal(root.get("workUnit"), request.getWorkUnit()));
      }

      Join<User, Role> rolesJoin = root.join("roles");
      if (request.getRole().equalsIgnoreCase("ADMIN")) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You try to hack me?");
      }
      predicates.add(builder.equal(rolesJoin.get("name"), request.getRole()));

      return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
    };

    Sort.Direction sortDirection = "latest".equalsIgnoreCase(request.getDateSortBy()) ? Sort.Direction.ASC : Sort.Direction.DESC;

    Sort sort = Sort.by(
            Sort.Order.desc("priority"),
            new Sort.Order(sortDirection, "updatedAt")
    );

    Pageable pageable = PageRequest.of(page, request.getSize(), sort);
    Page<User> users = userRepository.findAll(specification, pageable);
    List<PinUserResponse> userResponse = users.getContent().stream()
            .map(ResponseConverter::pinUserToResponse)
            .collect(Collectors.toList());

    return new PageImpl<>(userResponse, pageable, users.getTotalElements());
  }

  @Transactional(readOnly = true)
  public Page<SimpleUserResponse> searchUsers(User user, UserSearchRequest request) {

    GeneralHelper.isAdmin(user);

    int page = request.getPage() - 1;

    Specification<User> specification = (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (Objects.nonNull(request.getIdentity())) {
        predicates.add(builder.or(
                builder.equal(root.get("nip"), request.getIdentity()),
                builder.like(root.get("name"), "%" + request.getIdentity() + "%")));
      }

      if (Objects.nonNull(request.getStatus())) {
        predicates.add(builder.equal(root.get("status"), UserStatus.valueOf(request.getStatus())));
      }

      // Filter berdasarkan roles
      if (Objects.nonNull(request.getRoles()) && !request.getRoles().isEmpty()) {
        Join<User, Role> rolesJoin = root.join("roles", JoinType.INNER); // Melakukan join ke tabel roles
        predicates.add(rolesJoin.get("name").in(request.getRoles())); // Filter berdasarkan roles
      }

      return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
    };

    Pageable pageable = PageRequest.of(page, request.getSize(), Sort.by(Sort.Direction.DESC, "updatedAt"));
    Page<User> users = userRepository.findAll(specification, pageable);
    List<SimpleUserResponse> userResponse = users.getContent().stream()
            .map(ResponseConverter::userToSimpleResponse)
            .collect(Collectors.toList());

    return new PageImpl<>(userResponse, pageable, users.getTotalElements());
  }

  @Override
  @Transactional
  public SimpleUserResponse create(User admin, CreateUserRequest request) {

    GeneralHelper.validate(request);
    GeneralHelper.isAdmin(admin);

    User user = new User();
    user.setId(UUID.randomUUID().toString());
    if (!StringUtils.hasText(request.getUsername()) || !Objects.nonNull(request.getUsername())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username tidak boleh kosong!");
    }

    user.setUsername(request.getUsername());
    user.setPassword(BCrypt.hashpw(request.getUsername(), BCrypt.gensalt()));
    user.setGender(Gender.valueOf(request.getGender()));
    Optional.ofNullable(request.getName()).filter(StringUtils::hasText).ifPresent(user::setName);
    Optional.ofNullable(request.getEmail()).filter(StringUtils::hasText).ifPresent(user::setEmail);
    Optional.ofNullable(request.getNip()).filter(StringUtils::hasText).ifPresent(user::setNip);
    Optional.ofNullable(request.getPhone()).filter(StringUtils::hasText).ifPresent(user::setPhone);

    Address address = addressService.makeAddress(request.getAddress());
    user.setAddress(address);

    // status in corporation
    user.setPangkat(request.getPangkat());
    user.setGolongan(request.getGolongan());
    user.setPosition(request.getPosition());
    user.setWorkUnit(request.getWorkUnit());
    user.setStatus(UserStatus.ACTIVE);

    if (request.getRoles().size() < 1) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please choose at least one Role!");
    }

    for (String role : request.getRoles()) {
      Role exsistingRole = roleRepository.findByName(role)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Role input"));
      user.getRoles().add(exsistingRole);
    }

    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    userRepository.save(user);

    return ResponseConverter.userToSimpleResponse(user);
  }

  @Transactional
  public void updateAvatar(User current, MultipartFile file) {

    // supaya tidak buang buang memory
    Optional.ofNullable(current.getAvatar()).filter(StringUtils::hasText)
            .ifPresent(image -> fileService.removePrevFile(current.getAvatar()));

    current.setAvatar(fileService.saveImage(file));

    userRepository.save(current);
  }

  @Transactional
  public SimpleUserResponse update(User admin, CreateUserRequest request) {

    GeneralHelper.validate(request);
    GeneralHelper.isAdmin(admin);

    User user = getUser(request.getId());

    if (!StringUtils.hasText(request.getUsername()) || !Objects.nonNull(request.getUsername())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username tidak boleh kosong!");
    }
    if (!StringUtils.hasText(request.getPhone()) || !Objects.nonNull(request.getPhone())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone tidak boleh kosong!");
    }

    user.setUsername(request.getUsername());
    user.setPangkat(request.getPangkat());
    user.setGolongan(request.getGolongan());

    Optional.ofNullable(request.getNip()).filter(StringUtils::hasText).ifPresent(user::setNip);
    Optional.ofNullable(request.getName()).filter(StringUtils::hasText).ifPresent(user::setName);
    Optional.ofNullable(request.getEmail()).filter(StringUtils::hasText).ifPresent(user::setEmail);
    Optional.ofNullable(request.getPhone()).filter(StringUtils::hasText).ifPresent(user::setPhone);
    Optional.ofNullable(request.getPosition()).filter(StringUtils::hasText).ifPresent(user::setPosition);
    Optional.ofNullable(request.getWorkUnit()).filter(StringUtils::hasText).ifPresent(user::setWorkUnit);
    Optional.ofNullable(request.getGender()).filter(StringUtils::hasText)
            .ifPresent(gender -> user.setGender(Gender.valueOf(request.getGender())));

    if (Objects.nonNull(request.getRoles()) && request.getRoles().size() != 0) {
      user.getRoles().clear();
      for (String role : request.getRoles()) {
        Role exsistingRole = roleRepository.findByName(role)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Role input"));
        user.getRoles().add(exsistingRole);
      }
    }

    Address address = addressService.makeAddress(request.getAddress());
    user.setAddress(address);

    user.setUpdatedAt(LocalDateTime.now());

    return ResponseConverter.userToSimpleResponse(user);
  }

  @Transactional
  public SimpleUserResponse updatePassword(User current, UpdatePasswordRequest request) {

    GeneralHelper.validate(request);
    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Double check the new password and the confirm password!");
    }
    current.setUpdatedAt(LocalDateTime.now());
    current.setPassword(BCrypt.hashpw(request.getConfirmPassword(), BCrypt.gensalt()));
    userRepository.save(current);
    return ResponseConverter.userToSimpleResponse(current);
  }

  @Transactional
  public SimpleUserResponse resetPassword(User admin, String id) {
    GeneralHelper.isAdmin(admin);

    User user = getUser(id);
    user.setUpdatedAt(LocalDateTime.now());
    user.setPassword(BCrypt.hashpw(user.getUsername(), BCrypt.gensalt()));
    userRepository.save(user);

    return ResponseConverter.userToSimpleResponse(user);
  }

  @Transactional
  public SimpleUserResponse updateUserInfo(User current, UpdateUserRequest request) {

    GeneralHelper.validate(request);
    if (!StringUtils.hasText(request.getUsername()) || !Objects.nonNull(request.getUsername())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username tidak boleh kosong!");
    }
    if (!StringUtils.hasText(request.getPhone()) || !Objects.nonNull(request.getPhone())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone tidak boleh kosong!");
    }

    current.setUsername(request.getUsername());
    Optional.ofNullable(request.getPhone()).filter(StringUtils::hasText).ifPresent(current::setPhone);
    Optional.ofNullable(request.getEmail()).filter(StringUtils::hasText).ifPresent(current::setEmail);
    Optional.ofNullable(request.getInstagram()).filter(StringUtils::hasText).ifPresent(current::setInstagram);
    Optional.ofNullable(request.getLinkedin()).filter(StringUtils::hasText).ifPresent(current::setLinkedin);
    Optional.ofNullable(request.getTwitter()).filter(StringUtils::hasText).ifPresent(current::setTwitter);
    Optional.ofNullable(request.getFacebook()).filter(StringUtils::hasText).ifPresent(current::setFacebook);
    current.setUpdatedAt(LocalDateTime.now());

    userRepository.save(current);
    return ResponseConverter.userToSimpleResponse(current);
  }

  @Transactional
  public SimpleUserResponse delete(User admin, String id) {
    GeneralHelper.isAdmin(admin);
    User user = getUser(id);
    if (Objects.nonNull(user.getAvatar())) {
      fileService.removePrevFile(user.getAvatar());
    }
    userRepository.delete(user);
    return ResponseConverter.userToSimpleResponse(user);
  }

  @Transactional(readOnly = true)
  public UserResponse getById(String id) {
    User user = getUser(id);
    return ResponseConverter.userToResponse(user);
  }

  public User getUser(String id) {
    return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
  }

}
