package br.com.serasaexperian.android.widget.circularprogressbar;

/**
 * Created by lepkoski on 1/12/17.
 */

public enum VerticalAlignment {
    TOP(0),
    BOTTOM(1),
    CENTER(2);

    private int id;

    private VerticalAlignment(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    static VerticalAlignment fromId(int id) {
        for (VerticalAlignment alignment : values()) {
            if (alignment.id == id) {
                return alignment;
            }
        }

        throw new IllegalArgumentException();
    }
}
