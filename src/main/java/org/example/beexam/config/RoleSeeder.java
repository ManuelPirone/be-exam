package org.example.beexam.config;

import org.example.beexam.user.entity.Role; // IMPORTA LA TUA ENTITÀ ROLE
import org.example.beexam.user.repository.RoleRepository; // IMPORTA IL TUO REPOSITORY
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleSeeder {

    @Bean
    public CommandLineRunner loadDefaultRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {

                Role admin = new Role();
                admin.setName("ROLE_ADMIN");
                roleRepository.save(admin);

                Role librarian = new Role();
                librarian.setName("ROLE_LIBRARIAN");
                roleRepository.save(librarian);

                Role member = new Role();
                member.setName("ROLE_MEMBER");
                roleRepository.save(member);

                System.out.println("Seeding completato");
            }
        };
    }
}