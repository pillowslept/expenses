package co.com.expenses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.expenses.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
