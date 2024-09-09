package app.reprository;

import app.model.Laptop;
import app.model.People;
import netscape.javascript.JSObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface LaptopRepository extends JpaRepository<Laptop, Integer> {
    Laptop findByHrefimg(String hrefimg);

    //Laptop[] findAllById(ArrayList<Integer> ids);
}
