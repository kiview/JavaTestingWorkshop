import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractRabbitMQTest {


    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(getDockerImageName())
            .withReuse(true);

    private static DockerImageName getDockerImageName() {
        return DockerImageName.parse("rabbitmq:3.8.12");
    }

    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>()
            .withInitScript("init.sql");

    static {
        rabbitMQContainer.start();
        postgreSQLContainer.start();
    }

}