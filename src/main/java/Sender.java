import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by dharada on 2016/05/21.
 */
public class Sender {

    // URL of the JMS server
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;


    //This is the queue name bound in JNDI, that messages need to be sent to
    public static final String SEND_QUEUE = "queue/requestQueue";


    public void exe() throws JMSException {

        // Getting JMS connection from the server
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Create a Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        Queue destination = session.createQueue(SEND_QUEUE);

        // Create a MessageProducer from the Session to the Topic or Queue
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        // Create a messages
        String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
        TextMessage message = session.createTextMessage(text);

        // Tell the producer to send the message
        System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
        producer.send(message);

        // Clean up
        session.close();
        connection.close();
    }

    public static void main(String[] args) throws JMSException {

        new Sender().exe();
        new Receiver().exe();

    }

}
