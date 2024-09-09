package app.model;

public interface Product {

    Integer getId();

    void setName(String name);
    String getName();

    void setDescription(String description);
    String getDescription();

    void setPrice(int price);
    Integer getPrice();

    void setHrefimg(String hrefimg);
    String getHrefimg();
}
