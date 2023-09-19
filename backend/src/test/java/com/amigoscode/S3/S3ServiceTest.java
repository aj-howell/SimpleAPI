package com.amigoscode.S3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.ResponseBody;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {
    
    private S3Service underTest;

    @Mock
    private S3Client s3Client;

    @BeforeEach
    void setUp()
    {
      underTest = new S3Service(s3Client);
    }
    
    @Test
    void testPutObject() throws IOException {
        String bucket="customer";
        String key="team";
        byte[] data = "hello".getBytes();

        underTest.putObject(bucket, "team", data); // inserted object

        ArgumentCaptor<PutObjectRequest> captor= ArgumentCaptor.forClass(PutObjectRequest.class);

         ArgumentCaptor<RequestBody> captor2= ArgumentCaptor.forClass(RequestBody.class);

        verify(s3Client).putObject(captor.capture(), captor2.capture());
        
        PutObjectRequest captor1Object= captor.getValue();
        RequestBody captor2Object= captor2.getValue();


        assertThat(captor1Object.bucket()).isEqualTo(bucket);
        assertThat(captor1Object.key()).isEqualTo(key);
      
            assertThat(captor2Object.contentStreamProvider().newStream().readAllBytes()).isEqualTo(RequestBody.fromBytes(data).contentStreamProvider().newStream().readAllBytes());
    }

    @Test
    void testGetObject() throws IOException {
        String bucket="customer";
        String key="team";
        byte[] data = "hello".getBytes();

        GetObjectRequest objectRequest = GetObjectRequest
        .builder()
        .key(key)
        .bucket(bucket)
        .build();

        ResponseInputStream<GetObjectResponse> mockObj = mock(ResponseInputStream.class);
        when(mockObj.readAllBytes()).thenReturn(data);

        when(s3Client.getObject(eq(objectRequest))).thenReturn(mockObj);


        byte[] data2=underTest.getObject(key, bucket);
        
        assertThat(data2).isEqualTo(data);
    }


    @Test
    void WillThrowExceptionGetObject() throws IOException {
        String bucket="customer";
        String key="team";
        byte[] data = "hello".getBytes();

        GetObjectRequest objectRequest = GetObjectRequest
        .builder()
        .key(key)
        .bucket(bucket)
        .build();

        ResponseInputStream<GetObjectResponse> mockObj = mock(ResponseInputStream.class);
        when(mockObj.readAllBytes()).thenThrow(new IOException("Cannot Read Bytes"));

        when(s3Client.getObject(eq(objectRequest))).thenReturn(mockObj);


        assertThatThrownBy(()->underTest.getObject(key, bucket))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Cannot Read Bytes")
        .hasRootCauseInstanceOf(IOException.class);
    }

}
