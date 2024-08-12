package app.controller;


import app.model.JwtResponse;
import app.model.UserAuth;
import app.service.JwtService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    public JwtService jwtService;
    @Autowired
    public AuthController(JwtService jwtService){
        this.jwtService = jwtService;
    }

    @PostMapping(value = "login",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody UserAuth userAuth) throws JSONException {
        JSONObject token = jwtService.getAlltoken(userAuth);
        return ResponseEntity.ok(String.valueOf(token));
    }

    @PostMapping(value = "accesstoken",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAccessToken(@RequestBody JwtResponse jwtResponse) throws JSONException {
        System.out.println(jwtResponse.getAccessToken());
        ResponseEntity<String> accesstoken = jwtService.getAccessToken(jwtResponse);
        return accesstoken;
    }

    @PostMapping(value = "refreshtoken",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRefreshToken(@RequestBody JwtResponse jwtResponse) throws JSONException {
        ResponseEntity<String> refreshtoken = jwtService.getRefreshToken(jwtResponse);
        return refreshtoken;
    }
}
