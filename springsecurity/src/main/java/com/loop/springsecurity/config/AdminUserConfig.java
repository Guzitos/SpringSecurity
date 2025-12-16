package com.loop.springsecurity.config;

import com.loop.springsecurity.entities.Role;
import com.loop.springsecurity.entities.User;
import com.loop.springsecurity.repository.RoleRepository;
import com.loop.springsecurity.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;


@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {

        // ADMIN
        Role adminRole = roleRepository.findByName(Role.Values.ADMIN.name())
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(Role.Values.ADMIN.name());
                    return roleRepository.save(role);
                });

        // BASIC
        Role basicRole = roleRepository.findByName(Role.Values.BASIC.name())
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(Role.Values.BASIC.name());
                    return roleRepository.save(role);
                });

        // usuário admin
        userRepository.findByUsername("admin")
                .ifPresentOrElse(
                        user -> System.out.println("Usuário admin já existe"),
                        () -> {
                            User user = new User();
                            user.setUsername("admin");
                            user.setPassword(passwordEncoder.encode("123"));
                            user.setRoles(Set.of(adminRole));
                            userRepository.save(user);
                        }
                );
    }
}

