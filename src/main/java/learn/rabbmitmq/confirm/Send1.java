package learn.rabbmitmq.confirm;

import learn.rabbmitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send1 {

    private static final String QUEUE_NAME = "test_queue_confirm1";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        String msg = "hello confirm";

        // 生产者调confirmSelect 将channel设置为confirm模式，注意队列必须是一个新的队列
        channel.confirmSelect();

        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());

        if (!channel.waitForConfirms()){
            System.out.println("send message fail!");
        }else{
            System.out.println("send message success");
        }

        channel.close();

        connection.close();
    }
}
