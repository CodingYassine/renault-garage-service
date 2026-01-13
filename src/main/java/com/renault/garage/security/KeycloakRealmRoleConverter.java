package com.renault.garage.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security. core. GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream. Collectors;

/**
 * Extrait les rôles Keycloak et les convertit en GrantedAuthorities préfixés ROLE_.
 * Recherche dans :
 * - claim "realm_access.roles":  liste de rôles globaux au realm
 * - claim "resource_access.{client}.roles": rôles attribués pour un client spécifique
 */
public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Set<String> roles = new HashSet<>();

        // 1. Extraire les realm roles (realm_access.roles)
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            Object rolesObj = realmAccess. get("roles");
            if (rolesObj instanceof Collection) {
                Collection<? > rolesList = (Collection<?>) rolesObj;
                rolesList.forEach(role -> roles.add(String.valueOf(role)));
            }
        }

        // 2. Extraire les resource/client roles (resource_access.{client}.roles)
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            resourceAccess.values().forEach(resource -> {
                if (resource instanceof Map) {
                    Map<?, ?> resourceMap = (Map<?, ?>) resource;
                    Object rolesObj = resourceMap.get("roles");
                    if (rolesObj instanceof Collection) {
                        Collection<?> rolesList = (Collection<?>) rolesObj;
                        rolesList.forEach(role -> roles.add(String.valueOf(role)));
                    }
                }
            });
        }

        // 3. Convertir en Spring Security GrantedAuthority avec préfixe ROLE_
        return roles. stream()
                .map(role -> "ROLE_" + role. toUpperCase(Locale.ROOT))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}