package workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

public class Send {

    private final static String QUEUE_NAME = "durable_queue";

    public static void main(String[] args) throws Exception {

        // initialisation of a local connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        Scanner scanner = new Scanner(System.in);
        boolean continueWork = true;

        // declaration of the queue
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        // publication of 10 messages into the queue every second
        while (continueWork) {
            System.out.println("Please input a message to send. Every . will take a second to process for the receiver.");
            System.out.println("Write Q to quit.");
            String message = scanner.nextLine();
            if(message.equals("Q")) {
                continueWork = false;
            } else {
                channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
                System.out.println("Sent '" + message + "'");
            }
        }

        // closing the connection
        channel.close();
        connection.close();
    }
}
