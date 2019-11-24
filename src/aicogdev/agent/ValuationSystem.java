package aicogdev.agent;

import aicogdev.interaction.Interaction;

public class ValuationSystem {
    private int[][] values;

    private void initialize(int ... values) {
        this.values = new int[values.length / 2][2];

        for (int i = 0 ; i != values.length ; i = i + 2) {
            this.values[i / 2][0] = values[i];
            this.values[i / 2][1] = values[i + 1];
        }
    }

    public ValuationSystem(int... values) {
        initialize(values);
    }

    public ValuationSystem(String name) {
        switch (name) {
            case "TP4-Values1":
                initialize(1, 1, -1, -1);
                break;
            case "TP4-Values2":
                initialize(-1, -1, 1, 1);
                break;
            case "TP4-Values3":
                initialize(-1, 1, -1, 1);
                break;
            default:
                throw new RuntimeException("Unknown hard coded value system");
        }
    }

    public int getValue(int action, int reaction) {
        return values == null ? 0 : values[action - 1][reaction - 1];
    }

    public int getValue(Interaction interaction) {
        return values == null ? 0 : values[interaction.action - 1][interaction.reaction - 1];
    }
}
