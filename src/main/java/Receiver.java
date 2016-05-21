import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by dharada on 2016/05/21.
 */
public class Receiver implements MessageListener {


    //This is the queue name bound in JNDI, that the message listener will need to listen on
    public static final String REPLY_QUEUE = "queue/replyQueue";
    //This is the queue name bound in JNDI, that messages need to be sent to
    public static final String SEND_QUEUE = "queue/requestQueue";

    // URL of the JMS server
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    public void exe() throws JMSException {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        Queue queue = session.createQueue(REPLY_QUEUE);
        Queue queue = session.createQueue(SEND_QUEUE);

        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(this);

        ExceptionListener lister = new ExceptionListener() {
            @Override
            public void onException(JMSException e) {
                e.printStackTrace();
            }
        };

        connection.setExceptionListener(lister);

        waitForMessage();

        connection.close();

    }

    private void waitForMessage() {

        try {
            // wait for messages
            System.out.print("waiting for messages");
            for (int i = 0; i < 20; i++) {
                Thread.sleep(500);
                System.out.print(".");
            }
            System.out.println();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Name of the topic from which we will receive messages from = " testt"

    public static void main(String[] args) throws JMSException {

        new Receiver().exe();

    }

    @Override
    public void onMessage(Message message) {

        try {

            if (message instanceof TextMessage) {

                TextMessage textMessage = ( TextMessage ) message;
                System.out.println("\nReceived message=" + textMessage.getText() + "'");
            } else {

                System.out.println("not TextMessage");

            }

        } catch (JMSException e) {
            System.out.println("Caught:" + e);
            e.printStackTrace();
        }

    }
}
