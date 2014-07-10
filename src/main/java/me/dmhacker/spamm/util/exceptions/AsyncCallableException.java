package me.dmhacker.spamm.util.exceptions;

public class AsyncCallableException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public AsyncCallableException(Exception e){
		super(e+" decided to be fickle. Report this to skyrimfan1 ASAP with the stacktrace.");
		e.printStackTrace();
	}

}
