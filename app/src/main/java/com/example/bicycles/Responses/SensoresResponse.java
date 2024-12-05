package com.example.bicycles.Responses;

import java.util.List;

public class SensoresResponse {
    private boolean success;
    private List<SensorData> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<SensorData> getData() {
        return data;
    }

    public void setData(List<SensorData> data) {
        this.data = data;
    }

    public static class SensorData {
        private String key;
        private String last_value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getLastValue() {
            return last_value;
        }

        public void setLastValue(String last_value) {
            this.last_value = last_value;
        }
    }
}
