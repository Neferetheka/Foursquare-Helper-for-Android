package com.aerilys.api.foursquare;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Venue
{
	public String Name;
	public String Id;
	public String Adress = "";
	public String City = "";
	public int Distance = 0;
	public String Image;
	public String Category = "None";

	public Venue(String name, String id, JSONObject location, JSONArray categories) throws JSONException
	{
		Name = name;
		Id = id;

		if (Name == null)
			Name = "";

		if (location.has("address"))
			Adress = location.getString("address");
		if (location.has("city"))
			City = location.getString("city");
		if (location.has("distance"))
			Distance = Integer.parseInt(location.getString("distance"));

		if (categories.length() > 0)
		{
			Category = categories.getJSONObject(0).getString("name");
			Image = categories.getJSONObject(0).getJSONObject("icon").getString("prefix") + "88.png";
		}
		else
			Image = "";
	}
}
