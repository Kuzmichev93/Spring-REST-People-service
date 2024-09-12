package app.controller;

import app.exeption.СustomException;
import app.model.Laptop;
import app.reprository.LaptopRepository;
import app.service.LaptopService;
import app.service.ProductService;
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
   // private LaptopService laptopService;
    private ProductService<Laptop, LaptopRepository> productService;
    private Laptop laptop;
    @Autowired
    public LaptopRepository laptopRepository;
   /*
    @Autowired
    public LaptopController(LaptopService laptopService){
        this.laptopService = laptopService;

    }*/
    
    @PostMapping(value = "create",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createProduct(@RequestParam String name,String description,String price,MultipartFile img) throws JSONException, СustomException, IOException {

        String catalog = "laptop";
        laptop = new Laptop();
        productService = new ProductService(laptop,laptopRepository);
        JSONObject jsonObject = productService.addProduct(name,description,price,img,catalog);

        return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.OK);
    }
    @GetMapping(value = "product/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProduct(@PathVariable String id) throws JSONException, СustomException {
        productService = new ProductService(laptopRepository);
        JSONObject jsonObject = productService.getProduct(Integer.valueOf(id));

        return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.OK);
    }

    @GetMapping(value = "products",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProducts(HttpServletResponse response) throws JSONException, СustomException, InterruptedException {
        productService = new ProductService(laptopRepository);
        JSONArray jsonObject = productService.getProducts();
        return new ResponseEntity<>(String.valueOf(jsonObject), HttpStatus.OK);
    }

    @GetMapping(value = "pagination",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap> getPagination(HttpServletResponse response) throws JSONException, СustomException {
        productService = new ProductService(laptopRepository);
        HashMap<Integer, ArrayList> pages = productService.getPaginations();
        HttpHeaders headers = new HttpHeaders();
        headers.set("size",String.valueOf(pages.size()));

        return new ResponseEntity<>(pages, headers,HttpStatus.OK);
    }

    @PostMapping(value = "ids",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPag(@RequestBody String ids) throws JSONException, СustomException, InterruptedException {
        productService = new ProductService(laptopRepository);
        JSONArray jsonArray = productService.getProducts(ids);
        return new ResponseEntity<>(String.valueOf(jsonArray), HttpStatus.OK);
    }


}
