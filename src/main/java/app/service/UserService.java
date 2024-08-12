package app.service;

import app.model.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import app.model.UserAuth;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import app.reprository.UserRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuth user = userRepository.findByUsername(username);


        return user;
    }

    public ResponseEntity<String> registerUser(HttpServletRequest request,UserAuth userAuth) throws ServletException, JSONException {
        JSONObject jsonObject = new JSONObject();
        Pattern pat =  Pattern.compile("[^a-zа-я]",Pattern.CASE_INSENSITIVE);
        Matcher mat = pat.matcher(userAuth.getUsername());

        if(mat.find()){
            jsonObject.put("Error","Имя содержит не валидное значение");
            return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByUsername(userAuth.getUsername())){
            jsonObject.put("Error","Пользователь уже зарегистрирован");
            return new ResponseEntity<>(String.valueOf(jsonObject),HttpStatus.BAD_REQUEST);
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = userAuth.getPassword();

        userAuth.setPassword(bCryptPasswordEncoder.encode(userAuth.getPassword()));
        userAuth.setRoles(Role.USER.toString());
        userRepository.save(userAuth);

        request.login(userAuth.getUsername(),password);
        jsonObject.put("Message","Пользователь зарегистрирован");
        return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.OK);
    }

    public ResponseEntity<String> authUser(HttpServletRequest request,UserAuth userAuth) throws ServletException, JSONException {
        JSONObject jsonObject = new JSONObject();

        if (userRepository.existsByUsername(userAuth.getUsername())){
            request.login(userAuth.getUsername(),userAuth.getPassword());
            jsonObject.put("Message","Пользователь вошел в систему");
            return new ResponseEntity<>(String.valueOf(jsonObject),HttpStatus.OK);
        }
        Pattern pat =  Pattern.compile("[^a-zа-я]",Pattern.CASE_INSENSITIVE);
        Matcher mat = pat.matcher(userAuth.getUsername());
        if(mat.find()){
            jsonObject.put("Error","Имя содержит не валидное значение");
            return new ResponseEntity<>(String.valueOf(jsonObject),HttpStatus.BAD_REQUEST);
    }
        jsonObject.put("Error","Пользователь  с таким именем  не зарегеистрирован в системе");
         return new ResponseEntity<>(String.valueOf(jsonObject),HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<String> exitUser(HttpServletRequest request, HttpServletResponse response,UserAuth userAuth) throws ServletException, JSONException, IOException {
        JSONObject jsonObject = new JSONObject();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(!auth.getPrincipal().equals("anonymousUser")){

            request.logout();
            jsonObject.put("Message", String.format("Пользователь %s вышел из системы", userAuth.getUsername()));
            return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.OK);
        }

        jsonObject.put("Error", "Вы не авторизовались");

        return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.BAD_REQUEST);

    }

}
