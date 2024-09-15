package com.sdm.app.model.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

  @NotBlank(message = "must be not blank")
  @Size(min = 6, message = "length must be at least {min} character")
  private String username;

  @NotBlank(message = "must be not blank")
  @Size(min = 6, message = "length must be at least {min} character")
  private String password;
}
