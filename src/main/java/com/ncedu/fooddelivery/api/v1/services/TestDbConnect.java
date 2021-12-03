package com.ncedu.fooddelivery.api.v1.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class TestDbConnect {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private final String QUERY = "SELECT current_database() as db";

    public String getDbName() {
        String dbName = "unknown";
        //try with resources помогает самостоятельно закрыть объекты после использования.
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(QUERY)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                dbName = resultSet.getString("db");
            }
            return  dbName;
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
        }
        return  dbName;
    }
}
