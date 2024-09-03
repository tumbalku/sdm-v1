package com.sdm.app.service.impl;

import com.sdm.app.entity.Address;
import com.sdm.app.entity.Cuti;
import com.sdm.app.entity.Role;
import com.sdm.app.entity.User;
import com.sdm.app.repository.AddressRepository;
import com.sdm.app.repository.CutiRepository;
import com.sdm.app.repository.RoleRepository;
import com.sdm.app.repository.UserRepository;

import com.sdm.app.service.IdsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class IdsServiceImpl{

  private final UserRepository userRepository;
  private final CutiRepository cutiRepository;
  private final AddressRepository addressRepository;
  private final RoleRepository roleRepository;

  public User getUser(String id) {
    return userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
  }

  public Cuti getCuti(String id) {
    return cutiRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuti not found!"));
  }

  public Address getAddress(Long id) {
    return addressRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found!"));
  }

  public Role getRole(Long id) {
    return roleRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!"));
  }
}
