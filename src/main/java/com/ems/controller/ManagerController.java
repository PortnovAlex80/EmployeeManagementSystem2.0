package com.ems.controller;

import com.ems.model.Manager;
import com.ems.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping
    public List<Manager> getAllEmployees() {
        return managerService.getAllManagers();
    }

    @PostMapping
    public Manager createEmployee(@RequestBody Manager manager) {
        return managerService.saveManager(manager);
    }
}