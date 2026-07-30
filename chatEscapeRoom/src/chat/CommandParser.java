package chat;

import com.badlogic.gdx.Gdx;
import core.utils.JsonHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import quiz.Quiz;

/**
 * Manages the reading and processing of the chat commands.
 *
 * <p>Commands defined in a JSON file are loaded and assigned to the corresponding categories. Chat
 * inputs are subsequently analyzed and forwarded to the {@link CommandExecutor}.
 */
public class CommandParser {
  private static final Map<String, List<String>> moveCommands = new HashMap<>();
  private static final Map<String, List<String>> quizCommands = new HashMap<>();
  private static final Map<String, List<String>> menuCommands = new HashMap<>();
  private static final Map<String, List<String>> gameCommands = new HashMap<>();

  /**
   * Gets a map of move commands.
   *
   * @return The move commands as map.
   */
  public static Map<String, List<String>> getMoveCommands() {
    return moveCommands;
  }

  /**
   * Gets a map of quiz commands.
   *
   * @return The quiz commands as map.
   */
  public static Map<String, List<String>> getQuizCommands() {
    return quizCommands;
  }

  /**
   * Gets a map of menu commands.
   *
   * @return The menu commands as map.
   */
  public static Map<String, List<String>> getMenuCommands() {
    return menuCommands;
  }

  /**
   * Gets a map of game commands.
   *
   * @return The game commands as map.
   */
  public static Map<String, List<String>> getGameCommands() {
    return gameCommands;
  }

  /** Loads the commands from the JSON file and stored them to the category maps. */
  public static void loadCommands() {
    String commandPath = "comands/chat-commands.json";
    String contentAsString = Gdx.files.internal(commandPath).readString("UTF-8");
    Map<String, Object> contentAsMap = JsonHandler.readJson(contentAsString);

    fillCategoryMap(contentAsMap, "move", moveCommands);
    fillCategoryMap(contentAsMap, "quiz", quizCommands);
    fillCategoryMap(contentAsMap, "menu", menuCommands);
    fillCategoryMap(contentAsMap, "game", gameCommands);
  }

  private static void fillCategoryMap(
      Map<String, Object> contentAsMap, String category, Map<String, List<String>> categoryMap) {
    Map<?, ?> categoryTempMap = (Map<?, ?>) contentAsMap.get(category);

    for (Map.Entry<?, ?> entry : categoryTempMap.entrySet()) {
      String command = String.valueOf(entry.getKey());
      Map<?, ?> aliasMap = (Map<?, ?>) entry.getValue();

      List<String> aliases = new ArrayList<>();

      for (Object alias : (List<?>) aliasMap.get("aliases")) {
        aliases.add(String.valueOf(alias));
      }

      categoryMap.put(command, aliases);
    }
  }

  /**
   * Analyzed the chat input and forwarded a correct command.
   *
   * @param input The chat input to be analyzed.
   */
  public static void parseChatInput(String input) {
    String parsedInput = input;

    if (sameChars(input)) {
      parsedInput = String.valueOf(input.charAt(0));
    }

    String lowerCaseInput = parsedInput.toLowerCase();

    checkInput(lowerCaseInput);
  }

  private static boolean sameChars(String input) {
    String cleanedString = input.toLowerCase().replace(" ", "");
    char firstChar = cleanedString.charAt(0);

    for (int i = 1; i < cleanedString.length(); i++) {
      if (cleanedString.charAt(i) != firstChar) {
        return false;
      }
    }
    return true;
  }

  private static void checkInput(String input) {
    String foundCommand = null;

    if (Quiz.isActive()) {
      foundCommand = searchCommand(input, foundCommand, "quiz", quizCommands);
    } else {
      foundCommand = searchCommand(input, foundCommand, "move", moveCommands);
    }
    foundCommand = searchCommand(input, foundCommand, "menu", menuCommands);
    foundCommand = searchCommand(input, foundCommand, "game", gameCommands);

    if (foundCommand != null && !foundCommand.equals("INVALID")) {
      CommandExecutor.execute(foundCommand.split(",")[0], foundCommand.split(",")[1]);
    }
  }

  private static String searchCommand(
      String input, String foundCommand, String category, Map<String, List<String>> commandMap) {
    String command = compareInput(input, category, commandMap);

    if (foundCommand == null) {
      return command;
    }
    if (command != null) {
      return "INVALID";
    }
    return foundCommand;
  }

  private static String compareInput(
      String input, String category, Map<String, List<String>> commandMap) {
    String foundCommand = null;

    for (String key : commandMap.keySet()) {
      for (String alias : commandMap.get(key)) {
        if (alias.length() == 1) {
          if (input.length() == 1) {
            if (Objects.equals(input, alias) && foundCommand == null) {
              foundCommand = category + "," + key;
            } else if (Objects.equals(input, alias)) {
              if (!foundCommand.equals(category + "," + key)) {
                return "INVALID";
              } else {
                foundCommand = category + "," + key;
              }
            }
          }
        } else {
          if (input.contains(alias) && foundCommand == null) {
            foundCommand = category + "," + key;
          } else if (input.contains(alias)) {
            if (!foundCommand.equals(category + "," + key)) {
              return "INVALID";
            } else {
              foundCommand = category + "," + key;
            }
          }
        }
      }
    }

    return foundCommand;
  }
}
