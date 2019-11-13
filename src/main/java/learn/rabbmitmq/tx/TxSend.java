package learn.rabbmitmq.tx;

import learn.rabbmitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TxSend {

    private static final String QUEUE_NAME = "test_queue_tx";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        String msg = "hello tx";
        try {
            // 将信道置为事务模式
            channel.txSelect();

            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            // 提交事务
            channel.txCommit();

        }catch (Exception e){
            System.out.println("send message txRollback");
            // 回滚事务
            channel.txRollback();
        }

        channel.close();

        connection.close();
    }
}
