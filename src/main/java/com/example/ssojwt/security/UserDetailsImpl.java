package com.example.ssojwt.security;

import java.util.ArrayList;
import java.util.Collection;

import com.example.ssojwt.model.SysUser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private String username;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(SysUser user) {
    username = user.getUsername();
    password = user.getPassword();
    authorities = new ArrayList<>();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean isEnabled() {
    // TODO Auto-generated method stub
    return true;
  }

}