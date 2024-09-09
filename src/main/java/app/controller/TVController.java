package app.controller;


import app.exeption.СustomException;
import app.service.LaptopService;
import app.service.TVService;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("tv")
public class TVController {
    private TVService tvService;
    @Autowired
    public TVController(TVService tvService){
        this.tvService = tvService;

    }

    @PostMapping(value = "create",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createProduct(@RequestParam String name, String description, String price, MultipartFile img) throws JSONException, СustomException, IOException {
        JSONObject jsonObject = tvService.addProduct(name,description,price,img);
        return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.OK);
    }
    @GetMapping(value = "product/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProduct(@PathVariable String id) throws JSONException, СustomException {
        JSONObject jsonObject = tvService.getProduct(Integer.valueOf(id));

        return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.OK);
    }

    @GetMapping(value = "products",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProducts(HttpServletResponse response) throws JSONException, СustomException, InterruptedException {

        JSONArray jsonObject = tvService.getProducts();
        return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.OK);
    }

    @GetMapping(value = "pagination",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap> getPagination(HttpServletResponse response) throws JSONException, СustomException {

        HashMap<Integer, ArrayList> pages = tvService.getPaginations();
        HttpHeaders headers = new HttpHeaders();
        headers.set("size",String.valueOf(pages.size()));

        return new ResponseEntity<>(pages, headers,HttpStatus.OK);
    }

    @PostMapping(value = "ids",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPag(@RequestBody String ids) throws JSONException, СustomException, InterruptedException {

        JSONArray jsonArray = tvService.getProducts(ids);
        return new ResponseEntity<>(String.valueOf(jsonArray), HttpStatus.OK);
    }

}
