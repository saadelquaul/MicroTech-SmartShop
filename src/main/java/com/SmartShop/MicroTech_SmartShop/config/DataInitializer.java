package com.SmartShop.MicroTech_SmartShop.config;

import com.SmartShop.MicroTech_SmartShop.entity.Client;
import com.SmartShop.MicroTech_SmartShop.entity.User;
import com.SmartShop.MicroTech_SmartShop.enums.CustomerTier;
import com.SmartShop.MicroTech_SmartShop.enums.UserRole;
import com.SmartShop.MicroTech_SmartShop.repository.ClientRepository;
import com.SmartShop.MicroTech_SmartShop.repository.ProductRepository;
import com.SmartShop.MicroTech_SmartShop.repository.UserRepository;
import com.SmartShop.MicroTech_SmartShop.utils.PasswordUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if(userRepository.count() == 0) {
            seedUsers();
        }

    }


    private void seedUsers() {
        userRepository.save(
                User.builder().username("admin").password(PasswordUtil.hash("admin123")).role(UserRole.ADMIN).build()
        );
    }



}
