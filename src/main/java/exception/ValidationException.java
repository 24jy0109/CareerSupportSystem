package exception;

import model.Answer;
import model.Event;

public class ValidationException extends Exception {

    private final Answer answer;
    private final Event event;

    // Answer 用（既存コード互換）
    public ValidationException(String message, Answer answer) {
        super(message);
        this.answer = answer;
        this.event = null;
    }

    // Event 用
    public ValidationException(String message, Event event) {
        super(message);
        this.answer = null;
        this.event = event;
    }

    public Answer getAnswer() {
        return answer;
    }

    public Event getEvent() {
        return event;
    }
}