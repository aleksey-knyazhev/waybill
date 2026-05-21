package ru.waybill.consumer.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class LiquibaseSchemaInitializerConfig {
    @Bean
    static LiquibaseSchemaInitializer liquibaseSchemaInitializer(
            @Value("${spring.liquibase.url}") String url,
            @Value("${spring.liquibase.user}") String username,
            @Value("${spring.liquibase.password}") String password,
            @Value("${spring.datasource.driver-class-name}") Class<? extends Driver> driverClass
    ) {
        return new LiquibaseSchemaInitializer(url, username, password, driverClass);
    }

    @Bean
    static BeanFactoryPostProcessor liquibaseDependsOnSchemaInitializer() {
        return beanFactory -> {
            if (!beanFactory.containsBeanDefinition("liquibase")) {
                return;
            }

            BeanDefinition liquibase = beanFactory.getBeanDefinition("liquibase");
            List<String> dependsOn = new ArrayList<>();
            if (liquibase.getDependsOn() != null) {
                dependsOn.addAll(Arrays.asList(liquibase.getDependsOn()));
            }
            if (!dependsOn.contains("liquibaseSchemaInitializer")) {
                dependsOn.add("liquibaseSchemaInitializer");
            }
            liquibase.setDependsOn(dependsOn.toArray(String[]::new));
        };
    }

    static class LiquibaseSchemaInitializer implements BeanFactoryPostProcessor {
        private final String url;
        private final String username;
        private final String password;
        private final Class<? extends Driver> driverClass;

        LiquibaseSchemaInitializer(
                String url,
                String username,
                String password,
                Class<? extends Driver> driverClass
        ) {
            this.url = url;
            this.username = username;
            this.password = password;
            this.driverClass = driverClass;
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
            dataSource.setDriverClass(driverClass);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.execute("CREATE SCHEMA IF NOT EXISTS waybill_consumer");
            } catch (SQLException exception) {
                throw new IllegalStateException("Cannot initialize Liquibase schema", exception);
            }
        }
    }
}

