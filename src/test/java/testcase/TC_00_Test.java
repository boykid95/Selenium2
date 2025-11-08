package testcase;

import base.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import vn.agest.selenium.pageObjects.GooglePage;

public class TC_00_Test extends BaseTest {

    private static final Logger LOG = LogManager.getLogger(TC_00_Test.class);

    @Test
    public void testOpenGoogle() {
        LOG.info("===== Starting TC_00: Open Google =====");

        GooglePage google = new GooglePage();

        LOG.info("Page title = {}", google.getTitle());
        LOG.info("===== End TC_00 =====");
    }
}
