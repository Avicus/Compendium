package net.avicus.compendium.points;

import lombok.Getter;
import lombok.ToString;
import org.bukkit.util.Vector;

@ToString
public class TargetYawProvider implements AngleProvider {

  @Getter
  private final Vector target;

  public TargetYawProvider(Vector target) {
    this.target = target;
  }

  @Override
  public float getAngle(Vector from) {
    double dx = this.target.getX() - from.getX();
    double dz = this.target.getZ() - from.getZ();
    return (float) Math.toDegrees(Math.atan2(-dx, dz));
  }
}
