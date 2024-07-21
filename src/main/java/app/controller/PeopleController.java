package app.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import app.model.People;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import app.reprository.UserRepository;
import app.service.PeopleService;
import app.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class PeopleController {


    public UserRepository userRepository;
    public PasswordEncoder bCryptPasswordEncoder;
    public PeopleService peopleService;

    @Autowired

    public PeopleController(
                          UserRepository userRepository,
                          PasswordEncoder bCryptPasswordEncoder,
                          PeopleService peopleService){

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.peopleService = peopleService;

    }


    @GetMapping(value = "/user/{snils}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUser(@PathVariable String snils) throws JSONException {
        return peopleService.getUser(snils);
    }



    @GetMapping(value = "/users",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPeopleAll(HttpServletRequest request) throws JSONException, IOException, ServletException {
        JSONObject jsonObject = new JSONObject();
        try{
            if(!request.getHeader("Content-Length").equals(0)){
                jsonObject.put("Error","В методе GET запрещено передавать параметры в body");
                return new ResponseEntity<>(String.valueOf(jsonObject),HttpStatus.BAD_REQUEST);
            }

        }
        catch (NullPointerException e){
            e.getMessage();
        }
        return peopleService.getUserAll();
    }

    @PostMapping(value = "/user",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@RequestBody People people) throws JSONException {


        return peopleService.addUser(people);


    }

    @PutMapping(value = "/user",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUser (@RequestBody People people) throws JSONException {

        return peopleService.editUser(people);

    }

    @DeleteMapping(value = "/user",produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<String> deleteUser(@RequestBody People people) throws JSONException {

        return peopleService.deleteUser(people);


    }





}
