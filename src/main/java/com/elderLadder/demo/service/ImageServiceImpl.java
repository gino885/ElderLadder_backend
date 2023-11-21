package com.elderLadder.demo.service;

import com.elderLadder.demo.util.DalleClient;
import com.elderLadder.demo.util.ImageProcessingUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;


@Component
public class ImageServiceImpl implements ImageService{
    DalleClient dalleClient =new DalleClient();
    private final ResourceLoader resourceLoader;
    @Autowired
    public ImageServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
        public Map<String, String> getRandomText() {
            Map<String,String> tasks = new HashMap<String,String>();
            tasks.put("休閒娛樂", "請打通電話，跟爺爺奶奶聊\n聊最近空閒時做了甚麼來享\n受這段休閒時間？");
            tasks.put("學校", "打電話或傳訊息和長輩分享\n最近學校發生了什麼趣事");
            Random generator = new Random();
            Object[] keys = tasks.keySet().toArray();
            String randomKey =(String) keys[generator.nextInt(keys.length)];
            Map<String,String> task = new HashMap<>();
            task.put(randomKey, tasks.get(randomKey));
            return task;
        }

    public byte[] generateImageWithText(Map<String, String> task) throws IOException {
        // Load the image from the classpath
        Resource card = resourceLoader.getResource("classpath:static/card.jpg");
        BufferedImage originalImage = ImageIO.read(card.getInputStream());
        Resource seed = resourceLoader.getResource("classpath:static/IT_seed.png");
        BufferedImage IT_seed = ImageIO.read(seed.getInputStream());
        Map.Entry<String, String> entry = task.entrySet().iterator().next();
        String title = entry.getKey();
        String description = entry.getValue();
        String prompt = description + "並專注於溫暖且和諧的青年與長者互動";
        BufferedImage dallePicture = dalleClient.generateImage(prompt);

        // Add text to the image
        BufferedImage imageWithText = ImageProcessingUtility.addToImage(originalImage, title, description, IT_seed, dallePicture);

        // Convert the BufferedImage to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(imageWithText, "jpg", baos);
        return baos.toByteArray();
    }
}
