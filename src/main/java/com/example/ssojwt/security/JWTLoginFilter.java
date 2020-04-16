package com.example.ssojwt.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 验证用户名密码正确后，生成一个token，放在header里，返回给客户端
 * 
 * @author 程就人生
 * @date 2019年5月26日
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  public JWTLoginFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  /**
   * 接收并解析用户凭证，出現错误时，返回json数据前端
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {

    try {
      // 从输入流中获取到登录的信息
      LoginUser user = new ObjectMapper().readValue(request.getInputStream(), LoginUser.class);
      UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(),
          user.getPassword(), new ArrayList<>());
      return authenticationManager.authenticate(authRequest);
    } catch (Exception e) {
      try {
        // 未登錄出現賬號或密碼錯誤時，使用json進行提示
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter out = response.getWriter();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", HttpServletResponse.SC_UNAUTHORIZED);
        map.put("message", "账号或密码错误！");
        out.write(new ObjectMapper().writeValueAsString(map));
        out.flush();
        out.close();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
      throw new RuntimeException(e);
    }
  }

  /**
   * 用户登录成功后，生成token,并且返回json数据给前端
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {

    // json web token构建
    String token = Jwts.builder()
        // 此处为自定义的、实现org.springframework.security.core.userdetails.UserDetails的类，需要和配置中设置的保持一致
        // 此处的subject可以用一个用户名，也可以是多个信息的组合，根据需要来定
        .setSubject(((UserDetailsImpl) authResult.getPrincipal()).getUsername())
        // 设置token过期时间，24小時
        .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))

        // 设置token签名、密钥
        .signWith(SignatureAlgorithm.HS512, "MyJwtSecret")

        .compact();

    // 返回token
    response.addHeader("Authorization", "Bearer " + token);

    try {
      // 登录成功時，返回json格式进行提示
      response.setContentType("application/json;charset=utf-8");
      response.setStatus(HttpServletResponse.SC_OK);
      PrintWriter out = response.getWriter();
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("code", HttpServletResponse.SC_OK);
      map.put("message", "登陆成功！");
      out.write(new ObjectMapper().writeValueAsString(map));
      out.flush();
      out.close();
    } catch (Exception e1) {
      e1.printStackTrace();
    }
  }

}