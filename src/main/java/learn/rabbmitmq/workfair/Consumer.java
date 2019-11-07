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
public class Consumer {

    private static String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        // 创建频道
        final Channel channel =connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 手动回执
        channel.basicQos(1);//保证一次只分发一个

        // 定义消费者
        DefaultConsumer consumer = new DefaultConsumer(channel){
            // 消息到达触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("1"+msg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }finally {
                    // 手动回执一个消息
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        //监听队列
        //true为关闭，false为打开，一旦完成任务，就发送一个正确的确认
        boolean autoAck = false;// 自动应答false
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);
    }
}
