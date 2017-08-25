package net.avicus.compendium.number;

public class PreparedNumberAction {

  private final Number value;
  private final NumberAction action;

  public PreparedNumberAction(Number value, NumberAction action) {
    this.value = value;
    this.action = action;
  }

  public double perform(double current) {
    return this.action.perform(current, this.value.doubleValue());
  }

  public float perform(float current) {
    return this.action.perform(current, this.value.floatValue());
  }

  public long perform(long current) {
    return this.action.perform(current, this.value.longValue());
  }

  public int perform(int current) {
    return this.action.perform(current, this.value.intValue());
  }
}
