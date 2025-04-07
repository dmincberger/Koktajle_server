package springboot_ds.koktajle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springboot_ds.koktajle.config.SecurityConfigs;

import java.net.InetAddress;
import java.net.UnknownHostException;


@SpringBootApplication
public class KoktajleApplication {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(KoktajleApplication.class, args);
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println(localHost.getHostAddress());

    }

}
