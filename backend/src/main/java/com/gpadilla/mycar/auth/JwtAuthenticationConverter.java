package com.gpadilla.mycar.auth;

import com.gpadilla.mycar.enums.UserRole;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

    public JwtAuthenticationConverter() {
        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("ROLE_");
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = authoritiesConverter.convert(jwt);

        Long id = Long.valueOf(jwt.getSubject());
        List<String> rolesStrings = jwt.getClaimAsStringList("roles");

        if (rolesStrings == null || rolesStrings.isEmpty()) {
            throw new JwtException("Missing or empty roles claim");
        }

        List<UserRole> roles;
        try {
            roles = rolesStrings.stream().map(UserRole::valueOf).toList();
        } catch (IllegalArgumentException e) {
            throw new JwtException("Invalid role in token: " + rolesStrings);
        }

        CurrentUser userDetails = new CurrentUser(id, roles);

        return new UsernamePasswordAuthenticationToken(userDetails, jwt, authorities);
    }
}