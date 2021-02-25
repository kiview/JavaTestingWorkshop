import com.rabbitmq.client.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComplexRabbitMQIntegrationTest extends AbstractRabbitMQTest {

    static String lastMessage = "";

    @Test
    void testRabbitMQStartup() {
        assertTrue(rabbitMQContainer.isRunning());
    }

    @Test
    void sendTest() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(rabbitMQContainer.getAmqpUrl());
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
        ) {
            channel.basicPublish("", "", null, "Bla".getBytes(StandardCharsets.UTF_8));
            DefaultConsumer consumer = initConsumer(channel);
            channel.basicConsume("", consumer);
            await().untilAsserted(() -> {
                assertEquals("Bla", lastMessage);
            });
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void getFizzBuzzTest() throws SQLException {
        java.sql.Connection connection = DriverManager.getConnection(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
        FizzBuzzService fizzBuzzService = new FizzBuzzService(connection, null);
        String result = fizzBuzzService.translateFizzBuzz(1);
        assertEquals("1",result);
    }


    @Test
    void receiveFizzBuzzResultTest() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException, SQLException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(rabbitMQContainer.getAmqpUrl());
        try (
                Connection connection = factory.newConnection();
                Channel sendingChannel = connection.createChannel(1);
                Channel receivingChannel = connection.createChannel(2);
        ) {
            java.sql.Connection dbConnection = DriverManager.getConnection(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
            FizzBuzzService fizzBuzzService = new FizzBuzzService(dbConnection,connection);
            fizzBuzzService.listen();
            sendingChannel.queueDeclare("fizzBuzzRequestQueue", true, false, false, null);

            receivingChannel.queueDeclare("result", true, false, false, null);

            sendingChannel.basicPublish("", "fizzBuzzRequestQueue", null, "1".getBytes(StandardCharsets.UTF_8));
            DefaultConsumer consumer = initConsumer(receivingChannel);
            receivingChannel.basicConsume("result", consumer);

            await().untilAsserted(() -> {
                assertEquals("1", lastMessage);
            });
        }
    }

    private DefaultConsumer initConsumer(Channel channel) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                lastMessage = new String(body, StandardCharsets.UTF_8);
            }

        };
    }


}
