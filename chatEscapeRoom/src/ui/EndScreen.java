package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import core.Game;

public class EndScreen {
  private static Table rootTable;
  private static TextButton quitGameButton;
  private static Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
  private static Table tableInner = new Table();

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

    tableInner.add(quitGameButton).expandX().pad(20);
    tableInner.row();
    tableInner.add(title).expandX().fillX().pad(20);


    tableOuter.add(tableInner).expand().fill().pad(SIDE_LINE);

    rootTable.add(tableOuter).expand().fill();
    rootTable.setVisible(false);

    stage.addActor(rootTable);
  }

  public static void setEndScreenVisible(Boolean visible) {
    rootTable.setVisible(visible);
  }

  public static void addResultLabels(String question, String giveAnswer, String correctAnswer) {
      String content =
          "Frage: "
          + question
          + " Antwort: "
          + giveAnswer
          + " Lösung: "
          + correctAnswer;
      tableInner.add(new Label(content, skin)).left().pad(5);
      tableInner.row();
  }
}
