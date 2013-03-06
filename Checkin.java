package com.aerilys.api.foursquare;

public class Checkin
{
	public String Id;
	private Venue venue;
	public String date;
	public int KilledZombies = 0;
	public int Points;
	public String Loots = "";
	public int AmmodUsed = 0;

	public Venue getVenue()
	{
		return venue;
	}

	public void setVenue(Venue venue)
	{
		this.venue = venue;
	}
}
