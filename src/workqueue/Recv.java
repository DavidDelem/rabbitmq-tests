package workqueue;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Recv {

    private final static String QUEUE_NAME = "durable_queue";

    public static void main(String[] argv) throws Exception {

        // initialisation of a local connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // declaration of the queue
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        channel.basicQos(1);
        System.out.println("[INFO] Waiting for messages.");

        // Declaration of a consumer to handle messages
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received '" + message + "'");

                try {
                    doWork(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Done");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }

            }
        };
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch: task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}