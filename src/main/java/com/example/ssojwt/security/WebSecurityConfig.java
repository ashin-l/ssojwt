package com.example.ssojwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //覆盖UserDetailsService类
        auth.userDetailsService(userDetailsService)
        //覆盖默认的密码验证类
        .passwordEncoder(bCryptPasswordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        // 关闭跨站请求防护
        .cors().and().csrf().disable()
        // 允许不登陆就可以访问的方法，多个用逗号分隔
        .authorizeRequests().antMatchers("/home","/login").permitAll()
        // 其他的需要授权后访问
        .anyRequest().authenticated()

        .and()
        // 增加登录拦截
        .addFilter(new JWTLoginFilter(authenticationManager()))
        // 增加是否登陸过滤
        .addFilter(new JWTAuthenticationFilter(authenticationManager()))
        // 前后端分离是无状态的，所以暫時不用session，將登陆信息保存在token中。
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // 防止H2 web 页面的Frame 被拦截
    http.headers().frameOptions().disable();
  }


}