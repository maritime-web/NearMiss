package dk.dma.nearmiss.aissimulator;

public final class AisDataLine {
    private final String message;

    private final String time;

    AisDataLine(String message, String time) {
        this.message = message;
        this.time = time;
    }

    String getMessage() {
        return message;
    }

    String getTime() {
        return time;
    }

    String getTimedMessage() {
        String crLfString = getMessage();
        crLfString = crLfString.replace("!BS", "__r__n!BS");
        crLfString = crLfString.replace("!AI", "__r__n!AI");
        return String.format("%s%s", getTime(), crLfString);
    }
    //String getTimedMessage() {
    //    return String.format("%s\r\n%s", getTime(), getMessage());
    //}

    @Override
    public String toString() {
        return "AisDataLine{" +
                "message='" + message + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
