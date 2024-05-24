package com.ems.service;

import com.ems.model.TestEntity;
import com.ems.repository.TestEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class TestEntityService {

    private final TestEntityRepository repository;

    @Autowired
    public TestEntityService(TestEntityRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        TestEntity entity = new TestEntity();
        entity.setName("Test Name");
        repository.save(entity);
    }
}
