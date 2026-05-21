package ru.waybill.producer.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class ProducerSchemaInitializerConfig {
    private static final String SCHEMA_INITIALIZER_BEAN = "producerSchemaInitializer";
    private static final String ENTITY_MANAGER_FACTORY_BEAN = "entityManagerFactory";

    @Bean
    public static BeanFactoryPostProcessor producerEntityManagerFactoryDependsOnSchema() {
        return new EntityManagerFactoryDependsOnSchemaInitializer();
    }

    @Bean(name = SCHEMA_INITIALIZER_BEAN)
    public InitializingBean producerSchemaInitializer(
            DataSource dataSource,
            @Value("${spring.jpa.properties.hibernate.default_schema:waybill_producer}") String schemaName
    ) {
        return () -> createSchema(dataSource, schemaName);
    }

    private static void createSchema(DataSource dataSource, String schemaName) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE SCHEMA IF NOT EXISTS " + quoteIdentifier(schemaName));
        }
    }

    private static String quoteIdentifier(String identifier) {
        return "\"" + identifier.replace("\"", "\"\"") + "\"";
    }

    private static final class EntityManagerFactoryDependsOnSchemaInitializer implements BeanDefinitionRegistryPostProcessor {
        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            if (!registry.containsBeanDefinition(ENTITY_MANAGER_FACTORY_BEAN)) {
                return;
            }
            BeanDefinition beanDefinition = registry.getBeanDefinition(ENTITY_MANAGER_FACTORY_BEAN);
            beanDefinition.setDependsOn(addDependsOn(beanDefinition.getDependsOn(), SCHEMA_INITIALIZER_BEAN));
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        }

        private String[] addDependsOn(String[] dependsOn, String beanName) {
            if (dependsOn == null || dependsOn.length == 0) {
                return new String[]{beanName};
            }
            String[] updated = new String[dependsOn.length + 1];
            System.arraycopy(dependsOn, 0, updated, 0, dependsOn.length);
            updated[dependsOn.length] = beanName;
            return updated;
        }
    }
}
