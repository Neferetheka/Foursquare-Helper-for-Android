package com.aerilys.api.foursquare;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aerilys.tools; 
import android.util.Log;

/*
*Useful class to do some generic stuff on 4sq API
*Use Helper tools for Android for http request part (Network helper)
*You can find it on Github: https://github.com/Neferetheka/Helper-Tools-for-Android
*Replace your client Id, your client secret and your redirection url. You might also want to change limits and shout visibility
*/
public abstract class FoursquareManager
{
	public static final String urlVenueApi = "https://api.foursquare.com/v2/venues/search?";
	public static final String CLIENTID = "CLIENTID";
	public static final String CLIENTSECRET = "CLIENTSECRET";
	public static final String loginUrl = "https://foursquare.com/oauth2/authenticate" + "?display=touch&client_id="
			+ FoursquareManager.CLIENTID + "&response_type=token" + "&redirect_uri=REDIRECTURL";
	public static final String profileUrl = "https://api.foursquare.com/v2/users/self";
	public static final String checkinUrl = "https://api.foursquare.com/v2/checkins/";
	public static final String myCheckinsUrl = "https://api.foursquare.com/v2/users/self/checkins";

	public static List<Venue> getVenuesAroundMe(double latitude, double longitude)
	{
		List<Venue> listVenues = new ArrayList<Venue>();

		String result = NetworkHelper.HttpRequest(urlVenueApi + "ll=" + latitude + "," + longitude + "&client_id="
				+ CLIENTID + "&client_secret=" + CLIENTSECRET + "&limit=42&v=" + getFormattedDate());

		JSONObject jsonReponse;
		try
		{
			jsonReponse = new JSONObject(result);
			JSONArray array = jsonReponse.getJSONObject("response").getJSONArray("venues");
			for (int i = 0; i < array.length(); i++)
			{
				JSONObject json = array.getJSONObject(i);
				try
				{
					listVenues.add(new Venue(json.getString("name"), json.getString("id"), json
							.getJSONObject("location"), json.getJSONArray("categories")));
				}
				catch (Exception e)
				{
					Log.e("Foursquare", "Error in 4sq json : " + e.getMessage());
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return listVenues;
	}

	public static String publishCheckin(Venue venue, String shout)
	{
		String url = FoursquareManager.checkinUrl + "add";
		HashMap<String, String> parameters = new HashMap<String, String>(4);
		parameters.put("oauth_token", DataContainer.getInstancePlayer().AccessToken);
		parameters.put("venueId", venue.Id);
		//parameters.put("broadcast", "private");
		parameters.put("shout", shout);
		parameters.put("v", FoursquareManager.getFormattedDate());
		return NetworkHelper.HttpRequest(url, parameters);
	}

	public static String getFormattedDate()
	{
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(date);
	}

	@SuppressWarnings("deprecation")
	public static List<Checkin> getCheckins(String accessToken)
	{
		List<Checkin> listCheckins = new ArrayList<Checkin>();
		String url = myCheckinsUrl + "?oauth_token=" + accessToken + "&limit=180&v="
				+ getFormattedDate();
		String result = NetworkHelper.HttpRequest(url);

		JSONObject jsonReponse;
		try
		{
			jsonReponse = new JSONObject(result);
			JSONArray array = jsonReponse.getJSONObject("response").getJSONObject("checkins").getJSONArray("items");
			Checkin checkin;
			for (int i = 0; i < array.length(); i++)
			{
				JSONObject json = array.getJSONObject(i);
				try
				{
					checkin = new Checkin();
					checkin.Id = json.getString("id");
					Long date = Long.parseLong(json.getString("createdAt")+"000");
					checkin.date = new Date(date).toLocaleString();
					json = json.getJSONObject("venue");
					checkin.setVenue(new Venue(json.getString("name"), json.getString("id"), json
							.getJSONObject("location"), json.getJSONArray("categories")));
					listCheckins.add(checkin);
				}
				catch (Exception e)
				{
					Log.e("Foursquare", "Error in 4sq json : " + e.getMessage());
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return listCheckins;
	}
}
