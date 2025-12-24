package exception;

import model.Answer;

public class ValidationException extends Exception {
    private final Answer answer;

    public ValidationException(String message, Answer answer) {
        super(message);
        this.answer = answer;
    }

    public Answer getAnswer() {
        return answer;
    }
}
