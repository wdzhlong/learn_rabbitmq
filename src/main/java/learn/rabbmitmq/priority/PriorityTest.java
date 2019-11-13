package learn.rabbmitmq.priority;

import com.rabbitmq.client.*;
import learn.rabbmitmq.connection.ConnectionRabbit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author: zhenghailong
 * @date: 2019/11/7 10:30
 * @modified By:
 * @description:优先级队列
 */
public class PriorityTest {

    private static final String EXCHANGE_NAME = "priority-exchange";
    private static final String ROUTING_KEY = "priority_demo";
    private static final String QUEUE_NAME = "priority_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionRabbit.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true,false,null);
        Map<String,Object> arg = new HashMap<>(1);
        // 设置队列最大的优先级为10
        arg.put("x-max-priority",10);
        channel.queueDeclare(QUEUE_NAME,true,false,false,arg);
        // 将交换器与队列通过路由键绑定
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);
        // 发送一条持久化的消息：hello world!
        String message = "priority_message";
        // 设置消息的优先级为5，默认最低为0，最高为队列设置的最大优先级。
        // 优先级高的消息可以被优先消费，这个也是有前提的：如果在消费者的消费速度大于生产者的速度
        // 且Broker中没有消息堆积的情况下，对发送的消息设置优先级也就没有什么实际意义。因为生产者
        // 刚发送完一条消息就被消费者消费了，那么就相当于Broker中至多只有一条消息，对于单条消息来
        // 说优先级是没有意义的
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties().builder();
        builder.priority(5);
        AMQP.BasicProperties properties = builder.build();
        channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY, properties,
                message.getBytes());
        channel.close();
        connection.close();
    }
}
