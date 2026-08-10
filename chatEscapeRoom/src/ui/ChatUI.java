package ui;

import chat.CommandParser;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import core.Game;
import core.network.messages.ChatMessage;
import core.systems.CameraSystem;
import java.util.List;
import java.util.Map;
import systems.TimeSystem;

/**
 * Represents the user interface for the game chat.
 *
 * <p>The interface consist of a chat area and an input area. Entered messages are forwarded to the
 * {@link CommandParser} and displayed in the chat.
 */
public class ChatUI {
  private TextArea textArea;
  private TextField textField;
  private static Label modeTimeLabel;
  private static Label gameTimeLabel;
  private Table rootTable;
  Table infoTable;

  /**
   * Sets the time for the mode time.
   *
   * @param seconds Seconds left
   */
  public static void setModeTimeLabel(int seconds) {
    String time = TimeSystem.formatTime(seconds);
    if (seconds == 0) {
      ChatUI.modeTimeLabel.setVisible(false);
    } else {
      ChatUI.modeTimeLabel.setVisible(true);
      ChatUI.modeTimeLabel.setText("Abstimm-Zeit: " + time);
    }
  }

  /**
   * Sets the time for the game time.
   *
   * @param seconds Seconds and Minutes left
   */
  public static void setGameTimeLabel(int seconds) {
    String time = TimeSystem.formatTime(seconds);
    ChatUI.gameTimeLabel.setText("Verbleibende Zeit: " + time);
  }

  /**
   * Creates the chat-user-interface and adds it to the stage.
   *
   * @param stage The stage to which the chat interface is added.
   */
  public void create(Stage stage) {
    float SIDE_LINE = 8f;
    Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    rootTable = new Table();

    rootTable.setFillParent(true);

    Table chatTable = createChatTable(skin, SIDE_LINE);
    Table inputTable = createInputTable(skin, SIDE_LINE);

    rootTable.add(chatTable).width(Gdx.graphics.getWidth() * 0.15f).expandY().fillY();
    rootTable.add(inputTable).expand().fill();

    stage.addActor(rootTable);
  }

