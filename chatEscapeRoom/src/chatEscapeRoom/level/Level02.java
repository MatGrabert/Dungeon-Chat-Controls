package chatEscapeRoom.level;

import contrib.components.QuizNpcComponent;
import contrib.entities.NPCFactory;
import core.Entity;
import core.Game;
import core.level.DungeonLevel;
import core.level.elements.tile.ExitTile;
import core.level.utils.DesignLabel;
import core.level.utils.LevelElement;
import core.utils.Point;
import java.util.Map;

/** Level 1 includes a quiz. */
public class Level02 extends DungeonLevel {
  private ExitTile exit;

  /**
   * Creates level 1.
   *
   * @param layout The layout of the level.
   * @param designLabel The design label of the level.
   * @param namedPoints The custom points of the level.
   */
  public Level02(LevelElement[][] layout, DesignLabel designLabel, Map<String, Point> namedPoints) {
    super(layout, designLabel, namedPoints, "Level 2");
  }

  @Override
  protected void onFirstTick() {
    exit = (ExitTile) Game.randomTile(LevelElement.EXIT).get();
    exit.close();
    createQuizMaster();
  }

  private void createQuizMaster() {
    Entity quizMaster = NPCFactory.createNPC(new Point(3.5f, 4.5f), "character/char03");
    quizMaster.add(new QuizNpcComponent());
    Game.add(quizMaster);
  }
}
