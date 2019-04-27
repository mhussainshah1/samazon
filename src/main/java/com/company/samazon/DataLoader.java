package com.company.samazon;

import com.company.samazon.models.User;
import com.company.samazon.models.Role;
import com.company.samazon.models.repositories.RoleRepository;
import com.company.samazon.models.repositories.UserRepository;
import com.company.samazon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {


    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("Loading Data... ");

        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));

        Role adminRole = roleRepository.findByRoleName("ADMIN");
        Role userRole = roleRepository.findByRoleName("USER");


        User dave = new User("dave", "password");
        dave.setPassword(userService.encode(dave.getPassword()));
        userService.saveCustomer(dave);

//        User moe = new User("mhussainshah79@gmail.com", "password", "Muhammad", "Shah", true, "moe");
//        moe.setPassword(userService.encode(moe.getPassword()));
//        userService.saveUser(moe);
//
//        User tolani = new User("tolani.oyefule@gmail.com", "password", "Tolani", "Oyefule", true, "lan");
//        tolani.setPassword(userService.encode(tolani.getPassword()));
//        userService.saveUser(tolani);

        User admin = new User("admin", "password");
        admin.setPassword(userService.encode(admin.getPassword()));
        userService.saveAdmin(admin);


    }

}