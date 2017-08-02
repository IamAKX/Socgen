package com.akash.applications.socgen.Modal;

/**
 * Created by akash on 2/8/17.
 */

public class MoreInfoQuestionModel {

    String Question="";
    String[] options;

    public MoreInfoQuestionModel(String question, String[] options) {
        Question = question;
        this.options = options;
    }

    public String getQuestion() {
        return Question;
    }

    public String[] getOptions() {
        return options;
    }
}
