package learn.rabbmitmq.springboot.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhenghailong
 * @modified By:
 * @description:
 * 监听的队列名称 TestDirectQueue
 */
@Component
@RabbitListener(queues = "testDirectQueue")
public class DirectReceiver implements ChannelAwareMessageListener{

    /*@RabbitHandler
    public void process(Map testMessage,Message message,Channel channel) throws IOException {
        System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
    }*/

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //因为传递消息的时候用的map传递,所以将Map从Message内取出需要做些处理
            String str = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println(str);
            channel.basicAck(deliveryTag, true);
//			channel.basicReject(deliveryTag, true);//为true会重新放回队列
        } catch (Exception e) {
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
    }

    //{key=value,key=value,key=value} 格式转换成map
    private Map<String, String> mapStringToMap(String str) {
        str = str.substring(1, str.length() - 1);
        String[] strs = str.split(",");
        Map<String, String> map = new HashMap<String, String>();
        for (String string : strs) {
            String key = string.split("=")[0].trim();
            String value = string.split("=")[1];
            map.put(key, value);
        }
        return map;
    }
}
