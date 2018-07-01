package com.solution.alnahar.quizapp.model;

public class QuestionScore {
    private String Question_score;
    private String User;
    private String Score;

    public QuestionScore() {
    }

    public QuestionScore(String question_score, String user, String score) {
        Question_score = question_score;
        User = user;
        Score = score;
    }

    public String getQuestion_score() {
        return Question_score;
    }

    public void setQuestion_score(String question_score) {
        Question_score = question_score;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }
}
