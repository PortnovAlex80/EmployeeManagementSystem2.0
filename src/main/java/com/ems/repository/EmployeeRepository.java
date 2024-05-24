package com.ems.repository;

import com.ems.model.Employee;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.MappingProjection;
import com.querydsl.sql.SQLQueryFactory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.QEmployee.employee;

@Repository
public class EmployeeRepository {

    private final SQLQueryFactory queryFactory;
    private final MappingProjection<Employee> projection;


    public EmployeeRepository(SQLQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
        this.projection = new MappingProjection<Employee>(Employee.class, employee.id, employee.name) {
            @Override
            protected Employee map(Tuple tuple) {
                return Employee.builder()
                        .id(tuple.get(employee.id))
                        .name(tuple.get(employee.name))
                        .build();
            }
        };
    }

    @Transactional
    public List<Employee> findAll() {
        return queryFactory.select(projection)
                .from(employee)
                .fetch();
    }

    @Transactional
    public Employee save(Employee employeeEntity) {
        long id = queryFactory.insert(employee)
                .set(employee.name, employeeEntity.getName())
                .executeWithKey(employee.id);
        employeeEntity.setId(id);
        return  employeeEntity;
    }
}
