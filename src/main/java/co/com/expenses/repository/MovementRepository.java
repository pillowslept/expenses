package co.com.expenses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.expenses.model.Movement;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

}
