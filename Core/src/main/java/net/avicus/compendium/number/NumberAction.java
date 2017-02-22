package net.avicus.compendium.number;

/**
 * Performs an operation on a number.
 */
public interface NumberAction {
    NumberAction NONE = (original, modify) -> original;

    NumberAction SET = (original, modify) -> modify;

    NumberAction ADD = (original, modify) -> original + modify;

    NumberAction SUBTRACT = (original, modify) -> original - modify;

    NumberAction DIVIDE = (original, modify) -> Math.floor(original / modify);

    NumberAction MULTIPLY = (original, modify) -> original * modify;

    NumberAction POWER = Math::pow;

    /**
     * Performs the action on a value.
     *
     * @param original The original value.
     * @param modify   The value that modifies the original value.
     * @return The resultant double.
     */
    double perform(double original, double modify);

    /**
     * Performs the action on a float value.
     *
     * @param original
     * @param modify
     * @return
     */
    default float perform(float original, float modify) {
        return (float) perform((double) original, (double) modify);
    }

    /**
     * Performs the action on a long value.
     *
     * @param original
     * @param modify
     * @return
     */
    default long perform(long original, long modify) {
        return (long) perform(new Long(original).doubleValue(), new Long(modify).doubleValue());
    }

    /**
     * Performs the action on an integer value.
     *
     * @param original
     * @param modify
     * @return
     */
    default int perform(int original, int modify) {
        return (int) Math.floor(perform((float) original, (float) modify));
    }
}