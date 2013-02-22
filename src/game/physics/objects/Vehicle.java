package game.physics.objects;

import game.engine.Player;
import game.engine.Settings;
import game.physics.colliders.hooks.CollideEventHook;
import game.physics.colliders.hooks.VehicleCollide;
import game.physics.forces.BindedForce;
import game.physics.forces.ControlForce;
import game.utils.Vector2d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Vehicle extends Circle implements Serializable {
	private static final long serialVersionUID = 1563688715048158514L;
	
	private static transient BufferedImage image = null;
	public transient Player player;
	protected int indexInTeam;
	protected int playerId;
	protected String playerName;
	protected boolean isTeammate;
	protected transient Color color = Color.black;

	protected double health = 0.5;
	/*protected double armor;
	protected double fuel;
	protected double nitroFuel;*/
	
	public transient ControlForce engine;
	
	private Vehicle() {
		super(0);
	}
	
	public Vehicle(Player player) {
		super(Settings.Vehicle.defaultRadius);
		this.player = player;
		player.vehicles.add(this);
		new ControlForce(this);
		bindedForces = new ArrayList<BindedForce>();
		bindedForces.add(engine);
		collideEventHooks = new ArrayList<CollideEventHook>();
		collideEventHooks.add(new VehicleCollide(this));
	}
	
	public void doDamage(double healthDelta) {
		health = Math.min(Math.max(0, health - healthDelta), 1);
		// TODO add animation
	}
	
	public void addGoalPoints(int ptsCount) {
		player.score += ptsCount;
		// TODO add animation
	}
	
	public static void loadImages() {
		if(image == null)
			try {
				image = ImageIO.read(new File("res/car1.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		Color oldColor = graphics.getColor();
		if(Settings.Renderer.drawImages) {
			// loadImages();
			if(image != null) {
				AffineTransform oldTransform = graphics.getTransform();
				graphics.rotate(-angle, position.x, position.y);
				graphics.drawImage(image, (int)(position.x-radius), (int)(position.y-2*radius),
						(int)(2*radius), (int)(4*radius), null);
				graphics.setTransform(oldTransform);
			}
			else
				System.err.println("Vehicle::img not loaded");
		}
		else {
			graphics.setColor(color);
			super.draw(graphics);
			Vector2d n = new Vector2d(Math.sin(angle), Math.cos(angle));
			n.scale(radius);
			n.add(position);
			graphics.drawLine((int)position.x, (int)position.y, (int)n.x, (int)n.y);
		}
		graphics.setColor(Settings.Vehicle.HealthBar.defaultColor);
		graphics.fillRect((int)(position.x-radius), (int)(position.y-radius-Settings.Vehicle.HealthBar.descent-Settings.Vehicle.HealthBar.height),
				(int)(health*2*radius), Settings.Vehicle.HealthBar.height);
		graphics.setColor(Settings.Vehicle.HealthBar.borderColor);
		graphics.drawRect((int)(position.x-radius), (int)(position.y-radius-Settings.Vehicle.HealthBar.descent-Settings.Vehicle.HealthBar.height),
				(int)(2*radius), Settings.Vehicle.HealthBar.height);
		graphics.setColor(oldColor);
	}

}
