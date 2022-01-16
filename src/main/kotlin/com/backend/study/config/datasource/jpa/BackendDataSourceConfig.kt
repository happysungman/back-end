package com.backend.study.config.datasource.jpa

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EntityScan("com.backend.study.repository.jpa.entity")
@EnableJpaRepositories("com.backend.study.repository.jpa")
class BackendDataSourceConfig {

    @Bean("backendLeaderHikariConfig")
    @ConfigurationProperties(prefix = "spring.datasource.backend.leader")
    fun leaderHikariConfig(): HikariConfig = HikariConfig()

    @Bean("backendFollowerHikariConfig")
    @ConfigurationProperties(prefix = "spring.datasource.backend.follower")
    fun followerHikariConfig(): HikariConfig = HikariConfig()

    @Bean("backendLeaderDataSource")
    fun leaderDataSource(@Qualifier("backendLeaderHikariConfig") hikariConfig: HikariConfig):
            DataSource = HikariDataSource(hikariConfig)

    @Bean("backendFollowerDataSource")
    fun followerDataSource(@Qualifier("backendFollowerHikariConfig") hikariConfig: HikariConfig):
            DataSource = HikariDataSource(hikariConfig)


    @Bean("backendRoutingDataSource")
    @ConditionalOnBean(name = ["backendLeaderDataSource", "backendFollowerDataSource"])
    fun routingDataSource(
        @Qualifier("backendLeaderDataSource") leaderDataSource: DataSource,
        @Qualifier("backendFollowerDataSource") followerDataSource: DataSource
    ): DataSource = ReplicationRoutingDataSource(leaderDataSource, followerDataSource)

    @Bean("backendLazyConnectionDataSource")
    @ConditionalOnBean(name = ["backendRoutingDataSource"])
    fun lazyConnectionDataSource(@Qualifier("backendRoutingDataSource") dataSource: DataSource):
            DataSource = LazyConnectionDataSourceProxy(dataSource)

//    @Bean("backendTransactionManager")
//    fun transactionManager(@Qualifier("backendLazyConnectionDataSource") dataSource: DataSource):
//            PlatformTransactionManager = DataSourceTransactionManager(dataSource)

    @Bean("entityManagerFactory")
    fun entityManagerFactory(@Qualifier("backendLazyConnectionDataSource") dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val localContainerEntityManagerFactoryBean = LocalContainerEntityManagerFactoryBean()
        localContainerEntityManagerFactoryBean.dataSource = dataSource
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.backend.study.repository.jpa")

        val jpaVendorAdapter: JpaVendorAdapter = HibernateJpaVendorAdapter()
        localContainerEntityManagerFactoryBean.jpaVendorAdapter = jpaVendorAdapter

        return localContainerEntityManagerFactoryBean
    }

    @Bean("backendTransactionManager")
    fun transactionManager (@Qualifier("entityManagerFactory") entityManagerFactory: EntityManagerFactory):
            PlatformTransactionManager = JpaTransactionManager(entityManagerFactory)

    internal class ReplicationRoutingDataSource(leader: DataSource, follower: DataSource): AbstractRoutingDataSource() {

        init {
            super.setTargetDataSources(mapOf(LEADER to leader, FOLLOWER to follower))
            super.setDefaultTargetDataSource(leader)
        }

        override fun determineCurrentLookupKey(): Any =
            if (TransactionSynchronizationManager.isActualTransactionActive()
                && TransactionSynchronizationManager.isCurrentTransactionReadOnly()) FOLLOWER else LEADER

        companion object {
            const val LEADER: String = "leader"
            const val FOLLOWER: String = "follower"
        }

    }
}