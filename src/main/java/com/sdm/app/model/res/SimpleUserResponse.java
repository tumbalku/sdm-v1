package com.sdm.app.model.res;

import com.sdm.app.enumrated.Gender;
import com.sdm.app.enumrated.UserStatus;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleUserResponse {
  private String id;
  private Integer priority;
  private String nip;
  private String name;
  private String address;
  private String email;
  private String phone;
  private String avatar;
  private Gender gender;
  private String workUnit;
  private String instagram;
  private String position;
  private String linkedin;
  private String twitter;
  private String facebook;
  private List<String> roles;

  private UserStatus status;

}

