package learn.rabbmitmq.springboot.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: zhenghailong
 * @modified By:
 * @description:
 */
@Component
@RabbitListener(queues = "topic.women")
public class TopicTotalReceiver {

    @RabbitHandler
    public void process(Map testMessage){
        System.out.println("TopicTotalReceiver消费者收到消息："+testMessage.toString());
    }
}
