package co.com.expenses.specifications;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import co.com.expenses.model.Movement;

public class MovementFilterSpecification {

    private MovementFilterSpecification() {
    }

    public static Specification<Movement> withFilter(Object object, String columName) {
        return object == null ? null : (root, query, cb) -> cb.equal(root.get(columName), object);
    }

    public static Specification<Movement> withFilterBetween(Date startAt, Date endAt, String columName) {
        return (startAt == null || endAt == null) ? null
                : (root, query, cb) -> cb.between(root.get(columName), startAt, endAt);
    }

}
