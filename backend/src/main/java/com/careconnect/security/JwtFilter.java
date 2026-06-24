package com.careconnect.security;
import com.careconnect.user.UserRepository;
import jakarta.servlet.FilterChain; import jakarta.servlet.ServletException; import jakarta.servlet.http.*; import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; import org.springframework.security.core.authority.SimpleGrantedAuthority; import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.stereotype.Component; import org.springframework.web.filter.OncePerRequestFilter;
@Component public class JwtFilter extends OncePerRequestFilter {
  private final JwtService jwt; private final UserRepository users;
  public JwtFilter(JwtService jwt,UserRepository users){this.jwt=jwt;this.users=users;}
  protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)throws ServletException,IOException {
    String header=request.getHeader("Authorization");
    if(header!=null&&header.startsWith("Bearer ")) try {
      String email=jwt.email(header.substring(7));
      users.findByEmail(email).ifPresent(user -> {
        var authentication=new UsernamePasswordAuthenticationToken(user.getEmail(),null,java.util.List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      });
    } catch(Exception ignored) {}
    chain.doFilter(request,response);
  }
}
