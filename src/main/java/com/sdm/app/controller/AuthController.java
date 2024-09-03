package com.sdm.app.controller;

import com.sdm.app.entity.User;
import com.sdm.app.model.req.LoginRequest;
import com.sdm.app.model.req.update.UpdateUserRequest;
import com.sdm.app.model.res.LoginResponse;
import com.sdm.app.model.res.SimpleUserResponse;
import com.sdm.app.model.res.WebResponse;
import com.sdm.app.service.impl.AuthServiceImpl;
import com.sdm.app.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@Validated
@AllArgsConstructor
public class AuthController {

  private final AuthServiceImpl authService;
  private final UserServiceImpl userService;



  @PostMapping(
          path = "/login",
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<LoginResponse> loginUser(@RequestBody LoginRequest request){
    LoginResponse res = authService.loginUser(request);

    return WebResponse.<LoginResponse>builder()
            .data(res)
            .message("Logged in Successfully")
            .build();
  }

  @DeleteMapping(
          path = "/logout",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> logout(User user){
    authService.logout(user);
    return WebResponse.<String>builder()
            .data("OK")
            .message("Logged out Successfully")
            .build();
  }

  @PatchMapping("/reset/pwd/{id}")
  public WebResponse<SimpleUserResponse> resetPassword(User user, @PathVariable("id") String id) {

    SimpleUserResponse response = userService.resetPassword(user, id);
    return WebResponse.<SimpleUserResponse>builder()
            .data(response)
            .message("Password has been reset")
            .build();
  }

  @PatchMapping("/update/pwd")
  public WebResponse<SimpleUserResponse> updatePassword(User user,
                                                        @RequestBody UpdateUserRequest request) {

    SimpleUserResponse response = userService.updatePassword(user, request);
    return WebResponse.<SimpleUserResponse>builder()
            .data(response)
            .message("Password has been updated")
            .build();
  }
}