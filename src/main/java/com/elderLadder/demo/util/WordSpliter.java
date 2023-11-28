package com.elderLadder.demo.util;

public class WordSpliter {
    public String spliter(String sentence){

        StringBuilder stringBuilder = new StringBuilder();
        for(int i =0; i<sentence.length(); i++){
            stringBuilder.append(sentence.charAt(i));
            if( (i+1) % 12 == 0){
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
