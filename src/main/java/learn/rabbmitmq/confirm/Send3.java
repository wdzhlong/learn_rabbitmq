package learn.rabbmitmq.confirm;

import learn.rabbmitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

public class Send3 {

    private static final String QUEUE_NAME = "test_queue_confirm3";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        // 生产者调confirmSelect 将channel设置为confirm模式，注意队列必须是一个新的队列
        channel.confirmSelect();
        // 存放未确定的消息
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
        // 通道添加监听
        channel.addConfirmListener(new ConfirmListener() {
            //没有问题的handleAck
            public void handleAck(long l, boolean b) throws IOException {
                if (b){
                    System.out.println("---handleAck---multiple");
                    confirmSet.headSet(l+1).clear();
                }else {
                    System.out.println("---handleAck---multiple false");
                    confirmSet.remove(l);
                }
            }
            // handleNack 表示回执有问题的
            public void handleNack(long l, boolean b) throws IOException {
                if (b){
                    System.out.println("---handleNack---multiple");
                    confirmSet.headSet(l+1).clear();
                }else {
                    System.out.println("---handleNack---multiple false");
                    confirmSet.remove(l);
                }
            }
        });

        String msg = "sssssssss";

        while (true){
            long seqNo = channel.getNextPublishSeqNo();
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            confirmSet.add(seqNo);
        }
    }
}
