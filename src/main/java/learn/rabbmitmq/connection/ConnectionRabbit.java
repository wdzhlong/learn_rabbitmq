package learn.rabbmitmq.connection;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * @author: zhenghailong
 * @date: 2019/11/1 16:47
 * @modified By:
 * @description:
 */
public class ConnectionRabbit {

    public static Connection getConnection() throws IOException, TimeoutException {
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
        return factory.newConnection();
    }

    public static Connection getConnection2() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
        //1.定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        factory.setUri("amqp://userName:password@ipAddress:portNumber/virtualHost");
        return factory.newConnection();
    }
}
