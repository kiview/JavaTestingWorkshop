import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FizzBuzzService {
    private final Connection dbConnection;
    private final com.rabbitmq.client.Connection connection;

    public FizzBuzzService(Connection dbConnection, com.rabbitmq.client.Connection connection) {
        this.dbConnection = dbConnection;
        this.connection = connection;
    }

    public void listen() throws IOException {
        Channel receivingChannel = connection.createChannel(3);
        receivingChannel.basicConsume("fizzBuzzRequestQueue",  new DefaultConsumer(receivingChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String job = new String(body, StandardCharsets.UTF_8);
                try {
                    String answer = translateFizzBuzz(Integer.parseInt(job));
                    receivingChannel.basicPublish("", "result", null, answer.getBytes(StandardCharsets.UTF_8));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    public String translateFizzBuzz(int i) throws SQLException {
        ResultSet result = this.dbConnection.createStatement().executeQuery("SELECT fizzbuzz FROM number_to_fizzbuzz WHERE i = " + i);
        result.next();
        return result.getString(1);
    }
}
