package com.sdm.app.model.res;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

  private String token;

  private Long tokenExpiredAt;

//  @JsonProperty(value = "user")
  private UserResponse user;

}
