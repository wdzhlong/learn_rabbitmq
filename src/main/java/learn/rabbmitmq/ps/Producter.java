package learn.rabbmitmq.ps;

import learn.rabbmitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布，订阅
 * 向多个消费者传递一个信息
 *
 * 生产者是发送消息的用户应用程序
 * 队列是存储消息的缓冲区。
 * 用户是接收消息的用户应用程序。
 */
public class Producter {

    private static final String EXCHANGE_NAME = "test_exchange_fanout";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();
        //声明交换机
        /**
         * fanout 不处理路由键
         *
         * 只要和交换机绑定的队列都可以收到消息
         */
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");//分发

        String msg = "hello ps";

        channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());

        System.out.println("send :"+msg);

        channel.close();
        connection.close();
    }
}
