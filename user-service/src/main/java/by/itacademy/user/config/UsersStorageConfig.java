package by.itacademy.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class UsersStorageConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource, PasswordEncoder encoder) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        try{
            UserDetails user = User.builder()
                    .username("user")
                    .password(encoder.encode("123"))
                    .roles("USER")
                    .build();
            UserDetails user2 = User.builder()
                    .username("user2")
                    .password(encoder.encode("123"))
                    .roles("USER")
                    .build();
            UserDetails user3 = User.builder()
                    .username("user3")
                    .password(encoder.encode("123"))
                    .roles("USER")
                    .build();
            UserDetails admin = User.builder()
                    .username("admin")
                    .password(encoder.encode("321"))
                    .roles("USER", "ADMIN")
                    .build();

            manager.createUser(user);
            manager.createUser(user2);
            manager.createUser(user3);
            manager.createUser(admin);
        }catch (DuplicateKeyException e){
            //всё  ок, уже есть
        }

        return manager;
    }
}
