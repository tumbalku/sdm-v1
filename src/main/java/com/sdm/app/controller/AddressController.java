package com.sdm.app.controller;

import com.sdm.app.entity.User;
import com.sdm.app.model.req.create.CreateAddressRequest;
import com.sdm.app.model.res.AddressResponse;
import com.sdm.app.model.res.WebResponse;
import com.sdm.app.service.impl.AddressServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@AllArgsConstructor
public class AddressController {

  private final AddressServiceImpl addressService;

  @PostMapping
  public WebResponse<AddressResponse> create(User user, @RequestBody CreateAddressRequest request) {
    AddressResponse response = addressService.create(user, request);
    return WebResponse.<AddressResponse>builder()
            .data(response)
            .message("Success create new Address!")
            .build();
  }

  @PatchMapping("/{id}")
  public WebResponse<AddressResponse> update(User user, @PathVariable("id") Long id, @RequestBody CreateAddressRequest request) {
    request.setId(id);
    AddressResponse response = addressService.update(user, request);
    return WebResponse.<AddressResponse>builder()
            .data(response)
            .message("Address has been updated!")
            .build();
  }

  @DeleteMapping("/{id}")
  public WebResponse<AddressResponse> delete(User user, @PathVariable("id") Long id) {
    AddressResponse response = addressService.delete(user, id);
    return WebResponse.<AddressResponse>builder()
            .data(response)
            .message("Address has been deleted!")
            .build();
  }

  @GetMapping
  public WebResponse<List<AddressResponse>> findAll(User user) {
    List<AddressResponse> response = addressService.findAll(user);
    return WebResponse.<List<AddressResponse>>builder()
            .data(response)
            .message("Success get addresses")
            .build();
  }

  @GetMapping("/{id}")
  public WebResponse<AddressResponse> find(@PathVariable("id") Long id) {
    AddressResponse response = addressService.find(id);
    return WebResponse.<AddressResponse>builder()
            .data(response)
            .message("Success get role!")
            .build();
  }
}
