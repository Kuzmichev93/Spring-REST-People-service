package app.reprository;

import app.model.People;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeopleRepository extends JpaRepository<People, Integer> {
    People findBySnils(Integer snils);

    void deleteBySnils(Integer snils);

    boolean existsBySnils(Integer Snils);
}
