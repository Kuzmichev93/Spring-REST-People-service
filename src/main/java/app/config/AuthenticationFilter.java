package app.config;

import app.component.JwtComponent;
import app.exeption.СustomException;
import app.model.UserAuth;
import app.reprository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;


import java.io.IOException;
import java.util.Base64;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private  AuthenticationManager authenticationManager;
    private JwtComponent jwtComponent;
    private UserRepository userRepository;
    private HandlerExceptionResolver resolver;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                JwtComponent jwtComponent,
                                UserRepository userRepository,
                                HandlerExceptionResolver resolver
                                   ){

        this.authenticationManager=authenticationManager;
        this.jwtComponent = jwtComponent;
        this.userRepository = userRepository;
        this.resolver = resolver;


    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            try{
                //addHeader((HttpServletResponse) servletResponse,(HttpServletRequest) servletRequest);
                checkBodyPost((HttpServletRequest)servletRequest);
                String token = getHeaderAuthorization((HttpServletRequest) servletRequest);
                String jwttoken = getAuthorizationJWT(token);
                String basic = getAuthorizationBasic(token);

                if(!jwttoken.equals("Error")) {
                    if (jwtComponent.validateAccessToken(jwttoken)) {
                        Claims accessbody = jwtComponent.getBodyAccessToken(jwttoken);
                        String login = (String) accessbody.get("login");

                        if (userRepository.existsByUsername(login)) {
                            UserAuth userAuth = userRepository.findByUsername(login);
                            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(login, null, userAuth.getAuthorities());

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
                    else{
                        throw new СustomException("Пользователь не прошел аутентифицированию",HttpStatus.UNAUTHORIZED);
                    }
                }

                filterChain.doFilter(servletRequest,servletResponse);

            }
            catch (СustomException e){
                resolver.resolveException((HttpServletRequest) servletRequest,(HttpServletResponse) servletResponse,null,e);
            }

    }
    public void addHeader(HttpServletResponse response,HttpServletRequest request){
        System.out.println(request.getMethod());
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Credentials");
        response.addHeader("Access-Control-Allow-Credentials", "true");
    }
    public String getAuthorizationJWT(String bearer) throws СustomException {

       if(bearer!=null && bearer.startsWith("Bearer ")){
           return bearer.substring(7);
       }

       return "Error";
    }

    public String getHeaderAuthorization(HttpServletRequest request) throws СustomException {
        String header = request.getHeader("Authorization");

        if(header!=null){
            if(!header.startsWith("Bearer ") && !header.startsWith("Basic ")){

                throw new СustomException("Укажите Bearer или Basic в заголовке",HttpStatus.BAD_REQUEST);
            }
            return header;
        }
        return null;
    }

    public String getAuthorizationBasic(String basic){
        if(basic!=null && basic.startsWith("Basic ")){
            return basic.substring(6);
        }

        return "Error";}

    public void checkBodyPost(HttpServletRequest request) throws СustomException {

        if(request.getMethod().equals("POST")){
            if(request.getHeader("Content-Length").equals("0")){
                throw new СustomException("В методе POST тело сообщения не должно быть пустым",HttpStatus.BAD_REQUEST);
            }
        }
    }



}

