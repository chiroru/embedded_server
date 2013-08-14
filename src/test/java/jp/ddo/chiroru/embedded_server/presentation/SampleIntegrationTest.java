package jp.ddo.chiroru.embedded_server.presentation;

import org.junit.ClassRule;
import org.junit.Test;

import jp.ddo.chiroru.embedded_server.util.junit.EmbeddedServerResource;

public class SampleIntegrationTest {

    @ClassRule
    public static EmbeddedServerResource server = new EmbeddedServerResource();

    @Test
    public void serverTest()
            throws Exception {
        Thread.sleep(60000);
    }
}
