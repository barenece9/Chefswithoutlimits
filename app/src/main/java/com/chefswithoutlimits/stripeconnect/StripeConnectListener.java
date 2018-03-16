package com.chefswithoutlimits.stripeconnect;

public interface StripeConnectListener {
	
	public abstract void onConnected();
	
	public abstract void onDisconnected();

	public abstract void onError(String error);
	
}