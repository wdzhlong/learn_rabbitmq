package learn.rabbmitmq.routing;

import learn.rabbmitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {

    private static final String EXCHANGE_NAME = "test_exchange_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();
        //exchange
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");//分发

        String msg = "hello direct!";
        // 路由key
        String routingKey = "error";

        channel.basicPublish(EXCHANGE_NAME,routingKey,null,msg.getBytes());

        System.out.println("发送消息："+msg);

        channel.close();

        connection.close();
    }
}