  private Table createInputTable(Skin skin, float SIDE_LINE) {
    Table rightSideTable = new Table();

    Table inputTableOuter = new Table();
    inputTableOuter.setVisible(true);
    inputTableOuter.setBackground(skin.getDrawable("blueBackground"));

    Table inputTableMiddle = new Table();
    inputTableMiddle.setVisible(true);
    inputTableMiddle.setBackground(skin.getDrawable("grayBackground"));

    Table inputTableInner = new Table();
    inputTableInner.setVisible(true);
    inputTableInner.setBackground(skin.getDrawable("darkGrayBackground"));

    textField = new TextField("", skin);
    textField.setMessageText("Einen Befehl Senden");
    textField.setTextFieldListener(
        (tF, c) -> {
          if (c == '\n') transmitMessage();
        });

    Button sendButton = new TextButton("Senden", skin);
    sendButton.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            transmitMessage();
          }
        });

    Button infoButton = new TextButton("Info", skin);
    infoButton.addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            infoTable.setVisible(!infoTable.isVisible());
          }
        });

    inputTableInner.add(textField).expandX().fillX().colspan(2).pad(15);
    inputTableInner.row();
    inputTableInner.add().expandX();
    inputTableInner.add(infoButton).width(Gdx.graphics.getWidth() * 0.05f).pad(5).right();
    inputTableInner.add(sendButton).width(Gdx.graphics.getWidth() * 0.05f).pad(5).right();

    inputTableMiddle.add(inputTableInner).expand().fill().pad(SIDE_LINE);

    inputTableOuter.add(inputTableMiddle).fillX().expand().fill().pad(SIDE_LINE, 0, 0, 0);
    inputTableOuter.bottom();

    CameraSystem.camera().zoom = CameraSystem.camera().zoom * 1.25f;

    Table gameTable = new Table();
    gameTable.left().top();

    infoTable = new Table();
    infoTable.setVisible(true);
    infoTable.setBackground(skin.getDrawable("grayBackground"));
    addCommandLabels(infoTable, skin, "MOVE", CommandParser.getMoveCommands());
    addCommandLabels(infoTable, skin, "QUIZ", CommandParser.getQuizCommands());
    addCommandLabels(infoTable, skin, "MENU", CommandParser.getMenuCommands());
    addCommandLabels(infoTable, skin, "GAME", CommandParser.getGameCommands());

    Table timeTable = new Table();
    gameTimeLabel = new Label("Verbleibende Zeit: XX:XX", skin);
    gameTimeLabel.setColor(1, 0, 0, 1);
    modeTimeLabel = new Label("Abstimm-Zeit: XX", skin);
    modeTimeLabel.setColor(1, 0, 0, 1);
    timeTable.add(gameTimeLabel).right().top().pad(5);
    timeTable.row();
    timeTable.add(modeTimeLabel).right().top().pad(5);

    gameTable.add(infoTable).expandX().left().top();
    gameTable.add(timeTable).right().top();

    rightSideTable.add(gameTable).expand().fill();
    rightSideTable.row();
    rightSideTable.add(inputTableOuter).fillX().height(Gdx.graphics.getHeight() * 0.15f);
    return rightSideTable;
  }

  private void addCommandLabels(
      Table infoTable, Skin skin, String category, Map<String, List<String>> commandMap) {
    for (Map.Entry<String, List<String>> entry : commandMap.entrySet()) {
      String content =
          category
              + " "
              + entry.getKey().toUpperCase()
              + ": "
              + String.join(", ", entry.getValue());
      infoTable.add(new Label(content, skin)).left().pad(5);
      infoTable.row();
    }
  }

  private Table createChatTable(Skin skin, float SIDE_LINE) {
    Table chatTableOuter = new Table();
    chatTableOuter.setVisible(true);
    chatTableOuter.setBackground(skin.getDrawable("blueBackground"));

    Table chatTableMiddle = new Table();
    chatTableMiddle.setVisible(true);
    chatTableMiddle.setBackground(skin.getDrawable("grayBackground"));

    Table chatTableInner = new Table();
    chatTableInner.setBackground(skin.getDrawable("darkGrayBackground"));
    chatTableInner.setVisible(true);

    Label title = new Label("Spiel-Chat:", skin);
    title.setColor(1, 1, 1, 1);
    title.setAlignment(Align.center);

    textArea = new TextArea("", skin);
    textArea.setDisabled(true);
    textArea.setColor(0.54f, 0.54f, 0.54f, 1);

    ScrollPane scrollPane = new ScrollPane(textArea, skin);
    scrollPane.layout();
    scrollPane.setScrollY(1f);

    chatTableInner.add(title).fillX().pad(5).pad(15, 0, 0, 0);
    chatTableInner.row();
    chatTableInner.add(scrollPane).expand().fill().pad(15);

    chatTableMiddle.add(chatTableInner).expand().fill().pad(SIDE_LINE);

    chatTableOuter.add(chatTableMiddle).expand().fill().pad(0, 0, 0, SIDE_LINE);
    chatTableOuter.left();
    return chatTableOuter;
  }

  private void transmitMessage() {
    String message = textField.getText().trim();

    if (!message.isEmpty()) {
      Game.network()
          .send(
              (short) Game.network().assignedClientId(),
              new ChatMessage((short) Game.network().assignedClientId(), message),
              true);
      textField.setText("");
    }
  }

  /**
   * Adds a message to the chat.
   *
   * @param playerID The ID of the player who sends the message
   * @param message The message which was sent
   */
  public void addMessage(short playerID, String message) {
    textArea.appendText("Teilnehmer " + playerID + ": " + message + "\n");
  }

  /**
   * Sets ui visible or invisible.
   *
   * @param visible True if invisible
   */
  public void setChatUIVisible(Boolean visible) {
    rootTable.setVisible(visible);
  }
}
