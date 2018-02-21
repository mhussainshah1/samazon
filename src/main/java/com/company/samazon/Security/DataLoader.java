package com.company.samazon.Security;

import com.company.samazon.Models.Role;
import com.company.samazon.Repositories.RoleRepository;
import com.company.samazon.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {


    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("Loading Data... ");

        roleRepository.save(new Role("CUSTOMER"));
        roleRepository.save(new Role("ADMIN"));


        Role customerRole = roleRepository.findByRoleName("CUSTOMER");
        Role adminRole = roleRepository.findByRoleName("ADMIN");



    }

}