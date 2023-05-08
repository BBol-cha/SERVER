package project.BBolCha.global.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.BBolCha.domain.user.entity.User;
import project.BBolCha.domain.user.repository.UserRepository;
import project.BBolCha.global.exception.CustomException;
import project.BBolCha.global.model.Result;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Component("userDetailsService")
@RequiredArgsConstructor
public class JwtUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) {
        return userRepository.findOneWithAuthoritiesByEmail(email)
                .map(this::createUser)
                .orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));
    }

    private org.springframework.security.core.userdetails.User createUser(User user) {

        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                grantedAuthorities
        );
    }
}
