package com.technohub.demo.config.alumni;

import com.technohub.alumni.model.Person;
import com.technohub.demo.config.alumni.MultiTenantDvdRentalProperties.DataSourceProperties;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableConfigurationProperties({ MultiTenantDvdRentalProperties.class, JpaProperties.class })
// @ImportResource(locations = { "classpath:applicationContent.xml" }) // or @EnableJpaRepositories
@EnableJpaRepositories(basePackages = { "com.technohub.alumni.dao" }, transactionManagerRef = "txManager")
@EnableTransactionManagement
public class MultiTenantJpaConfiguration {

	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	private MultiTenantDvdRentalProperties multiTenantDvdRentalProperties;

	@Bean(name = "dataSourcesDvdRental" )
	public Map<String, DataSource> dataSourcesDvdRental() {
		Map<String, DataSource> result = new HashMap<>();
		for (DataSourceProperties dsProperties : this.multiTenantDvdRentalProperties.getDataSources()) {
			DataSourceBuilder factory = DataSourceBuilder
				.create()
				.url(dsProperties.getUrl())
				.username(dsProperties.getUsername())
				.password(dsProperties.getPassword())
				.driverClassName(dsProperties.getDriverClassName());
			result.put(dsProperties.getTenantId(), factory.build());
		}
		return result;
	}

	@Bean
	public MultiTenantConnectionProvider multiTenantConnectionProvider() {
		// Autowires dataSourcesDvdRental
		return new DvdRentalDataSourceMultiTenantConnectionProviderImpl();
	}

	@Bean
	public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
		return new TenantDvdRentalIdentifierResolverImpl();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(MultiTenantConnectionProvider multiTenantConnectionProvider,
		CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

		Map<String, Object> hibernateProps = new LinkedHashMap<>();
		hibernateProps.putAll(this.jpaProperties.getProperties());
		hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
		hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
		hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);

		// No dataSource is set to resulting entityManagerFactoryBean
		LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
		result.setPackagesToScan(new String[] { Person.class.getPackage().getName() });
		result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		result.setJpaPropertyMap(hibernateProps);

		return result;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
		return entityManagerFactoryBean.getObject();
	}

	@Bean
	public PlatformTransactionManager txManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}
}