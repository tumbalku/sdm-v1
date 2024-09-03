package com.sdm.app.service.impl;

import com.sdm.app.entity.Role;
import com.sdm.app.entity.User;
import com.sdm.app.model.req.create.RoleRequest;
import com.sdm.app.model.res.RoleResponse;
import com.sdm.app.repository.RoleRepository;
import com.sdm.app.utils.GeneralHelper;
import com.sdm.app.utils.ResponseConverter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleServiceImpl {

  private final RoleRepository roleRepository;

  @Transactional
  public RoleResponse create(User user, RoleRequest request) {

    // check if admin
    GeneralHelper.isAdmin(user);

    Role role = new Role();
    role.setName(request.getName());
    roleRepository.save(role);
    return ResponseConverter.roleToResponse(role);
  }

  @Transactional
  public RoleResponse update(User user, RoleRequest request) {

    GeneralHelper.isAdmin(user);

    Role role = getRole(request.getId());
    if(Objects.nonNull(request.getName())){
      role.setName(request.getName());
    }
    roleRepository.save(role);
    return ResponseConverter.roleToResponse(role);
  }

  @Transactional
  public RoleResponse delete(User user, Long id) {

    GeneralHelper.isAdmin(user);

    Role role = getRole(id);
    roleRepository.delete(role);
    return ResponseConverter.roleToResponse(role);
  }

  @Transactional(readOnly = true)
  public List<RoleResponse> findAll() {
    return roleRepository.findAll().stream()
            .map(ResponseConverter::roleToResponse).collect(Collectors.toList());
  }

  public RoleResponse find(Long id) {
    return ResponseConverter.roleToResponse(getRole(id));
  }


  public Role getRole(Long id) {
    return roleRepository.findById(id)
            .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!"));
  }
}
