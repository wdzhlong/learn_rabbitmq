package learn.rabbmitmq.workfair;

import learn.rabbmitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producter {

    public static String QUEST_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();
        // 获取channel
        Channel channel = connection.createChannel();

        // 声明队列
        /**
         * durable第二个参数：消息持久化
         * 我们将程序中的boolean durable = false,改为true;是不可以的，尽管代码是正确的，他也不会运行成功！因为我们已经定义了
         * 一个叫test_work_queue这个queue是未持久化的，rabbitmq是不准许重新定义（不同参数）一个已存在的队列
         */
        channel.queueDeclare(QUEST_NAME,false,false,false,null);
        /**
         * 每个消费者发送确认消息之前，消息队列不发送下一个消息到消费者，一次只处理一个消息
         * 限制发送给同一个消费者不得超过一条消息
         */
        int prefetchSize = 1;
        channel.basicQos(prefetchSize);

        for (int i = 0; i < 50; i++){
            String msg = "hello "+i;
            channel.basicPublish("",QUEST_NAME,null,msg.getBytes());
            Thread.sleep(i * 20);
        }

        channel.close();

        connection.close();
    }

}
