package com.sdm.app.seeder;

import com.sdm.app.entity.Address;
import com.sdm.app.entity.Kop;
import com.sdm.app.entity.Role;
import com.sdm.app.entity.User;
import com.sdm.app.enumrated.Gender;
import static com.sdm.app.enumrated.KopType.*;

import com.sdm.app.enumrated.KopType;
import com.sdm.app.enumrated.UserStatus;
import com.sdm.app.repository.AddressRepository;
import com.sdm.app.repository.KopRepository;
import com.sdm.app.repository.RoleRepository;
import com.sdm.app.repository.UserRepository;
import com.sdm.app.security.BCrypt;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@AllArgsConstructor
public class Setup implements CommandLineRunner {

  private final UserRepository userRepository;
  private final AddressRepository addressRepository;
  private final RoleRepository roleRepository;
  private final KopRepository kopRepository;

  @Override
  public void run(String... args) throws Exception {
    Address KENDARI = new Address();
    KENDARI.setName("Kendari");

    if(addressRepository.findAll().size() == 0){

      addressRepository.saveAll(
              List.of(
                      KENDARI,
                      saveAddress("Baubau"),
                      saveAddress("Kabupaten Bombana"),
                      saveAddress("Kabupaten Buton"),
                      saveAddress("Kabupaten Buton Selatan"),
                      saveAddress("Kabupaten Buton Tengah"),
                      saveAddress("Kabupaten Buton Utara"),
                      saveAddress("Kabupaten Kolaka"),
                      saveAddress("Kabupaten Kolaka Timur"),
                      saveAddress("Kabupaten Kolaka Utara"),
                      saveAddress("Kabupaten Konawe"),
                      saveAddress("Kabupaten Konawe Kepulauan"),
                      saveAddress("Kabupaten Konawe Selatan"),
                      saveAddress("Kabupaten Konawe Utara"),
                      saveAddress("Kabupaten Muna"),
                      saveAddress("Kabupaten Muna Barat"),
                      saveAddress("Kabupaten Wakatobi")
              )
      );
    }
    if (roleRepository.findAll().size() == 0){


      Role EMPLOYEE = new Role();
      EMPLOYEE.setName("EMPLOYEE");

      Role BLUD = new Role();
      BLUD.setName("BLUD");

      Role PPPK = new Role();
      PPPK.setName("PPPK");

      Role PNS = new Role();
      PNS.setName("PNS");

      Role ADMIN = new Role();
      ADMIN.setName("ADMIN");
      roleRepository.saveAll(List.of(BLUD, EMPLOYEE, ADMIN, PNS, PPPK));

      saveUser("admin", "admin@gmail.com","085336421912", KENDARI, ADMIN, UserStatus.ACTIVE);

    }


    if(kopRepository.findAll().size() == 0){

      saveKop(BESAR, "852", "XI" );
      saveKop(KARENA_ALASAN_PENTING, "857", "VI" );
      saveKop(BERSALIN, "854", "V" );
      saveKop(SAKIT, "853", "VI" );
      saveKop(IZIN, "858", "IV" );
      saveKop(TAHUNAN, "851", "VI" );

    }
  }

  public Address saveAddress(String name){
    Address address = new Address();
    address.setName(name);
    return address;
  }

  public void saveKop(KopType kopType, String uniKop, String romawi){
    Kop kop = new Kop();
    kop.setType(kopType);
    kop.setUniKop(uniKop);
    kop.setRomawi(romawi);
    kop.setYear(Year.now());
    kopRepository.save(kop);
  }

  public void saveUser(String name,
                       String email,
                       String phone,
                       Address address,
                       Role role,
                       UserStatus status){

    User user = new User();
    String id = UUID.randomUUID().toString();
    user.setId(id);
    user.setUsername(id);
    user.setName(name);
    user.setGender(Gender.MALE);
    user.setEmail(email);
    user.setPhone(phone);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    user.setPassword(BCrypt.hashpw(id, BCrypt.gensalt()));
    user.setAddress(address);
    user.setRoles(Set.of(role));
    user.setStatus(status);

    userRepository.save(user);
  }
}
