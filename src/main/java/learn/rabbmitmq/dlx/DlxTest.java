package learn.rabbmitmq.dlx;

import com.rabbitmq.client.AMQP;
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
 * @date: 2019/11/7 10:00
 * @modified By:
 * @description:死信队列
 * DLX,全称Dead-Letter-Exchange,可以称之为死信交换器，也有人称之为死信邮箱。当消息在一个队列中变成
 * 死信(dead message)之后，它能被重新被发送到另一个交换器中，这个交换器就是DLX，绑定DLX的队列就称之
 * 为死信队列
 * 消息变成死信一般是由于以下几种情况:
 *      1.消息被拒绝(Basic.Reject/Basic.Nack),并且设置requeue参数为false
 *      2.消息过期
 *      3.队列达到最大长度
 * DLX也是一个正常的交换器，和一般的交换器没有区别，它能在任何的队列上被指定，实际上就是设置某个队列
 * 的属性。当这个队列中存在死信时，RabbitMQ就会自动地将这个消息重新发布到设置的DLX上去，进而被路由到
 * 另一个队列，即死信队列。
 *
 * 通过在channel.queueDeclare方法中设置x-dead-letter-exchange参数来为这个队列添加DLX
 */
public class DlxTest {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionRabbit.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("exchange.dlx","direct",true);
        channel.exchangeDeclare("exchange.normal","fanout",true);
        Map<String,Object> arg = new HashMap<>(3);
        arg.put("x-message-ttl",10000);
        arg.put("x-dead-letter-exchange","exchange.dlx");
        arg.put("x-dead-letter-routing-key","routingkey");
        channel.queueDeclare("queue.normal",true,false,false,arg);
        channel.queueBind("queue.normal","exchange.normal","");
        channel.queueDeclare("queue.dlx",true,false,false,null);
        channel.queueBind("queue.dlx","exchange.dlx","routingkey");
        channel.basicPublish("exchange.normal","rk",
                MessageProperties.PERSISTENT_TEXT_PLAIN,"dlx".getBytes());
        connection.close();
    }
}
