package learn.rabbmitmq.demo;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: zhenghailong
 * @date: 2019/11/1 10:18
 * @modified By:
 * @description:
 */
public class RabbitProducer {

    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "127.0.0.1";
    // rabbitMQ服务端默认端口号为5672
    private static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(IP_ADDRESS);
        connectionFactory.setPort(PORT);
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root123");
        // 创建连接
        Connection connection = connectionFactory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        /**
         * 创建一个type = "direct"、持久化的、非自动删除的交换机
         * exchange：交换器的名称
         * type:交换器的类型，如：fanout,direct,topic,header等
         */
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true,false,null);
        // 创建一个持久化、非排他的、非自动删除的队列
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        // 将交换器与队列通过路由键绑定
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);
        // 发送一条持久化的消息：hello world!
        String message = "Hello World!";

        channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes());

//        channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY,
//                new AMQP.BasicProperties().builder().contentType("text/plain").deliveryMode(2).priority(1).build(),message.getBytes());
        channel.close();
        connection.close();
    }
}
