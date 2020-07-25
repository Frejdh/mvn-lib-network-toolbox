package com.frejdh.util.networking.exceptions;

public class CouldNotConnectException extends RuntimeException
{
	public CouldNotConnectException(String msg, Throwable exception)
	{
		super(msg, exception);
	}

	public CouldNotConnectException(String msg)
	{
		super(msg);
	}
	
	public CouldNotConnectException()
	{
		super();
	}
}
