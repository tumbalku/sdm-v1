package com.sdm.app.service;

import com.sdm.app.entity.Address;
import com.sdm.app.entity.Cuti;
import com.sdm.app.entity.Role;
import com.sdm.app.entity.User;


public interface IdsService {

  public User getUser(String id);

  public Cuti getCuti(String id);

  public Address getAddress(Long id);

  public Role getRole(Integer id);
}
