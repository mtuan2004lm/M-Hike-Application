package com.example.m_hike;

public class Hike {

    // Các trường bắt buộc theo yêu cầu đề bài
    private int id;
    private String name;
    private String location;
    private String date;
    private String parkingAvailable; // "Yes" hoặc "NO"
    private double length;
    private String difficultyLevel;

    // Trường tùy chọn
    private String description;

    // Hai trường sáng tạo của bạn
    private String weatherCondition;
    private String equipmentRequired;

    // Constructor mặc định
    public Hike() {
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getParkingAvailable() { return parkingAvailable; }
    public void setParkingAvailable(String parkingAvailable) { this.parkingAvailable = parkingAvailable; }

    public double getLength() { return length; }
    public void setLength(double length) { this.length = length; }

    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Getters và Setters cho các trường sáng tạo
    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }

    public String getEquipmentRequired() { return equipmentRequired; }
    public void setEquipmentRequired(String equipmentRequired) { this.equipmentRequired = equipmentRequired; }
}