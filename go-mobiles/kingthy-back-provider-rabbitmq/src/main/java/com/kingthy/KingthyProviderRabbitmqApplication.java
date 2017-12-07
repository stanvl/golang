package com.kingthy;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author:xumin
 * @Description:
 * @Date:19:38 2017/11/1
 */
@SpringCloudApplication
@EnableFeignClients
public class KingthyProviderRabbitmqApplication
{
    
    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }
    
    public static void main(String[] args)
    {
        SpringApplication.run(KingthyProviderRabbitmqApplication.class, args);
    }

}


