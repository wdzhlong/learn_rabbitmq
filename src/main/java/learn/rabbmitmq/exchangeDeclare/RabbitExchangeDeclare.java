package learn.rabbmitmq.exchangeDeclare;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import learn.rabbmitmq.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: zhenghailong
 * @date: 2019/11/1 16:52
 * @modified By:
 * @description:
 */
public class RabbitExchangeDeclare {

    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";

    public static void main(String[] args) throws IOException {

        declareType1();

        declareType2();
    }

    public static void declareType1() throws IOException {
        Connection connection = ConnectionUtils.getConnection();

        // 创建信道
        Channel channel = connection.createChannel();
        /**
         * 创建一个type = "direct"、持久化的、非自动删除的交换机
         * exchange：交换器的名称
         * type:交换器的类型，如：fanout,direct,topic,header等
         * durable:设置是否持久化。durable设置为true表示持久化，反之是非持久化。持久化可以将交换器存盘，
         * 在服务器重启的时候不会丢失相关信息
         * autoDelete:设置是否自动删除。autoDelete设置为true则表示自动删除。自动删除的前提是至少有一个
         * 队列或者交换器与这个交换器绑定，之后所有与这个交换器绑定的队列或者交换器都与此解绑。注意不能
         * 错误地把这个参数理解为：当与此交换器连接的客户端都断开时，RabbitMQ会自动删除本交换器。
         * internal:设置是否是内置的。如果设置为true,则表示是内置的交换器，客户端程序无法直接发送消息到
         * 这个交换器中，只能通过交换器路由到交换器这种方式。
         * argument:其他一些结构化参数，比如alternate-exchange
         */
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true);
        /**
         * 声名为持久化的、非排他的、非自动删除的
         * queue:队列名称
         * durable:设置是否持久化，为true则设置队列为持久化。持久化的队列会存盘，在服务器重启的时候
         * 可以保证不丢失相关信息
         * exclusive:设置是否排他。为true则设置队列为排他的。如果一个队列被声明为排他队列，该队列仅对
         * 首次声明它的连接可见，并在连接断开时自动删除。这里需要注意三点：排他队列是基于连接(Connection)
         * 可见的，同一个连接的不同信道(Channel)是可以同时访问同一连接创建的排他队列;“首次”是指如果一个
         * 连接已经声明了一个排他队列，其他连接是不允许建立同名的排他队列的，这个与普通队列不同;即使该队列
         * 是持久化的，一旦连接关闭或者客户端退出，该排他队列都会被自动删除，这种队列适用于一个客户端同时
         * 发送和读取消息的应该场景。
         * autoDelete:设置是否自动删除。为true则设置队列为自动删除。自动删除的前提是：至少有一个消费者连接
         * 到这个队列，之后所有与这个队列连接的消费者都断开时，才会自动删除。不能把这个参数错误的理解为：当
         * 连接到此队列的所有客户端断开时，这个队列自动删除，因为生产者客户端创建这个队列，或者没有消费者
         * 客户端与这个队列连接时，都不会自动删除这个队列。
         * arguments:设置队列的其他一些参数，如x-message-ttl、x-expires、x-max-length、x-max-length-bytes、
         * x-dead-letter-exchange、x-dead-letter-routing-key、x-max-priority等
         */
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        /**
         * 将队列与交换机绑定
         * queue:队列名称;
         * exchange:交换机的名称
         * routingkey:用来绑定队列和交换机的路由键
         * argument:定义绑定的一些参数
         */
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);
        // 解除绑定队列
        //channel.queueUnbind();
    }

    public static void declareType2() throws IOException {
        Connection connection = ConnectionUtils.getConnection();

        // 创建信道
        Channel channel = connection.createChannel();
        /**
         * 创建一个type = "direct"、持久化的、非自动删除的交换机
         * exchange：交换器的名称
         * type:交换器的类型，如：fanout,direct,topic,header等
         * durable:设置是否持久化。durable设置为true表示持久化，反之是非持久化。持久化可以将交换器存盘，
         * 在服务器重启的时候不会丢失相关信息
         * autoDelete:设置是否自动删除。autoDelete设置为true则表示自动删除。自动删除的前提是至少有一个
         * 队列或者交换器与这个交换器绑定，之后所有与这个交换器绑定的队列或者交换器都与此解绑。注意不能
         * 错误地把这个参数理解为：当与此交换器连接的客户端都断开时，RabbitMQ会自动删除本交换器。
         * internal:设置是否是内置的。如果设置为true,则表示是内置的交换器，客户端程序无法直接发送消息到
         * 这个交换器中，只能通过交换器路由到交换器这种方式。
         * argument:其他一些结构化参数，比如alternate-exchange
         */
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true);
        // 非持久化的、排化的、自动删除的队列（rabbitmq自动生成队列名称）
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName,EXCHANGE_NAME,ROUTING_KEY);
    }
}
