package me.skyrimfan1.spamm.listener.exceptions;

public class SpamCountRegistrationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public SpamCountRegistrationException(int warning, int punishing){
		super("Warning value ("+warning+") is greater than the punishing value ("+punishing+").");
	}
}
