package learn.rabbmitmq.topic;

import learn.rabbmitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv2 {
    private static final String EXCHANGE_NAME = "test_exchange_topic";
    private static final String QUEUE_NAME = "test_queue_topic_2";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();

        final Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        // 将队列绑定到交换机,error是路由Key
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"goods.#");

        channel.basicQos(1);

        // 定义消费者
        DefaultConsumer consumer = new DefaultConsumer(channel){
            // 消息到达触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("2"+msg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // 手动回执一个消息
                }finally {
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        //监听队列
        boolean autoAck = false;// 自动应答false
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);
    }
}
