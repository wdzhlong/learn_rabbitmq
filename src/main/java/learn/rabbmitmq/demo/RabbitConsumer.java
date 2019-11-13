package learn.rabbmitmq.demo;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author: zhenghailong
 * @date: 2019/11/1 10:54
 * @modified By:
 * @description:
 */
public class RabbitConsumer implements Serializable {

    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Address[] addresses = {new Address(IP_ADDRESS, PORT)};
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("root");
        factory.setPassword("root123");
        // 创建连接
        Connection connection = factory.newConnection(addresses);
        // 创建信道
        Channel channel = connection.createChannel();
        // 设置客户端最多接收未被ack的消息的个数
        // 允许限制信道上的消费者所能保持的最大未确认消息的数量
        // 设置为0表示没有上限
        channel.basicQos(64);

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("recv message:" + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        /**
         * queue:队列名称
         * autoAck:设置是否自动确认。建议设置为false,即不自动确认
         * consumerTag:消费者标签，用来区分多个消费者
         * noLocal:设置为true则表示不能将同一个Connection中生产者发送的消息传送给这个Connection中的消费者
         * exclusive:设置是否排他
         * arguments:设置消费者的其他参数
         * callback:设置消费者的回调函数。用来处理RabbitMQ推送过来的消息，比如DefaultConsumer,使得时需要、
         * 客户端重写其中的方法
         */
        channel.basicConsume(QUEUE_NAME,consumer);
        // 等待回调函数执行完毕之后，关闭资源
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();
    }
}
