package client.model;

import client.json.JSONClassCheckException;
import client.json.JSONObject;
import client.json.JSONSerializable;
import client.utils.Vector2d;

public abstract class Unit implements JSONSerializable {
	public int id;
	public Vector2d position = new Vector2d();
	public Vector2d speed = new Vector2d();
	public double angle = 0;
	public double angularSpeed = 0;
	
	@Override
	public String getClassName() {
		return "Unit";
	}
	
	@Override
	public JSONObject toJSON() {
		return null;
	}
	
	@Override
	public void fromJSON(JSONObject json) throws JSONClassCheckException {
		if(!json.has("class") || !json.getString("class").equals(getClassName()))
			throw new JSONClassCheckException(getClassName());
		id = json.getInt("id");
		position.fromJSON(json.getJSONObject("position"));
		speed.fromJSON(json.getJSONObject("speed"));
		angle = json.getDouble("angle");
		angularSpeed = json.getDouble("angularSpeed");
	}
	
}
