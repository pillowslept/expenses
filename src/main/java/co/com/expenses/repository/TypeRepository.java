package co.com.expenses.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.expenses.model.Type;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

    List<Type> findByState(String state);

    Type findByDescriptionIgnoreCase(String description);

}
