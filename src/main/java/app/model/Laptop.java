package app.model;

import jakarta.persistence.*;

@Entity
@Table(name="laptop",schema = "public")
public class Laptop implements Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private String hrefimg;

    public void setId(int id){
        this.id = id;
    }
    public Integer getId(){
        return id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }

    public void setPrice(int price){
        this.price = price;
    }
    public Integer getPrice(){
        return price;
    }

    public void setHrefimg(String hrefimg){
        this.hrefimg = hrefimg;
    }
    public String getHrefimg(){
        return hrefimg;
    }
}
