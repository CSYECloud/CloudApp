package com.hari.cloud.app.health;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        try {
             dataSource.getConnection();
        } catch (PSQLException e) {
            return new ResponseEntity(headers, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (SQLException e) {
            return new ResponseEntity(headers, HttpStatus.SERVICE_UNAVAILABLE);
        }
        return new ResponseEntity(headers, HttpStatus.OK);
    }
}
