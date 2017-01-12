package br.com.serasaexperian.android.widget.circularprogressbar;

/**
 * Created by lepkoski on 1/12/17.
 */

public enum HorizontalAlignment {
    LEFT(0),
    RIGHT(1),
    CENTER(2);

    private int id;

    private HorizontalAlignment(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    static HorizontalAlignment fromId(int id) {
        for (HorizontalAlignment alignment : values()) {
            if (alignment.id == id) {
                return alignment;
            }
        }

        throw new IllegalArgumentException();
    }
}