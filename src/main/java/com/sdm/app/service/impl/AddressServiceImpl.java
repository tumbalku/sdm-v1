package com.sdm.app.service.impl;

import com.sdm.app.entity.Address;
import com.sdm.app.entity.User;
import com.sdm.app.model.req.create.CreateAddressRequest;
import com.sdm.app.model.res.AddressResponse;
import com.sdm.app.repository.AddressRepository;
import com.sdm.app.repository.UserRepository;
import com.sdm.app.utils.GeneralHelper;
import com.sdm.app.utils.ResponseConverter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressServiceImpl {

  private final AddressRepository addressRepository;
  private final UserRepository userRepository;

  public void isAddressExist(String name) {
    boolean exist = addressRepository.existsByNameIgnoreCase(name);
    if(exist){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address already created!");
    }
  }

  // this method not usage yet
  public Address makeAddress(String name) {
    Optional.ofNullable(name).filter(StringUtils::hasText)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address name must be not blank"));
    Address exist = addressRepository.findByNameIgnoreCase(name).orElse(null);
    if(Objects.nonNull(exist)){
      return exist;
    }else{
      Address address = new Address();
      address.setName(name);
      addressRepository.save(address);
      return address;
    }
  }

  public Address getById(Long id) {
    return addressRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found!"));
  }

  public AddressResponse create(User admin, CreateAddressRequest request) {

    GeneralHelper.isAdmin(admin);
    isAddressExist(request.getName());

    Address address = new Address();
    address.setName(request.getName());
    addressRepository.save(address);
    return ResponseConverter.addressToResponse(address);
  }

  public AddressResponse update(User admin, CreateAddressRequest request) {

    GeneralHelper.isAdmin(admin);
    Address address = getById(request.getId());
    if(Objects.nonNull(request.getName())){
      address.setName(request.getName());
    }

    addressRepository.save(address);
    return ResponseConverter.addressToResponse(address);
  }

  public AddressResponse delete(User admin, Long id) {

    GeneralHelper.isAdmin(admin);
    Address address = getById(id);
    address.getUsers().forEach(candidate -> candidate.setAddress(null));

    userRepository.saveAll(address.getUsers());
    addressRepository.delete(address);
    return ResponseConverter.addressToResponse(address);
  }

  public List<AddressResponse> findAll() {
    return addressRepository.findAll().stream()
            .map(ResponseConverter::addressToResponse).collect(Collectors.toList());
  }

  public AddressResponse find(Long id) {
    return ResponseConverter.addressToResponse(getById(id));
  }
}
