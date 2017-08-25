package net.avicus.compendium.points;

import lombok.ToString;
import org.bukkit.util.Vector;

@ToString
public class StaticAngleProvider implements AngleProvider {

  private final float angle;

  public StaticAngleProvider(float angle) {
    this.angle = angle;
  }

  @Override
  public float getAngle(Vector positionFrom) {
    return this.angle;
  }
}
