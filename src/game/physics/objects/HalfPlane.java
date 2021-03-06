package game.physics.objects;

import game.utils.Vector2d;

import java.awt.Graphics2D;

// position of this object is control point
// normal to the edge of plane sets by an angle
public class HalfPlane extends DrawableUnit {
	private static final long serialVersionUID = 5013907462403385255L;

	public HalfPlane(Vector2d position, double angle) {
		this.position.assign(position);
		this.angle = angle;
		isStatic = true;
	}

	@Override
	public void draw(Graphics2D graphics) {
	}

}
