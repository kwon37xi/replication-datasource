package kr.pe.kwonnam.replicationdatasource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LazyReplicationConnectionDataSourceProxyTest {
    @Mock
    private DataSource writeDataSource;

    @Mock
    private DataSource readDataSource;

    @Mock
    private Connection writeConnection;

    @Mock
    private Connection readConnection;

    private LazyReplicationConnectionDataSourceProxy lazyReplicationConnectionDataSourceProxy;

    @Before
    public void setUp() throws Exception {
        given(writeDataSource.getConnection()).willReturn(writeConnection);
        given(readDataSource.getConnection()).willReturn(readConnection);

        lazyReplicationConnectionDataSourceProxy = new LazyReplicationConnectionDataSourceProxy(writeDataSource, readDataSource);
    }

    @Test
    public void shouldGetConnectionGiveProxy() throws Exception {
        Connection connection = lazyReplicationConnectionDataSourceProxy.getConnection();
        assertThat(connection).isNotNull();
        assertThat(connection.getClass().getName()).contains("Proxy");

        verify(writeDataSource, times(1)).getConnection();
        verify(readDataSource, never()).getConnection();
    }
}