package com.ems.repository;

import com.ems.model.Manager;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.MappingProjection;
import com.querydsl.sql.SQLQueryFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.QManager.manager;

@Repository
public class ManagerRepository {

    private final SQLQueryFactory queryFactory;
    private final MappingProjection<Manager> projection;

    public ManagerRepository(SQLQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
        this.projection = new MappingProjection<Manager>(Manager.class, manager.id, manager.name) {
            @Override
            protected Manager map(Tuple tuple) {
                return Manager.builder()
                        .id(tuple.get(manager.id))
                        .name(tuple.get(manager.name))
                        .build();
            }
        };
    }

    @Transactional
    public List<Manager> findAll() {
        return queryFactory.select(projection)
                .from(manager)
                .fetch();
    }

    @Transactional
    public Manager save(Manager managerEntity) {
        long id = queryFactory.insert(manager)
                .set(manager.name, managerEntity.getName())
                .executeWithKey(manager.id);
        managerEntity.setId(id);
        return  managerEntity;
    }
}
