package com.elderLadder.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class CardController {
    ArrayList<String> info = new ArrayList<>();

    static class DataModel {

        private String gender;
        private String taskDifficulty;

        // getter和setter方法
        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getTaskDifficulty() {
            return taskDifficulty;
        }

        public void setTaskDifficulty(String taskDifficulty) {
            this.taskDifficulty = taskDifficulty;
        }
    }

    @PostMapping("/cards")
    public ResponseEntity<String> receiveData(@RequestBody DataModel data) {

        try {
             info.add(data.getGender());
             info.add(data.getTaskDifficulty());

            // 如果一切顺利，返回一个成功的响应
            return ResponseEntity.ok("Data processed successfully");
        } catch (Exception e) {
            // 如果处理过程中发生异常，返回一个错误响应
            return ResponseEntity.internalServerError().body("Error processing data");
        }
    }
    public ArrayList<String> getInfo(){
        return info;
    }

}

