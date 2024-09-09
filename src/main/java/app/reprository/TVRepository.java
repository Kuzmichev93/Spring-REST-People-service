package app.reprository;

import app.model.Laptop;
import app.model.TV;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TVRepository extends JpaRepository<TV, Integer> {
}
