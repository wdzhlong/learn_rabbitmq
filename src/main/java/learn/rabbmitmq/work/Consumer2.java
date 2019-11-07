package learn.rabbmitmq.work;

import learn.rabbmitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
 * 消费者1和消费者2处理的数据是一样的
 * 消息者1都是奇数
 * 消费者2都是偶数
 * 这种方式叫轮询分发（round-robin）结果就是不管谁忙谁清闲都不会多给一个消息，任务消息总是你一个我一个
 */

public class Consumer2 {

    private static String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        // 创建频道
        Channel channel =connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
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
                }
            }
        };
        //监听队列
        boolean autoAck = true;
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);
    }
}
