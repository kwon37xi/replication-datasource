package kr.pe.kwonnam.lazyreplicationdatasourceproxy;

import kr.pe.kwonnam.lazyreplicationdatasourceproxy.config.LazyReplicationDataSourceProxyApplicationConfig;
import kr.pe.kwonnam.lazyreplicationdatasourceproxy.jpa.User;
import kr.pe.kwonnam.lazyreplicationdatasourceproxy.jpa.UserOuterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LazyReplicationDataSourceProxyApplicationConfig.class)
public class ReplicationDataSourceIntegrationTest {

    private Logger log = LoggerFactory.getLogger(ReplicationDataSourceIntegrationTest.class);

    @Autowired
    private UserOuterService userOuterService;

    @Test
    public void findByIdRead() throws Exception {
        User user = userOuterService.findByIdRead(1);
        log.info("findByIdRead : {}", user);

        assertThat(user.getName()).as("readOnly=true Transaction").isEqualTo("read_1");
    }

    @Test
    public void findByIdWrite() throws Exception {
        User user = userOuterService.findByIdWrite(3);
        log.info("findByIdWrite : {}", user);

        assertThat(user.getName()).as("readOnly=false Transaction").isEqualTo("write_3");
    }
}
