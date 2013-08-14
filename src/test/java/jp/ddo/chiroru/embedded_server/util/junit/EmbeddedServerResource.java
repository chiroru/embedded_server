package jp.ddo.chiroru.embedded_server.util.junit;

import java.io.File;

import org.glassfish.embeddable.CommandResult;
import org.glassfish.embeddable.CommandResult.ExitStatus;
import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.junit.rules.ExternalResource;

public class EmbeddedServerResource
extends ExternalResource {

    @Override
    protected void before() throws Throwable {
        try {
            GlassFishProperties glassfishProperties = new GlassFishProperties();
            glassfishProperties.setPort("http-listener", 8080);
            GlassFish glassfish = GlassFishRuntime.bootstrap().newGlassFish(glassfishProperties);
            glassfish.start();
            CommandResult r1 = glassfish.getCommandRunner().run(
                    "create-jdbc-connection-pool",
                    "--datasourceclassname",
                    "oracle.jdbc.pool.OracleDataSource",
                    "--restype",
                    "javax.sql.DataSource",
                    "--property",
                    "user=TEST:password=TEST:url=\"jdbc:oracle:thin:@127.0.0.1:1521:XE\"",
                    "OraclePool");
            if (r1.getExitStatus() != ExitStatus.SUCCESS) {
                throw new RuntimeException("Connection Pool の構成に失敗しました.");
            }
            CommandResult r2 = glassfish.getCommandRunner().run(
                    "create-jdbc-resource",
                    "--connectionpoolid",
                    "OraclePool",
                    "jdbc/oracle");
            if (r2.getExitStatus() != ExitStatus.SUCCESS) {
                throw new RuntimeException("DataSource の構成に失敗しました.");
            }
            File war = new File("target/javaee-rest-sample-1.0-SNAPSHOT.war");
            Deployer deployer = glassfish.getDeployer();
            deployer.deploy(war, "--force=true");
        } catch (GlassFishException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void after() {
        // do nothing.
    }

}
