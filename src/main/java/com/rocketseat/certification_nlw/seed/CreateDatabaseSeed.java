package com.rocketseat.certification_nlw.seed;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class CreateDatabaseSeed {
    private final JdbcTemplate jdbcTemplate;

    public CreateDatabaseSeed(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public static void main(String[] args) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5434/pg_nlw");
        dataSource.setUsername("admin");
        dataSource.setPassword("admin");

        CreateDatabaseSeed createDatabaseSeed = new CreateDatabaseSeed(dataSource);
        createDatabaseSeed.run();
    }

    public void run(String... args){
        executeSqlFile("src/main/resources/create.sql");
    }

    private void executeSqlFile(String filePath){
        try {
            String sqlScript = new String(Files.readAllBytes(Paths.get(filePath)));
            jdbcTemplate.execute(sqlScript);
            System.out.println("Seed realizado com sucesso");
        } catch (IOException e) {
            System.out.println("ERRO AO EXECUTAR ARQUIVO" + e.getMessage());
        }
    }
    
}
