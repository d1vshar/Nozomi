package io.github.adorableskullmaster.nozomi.logging;

public class LogContext {
    private LogType logType;
    private String message;

    public LogContext(LogType logType, String message) {
        this.logType = logType;
        this.message = message;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
