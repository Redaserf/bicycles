package com.example.bicycles.Models;

import android.util.Base64;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class Token {
    public static boolean isTokenExpired(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                return true;
            }

            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.UTF_8);

            JSONObject jsonObject = new JSONObject(payload);

            long exp = jsonObject.getLong("exp");

            long currentTime = System.currentTimeMillis() / 1000;
            return exp < currentTime;

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}
