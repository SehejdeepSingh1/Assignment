package com.example.assignment.config;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration

public class neo4jConfig {

    @Bean

    public Driver driver() {

        System.out.println("Driver Bean Created");

        return GraphDatabase.driver(

                "bolt://localhost:7687",

                AuthTokens.basic("neo4j", "sehejdeep55")

        );

    }

}
