package com.sdm.app.entity;

import com.sdm.app.enumrated.Gender;
import com.sdm.app.enumrated.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Id
  @Column(unique = true, nullable = false)
  private String id;

  @Column(unique = true)
  private String nip;
  @Column(nullable = false)
  private String name;
  private String token;
  private Long tokenExpiredAt;
  private String avatar;
  private String pangkat;
  private String golongan;
  private String position;
  private String workUnit;

  // social media
  private String instagram;
  private String linkedin;
  private String twitter;
  private String facebook;

  @Column(unique = true, nullable = false)
  private String username;
  @Column(nullable = false)
  private String password;

  @Column(unique = true)
  private String email;

  @Enumerated(value = EnumType.STRING)
  private Gender gender;

  @Column(nullable = false, unique = true)
  private String phone;

  @Enumerated(value = EnumType.STRING)
  private UserStatus status;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // ========== ONE TO MANY RELATION ========== \\

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<Letter> letters;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<Sip> sip;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<Cuti> cutiSet;
  // ========== ONE TO MANY RELATION ========== \\


  // ========== MANY TO MANY RELATION ========== \\
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles",
          joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Set<Role> roles = new HashSet<>();;
  // ========== MANY TO MANY RELATION ========== \\

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "address", referencedColumnName = "id")
  private Address address;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Post> posts;

}
