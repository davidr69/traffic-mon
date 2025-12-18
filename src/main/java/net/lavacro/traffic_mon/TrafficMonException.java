package net.lavacro.traffic_mon;

public class TrafficMonException extends RuntimeException {
	public TrafficMonException(String message) {
		super(message);
	}

	public TrafficMonException(Exception e) {
		super(e);
	}

	public TrafficMonException(String message, Exception e) {
		super(message, e);
	}
}
