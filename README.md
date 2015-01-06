# Java (Spring & Non Spring) replication-datasource

When you need database replication, you have to route read/write connections to appropriate databses.

There are two ways of implementing replication datasources in Java environment.
(actually four Database Proxy server like [MySQL Proxy](http://dev.mysql.com/doc/mysql-proxy/en/) or [MaxScale](https://github.com/mariadb-corporation/MaxScale) and [MySql Replication JDBC Driver](http://dev.mysql.com/doc/connector-j/en/connector-j-master-slave-replication-connection.html)).

I introduce two ways the first one is only for Spring framework and the second one is for general java applications.

You can test these two ways with [ReplicationRoutingDataSourceIntegrationTest](https://github.com/kwon37xi/replication-datasource/blob/master/src/test/java/kr/pe/kwonnam/replicationdatasource/ReplicationRoutingDataSourceIntegrationTest.java)
and [ReplicationLazyConnectionDataSourceProxyIntegrationTest](https://github.com/kwon37xi/replication-datasource/blob/master/src/test/java/kr/pe/kwonnam/replicationdatasource/ReplicationLazyConnectionDataSourceProxyIntegrationTest.java).

## Spring's LazyConnectionDataSourceProxy + AbstractRoutingDataSource

Refer to [ReplicationRoutingDataSource](https://github.com/kwon37xi/replication-datasource/blob/master/src/test/java/kr/pe/kwonnam/replicationdatasource/routingdatasource/ReplicationRoutingDataSource.java) and [LazyReplicationDataSourceProxyApplicationConfig](https://github.com/kwon37xi/replication-datasource/blob/master/src/test/java/kr/pe/kwonnam/replicationdatasource/config/LazyReplicationDataSourceProxyApplicationConfig.java).
You can make replication data source with only spring framework's two basic classes.

This works very nicely with Spring's [TransactionSynchronizationManager](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/support/TransactionSynchronizationManager.html).

If you use [Spring framework]() for your application, this is enough for your database replication.

You just need to set `@Transactional(readOnly = true|false)`.

## LazyReplicationConnectionDataSourceProxy

I refered to Spring framework's [LazyConnectionDataSourceProxy](https://github.com/spring-projects/spring-framework/blob/master/spring-jdbc/src/main/java/org/springframework/jdbc/datasource/LazyConnectionDataSourceProxy.java) and modified a little for supporting replication
to make [LazyReplicationConnectionDataSourceProxy](https://github.com/kwon37xi/replication-datasource/blob/master/src/main/java/kr/pe/kwonnam/rezyreplicationdatasourceproxy/LazyReplicationConnectionDataSourceProxy.java).

It's enough to copy & paste [LazyReplicationConnectionDataSourceProxy](https://github.com/kwon37xi/replication-datasource/blob/master/src/main/java/kr/pe/kwonnam/rezyreplicationdatasourceproxy/LazyReplicationConnectionDataSourceProxy.java) to make replication datasource.

This has features of LazyConnectionDataSourceProxy and support database replication(master/slave | read/write) routing.

This also does not depend on Spring framework. So you can use this code with any Java applications.
But you have to remember to call `connection.setReadOnly(true|false)` for replication before executing statements.
And You cannot reuse the connection for different readOnly status, you have close and get again another connection for new statement.

```java
@Bean
public DataSource writeDataSource() {
    DataSource writeDataSource = ...;
    reaturn writeDataSource;
}

@Bean
public DataSource readDataSource() {
    DataSource readDataSource = ...;
    return readDataSource;
}

@Bean
public DataSource dataSource(DataSource writeDataSource, DataSource readDataSource) {
    return new LazyReplicationConnectionDataSourceProxy(writeDataSource, readDataSource);
}
```

when you use with spring framework
```java
// Spring's @Transaction AOP automatically call connection.setReadOnly(true|false).
// But before Spring 4.1.x JPA does not call setReadOnly method. In this situation you'd better use LazyConnectionDataSourceProxy + AbstractRoutingDataSource.
// working with read database
@Transactional(readOnly = true)
public Object readQuery() {
    ....
}

// working with write database
@Transactional(readOnly = false)
public void writeExection() {
    ....
}
```

when you use without spring framwork
```java
Connection readConn = dataSource.getConnection();
readConn.setReadOnly(true);

// ... working with readConn...

readConn.close();

Connection writeConn = dataSource.getConnection();
writeConn.setReadOnly(false);

// ... working with writeConn...
```