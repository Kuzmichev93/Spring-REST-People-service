package app.service;

import app.component.JwtComponent;
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

    public JSONObject getAlltoken(UserAuth userAuth) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        System.out.println(userRepository.existsByUsername(userAuth.getUsername()));
        if(userRepository.existsByUsername(userAuth.getUsername())){
            String accessToken = new JwtComponent().createtAccessToken(userAuth);
            String refreshToken = new JwtComponent().createRefreshToken(userAuth);
            refreshmap.put(userAuth.getUsername(),refreshToken);
            jsonObject.put("accessToken",accessToken);
            jsonObject.put("refreshToken",refreshToken);
            jsonObject.put("header","Bearer");

            return jsonObject;
        }
        jsonObject.put("Error","Пользователя нет в бд");

        return jsonObject;
    }

    public ResponseEntity<String> getAccessToken(JwtResponse jwtResponse) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if(jwtComponent.validateRefreshToken(jwtResponse.getRefreshToken())){
            Claims refreshBody = jwtComponent.getBodyRefreshToken(jwtResponse.getRefreshToken());
            String login = (String) refreshBody.get("login");
            if(jwtResponse.getRefreshToken().equals(refreshmap.get(login))){
                UserAuth userAuth = userRepository.findByUsername(login);
                if(userAuth.getUsername()!=null){
                    String accesstoken = jwtComponent.createtAccessToken(userAuth);

                    jsonObject.put("accesstoken",accesstoken);
                    return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.OK);
                }
                jsonObject.put("Error","Пользователя нет в бд");
                return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.NOT_FOUND);
            }
            jsonObject.put("Error","login в refreshtoken не действителен ");
            return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.BAD_REQUEST);
        }
        jsonObject.put("Error","Время существования refreshtoken закончилось");
        return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<String> getRefreshToken(JwtResponse jwtResponse) throws JSONException {
        String token = jwtResponse.getRefreshToken();

        JSONObject jsonObject = new JSONObject();
        if(jwtComponent.validateRefreshToken(token)){
                Claims tokenBody = jwtComponent.getBodyRefreshToken(token);
                UserAuth userAuth = userRepository.findByUsername((String) tokenBody.get("login"));
                String newRefreshToken = jwtComponent.createRefreshToken(userAuth);
                refreshmap.put(userAuth.getUsername(),newRefreshToken);
                jsonObject.put("refreshtoken",newRefreshToken);
                return new ResponseEntity<>(String.valueOf(jsonObject),HttpStatus.OK);
            }

        jsonObject.put("Error","Данный refreshtoken недействителен");
        return new ResponseEntity<>(String.valueOf(jsonObject),HttpStatus.BAD_REQUEST);
    }

}
