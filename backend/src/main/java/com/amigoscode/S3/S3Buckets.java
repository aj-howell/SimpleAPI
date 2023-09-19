package com.amigoscode.S3;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.s3.buckets")
public class S3Buckets {
    private String customer;

    public void setCustomer(String customer)
    {
        this.customer=customer;
    }

    public String getCustomer() {
        return customer;
    }
}
