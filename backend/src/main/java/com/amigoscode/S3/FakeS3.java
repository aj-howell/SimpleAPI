package com.amigoscode.S3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public class FakeS3 implements S3Client
{
    private static final String Path= System.getProperty("user.home")+"/.anazj/s3";

    @Override
    public String serviceName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'serviceName'");
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'close'");
    }

        @Override
    public PutObjectResponse putObject(PutObjectRequest putObjectRequest, RequestBody requestBody)
            throws AwsServiceException, SdkClientException{
        // TODO Auto-generated method stub
        InputStream inputStream = requestBody.contentStreamProvider().newStream();
        try {
           byte[] bytes=IOUtils.toByteArray(inputStream);
           FileUtils.writeByteArrayToFile(new File
           (
            buildObjectFullPath(putObjectRequest.bucket(), putObjectRequest.key())
           ), bytes);
           return PutObjectResponse.builder().build();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return S3Client.super.putObject(putObjectRequest, requestBody);
    }

    @Override
    public ResponseInputStream<GetObjectResponse> getObject(GetObjectRequest getObjectRequest)
            throws AwsServiceException, SdkClientException
            {
             try {
                FileInputStream  fileInputStream= new FileInputStream(
                        buildObjectFullPath
                        (getObjectRequest.bucket(), getObjectRequest.key())
                    );
                return new ResponseInputStream<>(GetObjectResponse.builder().build(), fileInputStream);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
    }

 
    private String buildObjectFullPath(String bucketName, String key)
    {
        return Path+"/"+bucketName+"/"+key;
    };
  

    
}