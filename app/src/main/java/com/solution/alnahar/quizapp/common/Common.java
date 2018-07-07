package com.solution.alnahar.quizapp.common;

import com.solution.alnahar.quizapp.model.Question;
import com.solution.alnahar.quizapp.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class Common {
    public  static  String categoryId,categoryName;
    public  static UserModel currentUser;
    public  static List<Question> questionList=new ArrayList<>();

    public  static String PUSH_NOTIFICATION="pushNotification";

}
