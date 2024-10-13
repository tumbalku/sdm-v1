package com.sdm.app.controller;


import com.sdm.app.entity.User;
import com.sdm.app.model.req.create.CreateUserRequest;
import com.sdm.app.model.req.search.UserSearchPin;
import com.sdm.app.model.req.search.UserSearchRequest;
import com.sdm.app.model.req.update.PinPriorityRequest;
import com.sdm.app.model.req.update.UpdateUserRequest;
import com.sdm.app.model.res.*;
import com.sdm.app.service.impl.UserServiceImpl;
import com.sdm.app.utils.ResponseConverter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserServiceImpl userService;


  @PatchMapping("priority/{id}")
  public WebResponse<UserResponse> pinPriority(User user,
                                               @PathVariable("id") String id,
                                               @RequestBody PinPriorityRequest request) {
    request.setId(id);
    UserResponse response = userService.pinPriority(user, request);

    return WebResponse.<UserResponse>builder()
            .data(response)
            .message("Success pin")
            .build();
  }

  @GetMapping(path = "pin", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponseWithPaging<List<PinUserResponse>> findPin(@RequestParam(name = "dateSortBy", required = false, defaultValue = "latest") String dateSortBy,
                                                              @RequestParam(name = "role", required = false) String role,
                                                              @RequestParam(name = "workUnit", required = false) String workUnit,
                                                              @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                              @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

    UserSearchPin request = new UserSearchPin();
    request.setDateSortBy(dateSortBy);
    request.setWorkUnit(workUnit);
    request.setPage(page);
    request.setRole(role);
    request.setSize(size);

    Page<PinUserResponse> responses = userService.searchPin(request);

    return WebResponseWithPaging.<List<PinUserResponse>>builder()
            .data(responses.getContent())
            .message("Search Success")
            .pagination(ResponseConverter.getPagingResponse(responses))
            .build();
  }

  @PostMapping
  public WebResponse<SimpleUserResponse> create(User user,
                                                @RequestBody CreateUserRequest request) {
    SimpleUserResponse response = userService.create(user, request);
    return WebResponse.<SimpleUserResponse>builder()
            .data(response)
            .message("Success create new user!")
            .build();
  }


  @PatchMapping("/{id}")
  public WebResponse<SimpleUserResponse> update(User user,
                                                @PathVariable("id") String id,
                                                @RequestBody CreateUserRequest request) {
    request.setId(id);
    SimpleUserResponse response = userService.update(user, request);
    return WebResponse.<SimpleUserResponse>builder()
            .data(response)
            .message("User has been updated")
            .build();
  }


  @DeleteMapping("/{id}")
  public WebResponse<SimpleUserResponse> delete(User user, @PathVariable("id") String id) {
    SimpleUserResponse response = userService.delete(user, id);
    return WebResponse.<SimpleUserResponse>builder()
            .data(response)
            .message("User has been deleted")
            .build();
  }

  @GetMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponseWithPaging<List<SimpleUserResponse>> search(User user,
                                                                @RequestParam(name = "identity", required = false) String identity,
                                                                @RequestParam(name = "status", required = false) String status,
                                                                @RequestParam(name = "roles", required = false) List<String> roles,
                                                                @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                                @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

    UserSearchRequest request = new UserSearchRequest();
    request.setIdentity(identity);
    request.setStatus(status);
    request.setPage(page);
    request.setRoles(roles);
    request.setSize(size);

    Page<SimpleUserResponse> responses = userService.searchUsers(user, request);

    return WebResponseWithPaging.<List<SimpleUserResponse>>builder()
            .data(responses.getContent())
            .message("Search Success")
            .pagination(ResponseConverter.getPagingResponse(responses))
            .build();
  }


  @GetMapping("/{id}")
  public WebResponse<UserResponse> find(@PathVariable("id") String id) {
    UserResponse response = userService.getById(id);
    return WebResponse.<UserResponse>builder()
            .data(response)
            .message("User founded!")
            .build();
  }


  @PatchMapping(path = "/avatar",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> updateAvatar(User user,
                                          @RequestParam("avatar") MultipartFile file) {

    userService.updateAvatar(user, file);

    return WebResponse.<String>builder()
            .data("OK")
            .message("avatar has been updated")
            .build();
  }

  @PatchMapping(path = "/info",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<SimpleUserResponse> updateUserInfo(User user, @RequestBody UpdateUserRequest request) {

    SimpleUserResponse response = userService.updateUserInfo(user, request);
    return WebResponse.<SimpleUserResponse>builder()
            .data(response)
            .message("Info has been updated!")
            .build();
  }

}
