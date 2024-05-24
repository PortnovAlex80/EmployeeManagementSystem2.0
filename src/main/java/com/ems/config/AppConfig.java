package com.ems.config;

import com.ems.repository.DepartmentRepository;
import com.ems.repository.EmployeeRepository;
import com.ems.repository.ManagerRepository;
import com.ems.service.DepartmentService;
import com.ems.service.EmployeeService;
import com.ems.service.ManagerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;
    private final DepartmentRepository departmentRepository;

    public AppConfig(EmployeeRepository employeeRepository, ManagerRepository managerRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.managerRepository = managerRepository;
        this.departmentRepository = departmentRepository;
    }

    @Bean
    public DepartmentService departmentService() {
        return new DepartmentService(departmentRepository);
    }

    @Bean
    public ManagerService managerService() {
        return new ManagerService(managerRepository);
    }

    @Bean
    public EmployeeService employeeService() {
        return new EmployeeService(employeeRepository);
    }
}
