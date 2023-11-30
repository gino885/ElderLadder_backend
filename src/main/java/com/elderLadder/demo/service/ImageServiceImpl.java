package com.elderLadder.demo.service;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.elderLadder.demo.controller.CardController;
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
    @Autowired
    CardController cardController;
    private final ResourceLoader resourceLoader;
    @Autowired
    public ImageServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
        public Map<String, String> getRandomText() {
            WordSpliter wordSpliter = new WordSpliter();
            Map<String, String> easyTasks = new HashMap<>();
            easyTasks.put("生活 A", "與長輩聊聊他們最近空閒時做了什麼？");
            easyTasks.put("心靈交流 A", "與長輩分享自己生活中遇到的瓶頸，並請他們分享一些看法");
            easyTasks.put("休閒娛樂 A", "詢問長輩最近有沒有看什麼劇，再和他們一起討論劇情跟最喜歡的角色等內容");
            easyTasks.put("休閒娛樂 B", "與長輩分享自己最近在看的書，也可以問問看長輩最近是否也有在閱讀的書籍");
            easyTasks.put("學校 A", "與長輩分享自己在學校的趣事或成就，也可以鼓勵他們分享自己學生時代的趣事");
            easyTasks.put("工作 A", "與長輩分享工作或實習上發生的事，並引導長輩給予建議或分享看法");

            Map<String, String> mediumTasks = new HashMap<>();
            mediumTasks.put("中等休閒娛樂 C", "詢問長輩是否願意一起共度休閒時光（如觀賞一部輕鬆有趣的影劇或品嚐點心");
            mediumTasks.put("中等生活 B", "邀請長輩一起出門走走（如去附近的公園散步或是一起去市場買菜等");
            mediumTasks.put("中等生活 C", "與長輩一起準備一份餐點");
            mediumTasks.put("中等休閒娛樂 D", "與長輩一起下棋或玩遊戲");

            Map<String, String> hardTasks = new HashMap<>();
            hardTasks.put("生活 D", "關心長輩並表達你對於上次過年的美好回憶，並提議下次的年夜飯計劃");
            hardTasks.put("心靈交流 B", "與長輩聊聊「組成家庭」對他們來說的意義為何");
            hardTasks.put("心靈交流 C", "致電長輩，詢問他們對於當今工作環境的看法，同時分享一些你自己的工作經歷");
            hardTasks.put("心靈交流 D", "與長輩分享自己的一個小秘密，也鼓勵長輩也分享自己的故事");

            String taskDifficulty = cardController.getInfo().get(1);
            Random generator = new Random();

            // 根据难度选择任务组
            Map<String, String> selectedTasks;
            if (taskDifficulty.contains("簡單")) {
                selectedTasks = easyTasks;
            } else if (taskDifficulty.contains("普通")) {
                selectedTasks = mediumTasks;
            } else if (taskDifficulty.contains("困難")) {
                selectedTasks = hardTasks;
            } else {
                selectedTasks = new HashMap<>();
            }

            // 从选中的任务组中随机选择一个任务
            Object[] keys = selectedTasks.keySet().toArray();
            if (keys.length == 0) {
                return new HashMap<>(); // 如果没有任务可选，返回空的Map
            }

            String randomKey = (String) keys[generator.nextInt(keys.length)];
            Map<String, String> task = new HashMap<>();
            task.put(randomKey, wordSpliter.spliter(selectedTasks.get(randomKey)));
            return task;
        }

    public byte[] generateImageWithText(Map<String, String> task) throws IOException {

        BufferedImage originalImage = imageLoader.loadImageFromS3("https://elasticbeanstalk-ap-southeast-2-617849466687.s3.ap-southeast-2.amazonaws.com/images/card.jpg");
        BufferedImage IT_seed = imageLoader.loadImageFromS3("https://elasticbeanstalk-ap-southeast-2-617849466687.s3.ap-southeast-2.amazonaws.com/images/IT_seed.png");
        Map.Entry<String, String> entry = task.entrySet().iterator().next();
        String title = entry.getKey();
        String description = entry.getValue();
        String gender = cardController.getInfo().get(0);

        if(gender.equals("生理男")){
            gender = "男大學生";
        }
        else if(gender.equals("生理女") ){
            gender = "女大學生";
        }
        else {
            gender = "大學生";
        }

        String prompt = description + "並專注於溫暖且和諧的青年與長者互動，" + "其中的青年是" +gender;

        BufferedImage dallePicture = dalleClient.generateImage(prompt);

        // Add text to the image
        BufferedImage imageWithText = ImageProcessingUtility.addToImage(originalImage, title, description, IT_seed, dallePicture);

        // Convert the BufferedImage to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(imageWithText, "png", baos);
        return baos.toByteArray();
    }
}
