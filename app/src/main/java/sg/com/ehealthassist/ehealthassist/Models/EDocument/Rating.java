package sg.com.ehealthassist.ehealthassist.Models.EDocument;

/**
 * Created by User on 9/12/2017.
 */

public class Rating {
    String question="";
    float score = 0;


    public Rating(String question, float score) {
        this.question = question;
        this.score = score;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
