package app.utils;

import app.model.Laptop;
import app.model.People;
import app.model.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Pagination {

    public static HashMap getPagination(List<? extends Product> data) throws JSONException {

        int limit = 24;
        int pageAll = data.size()/limit;
        int modul = data.size() % limit;
        HashMap<Integer, ArrayList> paginatPage = new HashMap<>();

        ArrayList arrayID;
        int index = 0;
        int id = 0;
        for(int i = 1;i<pageAll+1;i++){
            arrayID = new ArrayList();
            for(int k = id;k<limit;k++){
                //arrayID.add(index,values.getJSONObject(k).get("id"));
                arrayID.add(index,data.get(k).getId());
                index +=1;
                id +=1;
            }
            paginatPage.put(i,arrayID);
            index = 0;
            limit += id;

        }
        if(modul>0){
            arrayID = new ArrayList();
            for (int i = id;i<data.size();i++){
                arrayID.add(index,data.get(i).getId());
                index +=1;
            }
            paginatPage.put(pageAll+1,arrayID);
        }
        return paginatPage;
}}
