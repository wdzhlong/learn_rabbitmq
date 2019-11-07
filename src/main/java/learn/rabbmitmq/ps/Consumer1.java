package learn.rabbmitmq.ps;

import learn.rabbmitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer1 {

    private static final String QUEUE_NAME = "test_queue_fanout_sms";
    private static final String EXCHANGE_NAME = "test_exchange_fanout";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();

        final Channel channel = connection.createChannel();
        // 队列声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 绑定队列到交换机(转发器)
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");

        channel.basicQos(1);//保证一次只分发一个

        // 定义消费者
        DefaultConsumer consumer = new DefaultConsumer(channel){
            // 消息到达触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("1"+msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    // 手动回执一个消息
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        //监听队列
        /*
        * autoAck:消息应答
        * true:自动确认模式，一量rabbitmq将消息分发给消费者，就会从内存中消息，这种情况下如果杀死正在执行的消息者，就会丢失正在处理的消息
        * false:手动模式，如果有一个消费者挂掉，就会交付给其他消费者，rabbitmq支持消息应答，消费者发送一个消息应答，告诉rabbitmq这个消息
        * 我已经处理完成，你可以删除了，然后rabbitmq就删除内存中的消息
        * 消息应答默认是打开的，false
        *
        * 想一想？如果rabbitmq挂了，我们的消息任然会丢失！！！
        * */
        boolean autoAck = false;// 自动应答false
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);
    }
}
