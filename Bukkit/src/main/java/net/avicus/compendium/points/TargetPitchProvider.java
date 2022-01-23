package net.avicus.compendium.points;

import lombok.Getter;
import lombok.ToString;
import org.bukkit.util.Vector;

@ToString
public class TargetPitchProvider implements AngleProvider {

  @Getter
  private final Vector target;

  public TargetPitchProvider(Vector target) {
    this.target = target;
  }

  @Override
  public float getAngle(Vector from) {
    double dx = this.target.getX() - from.getX();
    double dz = this.target.getZ() - from.getZ();
    double distance = Math.sqrt(dx * dx + dz * dz);
    double dy = this.target.getY() - (from.getY() + 1.62);
    return (float) Math.toDegrees(Math.atan2(-dy, distance));
  }
}
