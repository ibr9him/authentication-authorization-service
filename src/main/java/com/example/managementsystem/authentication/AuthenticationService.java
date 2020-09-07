package com.example.managementsystem.authentication;

import com.example.managementsystem.clientmanagement.client.ClientEntity;
import com.example.managementsystem.clientmanagement.client.ClientRepository;
import com.example.managementsystem.clientmanagement.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Lazy}))
@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    @Override
    @Transactional(readOnly = true)
    public AuthenticationUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findOneByUsernameIgnoreCase(username)
                .map((user) -> clientRepository.findById(user.getClient().getId())
                        .filter(ClientEntity::isEnabled)
                        .map((client) -> new AuthenticationUser(user))
                        .orElseThrow(() -> {
                            log.error("User's client account is locked");
                            throw new DisabledException("User's client account is locked");
                        }))
                .orElseThrow(() -> {
                    log.error("Username: {}, not found", username);
                    throw new UsernameNotFoundException("Username not found");
                });
    }
}
