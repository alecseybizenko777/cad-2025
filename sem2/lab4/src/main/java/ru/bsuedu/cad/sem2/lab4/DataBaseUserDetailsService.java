package ru.bsuedu.cad.sem2.lab4;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class DataBaseUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public DataBaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("🔍 Попытка загрузки пользователя: " + username);
        User user = userRepository.findByName(username).orElseThrow();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());
        System.out.println("✅ Найден пользователь: " + user.getName());
        System.out.println("   Роль: " + user.getRole());
        return new org.springframework.security.core.userdetails.User(
            user.getName(),
            user.getPassword(),
            Collections.singleton(authority));
    }
}
