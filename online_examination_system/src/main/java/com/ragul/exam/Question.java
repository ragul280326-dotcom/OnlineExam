package com.ragul.exam;

public class Question {
    private final int id;
    private final String text;
    private final String[] options;
    private final int answer;

    public Question(int id, String text, String[] options, int answer) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }
    public int getId() { return id; }
    public String getText() { return text; }
    public String[] getOptions() { return options; }
    public int getAnswer() { return answer; }
}
