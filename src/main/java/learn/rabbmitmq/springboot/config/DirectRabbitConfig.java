package learn.rabbmitmq.springboot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zhenghailong
 * @modified By:
 * @description:
 */
@Configuration
public class DirectRabbitConfig {
    /**
     * 声名队列
     * @return
     */
    @Bean
    public Queue testDirectQueue(){
        return new Queue("testDirectQueue",true);
    }

    /**
     * 声名交换机
     * @return
     */
    @Bean
    public DirectExchange testDirectExchange(){
        return new DirectExchange("TestDirectExchange");
    }

    @Bean
    DirectExchange lonelyDirectExchange() {
        return new DirectExchange("lonelyDirectExchange");
    }

    /**
     * 将队列和交换机绑定，并设置路由键TestDirectRouting
     * @return
     */
    @Bean
    public Binding bindingDirect(){
        return BindingBuilder.bind(testDirectQueue()).to(testDirectExchange()).with("TestDirectRouting");
    }
}
