package app.service;

import app.exeption.СustomException;
import app.model.Laptop;
import app.reprository.LaptopRepository;
import app.utils.Pagination;
import app.utils.CreateProduct;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class LaptopService {

    @Autowired
    public LaptopRepository laptopRepository;


    public JSONObject addProduct(String name,String description,String price,MultipartFile img) throws СustomException, IOException, JSONException {
        Laptop laptop = new Laptop();
        String catalog = "laptop";
        CreateProduct<Laptop,LaptopRepository> productService = new CreateProduct<>();
        JSONObject jsonObject = productService.createProduct(laptop,laptopRepository,name,description,price,img,catalog);
        return jsonObject;

    }

    public JSONObject getProduct(Integer id) throws JSONException, СustomException {
        JSONObject jsonObject = new JSONObject();
        if(!laptopRepository.existsById(id)){
            throw new СustomException("Пользователь не найден в бд", HttpStatus.NOT_FOUND);
        }
        Optional<Laptop> date = laptopRepository.findById(id);

        jsonObject.put("id",date.get().getId());
        jsonObject.put("name",date.get().getName());
        jsonObject.put("description",date.get().getDescription());
        jsonObject.put("price",date.get().getPrice());
        jsonObject.put("hrefimg",date.get().getHrefimg());
        return jsonObject;

    }

    public JSONArray getProducts() throws JSONException {

        JSONArray jsonArray = new JSONArray();
        List<Laptop> date = laptopRepository.findAll();
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

        List<Laptop> data = laptopRepository.findAll();
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
        List<Laptop> array = laptopRepository.findAllById(arrayList);
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
