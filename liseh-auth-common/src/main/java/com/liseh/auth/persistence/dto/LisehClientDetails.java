package com.liseh.auth.persistence.dto;

import com.liseh.auth.persistence.entity.Client;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LisehClientDetails implements ClientDetails {
    private static final Integer ACCESS_TOKEN_EXPIRY = 24 * 60 * 60;
    private static final Integer REFRESH_TOKEN_EXPIRY = 15 * 24 * 60 * 60;
    private Client client;

    public LisehClientDetails(Client client) {
        this.client = client;
    }

    @Override
    public String getClientId() {
        return client.getClientId();
    }

    @Override
    public Set<String> getResourceIds() {
        return null;
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return client.getClientSecret();
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        Set<String> scopes = new HashSet<>();
        scopes.add("READ");
        scopes.add("WRITE");
        return scopes;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        Set<String> grantTypes = new HashSet<>();
        grantTypes.add("client_credentials");
        return grantTypes;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
        return grantedAuthorities;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return ACCESS_TOKEN_EXPIRY;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return REFRESH_TOKEN_EXPIRY;
    }

    @Override
    public boolean isAutoApprove(String s) {
        return true;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }
}
