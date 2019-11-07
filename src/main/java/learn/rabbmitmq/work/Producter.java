package learn.rabbmitmq.work;

import learn.rabbmitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producter {

    public static String QUEST_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();
        // 获取channel
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEST_NAME,false,false,false,null);

        for (int i = 0; i < 50; i++){
            String msg = "hello "+i;
            channel.basicPublish("",QUEST_NAME,null,msg.getBytes());

            Thread.sleep(i * 20);
        }

        channel.close();

        connection.close();
    }

}
