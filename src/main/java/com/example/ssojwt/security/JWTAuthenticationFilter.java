package com.example.ssojwt.security;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;

/**
 * 是否登陆验证方法
 * 
 * @author 程就人生
 * @date 2019年5月26日
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  /**
   * 對請求進行過濾
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
    try {
      // 请求体的头中是否包含Authorization
      String header = request.getHeader("Authorization");
      // Authorization中是否包含Bearer，有一个不包含时直接返回
      if (header == null || !header.startsWith("Bearer ")) {
        chain.doFilter(request, response);
        //responseJson(response);
        return;
      }
      // 获取权限失败，会抛出异常
      UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
      // 获取后，将Authentication写入SecurityContextHolder中供后序使用
      SecurityContextHolder.getContext().setAuthentication(authentication);
      chain.doFilter(request, response);
    } catch (Exception e) {
      responseJson(response);
      e.printStackTrace();
    }
  }

  /**
   * 未登錄時的提示
   * 
   * @param response
   */
  private void responseJson(HttpServletResponse response) {
    try {
      // 未登錄時，使用json進行提示
      response.setContentType("application/json;charset=utf-8");
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      PrintWriter out = response.getWriter();
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("code", HttpServletResponse.SC_FORBIDDEN);
      map.put("message", "请登录！");
      out.write(new ObjectMapper().writeValueAsString(map));
      out.flush();
      out.close();
    } catch (Exception e1) {
      e1.printStackTrace();
    }
  }

  /**
   * 通过token，获取用户信息
   * 
   * @param request
   * @return
   */
  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null) {
      // 通过token解析出用户信息
      String user = Jwts.parser()
          // 签名、密钥
          .setSigningKey("MyJwtSecret").parseClaimsJws(token.replace("Bearer ", "")).getBody().getSubject();
      
      System.out.println("********** username: " + user);
      // 不为null，返回
      if (user != null) {
        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
      }
      return null;
    }
    return null;
  }

}