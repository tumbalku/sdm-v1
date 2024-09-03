package com.sdm.app.model.res;

import com.sdm.app.enumrated.Gender;
import com.sdm.app.enumrated.UserStatus;

import java.util.List;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

  private String id;
  private String nip;
  private String name;
  private String email;
  private String phone;
  private String avatar;
  private Gender gender;
  private String address;
  private String username;
  private UserStatus status;
  private List<String> roles;

  // status in corporation
  private String pangkat;
  private String golongan;
  private String position;
  private String workUnit;

  // social media
  private String instagram;
  private String linkedin;
  private String twitter;
  private String facebook;
}
