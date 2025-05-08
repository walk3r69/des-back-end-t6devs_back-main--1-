package T6Devs_Back.T6Devs_Back.api.model.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

@Configuration
public class AwsConfig {
    
    @Value("${aws.accessKeyId}")
    private String awsAccessKey;
    
    @Value("${aws.secretKey}")
    private String awsSecretKey;
    
    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public AWSCognitoIdentityProvider cognitoClient() {
        // Validação das variáveis de ambiente
        if (awsAccessKey == null || awsAccessKey.isEmpty()) {
            throw new IllegalArgumentException("AWS Access Key não configurada");
        }
        if (awsSecretKey == null || awsSecretKey.isEmpty()) {
            throw new IllegalArgumentException("AWS Secret Key não configurada");
        }
        if (awsRegion == null || awsRegion.isEmpty()) {
            throw new IllegalArgumentException("AWS Region não configurada");
        }

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        
        return AWSCognitoIdentityProviderClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .withRegion(Regions.fromName(awsRegion))
            .build();
    }
}