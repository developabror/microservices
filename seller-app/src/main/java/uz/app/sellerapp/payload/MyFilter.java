package uz.app.sellerapp.payload;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import uz.app.sellerapp.entity.User;
import uz.app.sellerapp.repository.UserRepository;

import java.io.IOException;
import java.util.Base64;

@Component
public class MyFilter implements Filter {
    @Autowired
    @Lazy
    UserDetailsService userDetailsService;
    @Lazy
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        System.out.println(request.getServletPath());
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            filterChain.doFilter(request, servletResponse);
            return;
        }
        System.out.println(authorization);
        if (authorization.startsWith("Bearer ")) {
            authorization = authorization.substring(7);
            String username = jwtProvider.getUsernameFromToken(authorization);
            setUserToContext(username);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void setUserToContext(String username) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user,
                        null,
                        user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
