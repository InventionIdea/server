package iakka.platform.global.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.net.URI;

@Service
public class R2StorageService {

    private final S3Client s3Client;
    private final String bucket;
    private final String endpoint;
    private final String publicUrlBase;

    public R2StorageService(
            @Value("${cloud.aws.s3.endpoint}") String endpoint,
            @Value("${cloud.aws.credentials.access-key}") String accessKey,
            @Value("${cloud.aws.credentials.secret-key}") String secretKey,
            @Value("${cloud.aws.s3.bucket}") String bucket,
            @Value("${cloud.aws.region.static}") String region,
            @Value("${cloud.aws.s3.public-url-base:}") String publicUrlBase) {
        
        this.endpoint = endpoint;
        this.bucket = bucket;
        this.publicUrlBase = publicUrlBase;

        // Create S3Client configured for R2 (S3-compatible)
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        
        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(region))
                .build();
    }

    /**
     * Uploads a file to R2 storage and returns the public URL.
     * 
     * @param file The file to upload
     * @param key The S3 key (path) where the file will be stored
     * @return The public URL of the uploaded file
     */
    public String uploadFile(File file, String key) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType("video/mp4")
                    .build();

            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromFile(file)
            );

            // Construct the public URL
            // For R2, the public URL format depends on your setup
            // If using a custom domain: https://your-domain.com/key
            // If using R2 public URL: https://<account-id>.r2.dev/key
            // For now, we'll construct it from the endpoint
            // Note: You may need to adjust this based on your R2 public URL configuration
            String publicUrl = constructPublicUrl(key);
            
            return publicUrl;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to R2: " + e.getMessage(), e);
        }
    }

    /**
     * Constructs the public URL for the uploaded file.
     * Priority:
     * 1. Use configured public-url-base if provided
     * 2. Extract account ID from endpoint and construct R2 public URL
     * 3. Fallback to endpoint-based URL construction
     */
    private String constructPublicUrl(String key) {
        // If a custom public URL base is configured, use it
        if (publicUrlBase != null && !publicUrlBase.isEmpty()) {
            // Ensure the base URL doesn't end with a slash
            String base = publicUrlBase.endsWith("/") 
                ? publicUrlBase.substring(0, publicUrlBase.length() - 1)
                : publicUrlBase;
            return String.format("%s/%s", base, key);
        }
        
        // Try to extract account ID from endpoint and construct R2 public URL
        if (endpoint.contains("r2.cloudflarestorage.com")) {
            try {
                // Extract account ID from endpoint
                // Format: https://<account-id>.r2.cloudflarestorage.com
                int startIdx = endpoint.indexOf("//") + 2;
                int endIdx = endpoint.indexOf(".r2.cloudflarestorage.com");
                if (startIdx > 1 && endIdx > startIdx) {
                    String accountId = endpoint.substring(startIdx, endIdx);
                    return String.format("https://%s.r2.dev/%s", accountId, key);
                }
            } catch (Exception e) {
                // Fall through to fallback
            }
        }
        
        // Fallback: construct URL from endpoint
        // Remove trailing slash from endpoint if present
        String baseEndpoint = endpoint.endsWith("/") 
            ? endpoint.substring(0, endpoint.length() - 1)
            : endpoint;
        return String.format("%s/%s", baseEndpoint, key);
    }
}

