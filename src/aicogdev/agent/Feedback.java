package aicogdev.agent;

import aicogdev.interaction.Interaction;

public class Feedback {
    private int[][] values;

    public Feedback(int[] values) {
        this.values = new int[values.length / 2][2];

        for (int i = 0 ; i != values.length ; i = i + 2) {
            this.values[i / 2][0] = values[i];
            this.values[i / 2][1] = values[i + 1];
        }
    }

    public int getValue(int action, int reaction) {
        return values == null ? 0 : values[action - 1][reaction - 1];
    }

    public int getValue(Interaction interaction) {
        return values == null ? 0 : values[interaction.action - 1][interaction.reaction - 1];
    }
}
