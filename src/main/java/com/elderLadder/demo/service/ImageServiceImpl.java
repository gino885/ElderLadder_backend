package com.elderLadder.demo.service;

import com.elderLadder.demo.util.DalleClient;
import com.elderLadder.demo.util.ImageProcessingUtility;
import com.elderLadder.demo.util.S3ImageLoader;
import com.elderLadder.demo.util.WordSpliter;
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
    S3ImageLoader imageLoader = new S3ImageLoader();
    private final ResourceLoader resourceLoader;
    @Autowired
    public ImageServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
        public Map<String, String> getRandomText() {
            WordSpliter wordSpliter = new WordSpliter();
            Map<String,String> tasks = new HashMap<String,String>();
            tasks.put("休閒娛樂 A", "請打通電話，跟爺爺奶奶聊聊最近空閒時做了甚麼來享受這段休閒時間？");
            tasks.put("休閒娛樂 B", "致電給長輩，詢問他們是否願意一同觀賞一部有趣的電影");
            tasks.put("學校 A", "打電話或傳訊息和長輩分享最近學校發生了什麼趣事");
            tasks.put("學校 B", "致電給長輩，分享自己在學校的一個有趣事件也鼓勵他們分享自己學生時代的趣事");
            tasks.put("家庭 A", "打通電話，和爺奶聊聊「組成家庭」對他們來說的意義為何？");
            tasks.put("家庭 B", "打給長輩表達你對於上次過年的美好回憶，並提議下次的年夜飯計劃");
            tasks.put("工作 A", "打電話跟長輩分享工作上發生的事並引導長輩給予建議或分享看法");
            tasks.put("工作 B", "致電長輩，詢問他們對於當今工作環境的看法，同時分享一些你自己的工作經歷");
            tasks.put("技藝 A", "打給長輩，分享你最近學到了甚麼、詢問長輩會唱的一首歌或會做的一道菜");
            tasks.put("生活 A", "致電長輩，跟他們分享生活中遇到的瓶頸，並請他們分享一些看法");
            tasks.put("生活 B", "致電長輩，先詢問最近有沒有看什麼劇，再問他們劇情跟最喜歡的角色");
            Random generator = new Random();
            Object[] keys = tasks.keySet().toArray();
            String randomKey =(String) keys[generator.nextInt(keys.length)];
            Map<String,String> task = new HashMap<>();
            task.put(randomKey, wordSpliter.spliter(tasks.get(randomKey)));
            return task;
        }

    public byte[] generateImageWithText(Map<String, String> task) throws IOException {

        BufferedImage originalImage = imageLoader.loadImageFromS3("https://elasticbeanstalk-ap-southeast-2-617849466687.s3.ap-southeast-2.amazonaws.com/images/card.jpg");
        BufferedImage IT_seed = imageLoader.loadImageFromS3("https://elasticbeanstalk-ap-southeast-2-617849466687.s3.ap-southeast-2.amazonaws.com/images/IT_seed.png");
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
