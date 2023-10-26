package com.hari.cloud.app.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

@RestController
public class HealthCheckManager {
    @Autowired
    DataSource dataSource;
    @GetMapping("/healthz")
    public ResponseEntity checkHealth() {
        try {
             dataSource.getConnection();
        } catch (PSQLException e) {
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        } catch (SQLException e) {
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        } catch (CannotCreateTransactionException e) {
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
