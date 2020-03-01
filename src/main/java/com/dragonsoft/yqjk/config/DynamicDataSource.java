package com.dragonsoft.yqjk.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 多数据源配置
 * @author ronin
 */
public class DynamicDataSource {

    /**
     * jqjk数据源：
     */
    @Configuration
    @MapperScan(basePackages = "com.dragonsoft.yqjk.dao.yqjk",
            sqlSessionFactoryRef = "yqjkSqlSessionFactory",
            sqlSessionTemplateRef = "yqjkSqlSessionTemplate")
    public static class YqjkDataSource {
        @Primary
        @Bean("yqjkDataSource")
        @Qualifier("yqjkDataSource")
        @ConfigurationProperties(prefix = "spring.datasource.yqjk")
        public DataSource dataSource() {
            return new DruidDataSource();
        }

        @Primary
        @Bean("yqjkSqlSessionFactory")
        @Qualifier("yqjkSqlSessionFactory")
        public SqlSessionFactory sqlSessionFactory(@Qualifier("yqjkDataSource") DataSource dataSource) throws Exception {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            factoryBean.setDataSource(dataSource);
            factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/yqjk/*.xml"));
            return factoryBean.getObject();
        }

        @Primary
        @Bean("yqjkTransactionManager")
        @Qualifier("yqjkTransactionManager")
        public DataSourceTransactionManager transactionManager(@Qualifier("yqjkDataSource") DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }

        @Primary
        @Bean("yqjkSqlSessionTemplate")
        @Qualifier("yqjkSqlSessionTemplate")
        public SqlSessionTemplate sqlSessionTemplate(@Qualifier("yqjkSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }

    /**
     * dids数据源
     */
    @Configuration
    @MapperScan(basePackages = "com.dragonsoft.yqjk.dao.dids",
            sqlSessionFactoryRef = "didsSqlSessionFactory",
            sqlSessionTemplateRef = "didsSqlSessionTemplate")
    public static class DidsDataSource {
        @Bean("didsDataSource")
        @Qualifier("didsDataSource")
        @ConfigurationProperties(prefix = "spring.datasource.dids")
        public DataSource dataSource() {
            return new DruidDataSource();
        }

        @Bean("didsSqlSessionFactory")
        @Qualifier("didsSqlSessionFactory")
        public SqlSessionFactory sqlSessionFactory(@Qualifier("didsDataSource") DataSource dataSource) throws Exception {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            factoryBean.setDataSource(dataSource);
            factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/dids/*.xml"));
            return factoryBean.getObject();
        }

        @Bean("didsTransactionManager")
        @Qualifier("didsTransactionManager")
        public DataSourceTransactionManager transactionManager(@Qualifier("didsDataSource") DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }

        @Bean("didsSqlSessionTemplate")
        @Qualifier("didsSqlSessionTemplate")
        public SqlSessionTemplate sqlSessionTemplate(@Qualifier("didsSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }

}
