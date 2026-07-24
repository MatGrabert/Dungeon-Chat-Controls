package chatEscapeRoom.level;

import contrib.components.CollideComponent;
import contrib.components.QuizNpcComponent;
import contrib.entities.NPCFactory;
import core.Entity;
import core.Game;
import core.components.DrawComponent;
import core.components.PositionComponent;
import core.components.VelocityComponent;
import core.level.DungeonLevel;
import core.level.elements.tile.ExitTile;
import core.level.utils.DesignLabel;
import core.level.utils.LevelElement;
import core.utils.Point;
import core.utils.components.path.SimpleIPath;

import java.util.Map;

public class Level02 extends DungeonLevel {
  private ExitTile exit;
  private Entity quizMaster;

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
    quizMaster = new Entity();
    quizMaster = NPCFactory.createNPC(new Point(3.5f,4.5f), "character/char03");
    quizMaster.add(new QuizNpcComponent());
    Game.add(quizMaster);
  }
}
