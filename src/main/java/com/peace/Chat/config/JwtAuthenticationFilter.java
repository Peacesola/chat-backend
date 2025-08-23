package com.peace.Chat.config;
import com.peace.Chat.security.CustomUserDetailsService;
import com.peace.Chat.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.http.HttpHeaders;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader= request.getHeader("Authorization");
        if (authHeader== null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        final String token= authHeader.substring(7);

        try {
            String username= jwtService.extractUsername(token);

            if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null){
                var userDetails= userDetailsService.loadUserByUsername(username);

                var authtoken= new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authtoken);
            }
        }
        catch (Exception e){
            System.out.println("JWT Error: " + e.getMessage());
        }
        filterChain.doFilter(request,response);
    }

}
