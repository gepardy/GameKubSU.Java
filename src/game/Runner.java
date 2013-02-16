package game;

import java.awt.Color;

import client.ClientRunner;

import game.engine.Settings;
import game.engine.World;
import game.graphics.MainFrame;
import game.graphics.Renderer;
import game.physics.Physics;
import game.physics.objects.Circle;
import game.physics.objects.HalfPlane;
import game.physics.objects.InfoTip;
import game.physics.objects.Unit;
import game.physics.objects.Vehicle;
import game.server.Server;
import game.utils.Vector2d;

public class Runner {
	public final MainFrame mainFrame;
	public final World world;
	public final Renderer renderer;
	public final Server server;
	public final Physics physics;
	
	public InfoTip infoTick, infoRendererFPS, infoPhysFPS;
	
	public int tick = 0;

	public Runner() {
		physics = new Physics(this);
		mainFrame = new MainFrame(this);
		renderer = new Renderer(this);
		world = new World(this);
		server = new Server(this);
	}
	
	protected void delay(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void waitForTime(long time) {
		try {
			while(System.currentTimeMillis()<time)
				Thread.sleep(Settings.waitInterval);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void updateTick() {
		infoTick.message = String.format("ticks = %s", tick);
	}
	
	public void addUnit(Unit unit) {
		world.addUnit(unit);
		physics.addUnit(unit);
	}
	
	public void clearUnits() {
		world.clearUnits();
		physics.clearUnits();
	}
	
	protected void showGreeting() {
		InfoTip info = new InfoTip("Wait for client AI");
		info.position.assign(world.width/2, world.height/2);
		addUnit(info);
		mainFrame.mainCanvas.render();
		// test client
		new Thread(new ClientRunner()).start();
		//
		server.acceptClients();
	}

	protected void prepareGame() {
		clearUnits();
		// edges:
		addUnit(new HalfPlane(new Vector2d(0, 0), 0));
		addUnit(new HalfPlane(new Vector2d(world.width, 0), Math.PI));
		addUnit(new HalfPlane(new Vector2d(0, world.height), 3*Math.PI/2));
		addUnit(new HalfPlane(new Vector2d(0, 0), Math.PI/2));
		// test:
		Circle c = new Circle(100);
		c.mass = Math.pow(c.radius,2)*Math.PI*0.01;
		c.position.assign(Math.random()*world.width, Math.random()*world.height);
		c.speed.assign(Math.random()-0.5, Math.random()-0.5);
		c.speed.scale(20);
		addUnit(c);
		for(int i=0; i<10; i++)
		{
			c = new Circle((Math.random()+0.5)*8);
			c.mass = Math.pow(c.radius,2)*Math.PI*0.01;
			c.position.assign(Math.random()*world.width, Math.random()*world.height);
			c.speed.assign(Math.random()-0.5, Math.random()-0.5);
			c.speed.scale(2);
			addUnit(c);
		}
		
		// test vehicle:
		Vehicle v = new Vehicle(physics, 10);
		v.position.assign(10, 10);
		v.engine.powerFactor = 0.1;
		v.engine.turnFactor = 0.1;
		addUnit(v);
		server.clients.get(0).player.vehicles.add(v);
		
		infoTick = new InfoTip(String.format("Ticks=%s", tick));
		infoTick.isStatic = true;
		infoTick.color = Color.red;
		addUnit(infoTick);
		
		infoRendererFPS = new InfoTip("");
		infoRendererFPS.isStatic = true;
		infoRendererFPS.position.assign(0, 16);
		infoRendererFPS.color = Color.red;
		renderer.updateFPS();
		addUnit(infoRendererFPS);
		
		infoPhysFPS = new InfoTip("");
		infoPhysFPS.isStatic = true;
		infoPhysFPS.position.assign(0, 32);
		infoPhysFPS.color = Color.red;
		physics.updateFPS();
		addUnit(infoPhysFPS);
	}
	
	protected void showStats() {
		clearUnits();
		InfoTip info = new InfoTip("Game over");
		info.position = new Vector2d(world.width/2, world.height/2);
		addUnit(info);
		mainFrame.mainCanvas.render();
	}

	protected void tick() {
		server.tick();
		physics.tick();
		mainFrame.mainCanvas.render();
		tick++;
		world.tick = tick;
		updateTick();
	}
	
	public void start() {
		showGreeting();
		delay(Settings.waitBeforeDuration);
		prepareGame();
		while(tick < Settings.maxTicksCount)
		{
			long t1 = System.currentTimeMillis();
			tick();
			// specially for long ticks instead of delay:
			waitForTime(t1+Settings.tickDuration);
		}
		showStats();
		delay(Settings.waitAfterDuration);
		server.releaseClients();
		mainFrame.dispose();
	}

	public static void main(String[] args) {
		Runner runner = new Runner();
		runner.start();
	}

}
