package app.service;

import app.component.JwtComponent;
import app.exeption.СustomException;
import app.model.JwtResponse;
import app.model.UserAuth;
import app.reprository.UserRepository;
import io.jsonwebtoken.Claims;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.HashMap;

@Service
public class JwtService {
    private UserRepository userRepository;
    private HashMap<String,String> refreshmap = new HashMap<>();
    public JwtComponent jwtComponent;



    @Autowired
    public JwtService(UserRepository userRepository,JwtComponent jwtComponent){

        this.userRepository = userRepository;
        this.jwtComponent = jwtComponent;
    }

    public JSONObject getAlltoken(UserAuth userAuth) throws JSONException, СustomException {
        JSONObject jsonObject = new JSONObject();
        if(userAuth.getUsername()==null){
            throw new СustomException("Тело сообщения должно содерать поле username",HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByUsername(userAuth.getUsername())){
            String accessToken = new JwtComponent().createtAccessToken(userAuth);
            String refreshToken = new JwtComponent().createRefreshToken(userAuth);
            refreshmap.put(userAuth.getUsername(),refreshToken);
            jsonObject.put("accessToken",accessToken);
            jsonObject.put("refreshToken",refreshToken);
            jsonObject.put("header","Bearer");

            return jsonObject;
        }
        else{
            throw new СustomException("Пользователя нет в бд",HttpStatus.NOT_FOUND);
        }

    }

    public JSONObject getAccessToken(JwtResponse jwtResponse) throws JSONException, СustomException {
        JSONObject jsonObject = new JSONObject();
        if(jwtResponse.getRefreshToken() == null){
            throw new СustomException("Тело сообщения должно содерать поле refreshToken",HttpStatus.BAD_REQUEST);
        }
        jwtComponent.validateRefreshToken(jwtResponse.getRefreshToken());
        Claims refreshBody = jwtComponent.getBodyRefreshToken(jwtResponse.getRefreshToken());
        String login = (String) refreshBody.get("login");
        if(jwtResponse.getRefreshToken().equals(refreshmap.get(login))) {
            UserAuth userAuth = userRepository.findByUsername(login);
            if (userAuth.getUsername() != null) {
                String accesstoken = jwtComponent.createtAccessToken(userAuth);

                jsonObject.put("accesstoken", accesstoken);
                return jsonObject;
            }
            throw new СustomException("Пользователя нет в бд", HttpStatus.NOT_FOUND);
        }
        throw new СustomException("Введенный refreshtoken больше не содержится в бд", HttpStatus.NOT_FOUND);


    }

    public JSONObject getRefreshToken(JwtResponse jwtResponse) throws JSONException, СustomException {
        String token = jwtResponse.getRefreshToken();
        JSONObject jsonObject = new JSONObject();

        if(jwtResponse.getRefreshToken() == null){
            throw new СustomException("Тело сообщения должно содержать поле refreshToken",HttpStatus.BAD_REQUEST);
        }

        jwtComponent.validateRefreshToken(token);
        Claims tokenBody = jwtComponent.getBodyRefreshToken(token);
        UserAuth userAuth = userRepository.findByUsername((String) tokenBody.get("login"));
        String newRefreshToken = jwtComponent.createRefreshToken(userAuth);
        refreshmap.put(userAuth.getUsername(),newRefreshToken);
        jsonObject.put("refreshtoken",newRefreshToken);

        return jsonObject;


    }

}
