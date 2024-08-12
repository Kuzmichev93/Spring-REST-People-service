package app.config;

import app.component.JwtComponent;
import app.model.UserAuth;
import app.reprository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.io.IOException;
import java.util.Base64;

//@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter  {

    private  AuthenticationManager authenticationManager;
    private JwtComponent jwtComponent;
    private UserRepository userRepository;


    @Autowired
    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                JwtComponent jwtComponent,
                                UserRepository userRepository
                                   ){

        this.authenticationManager=authenticationManager;
        this.jwtComponent = jwtComponent;
        this.userRepository = userRepository;

    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

            String jwttoken = getAuthorizationJWT((HttpServletRequest) servletRequest);
            String basic = getAuthorizationBasic((HttpServletRequest) servletRequest);

            if(!jwttoken.equals("Error")){
                if (jwtComponent.validateAccessToken(jwttoken)){
                    Claims accessbody = jwtComponent.getBodyAccessToken(jwttoken);
                    String login = (String) accessbody.get("login");

                    if(userRepository.existsByUsername(login)){
                        UserAuth userAuth = userRepository.findByUsername(login);
                        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(login,null,userAuth.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authRequest);
                    }
                }

            }
        if(!basic.equals("Error")){
            byte[] btoken = Base64.getDecoder().decode(basic);
            String stoken =  new String(btoken);

            String login = stoken.substring(0,stoken.indexOf(":"));
            UserAuth userAuth = userRepository.findByUsername(login);
            if(userAuth!=null){
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(login,null,userAuth.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authRequest);
            }
        }




        filterChain.doFilter(servletRequest,servletResponse);
    }

    public String getAuthorizationJWT(HttpServletRequest request){
       String bearer = request.getHeader("Authorization");

       if(bearer!=null && bearer.startsWith("Bearer ")){

           return bearer.substring(7);
       }

       return "Error";
    }

    public String getAuthorizationBasic(HttpServletRequest request){
        String basic = request.getHeader("Authorization");
        if(basic!=null && basic.startsWith("Basic ")){
            return basic.substring(6);}

        return "Error";}




}

