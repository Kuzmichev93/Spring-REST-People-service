package app.service;

import app.model.People;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import app.reprository.PeopleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PeopleService {

    public PeopleRepository peopleRepository;

    public JSONObject jsonObject = new JSONObject();
    public JSONArray jsonArray = new JSONArray();
    @Autowired
    public PeopleService(PeopleRepository peopleRepository){
        this.peopleRepository = peopleRepository;

    }


    public ResponseEntity<String> getUser(String snils) throws JSONException {

        Pattern pat = Pattern.compile("\\D");
        Matcher mat = pat.matcher(snils);
        if(mat.find()){
            jsonObject.put("Error","Снилс содержит не валидные данные");
            return new ResponseEntity<>(String.valueOf(jsonObject),HttpStatus.BAD_REQUEST);
        }
        People people = peopleRepository.findBySnils(Integer.valueOf(snils));
        if(people!=null){
                jsonObject.put("id",people.getId());
                jsonObject.put("name",people.getName_user());
                jsonObject.put("surname",people.getSurname());
                jsonObject.put("city",people.getCity());
                jsonObject.put("city",people.getSnils());
                jsonArray.put(jsonObject);
            return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.OK);
        }
        jsonObject.put("Error","Пользователя с таким снилс нет в бд");
        return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.NOT_FOUND);



    }

    public ResponseEntity<String> getUserAll() throws JSONException {
        int i;
        List<People> data = peopleRepository.findAll();

        JSONArray jsonArray = new JSONArray();
        for(i=0;i<data.size();i++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",data.get(i).getId());
            jsonObject.put("name",data.get(i).getName_user());
            jsonObject.put("surname",data.get(i).getSurname());
            jsonObject.put("city",data.get(i).getCity());
            jsonObject.put("snils",data.get(i).getSnils());
            jsonArray.put(i,jsonObject);

        }
        return new ResponseEntity<>(String.valueOf(jsonArray),HttpStatus.OK);
    }

    public ResponseEntity<String> addUser(People people) throws JSONException {
        peopleRepository.save(people);
        jsonObject.put("Message","Пользователь добавлен в бд");
        return new ResponseEntity<>(String.valueOf(jsonObject),HttpStatus.OK);
    }

    public ResponseEntity<String> editUser(People people) throws JSONException {
        if(peopleRepository.existsBySnils(people.getSnils())){
            People edit = peopleRepository.findBySnils(people.getSnils());
            edit.setName_user(people.getName_user());
            edit.setSurname(people.getSurname());
            edit.setCity(people.getCity());
            peopleRepository.save(edit);

            jsonObject.put("id",edit.getId());
            jsonObject.put("name",edit.getName_user());
            jsonObject.put("surname",edit.getSurname());
            jsonObject.put("city",edit.getCity());
            jsonObject.put("city",edit.getSnils());
            jsonArray.put(jsonObject);



            return new ResponseEntity<>(String.valueOf(jsonArray),HttpStatus.OK);
        }
        jsonObject.put("Error","Пользователя не зарегистрирован в бд");
        return new ResponseEntity<>(String.valueOf(jsonObject),HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteUser(People people) throws JSONException {
        if(peopleRepository.existsBySnils(people.getSnils())){
            peopleRepository.deleteBySnils(people.getSnils());
            jsonObject.put("Message","Пользователь удален из бд");
            return new ResponseEntity<>(String.valueOf(jsonObject),HttpStatus.OK);
        }
        jsonObject.put("Error","Пользователя с таким снилс нет в бд");
        return new ResponseEntity<>(String.valueOf(jsonObject),HttpStatus.NOT_FOUND);
    }
}
