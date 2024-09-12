package app.service;

import app.exeption.СustomException;
import app.model.Laptop;
import app.model.Product;
import app.reprository.LaptopRepository;
import app.utils.CreateProduct;
import app.utils.Pagination;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public class ProductService<T extends Product,E extends JpaRepository> {
    private T obj;
    private E repository;
    private String path = "D:/springapp/src/main/resources/img/%s/%s";
    private String host = "http://localhost:8080/img/%s/%s";
    public ProductService(T obj,E repository){
        this.obj = obj;
        this.repository = repository;
    }
    public ProductService(E repository){

        this.repository = repository;
    }


    public JSONObject addProduct(String name, String description, String price, MultipartFile img,String catalog) throws СustomException, IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if(name==null || description==null || price==null || img==null){
            throw new СustomException("Поля name, description, price обязательны к заполнению",HttpStatus.BAD_REQUEST);
        }


        try{
            obj.setName(name);
            obj.setDescription(description);
            obj.setPrice(Integer.valueOf(price));

            obj.setHrefimg(String.format(host,catalog,img.getOriginalFilename()));
            repository.save(obj);

        }
        catch (Exception e){
            throw new СustomException(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        try(FileOutputStream fileOutputStream = new FileOutputStream(String.format(path,catalog,img.getOriginalFilename()))){

            fileOutputStream.write(img.getBytes());
        }

        jsonObject.put("id",obj.getId());
        jsonObject.put("name",obj.getName());
        jsonObject.put("description",obj.getDescription());
        jsonObject.put("price",obj.getPrice());
        jsonObject.put("hrefimg",obj.getHrefimg());
        jsonArray.put(jsonObject);

        return jsonObject;


    }
    public JSONObject getProduct(Integer id) throws JSONException, СustomException {
        JSONObject jsonObject = new JSONObject();
        if(!repository.existsById(id)){
            throw new СustomException("Пользователь не найден в бд", HttpStatus.NOT_FOUND);
        }
        Optional<T> date = repository.findById(id);

        jsonObject.put("id",date.get().getId());
        jsonObject.put("name",date.get().getName());
        jsonObject.put("description",date.get().getDescription());
        jsonObject.put("price",date.get().getPrice());
        jsonObject.put("hrefimg",date.get().getHrefimg());
        return jsonObject;

    }

    public JSONArray getProducts() throws JSONException {

        JSONArray jsonArray = new JSONArray();
        List<T> date = repository.findAll();
        for (int k = 0;k<date.size();k++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",date.get(k).getId());
            jsonObject.put("name",date.get(k).getName());
            jsonObject.put("description",date.get(k).getDescription());
            jsonObject.put("price",date.get(k).getPrice());
            jsonObject.put("hrefimg",date.get(k).getHrefimg());
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    public HashMap getPaginations() throws JSONException {

        List<T> data = repository.findAll();
        return Pagination.getPagination(data);


    }

    public JSONArray getProducts(String str) throws JSONException {

        String[] st = str.substring(7).
                replace("[","").
                replace("]","").
                replace("}","").split(",");

        ArrayList<Integer> arrayList = new ArrayList();
        for (int k = 0;k<st.length;k++){
            arrayList.add(Integer.valueOf(st[k]));
        }
        List<T> array = repository.findAllById(arrayList);
        JSONArray jsonArray = new JSONArray();
        for(int i = 0;i<array.size();i++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",array.get(i).getId());
            jsonObject.put("name",array.get(i).getName());
            jsonObject.put("description",array.get(i).getDescription());
            jsonObject.put("price",array.get(i).getPrice());
            jsonObject.put("hrefimg",array.get(i).getHrefimg());
            jsonArray.put(i,jsonObject);
        }
        return jsonArray;

    }
}
