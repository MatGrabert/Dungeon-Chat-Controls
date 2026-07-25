package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

/** The ui of the menu. */
public class MenuUI {
  private static Table rootTable;
  private static TextButton gameModeButton;

  /**
   * Creates a menu ui
   *
   * @param stage The stage to which the chat interface is added.
   */
  public static void create(Stage stage) {
    Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    float SIDE_LINE = 8f;

    rootTable = new Table();
    rootTable.setFillParent(true);

    Actor chatDummy = new Actor();
    Actor inputDummy = new Actor();

    Table rightSideTable = new Table();

    Table menuTableOuter = new Table();
    menuTableOuter.setVisible(true);
    menuTableOuter.setBackground(skin.getDrawable("grayBackground"));

    Table menuTableInner = new Table();
    menuTableInner.setVisible(true);
    menuTableInner.setBackground(skin.getDrawable("darkGrayBackground"));

    Label title = new Label("Menu", skin);
    title.setAlignment(Align.center);

    gameModeButton = new TextButton("Anarchie", skin);
    Button exitButton = new TextButton("Schließen", skin);

    menuTableInner.add(title).expandX().fillX().pad(20);
    menuTableInner.row();
    menuTableInner.add(gameModeButton).expandX().fillX().pad(20);
    menuTableInner.row();
    menuTableInner.add(exitButton).expandX().fillX().pad(20);

    menuTableOuter.add(menuTableInner).expand().fill().pad(SIDE_LINE);

    rightSideTable.add(menuTableOuter).expand().fill().pad(200, 400, 200, 400);
    rightSideTable.row();
    rightSideTable.add(inputDummy).fillX().height(Gdx.graphics.getHeight() * 0.15f);

    rootTable.add(chatDummy).width(Gdx.graphics.getWidth() * 0.15f).expandY().fillY();
    rootTable.add(rightSideTable).expand().fill();
    rootTable.setVisible(false);

    stage.addActor(rootTable);
  }

  /**
   * Sets the menu visible or invisible.
   *
   * @param visible True or false
   */
  public static void setMenuVisible(Boolean visible) {
    rootTable.setVisible(visible);
  }

  /**
   * Gets the current game mode.
   *
   * @return String of the current game mode
   */
  public static String getGameMode() {
    return gameModeButton.getText().toString();
  }

  /**
   * Sets the current game mode.
   *
   * @param gameMode The current game mode as String
   */
  public static void setGameMode(String gameMode) {
    MenuUI.gameModeButton.setText(gameMode);
  }
}
