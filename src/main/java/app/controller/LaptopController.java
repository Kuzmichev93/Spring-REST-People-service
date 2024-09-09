package app.controller;

import app.exeption.СustomException;
import app.service.LaptopService;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("laptop")
public class LaptopController {
    private LaptopService laptopService;

    @Autowired
    public LaptopController(LaptopService laptopService){
        this.laptopService = laptopService;

    }
    
    @PostMapping(value = "create",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createProduct(@RequestParam String name,String description,String price,MultipartFile img) throws JSONException, СustomException, IOException {
        JSONObject jsonObject = laptopService.addProduct(name,description,price,img);

        return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.OK);
    }
    @GetMapping(value = "product/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProduct(@PathVariable String id) throws JSONException, СustomException {
        JSONObject jsonObject = laptopService.getProduct(Integer.valueOf(id));

        return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.OK);
    }

    @GetMapping(value = "products",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProducts(HttpServletResponse response) throws JSONException, СustomException, InterruptedException {

        JSONArray jsonObject = laptopService.getProducts();
        return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.OK);
    }

    @GetMapping(value = "pagination",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap> getPagination(HttpServletResponse response) throws JSONException, СustomException {

        HashMap<Integer, ArrayList> pages = laptopService.getPaginations();
        HttpHeaders headers = new HttpHeaders();
        headers.set("size",String.valueOf(pages.size()));

        return new ResponseEntity<>(pages, headers,HttpStatus.OK);
    }

    @PostMapping(value = "ids",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPag(@RequestBody String ids) throws JSONException, СustomException, InterruptedException {

        JSONArray jsonArray = laptopService.getProducts(ids);
        return new ResponseEntity<>(String.valueOf(jsonArray), HttpStatus.OK);
    }


}
