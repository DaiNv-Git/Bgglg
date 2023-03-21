package com.example.itspower.controller;

import com.example.itspower.service.GroupRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupRoleController {
    @Autowired
    private GroupRoleService groupRoleService;
    @GetMapping("/groupRole")
    public ResponseEntity<Object> searchAll() {
        return ResponseEntity.status(HttpStatus.OK).body(groupRoleService.searchAll());
    }
}
