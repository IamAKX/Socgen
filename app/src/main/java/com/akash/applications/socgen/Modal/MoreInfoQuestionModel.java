package com.akash.applications.socgen.Modal;

/**
 * Created by akash on 2/8/17.
 */

public class MoreInfoQuestionModel {

    String Question, option1,option2,option3,option4;

    public MoreInfoQuestionModel(String question, String option1, String option2, String option3, String option4) {
        Question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
    }

    public MoreInfoQuestionModel(String question, String option1, String option2, String option3) {
        Question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
    }

    public MoreInfoQuestionModel(String question, String option1, String option2) {
        Question = question;
        this.option1 = option1;
        this.option2 = option2;
    }

    public String getQuestion() {
        return Question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }
}
