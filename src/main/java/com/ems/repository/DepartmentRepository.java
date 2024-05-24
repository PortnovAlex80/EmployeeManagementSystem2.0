package com.ems.repository;

import com.ems.model.Department;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.MappingProjection;
import com.querydsl.sql.SQLQueryFactory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import static com.querydsl.QDepartment.department;

@Repository
public class DepartmentRepository {

    private final SQLQueryFactory queryFactory;
    private final MappingProjection<Department> projection;

    @Autowired
    public DepartmentRepository(SQLQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
        this.projection = new MappingProjection<Department>(Department.class, department.id, department.name) {
            @Override
            protected Department map(Tuple tuple) {
                return Department.builder()
                        .id(tuple.get(department.id))
                        .name(tuple.get(department.name))
                        .build();
            }
        };
    }

    @Transactional
    public List<Department> findAll() {
        return queryFactory.select(projection)
                .from(department)
                .fetch();
    }

    @Transactional
    public Department save(Department departmentEntity) {
        long id = queryFactory.insert(department)
                .set(department.name, departmentEntity.getName())
                .executeWithKey(department.id);
        departmentEntity.setId(id);
        return departmentEntity;
    }
}
