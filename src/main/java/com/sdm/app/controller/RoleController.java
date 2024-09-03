package com.sdm.app.controller;

import com.sdm.app.entity.User;
import com.sdm.app.model.req.create.RoleRequest;
import com.sdm.app.model.res.RoleResponse;
import com.sdm.app.model.res.WebResponse;
import com.sdm.app.service.impl.RoleServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
public class RoleController {

  private final RoleServiceImpl roleService;

  @PostMapping
  public WebResponse<RoleResponse> create(User user, @RequestBody RoleRequest request) {
    RoleResponse response = roleService.create(user, request);
    return WebResponse.<RoleResponse>builder()
            .data(response)
            .message("Success create new Role!")
            .build();
  }

  @PatchMapping("/{id}")
  public WebResponse<RoleResponse> update(User user, @PathVariable("id") Long id, @RequestBody RoleRequest request) {
    request.setId(id);
    RoleResponse response = roleService.update(user, request);
    return WebResponse.<RoleResponse>builder()
            .data(response)
            .message("Role has been updated!")
            .build();
  }

  @DeleteMapping("/{id}")
  public WebResponse<RoleResponse> delete(User user, @PathVariable("id") Long id) {
    RoleResponse response = roleService.delete(user, id);
    return WebResponse.<RoleResponse>builder()
            .data(response)
            .message("Role has been deleted!")
            .build();
  }

  @GetMapping
  public WebResponse<List<RoleResponse>> findAll() {
    List<RoleResponse> response = roleService.findAll();
    return WebResponse.<List<RoleResponse>>builder()
            .data(response)
            .message("Success get roles")
            .build();
  }

  @GetMapping("/{id}")
  public WebResponse<RoleResponse> find(@PathVariable("id") Long id) {
    RoleResponse response = roleService.find(id);
    return WebResponse.<RoleResponse>builder()
            .data(response)
            .message("Success get role!")
            .build();
  }
}
