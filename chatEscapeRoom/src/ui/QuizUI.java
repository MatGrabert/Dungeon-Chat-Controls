package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.List;

public class QuizUI {
  private static Label answerALabel;
  private static Label answerBLabel;
  private static Label answerCLabel;
  private static Label answerDLabel;
  private static Label timerLabel;
  private static Table rootTable;
  private static Label quizQuestionLabel;

  public static void setTimerLabel(String timerLabel) {
    QuizUI.timerLabel.setText(timerLabel);
  }

  public static void create(Stage stage) {
    Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    float SIDE_LINE = 8f;

    rootTable = new Table();
    rootTable.setFillParent(true);

    Actor chatDummy = new Actor();
    Actor inputDummy = new Actor();

    Table rightSideTable = new Table();

    Table quizTableOuter = new Table();
    quizTableOuter.setVisible(true);
    quizTableOuter.setBackground(skin.getDrawable("grayBackground"));

    Table quizTableInner = new Table();
    quizTableInner.setVisible(true);
    quizTableInner.setBackground(skin.getDrawable("darkGrayBackground"));

    quizQuestionLabel = new Label("?", skin);
    quizQuestionLabel.setAlignment(Align.center);

    answerALabel = new Label("A", skin);
    answerALabel.setColor(0.0f, 0.5843138f, 1.0f, 1);
    answerBLabel = new Label("B", skin);
    answerBLabel.setColor(0.0f, 0.5843138f, 1.0f, 1);
    answerCLabel = new Label("C", skin);
    answerCLabel.setColor(0.0f, 0.5843138f, 1.0f, 1);
    answerDLabel = new Label("D", skin);
    answerDLabel.setColor(0.0f, 0.5843138f, 1.0f, 1);

    timerLabel = new Label("Verbleibende Zeit: 00:00", skin);
    timerLabel.setColor(1, 0, 0, 1);
    timerLabel.setAlignment(Align.center);

    quizTableInner.add(quizQuestionLabel).expandX().fillX().pad(20).center();
    quizTableInner.row();
    quizTableInner.add(answerALabel).fillX().pad(10, 20, 10, 10);
    quizTableInner.row();
    quizTableInner.add(answerBLabel).fillX().pad(10, 20, 10, 10);
    quizTableInner.row();
    quizTableInner.add(answerCLabel).fillX().pad(10, 20, 10, 10);
    quizTableInner.row();
    quizTableInner.add(answerDLabel).fillX().pad(10, 20, 10, 10);
    quizTableInner.row();
    quizTableInner.add(timerLabel).expandX().fillX().pad(20);

    quizTableOuter.add(quizTableInner).expand().fill().pad(SIDE_LINE);

    rightSideTable.add(quizTableOuter).expand().fill().pad(200);
    rightSideTable.row();
    rightSideTable.add(inputDummy).fillX().height(Gdx.graphics.getHeight() * 0.15f);

    rootTable.add(chatDummy).width(Gdx.graphics.getWidth() * 0.15f).expandY().fillY();
    rootTable.add(rightSideTable).expand().fill();

    stage.addActor(rootTable);
  }

  public static void setQuizVisible(Boolean visible) {
      rootTable.setVisible(visible);
  }

  public static void setAnswerAVisible(Boolean visible) {answerALabel.setVisible(visible);}

  public static void setAnswerBVisible(Boolean visible) {answerBLabel.setVisible(visible);}

  public static void setAnswerCVisible(Boolean visible) {answerCLabel.setVisible(visible);}

  public static void setAnswerDVisible(Boolean visible) {answerDLabel.setVisible(visible);}

  public static void setAnswerColor(char answer, boolean correct) {
    switch (answer) {
      case 'a'-> {
        if (correct) {
          answerALabel.setColor(0,1,0,1);
        } else {
          answerALabel.setColor(1,0,0,1);
        }
      }
      case 'b'-> {
        if (correct) {
          answerBLabel.setColor(0,1,0,1);
        } else {
          answerBLabel.setColor(1,0,0,1);
        }
      }
      case 'c'-> {
        if (correct) {
          answerCLabel.setColor(0,1,0,1);
        } else {
          answerCLabel.setColor(1,0,0,1);
        }
      }
      case 'd'-> {
        if (correct) {
          answerDLabel.setColor(0,1,0,1);
        } else {
          answerDLabel.setColor(1,0,0,1);
        }
      }
      case 'r' ->{
        answerALabel.setColor(1,1,1,1);
        answerBLabel.setColor(1,1,1,1);
        answerCLabel.setColor(1,1,1,1);
        answerDLabel.setColor(1,1,1,1);
      }
    }
  }

  public static void fillQuizUI(String question, List<String> answers) {
    QuizUI.quizQuestionLabel.setText(question);
    QuizUI.answerALabel.setText("A: " + answers.get(0));
    QuizUI.answerBLabel.setText("B: " + answers.get(1));
    QuizUI.answerCLabel.setText("C: " + answers.get(2));
    QuizUI.answerDLabel.setText("D: " + answers.get(3));
  }
}
