package aicogdev.interaction;

public class ResultatInteraction {
    public final boolean rightPrediction;
    public final int recompense;
    public final boolean isBored;

    public ResultatInteraction(boolean rightPrediction, int recompense, boolean isBored) {
        this.rightPrediction = rightPrediction;
        this.recompense = recompense;
        this.isBored = isBored;
    }


    @Override
    public String toString() {
        return "" + rightPrediction + " ; " + recompense + " ; " + !isBored;
    }
}
