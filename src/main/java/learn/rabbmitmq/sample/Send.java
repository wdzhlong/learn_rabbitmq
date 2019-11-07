package learn.rabbmitmq.sample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import learn.rabbmitmq.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {

    private static String QUEUE_NAME = "test_sample_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtils.getConnection();
        // 从连接中获取一个通道
        Channel channel = connection.createChannel();
        // 创建队列声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        String msg = "hello world";

        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());

        channel.close();

        connection.close();
    }
}
