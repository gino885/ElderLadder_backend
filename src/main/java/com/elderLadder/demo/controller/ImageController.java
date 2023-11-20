package com.elderLadder.demo.controller;

import com.elderLadder.demo.service.DalleService;
import com.elderLadder.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;
    @Autowired
    private DalleService dalleService;

    @GetMapping("/image")
    public ResponseEntity<String> generateImageWithText() {
        try {
            Map<String, String> task = imageService.getRandomText();
            byte[] imageData = imageService.generateImageWithText(task);
            if (imageData == null || imageData.length == 0) {
                // Log an error or throw an exception
                throw new RuntimeException("Image data is null or empty");
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.set("Content-Encoding", "UTF-8");
            return new ResponseEntity<>(dalleService.getImageUrl("your mon"), headers, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception for debugging
            Logger logger = LoggerFactory.getLogger(ImageController.class);
            logger.error("Error generating image", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}