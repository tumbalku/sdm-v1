package com.sdm.app.model.req.update;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {


  private String username;
  private String newPassword;
  private String confirmPassword;

  // social media
  private String phone;
  private String email;
  private String instagram;
  private String linkedin;
  private String twitter;
  private String facebook;

}
