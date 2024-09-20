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
public class UserLite {
  private String id;
  private String nip;
  private String name;
  private String avatar;
  private String workUnit;
  private String address;

}

