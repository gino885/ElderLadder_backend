package com.elderLadder.demo.service;

import java.io.IOException;
import java.util.Map;

public interface ImageService {
    Map<String, String> getRandomText();

    byte[] generateImageWithText(Map<String, String> task) throws IOException;
}
