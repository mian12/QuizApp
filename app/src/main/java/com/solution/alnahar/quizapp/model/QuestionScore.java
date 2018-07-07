package com.solution.alnahar.quizapp.model;

public class QuestionScore {
    private String Question_score;
    private String User;
    private String Score;
    private String CategoryName;
    private String CategoryId;

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public QuestionScore() {
    }

    public QuestionScore(String question_score, String user, String score, String categoryName, String categoryId) {
        Question_score = question_score;
        User = user;
        Score = score;
        CategoryName = categoryName;
        CategoryId = categoryId;
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
