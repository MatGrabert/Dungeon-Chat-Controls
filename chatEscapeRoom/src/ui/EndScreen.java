package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import core.Game;

/** The end-screen of a game. Includes all results. */
public class EndScreen {
  private static Table rootTable;
  private static TextButton quitGameButton;
  private static final Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
  private static final Table tableInner = new Table();

  /**
   * Creates an end-screen.
   *
   * @param stage The stage to which the chat interface is added.
   */
  public static void create(Stage stage) {
    float SIDE_LINE = 8f;

    rootTable = new Table();
    rootTable.setFillParent(true);

    Table tableOuter = new Table();
    tableOuter.setVisible(true);
    tableOuter.setBackground(skin.getDrawable("grayBackground"));

    tableInner.setVisible(true);
    tableInner.setBackground(skin.getDrawable("darkGrayBackground"));

    Label title = new Label("Ergebnisse:", skin);
    title.setAlignment(Align.center);

    quitGameButton = new TextButton("Spiel beenden", skin);
    quitGameButton.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            Game.exit();
          }
        });

    tableInner.add(quitGameButton).expandX().fillX().center().pad(5);
    tableInner.row();
    tableInner.add(title).expandX().fillX().center().pad(5);
    tableInner.row();

    tableOuter.add(tableInner).expand().fillX().pad(SIDE_LINE);

    rootTable.add(tableOuter).expand().fill();
    rootTable.setVisible(false);

    stage.addActor(rootTable);
  }

  /**
   * Sets the end-screen visible or invisible.
   *
   * @param visible True or false
   */
  public static void setEndScreenVisible(boolean visible) {
    rootTable.setVisible(visible);
  }

  /**
   * Adds the results to the end-screen.
   *
   * @param question The current question
   * @param giveAnswer The given answer
   * @param correctAnswer The correct answer
   */
  public static void addResultLabels(String question, String giveAnswer, String correctAnswer) {
    String content = "Frage: " + question + " Antwort: " + giveAnswer + " Lösung: " + correctAnswer;
    Label resultLabel = new Label(content, skin);
    resultLabel.setAlignment(Align.center);
    tableInner.add(resultLabel).expandX().fillX().center().pad(5);
    tableInner.row();
  }
}
