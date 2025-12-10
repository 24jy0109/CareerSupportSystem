package model;

import java.time.LocalDateTime;

public class Answer {

    private Integer answerId;
    private String graduateStudentNumber;
    private Boolean eventAvailability;

    private LocalDateTime firstChoiceStartTime;
    private LocalDateTime firstChoiceEndTime;

    private LocalDateTime secondChoiceStartTime;
    private LocalDateTime secondChoiceEndTime;

    private LocalDateTime thirdChoiceStartTime;
    private LocalDateTime thirdChoiceEndTime;

    public Answer() {}

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
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

    public LocalDateTime getFirstChoiceStartTime() {
        return firstChoiceStartTime;
    }

    public void setFirstChoiceStartTime(LocalDateTime firstChoiceStartTime) {
        this.firstChoiceStartTime = firstChoiceStartTime;
    }

    public LocalDateTime getFirstChoiceEndTime() {
        return firstChoiceEndTime;
    }

    public void setFirstChoiceEndTime(LocalDateTime firstChoiceEndTime) {
        this.firstChoiceEndTime = firstChoiceEndTime;
    }

    public LocalDateTime getSecondChoiceStartTime() {
        return secondChoiceStartTime;
    }

    public void setSecondChoiceStartTime(LocalDateTime secondChoiceStartTime) {
        this.secondChoiceStartTime = secondChoiceStartTime;
    }

    public LocalDateTime getSecondChoiceEndTime() {
        return secondChoiceEndTime;
    }

    public void setSecondChoiceEndTime(LocalDateTime secondChoiceEndTime) {
        this.secondChoiceEndTime = secondChoiceEndTime;
    }

    public LocalDateTime getThirdChoiceStartTime() {
        return thirdChoiceStartTime;
    }

    public void setThirdChoiceStartTime(LocalDateTime thirdChoiceStartTime) {
        this.thirdChoiceStartTime = thirdChoiceStartTime;
    }

    public LocalDateTime getThirdChoiceEndTime() {
        return thirdChoiceEndTime;
    }

    public void setThirdChoiceEndTime(LocalDateTime thirdChoiceEndTime) {
        this.thirdChoiceEndTime = thirdChoiceEndTime;
    }
}
