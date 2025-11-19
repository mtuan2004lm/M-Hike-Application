package com.example.m_hike;

public class Observation {
    private int id;
    private int hikeId; // Khóa ngoại trỏ đến Hike
    private String observation;
    private String timeOfTheObservation;
    private String additionalComments;

    // Constructor trống
    public Observation() {
    }

    // Constructor đầy đủ
    public Observation(int hikeId, String observation, String timeOfTheObservation, String additionalComments) {
        this.hikeId = hikeId;
        this.observation = observation;
        this.timeOfTheObservation = timeOfTheObservation;
        this.additionalComments = additionalComments;
    }

    // --- Getters và Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHikeId() {
        return hikeId;
    }

    public void setHikeId(int hikeId) {
        this.hikeId = hikeId;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getTimeOfTheObservation() {
        return timeOfTheObservation;
    }

    public void setTimeOfTheObservation(String timeOfTheObservation) {
        this.timeOfTheObservation = timeOfTheObservation;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }
}