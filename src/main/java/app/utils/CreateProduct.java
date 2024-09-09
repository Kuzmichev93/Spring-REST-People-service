package app.utils;

import app.exeption.СustomException;

import app.model.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;



public class CreateProduct<T extends Product, E extends JpaRepository> {
    private String path = "D:/springapp/src/main/resources/img/%s/%s";
    private String host = "http://localhost:8080/img/%s/%s";

    public  JSONObject createProduct(T object,E repository, String name,
                                    String description,
                                    String price,
                                    MultipartFile img,
                                    String catalog) throws СustomException, IOException, JSONException {

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if(name==null || description==null || price==null || img==null){
            throw new СustomException("Поля name, description, price обязательны к заполнению",HttpStatus.BAD_REQUEST);
        }


        try{
            object.setName(name);
            object.setDescription(description);
            object.setPrice(Integer.valueOf(price));

            object.setHrefimg(String.format(host,catalog,img.getOriginalFilename()));
            repository.save(object);

        }
        catch (Exception e){
            throw new СustomException(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        try(FileOutputStream fileOutputStream = new FileOutputStream(String.format(path,catalog,img.getOriginalFilename()))){

            fileOutputStream.write(img.getBytes());
        }

        jsonObject.put("id",object.getId());
        jsonObject.put("name",object.getName());
        jsonObject.put("description",object.getDescription());
        jsonObject.put("price",object.getPrice());
        jsonObject.put("hrefimg",object.getHrefimg());
        jsonArray.put(jsonObject);

        return jsonObject;
    }
}
