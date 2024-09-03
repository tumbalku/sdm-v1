package com.sdm.app.service.impl;

import com.sdm.app.entity.User;
import com.sdm.app.model.req.LoginRequest;
import com.sdm.app.model.res.LoginResponse;
import com.sdm.app.repository.UserRepository;
import com.sdm.app.security.BCrypt;
import com.sdm.app.utils.GeneralHelper;
import com.sdm.app.utils.ResponseConverter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl{

  private final UserRepository userRepository;
  private GeneralHelper helper;


  @Transactional
  public LoginResponse loginUser(LoginRequest request) {

    helper.validate(request);

    User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This username maybe not exist"));
    if(BCrypt.checkpw(request.getPassword(), user.getPassword())){

      // success login
      user.setToken(UUID.randomUUID().toString());
      user.setTokenExpiredAt(System.currentTimeMillis() + (36L * 1_00_000 * 24 * 30));
      userRepository.save(user);

      return LoginResponse.builder()
              .token(user.getToken())
              .tokenExpiredAt(user.getTokenExpiredAt())
              .user(ResponseConverter.userToResponse(user))
              .build();
    }else{
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You send wrong password!");
    }
  }

  @Transactional
  public void logout(User user){
    user.setTokenExpiredAt(null);
    user.setToken(null);

    userRepository.save(user);
  }
}
