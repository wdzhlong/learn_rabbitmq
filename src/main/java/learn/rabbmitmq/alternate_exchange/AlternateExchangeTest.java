package learn.rabbmitmq.alternate_exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import learn.rabbmitmq.connection.ConnectionRabbit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author: zhenghailong
 * @date: 2019/11/6 14:20
 * @modified By:
 * @description:
 */
public class AlternateExchangeTest {

    private static final String EXCHANGE_NAME = "normalExchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionRabbit.getConnection();
        Channel channel = connection.createChannel();

        Map<String,Object> arg = new HashMap<>(1);
        // 备份交换机myAe
        arg.put("alternate-exchange","myAe");
        // 通过参数arg设置备份交换机为myAe
        channel.exchangeDeclare("normalExchange","direct",true,false,arg);
        channel.exchangeDeclare("myAe","fanout",true,false,null);
        channel.queueDeclare("normalQueue",true,false,false,null);
        channel.queueBind("normalQueue","normalExchange","normalKey");
        channel.queueDeclare("unrouteQueue",true,false,false,null);
        channel.queueBind("unrouteQueue","myAe","");

        String message = "Hello World!";
        /**
         * 备份交换器，英文名称为Alternate Exchange,简称AE，或者更直白地称之为"备胎交换器"。
         * 生产者在发送消息的时候如果不设置mandatory参数，那么消息在未被路由的情况下将会丢失;
         * 如果设置了mandatory参数，那么需要添加ReturnListener的编程逻辑，生产者的代码将变得
         * 复杂。如果既不想复杂化生产者的编程逻辑，又不想消息丢失，那么可以使用备份交换器，这
         * 样可以将未被路由的消息存储在RabbitMQ中，再在需要的时候去处理这些消息。
         *
         * 可以通过在声明交换器(调用channel.exchangeDeclare方法)的时候添加alternate-exchange
         * 参数来实现，也可以通过策略的方式实现。如果两者同时使用，则前者的优先级更高，会覆盖
         * 掉Policy的设置
         */
        channel.basicPublish(EXCHANGE_NAME,"",false,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes());

        connection.close();
    }
}
