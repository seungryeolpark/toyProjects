package com.example.toyProject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootTest
public class JDBCTest {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    static {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void connectionTest() {
        try(Connection con = DriverManager.getConnection(
                url,
                username,
                password)) {
            System.out.println(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
