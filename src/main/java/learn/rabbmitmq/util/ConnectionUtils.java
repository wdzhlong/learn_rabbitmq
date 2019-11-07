package learn.rabbmitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtils {

    public static Connection getConnection(){
        //1.定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2.设置服务器地址
        factory.setHost("127.0.0.1");
        //3.设置端口号
        factory.setPort(5672);
        //4.vhost
        factory.setVirtualHost("/user");
        //5.设置用户名
        factory.setUsername("zhl");
        //6.设置密码
        factory.setPassword("zhl");
        try {
            return factory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
