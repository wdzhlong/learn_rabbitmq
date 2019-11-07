package learn.rabbmitmq.ttl;

import com.rabbitmq.client.*;
import learn.rabbmitmq.connection.ConnectionRabbit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author: zhenghailong
 * @date: 2019/11/7 9:24
 * @modified By:
 * @description:消息的过期时间
 * 对于第一种设置队列TTL属性的方法，一旦消息过期，就会从队列中抹去，而在第二种方法中，即使消息过期，也不会
 * 马上从队列中抹去，因为每条消息是否过期是在即将投递到消费者之前判断的。
 * 第一种方法里，队列中已过期的消息肯定在队列头部，RabbitMQ只要定期从队头开始扫描是否有过期的消息即可。而第
 * 二种方法里，每条消息的过期时间不同，如果要删除所有过期消息势必要扫描整个队列，所以不如等到此消息即将被消
 * 费时再判定是否过期，如果过期再进行删除即可。
 */
public class TtlTest {

    private static final String EXCHANGE_NAME = "ttl_exchange_demo";
    private static final String ROUTING_KEY = "ttl_routingkey_demo";
    private static final String QUEUE_NAME = "ttl_queue_demo";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionRabbit.getConnection();
        // 创建信道
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true,false,null);
        Map<String,Object> arg = new HashMap<>(1);
        // 针对队列设置TTL
        // 单位毫秒
        // 如果不设置TTL，则表示此消息不会过期;如果将消息设置为0，则表示除非此时可以直接将消息投递到消费者，否则
        // 该消息会被立即丢弃
        arg.put("x-message-ttl",6000);
        // 通过参数设置队列消息过期时间
        channel.queueDeclare(QUEUE_NAME,true,false,false,arg);

        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);

        String message = "ttlTestMessage!";
        /**
         * 当mandatory参数设为true时，交换器无法根据自身的类型和路由键找到一个符合条件的队列，那么
         * RabbitMQ会调用Basic.Return命令将消息返回给生产者。当mandatory设置为false时，出现上述
         * 情形，则消息直接被丢弃
         */
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties().builder();
        // 持久化消息
        builder.deliveryMode(2);
        // 设置TTL=60000ms
        builder.expiration("60000");
        AMQP.BasicProperties properties = builder.build();
        // 每条消息单独设置TTL,如果队列和basicPublish都设置，取最小值的那个
        channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY,false,
                properties,
                message.getBytes());

        connection.close();
    }
}
