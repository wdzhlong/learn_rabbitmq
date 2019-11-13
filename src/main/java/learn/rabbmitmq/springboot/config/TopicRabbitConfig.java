package learn.rabbmitmq.springboot.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zhenghailong
 * @date: 2019/11/12 11:16
 * @modified By:
 * @description:声名topic交换机和队列
 */
@Configuration
public class TopicRabbitConfig {

    public static final String man = "topic.man";
    public static final String women = "topic.women";

    @Bean
    public Queue firstQueue(){
        return new Queue(man);
    }

    @Bean
    public Queue secondQueue(){
        return new Queue(women);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange("topicExchange");
    }

    /**
     * 将firstQueue和topicExchange绑定，绑定的键值为topic.man
     * 这样只要是消息携带的路由键是topic.man，才会分发到该队列
     * @return
     */
    @Bean
    public Binding bindingExchangeMessage(){
        return BindingBuilder.bind(firstQueue()).to(exchange()).with(man);
    }

    /**
     * *  (星号) 用来表示一个单词 (必须出现的)
     * #  (井号) 用来表示任意数量（零个或多个）单词
     * 将secondQueue和topicExchange绑定，而且绑定的键值为用上通配路由键规则topic.#
     * 这样只要是消息携带的路由键是以topic.开头，都会分发到该队列
     * @return
     */
    @Bean
    public Binding bindingExchangeMessag2(){
        return BindingBuilder.bind(secondQueue()).to(exchange()).with("topic.#");
    }
}
