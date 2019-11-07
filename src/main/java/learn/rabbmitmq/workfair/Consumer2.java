package learn.rabbmitmq.workfair;

import learn.rabbmitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者1和消费者2处理的数据是一样的
 * 消息者1都是奇数
 * 消费者2都是偶数
 * 这种方式叫轮询分发（round-robin）结果就是不管谁忙谁清闲都不会多给一个消息，任务消息总是你一个我一个
 *
 * 使用公平分发必须关闭自动应答ack,改成手动
 */
public class Consumer2 {

    private static String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        // 创建频道
        final Channel channel =connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 每个消费者发送确认消息之前，消息队列不发送下一个消息到消费者，一次只处理一个消息
        // 限制发送给同一个的消费者的消息一次只发一条
        channel.basicQos(1);//保证一次只分发一个

        // 定义消费者
        DefaultConsumer consumer = new DefaultConsumer(channel){
            // 消息到达触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("2"+msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // 手动回执一个消息
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }finally {
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
