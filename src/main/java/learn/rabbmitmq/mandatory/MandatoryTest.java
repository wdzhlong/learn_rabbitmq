package learn.rabbmitmq.mandatory;

import com.rabbitmq.client.*;
import learn.rabbmitmq.connection.ConnectionRabbit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: zhenghailong
 * @date: 2019/11/6 14:01
 * @modified By:
 * @description:
 */
public class MandatoryTest {

    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionRabbit.getConnection();
        // 创建信道
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true,false,null);

        channel.queueDeclare(QUEUE_NAME,true,false,false,null);

        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);

        String message = "Hello World!";
        /**
         * 当mandatory参数设为true时，交换器无法根据自身的类型和路由键找到一个符合条件的队列，那么
         * RabbitMQ会调用Basic.Return命令将消息返回给生产者。当mandatory设置为false时，出现上述
         * 情形，则消息直接被丢弃
         */
        channel.basicPublish(EXCHANGE_NAME,"",true,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes());
        // 添加ReturnListener监听器
        channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> {
            String message1 = new String(body);
            System.out.println("Basic.Return返回的结果是："+ message1);
        });
    }
}
