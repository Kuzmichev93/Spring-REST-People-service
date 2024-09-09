package app.controller;


import app.exeption.СustomException;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import app.model.UserAuth;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import app.service.UserService;

import java.io.IOException;


@RestController
//@CrossOrigin(origins = "http://localhost:3000",methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS})
public class RegistrationController {
    public UserService userService;

    @Autowired
    public RegistrationController(UserService userService){
        this.userService = userService;
    }

    @PostMapping(value = "/registration",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registUser(HttpServletRequest request, @RequestBody UserAuth userAuth) throws ServletException, JSONException {

        return userService.registerUser(request,userAuth);
    }

    @PostMapping(value = "/auth",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> authUser( HttpServletRequest request, HttpServletResponse response, @RequestBody UserAuth userAuth) throws ServletException, JSONException, СustomException {

        return userService.authUser(request,userAuth);
    }


    @PostMapping(value = "/exit",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> exitUser(HttpServletRequest request, HttpServletResponse response,@RequestBody UserAuth userAuth) throws ServletException, IOException, JSONException {

        return userService.exitUser(request,response,userAuth);
    }


}
