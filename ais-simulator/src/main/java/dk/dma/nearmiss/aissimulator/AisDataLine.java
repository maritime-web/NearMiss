package dk.dma.nearmiss.aissimulator;

public final class AisDataLine {
    private final String message;

    private final String time;

    AisDataLine(String message, String time) {
        this.message = message;
        this.time = time;
    }

    @SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }

    @SuppressWarnings("unused")
    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "AisDataLine{" +
                "message='" + message + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
