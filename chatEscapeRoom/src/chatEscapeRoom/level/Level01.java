package chatEscapeRoom.level;

import contrib.components.CollideComponent;
import contrib.entities.EntityFactory;
import core.Entity;
import core.Game;
import core.components.DrawComponent;
import core.components.PositionComponent;
import core.components.VelocityComponent;
import core.level.DungeonLevel;
import core.level.Tile;
import core.level.elements.tile.ExitTile;
import core.level.utils.DesignLabel;
import core.level.utils.LevelElement;
import core.utils.Point;
import core.utils.Vector2;
import core.utils.components.path.SimpleIPath;
import java.io.IOException;
import java.util.Map;

/** Level 1 includes a puzzle. */
public class Level01 extends DungeonLevel {
  private ExitTile exit;
  private final Point redTilePoint = new Point(6, 1);
  private final Point blueTilePoint = new Point(3, 1);
  private final Entity redStone = new Entity();
  private final Entity blueStone = new Entity();

  /**
   * Creates level 1.
   *
   * @param layout The layout of the level.
   * @param designLabel The design label of the level.
   * @param namedPoints The custom points of the level.
   */
  public Level01(LevelElement[][] layout, DesignLabel designLabel, Map<String, Point> namedPoints) {
    super(layout, designLabel, namedPoints, "Level 1");
  }

  @Override
  protected void onFirstTick() {
    exit = (ExitTile) Game.randomTile(LevelElement.EXIT).get();
    exit.close();
    createObjects();
  }

  @Override
  protected void onTick() {
    if (missionAccomplished()) {
      exit.open();
    }
  }

  private boolean missionAccomplished() {
    Point blueStonePoint = this.blueStone.fetch(PositionComponent.class).get().position();
    Point redStonePoint = this.redStone.fetch(PositionComponent.class).get().position();

    return blueStonePoint.equals(blueTilePoint) && redStonePoint.equals(redTilePoint);
  }

  private void createObjects() {
    int red = 0xFF0000FF;
    int blue = 0x0000FFFF;
    Entity vase1 = null;
    Entity vase2 = null;

    Tile redTile = Game.tileAt(this.redTilePoint).get();
    redTile.tintColor(red);

    Tile blueTile = Game.tileAt(this.blueTilePoint).get();
    blueTile.tintColor(blue);

    DrawComponent drawComponentBlue =
        new DrawComponent(new SimpleIPath("escapeRoom/assets/objects/push-stone.png"));
    DrawComponent drawComponentRed =
        new DrawComponent(new SimpleIPath("escapeRoom/assets/objects/push-stone.png"));
    drawComponentBlue.tintColor(blue);
    drawComponentRed.tintColor(red);

    redStone.add(new PositionComponent(new Point(6, 7)));
    redStone.add(new VelocityComponent(5.0f));
    redStone.add(new CollideComponent(Vector2.of(0.05f, 0.05f), Vector2.of(0.9f, 0.9f)));
    redStone.add(drawComponentBlue);
    // Swapped because the entity rendering order does not match the corresponding puzzle tiles.
    blueStone.add(drawComponentRed);
    blueStone.add(new PositionComponent(new Point(3, 7)));
    blueStone.add(new VelocityComponent(5.0f));
    blueStone.add(new CollideComponent(Vector2.of(0.05f, 0.05f), Vector2.of(0.9f, 0.9f)));

    try {
      vase1 = EntityFactory.newVase(new Point(4, 5));
      vase1.add(new VelocityComponent(6.0f));
      vase1.add(new CollideComponent(Vector2.of(0.05f, 0.05f), Vector2.of(0.9f, 0.9f)));
      vase2 = EntityFactory.newVase(new Point(4, 3));
      vase2.add(new VelocityComponent(5.0f));
      vase2.add(new CollideComponent(Vector2.of(0.05f, 0.05f), Vector2.of(0.9f, 0.9f)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Game.add(redStone);
    Game.add(blueStone);
    Game.add(vase1);
    Game.add(vase2);
  }
}
