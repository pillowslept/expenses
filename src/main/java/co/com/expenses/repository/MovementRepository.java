package co.com.expenses.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.expenses.model.Movement;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    List<Movement> findAllByOrderByCreationDateAsc();

    List<Movement> findByCreationDateBetween(Date startDate, Date endDate);
}
