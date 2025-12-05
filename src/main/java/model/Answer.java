package model;

import java.time.LocalDateTime;

public class Answer {

    private int answerId;
    private String graduateStudentNumber;
    private Boolean eventAvailability; 
    private LocalDateTime firstChoice;
    private LocalDateTime secondChoice;
    private LocalDateTime thirdChoice;

    public Answer() {}

    public Answer(int answerId, String graduateStudentNumber, Boolean eventAvailability,
                  LocalDateTime firstChoice, LocalDateTime secondChoice, LocalDateTime thirdChoice) {
        this.answerId = answerId;
        this.graduateStudentNumber = graduateStudentNumber;
        this.eventAvailability = eventAvailability;
        this.firstChoice = firstChoice;
        this.secondChoice = secondChoice;
        this.thirdChoice = thirdChoice;
    }

    // ---- Getter / Setter ----

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getGraduateStudentNumber() {
        return graduateStudentNumber;
    }

    public void setGraduateStudentNumber(String graduateStudentNumber) {
        this.graduateStudentNumber = graduateStudentNumber;
    }

    public Boolean getEventAvailability() {
        return eventAvailability;
    }

    public void setEventAvailability(Boolean eventAvailability) {
        this.eventAvailability = eventAvailability;
    }

    public LocalDateTime getFirstChoice() {
        return firstChoice;
    }

    public void setFirstChoice(LocalDateTime firstChoice) {
        this.firstChoice = firstChoice;
    }

    public LocalDateTime getSecondChoice() {
        return secondChoice;
    }

    public void setSecondChoice(LocalDateTime secondChoice) {
        this.secondChoice = secondChoice;
    }

    public LocalDateTime getThirdChoice() {
        return thirdChoice;
    }

    public void setThirdChoice(LocalDateTime thirdChoice) {
        this.thirdChoice = thirdChoice;
    }
}
