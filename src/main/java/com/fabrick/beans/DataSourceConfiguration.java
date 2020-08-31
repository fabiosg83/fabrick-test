package com.fabrick.beans;

import java.util.Properties;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author fabio.sgroi
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfiguration {

    @Value("${tomcat.pool.max-idle}")
    private int maxIdle;

    @Value("${tomcat.pool.max-active}")
    private int maxActive;

    @Value("${tomcat.pool.min-idle}")
    private int minIdle;

    @Value("${tomcat.pool.initial-size}")
    private int initialSize;

    @Value("${tomcat.pool.test-on-borrow}")
    private boolean testOnBorrow;

    @Value("${tomcat.pool.test-while-idle}")
    private boolean testWhileIdle;

    @Value("${tomcat.pool.test-on-connect}")
    private boolean testOnConnect;

    @Value("${tomcat.pool.username}")
    private String username;

    @Value("${tomcat.pool.password}")
    private String password;

    @Value("${tomcat.pool.url}")
    private String url;

    @Value("${tomcat.pool.driver-class-name}")
    private String driverClassName;

    @Value("${tomcat.pool.validation-query}")
    private String validationQuery;

    @Value("${tomcat.pool.validation-interval}")
    private long validationInterval;

    @Value("${spring.jpa.show-sql}")
    private String showSql;

    @Value("${spring.jpa.format-sql}")
    private String formatSql;

    @Value("${spring.jpa.log-sql-type}")
    private String logSqlType;

    @Bean
    @Primary
    public DataSource dataSource() {
        DataSource dataSource = new DataSource();

        // Configurazione di base
        dataSource.setUrl(this.url);
        dataSource.setPassword(this.password);
        dataSource.setUsername(this.username);
        dataSource.setDriverClassName(this.driverClassName);
        // Validazione, si tratta della cosa più importante
        dataSource.setValidationQuery(this.validationQuery);
        dataSource.setValidationInterval(this.validationInterval);
        // Controllo sulle dimensioni
        dataSource.setInitialSize(this.initialSize);
        dataSource.setMaxIdle(this.maxIdle);
        dataSource.setMinIdle(this.minIdle);
        dataSource.setMaxActive(this.maxActive);
        // Test sul funzionamento delle connessioni
        dataSource.setTestOnBorrow(this.testOnBorrow);
        dataSource.setTestWhileIdle(this.testWhileIdle);
        dataSource.setTestOnConnect(this.testOnConnect);

        return dataSource;
    }

    private Properties hibernateProps() {
        Properties p = new Properties();
        // p.setProperty("hibernate.current_session_context_class", "thread");
        p.setProperty("hibernate.show_sql", this.showSql);
        //p.setProperty("hibernate.use_sql_comments", "true");
        p.setProperty("hibernate.format_sql", this.formatSql);
        p.setProperty("org.hibernate.type", this.logSqlType);
        // spring.jpa.properties.hibernate.use_sql_comments=true
        // spring.jpa.properties.hibernate.format_sql=true

        p.setProperty("hibernate.transaction.auto_close_s ession", "false");
        // p.setProperty("hibernate.current_session_context_class", "thread");

        // p.setProperty("hibernate.connection.CharSet", "utf-8");
        // p.setProperty("hibernate.connection.useUnicode", "true");
        // p.setProperty("hibernate.connection.characterEncoding", "utf-8");
        return p;
    }

    @Bean(name = "entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory() {
        // String[] packagesToScan = strPackagesToScan.split("\\|");
        String[] packagesToScan = {};

        // <editor-fold defaultstate="collapsed" desc="PACKAGE_MANAGE">
        String strItems = "com.fabrick.entity|";

        if (!strItems.equals("")) {
            strItems = strItems.substring(0, strItems.length() - 1);
            packagesToScan = strItems.split("\\|");
        }
        // </editor-fold>

        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(packagesToScan);
        sessionFactory.setHibernateProperties(hibernateProps());
        return sessionFactory;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);

        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
