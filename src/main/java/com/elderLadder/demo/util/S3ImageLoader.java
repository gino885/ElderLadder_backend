package com.elderLadder.demo.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class S3ImageLoader {

    private AmazonS3 s3Client;

    public BufferedImage loadImageFromS3(String s3ObjectUrl) throws IOException {
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion("ap-southeast-2") // specify the AWS region here
                .build();
        // Parse the S3 URL, extract bucket name and object key
        URL url = new URL(s3ObjectUrl);
        String host = url.getHost();
        String bucketName = host.split("\\.")[0]; // Extract bucket name from the host
        String objectKey = url.getPath().substring(1); // Remove leading slash

        // Download the image object from S3
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, objectKey));
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        // Convert the image stream to BufferedImage
        BufferedImage image = ImageIO.read(inputStream);

        // Close the input stream
        inputStream.close();

        return image;
    }
}