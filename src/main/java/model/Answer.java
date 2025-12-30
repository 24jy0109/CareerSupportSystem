package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Answer {

    private int answerId;
    
    private Event event;
	private Graduate graduate;
    private Boolean eventAvailability;

    private LocalDateTime firstChoiceStartTime;
    private LocalDateTime firstChoiceEndTime;

    private LocalDateTime secondChoiceStartTime;
    private LocalDateTime secondChoiceEndTime;

    private LocalDateTime thirdChoiceStartTime;
    private LocalDateTime thirdChoiceEndTime;

    public Answer() {}

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }
    
    public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

    public Graduate getGraduate() {
        return graduate;
    }

    public void setGraduate(Graduate graduate) {
        this.graduate = graduate;
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
    
    public Event toInputEvent() {
        Event inputEvent = new Event();

        if (this.event != null) {
            // Event ID は Answer の Event から
            inputEvent.setEventId(event.getEventId());

            // 日時は希望順でセット
            if (firstChoiceStartTime != null) {
                inputEvent.setEventStartTime(firstChoiceStartTime);
                inputEvent.setEventEndTime(firstChoiceEndTime);
            } else if (secondChoiceStartTime != null) {
                inputEvent.setEventStartTime(secondChoiceStartTime);
                inputEvent.setEventEndTime(secondChoiceEndTime);
            } else if (thirdChoiceStartTime != null) {
                inputEvent.setEventStartTime(thirdChoiceStartTime);
                inputEvent.setEventEndTime(thirdChoiceEndTime);
            }

            // その他 Event 情報
            inputEvent.setEventPlace(event.getEventPlace());
            inputEvent.setEventCapacity(event.getEventCapacity());
            inputEvent.setEventProgress(event.getEventProgress());
            inputEvent.setStaff(event.getStaff());
        }

        // joinGraduates リストを作って Answer の Graduate を追加
        List<Graduate> joinGraduates = new ArrayList<>();
        if (graduate != null) {
            joinGraduates.add(graduate);
        }
        inputEvent.setJoinGraduates(joinGraduates);

        return inputEvent;
    }
}
