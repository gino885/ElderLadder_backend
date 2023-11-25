package com.elderLadder.demo.util;



import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DalleClient {
    ImageResize imageResize = new ImageResize();
    public BufferedImage generateImage(String prompt) {
        String endpoint = "https://api.openai.com/v1/images/generations";

        HttpClient client = HttpClient.newHttpClient();
        String apiKey = System.getenv("API_KEY");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(new JSONObject()
                        .put("model", "dall-e-3")
                        .put("prompt", prompt)
                        .put("size", "1024x1024")
                        .put("quality", "standard")
                        .put("n", 1)
                        .toString()))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            String url = jsonResponse.getJSONArray("data").getJSONObject(0).getString("url");
            int dpi = 96;
            double cm = 3.7;
            double cm2 = 3.6;

            int width = (int)(cm / 2.54 * dpi);
            int height = (int)(cm2 / 2.54 * dpi);
            return imageResize.resizeImage(url, width, height);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

