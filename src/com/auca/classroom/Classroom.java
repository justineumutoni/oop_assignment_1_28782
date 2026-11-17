package com.auca.classroom;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;

public class Classroom implements Identifiable, Registrable {

    private String classroomId;
    private String buildingName;
    private String roomNumber;
    private int capacity;
    private int floorNumber;
    private boolean available;

    public Classroom(String classroomId, String buildingName, String roomNumber,
                     int capacity, int floorNumber, boolean available) {
        this.classroomId = clean(classroomId);
        this.buildingName = clean(buildingName);
        this.roomNumber = clean(roomNumber);
        this.capacity = capacity;
        this.floorNumber = floorNumber;
        this.available = available;
    }

    // ---------- Identifiable ----------
    @Override
    public String getId() {
        return classroomId;
    }

    @Override
    public String getDisplayName() {
        return buildingName + " " + roomNumber;
    }

    @Override
    public boolean isActive() {
        return available;   // a room is "active" when it is available
    }

    // ---------- Registrable ----------
    @Override
    public void validate() throws ValidationException {
        if (classroomId == null || classroomId.length() < MIN_ID_LENGTH
                || classroomId.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Classroom ID must be " + MIN_ID_LENGTH
                    + "-" + MAX_ID_LENGTH + " characters.");
        }
        if (buildingName == null || buildingName.length() < MIN_NAME_LENGTH
                || buildingName.length() > MAX_NAME_LENGTH) {
            throw new ValidationException("Building name must be " + MIN_NAME_LENGTH
                    + "-" + MAX_NAME_LENGTH + " characters.");
        }
        if (roomNumber == null || roomNumber.isEmpty()) {
            throw new ValidationException("Room number cannot be blank.");
        }
        if (capacity <= 0) {
            throw new ValidationException("Capacity must be greater than zero.");
        }
        if (floorNumber < 0) {
            throw new ValidationException("Floor number must be zero or greater.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("Classroom ID  : " + classroomId);
        System.out.println("Building      : " + buildingName);
        System.out.println("Room number   : " + roomNumber);
        System.out.println("Capacity      : " + capacity);
        System.out.println("Floor         : " + floorNumber);
        System.out.println("Available     : " + (available ? "YES" : "NO"));
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + classroomId + "] " + buildingName + " " + roomNumber
                + " (capacity " + capacity + ")";
    }

    // ---------- Helper ----------
    private static String clean(String value) {
        return value == null ? null : value.trim();
    }

    // ---------- Getters and setters ----------
    public String getClassroomId() { return classroomId; }
    public void setClassroomId(String classroomId) { this.classroomId = clean(classroomId); }

    public String getBuildingName() { return buildingName; }
    public void setBuildingName(String buildingName) { this.buildingName = clean(buildingName); }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = clean(roomNumber); }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getFloorNumber() { return floorNumber; }
    public void setFloorNumber(int floorNumber) { this.floorNumber = floorNumber; }

    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}