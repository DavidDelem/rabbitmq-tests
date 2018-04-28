package simplequeue;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class Send {

    private final static String QUEUE_NAME = "test";

    public static void main(String[] args) throws Exception {

        // initialisation of a local connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // declaration of the queue
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // publication of 10 messages into the queue every second
        for(int i=0; i<10; i++) {
            String message = "Bonjour, message numéro" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println("Sent '" + message + "'");
            Thread.sleep(1000);
        }

        // closing the connection
        channel.close();
        connection.close();
    }
}
