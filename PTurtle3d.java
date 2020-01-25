package pturtle;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PMatrix3D;
import processing.core.PVector;

/**
 * Turtle that utilizes Processing’s graphics tools to draw 3D shapes, keeping track of position and angle as it draws.
 * The current Processing instance must be using P3D graphics. By default, the pen is down. Rotations are handled Y
 * (yaw) first, then X (pitch).
 */
public class PTurtle3d {
	
	private PGraphics graphics;
	
	private PVector pos;

	private float yAngle;
	private float xAngle;
	
	private float spin;
	
	private float penWidth;
	private float penHeight;
	
	private boolean penDown;
	
	private int penColor;
	
	private PenLine penLine;
	
	private PTurtle3d subturtle;
	private PTurtle3d superturtle;

	/**
	 * Creates a new turtle starting at the origin.
	 * @param graphicsInit Reference to the target PGraphics.
	 */
	public PTurtle3d(PGraphics graphicsInit) {
		this(graphicsInit, 0, 0, 0);
	}

	/**
	 * Creates a new turtle starting at the specified coordinates.
	 * @param graphicsInit Reference to the target PGraphics.
	 */
	public PTurtle3d(PGraphics graphicsInit, float xInit, float yInit) {
		this(graphicsInit, xInit, yInit, 0);
	}
	
	/**
	 * Creates a new turtle starting at the specified coordinates.
	 * @param graphicsInit Reference to the target PGraphics.
	 * @param xInit Initial X position of the turtle.
	 * @param yInit Initial Y position of the turtle.
	 * @param zInit Initial Z position of the turtle.
	 */
	public PTurtle3d(PGraphics graphicsInit, float xInit, float yInit, float zInit) {
		graphics = graphicsInit;

		pos(new PVector(xInit, yInit, zInit));

		yAngle(0);
		xAngle(0);
		
		spin(0);

		penSize(1);
		
		penDown(true);
		
		penColor(graphics.color(255));
		
		penLine(PenLine.BOX);
		
		subturtle = null;
		superturtle = null;
	}
	
	/**
	 * Constants to specify a turtle’s line shape.
	 */
	public static enum PenLine {
		LINE, BOX;
	}

	private static float validateCoordinate(float px) {
		if (Float.isNaN(px) || Float.isInfinite(px)) {
			throw new IllegalArgumentException("invalid coordinate");
		}
		
		return px;
	}
	
	/**
	 * Verifies that an angle measure, in radians, is valid and moves it into [0, 2π).
	 * @param rad Angle measure to be checked.
	 * @return The moduloed angle measure.
	 */
	private static float validateAngle(float rad) {
		if (Double.isNaN(rad) || Double.isInfinite(rad)) {
			throw new IllegalArgumentException("invalid angle");
		}
		
		return mod(rad, PApplet.TAU);
	}
	
	/**
	 * Verifies that a pen size is valid.
	 * @param px Pen size to be checked.
	 * @return The pen size.
	 */
	private static float validatePenSize(float px) {
		if (px < 0 || Float.isNaN(px) || Float.isInfinite(px)) {
			throw new IllegalArgumentException("invalid pen size");
		}
		
		return px;
	}
	
	private static float sq(float n) {
		return n * n;
	}
	
	private static float mod(float dend, float dsor) {
		return (dend % dsor + dsor) % dsor;
	}
	
	/**
	 * Gets the turtle’s graphics object.
	 * @return The graphics object.
	 */
	public PGraphics graphics() {
		return graphics;
	}
	
	/**
	 * Gets the turtle’s current position vector.
	 * @return The position vector.
	 */
	public PVector pos() {
		return pos;
	}
	/**
	 * Sets the turtle’s current position vector. The turtle will not draw a line.
	 * @param posDest The target vector. This object will be cloned before assignment and will remain unchanged.
	 * @return This turtle.
	 */
	public PTurtle3d pos(PVector posDest) {
		pos = posDest.copy();
		return this;
	}
	
	/**
	 * Gets the turtle’s current X coordinate.
	 * @return The X coordinate.
	 */
	public float x() {
		return pos.x;
	}
	/**
	 * Sets the turtle’s current X coordinate. The turtle will not draw a line.
	 * @param value The target X coordinate.
	 * @return This turtle.
	 */
	public PTurtle3d x(float value) {
		pos.x = validateCoordinate(value);
		return this;
	}

	/**
	 * Gets the turtle’s current Y coordinate.
	 * @return The Y coordinate.
	 */
	public float y() {
		return pos.y;
	}
	/**
	 * Sets the turtle’s current Y coordinate. The turtle will not draw a line.
	 * @param value The target Y coordinate.
	 * @return This turtle.
	 */
	public PTurtle3d y(float value) {
		pos.y = validateCoordinate(value);
		return this;
	}

	/**
	 * Gets the turtle’s current Z coordinate.
	 * @return The Z coordinate.
	 */
	public float z() {
		return pos.z;
	}
	/**
	 * Sets the turtle’s current Z coordinate. The turtle will not draw a line.
	 * @param value The target Z coordinate.
	 * @return This turtle.
	 */
	public PTurtle3d z(float value) {
		pos.z = validateCoordinate(value);
		return this;
	}
	
	/**
	 * Gets the turtle’s rotation around the Y axis (yaw).
	 * @return The Y angle.
	 */
	public float yAngle() {
		return yAngle;
	}
	/**
	 * Sets the turtle’s rotation around the Y axis (yaw).
	 * @param value Angle to which to set the Y rotation.
	 * @return This turtle.
	 */
	public PTurtle3d yAngle(float value) {
		yAngle = validateAngle(value);
		return this;
	}
	/**
	 * Adds to the turtle’s rotation around the Y axis (yaw).
	 * @param increment Angle by which to rotate around the Y axis.
	 * @return This turtle.
	 */
	public PTurtle3d yRotate(float increment) {
		yAngle = validateAngle(yAngle + increment);
		return this;
	}
	
	/**
	 * Gets the turtle’s rotation around the X axis (pitch).
	 * @return The X angle.
	 */
	public float xAngle() {
		return xAngle;
	}
	/**
	 * Sets the turtle’s rotation around the X axis (pitch).
	 * @param value Angle to which to set the X rotation.
	 * @return This turtle.
	 */
	public PTurtle3d xAngle(float value) {
		xAngle = validateAngle(value);
		return this;
	}
	/**
	 * Adds to the turtle’s rotation around the X axis (pitch).
	 * @param increment Angle by which to rotate around the X axis.
	 * @return This turtle.
	 */
	public PTurtle3d xRotate(float increment) {
		xAngle = validateAngle(xAngle + increment);
		return this;
	}
	
	/**
	 * Gets the turtle’s spin angle.
	 * @return The spin angle.
	 */
	public float spin() {
		return spin;
	}
	/**
	 * Sets the turtle’s spin angle.
	 * @param value The target spin angle.
	 * @return This turtle.
	 */
	public PTurtle3d spin(float value) {
		spin = validateAngle(value);
		return this;
	}
	/**
	 * Adds to the turtle’s spin rotation.
	 * @param increment Angle by which to spin.
	 * @return This turtle.
	 */
	public PTurtle3d spinRotate(float increment) {
		spin = validateAngle(spin + increment);
		return this;
	}
	
	/**
	 * Sets the turtle’s pen width and height.
	 * @param px Size at which to draw future shapes.
	 * @return This turtle.
	 */
	public PTurtle3d penSize(float px) {
		penWidth(px).penHeight(px);
		return this;
	}
	
	/**
	 * Gets the turtle’s pen width.
	 * @return The pen width.
	 */
	public float penWidth() {
		return penWidth;
	}
	/**
	 * Sets the turtle’s pen width.
	 * @param px Width at which to draw future shapes.
	 * @return This turtle.
	 */
	public PTurtle3d penWidth(float px) {
		penWidth = validatePenSize(px);
		return this;
	}
	
	/**
	 * Gets the turtle’s pen height.
	 * @return The pen height.
	 */
	public float penHeight() {
		return penHeight;
	}
	/**
	 * Sets the turtle’s pen height.
	 * @param px Height at which to draw future shapes.
	 * @return This turtle.
	 */
	public PTurtle3d penHeight(float px) {
		penHeight = validatePenSize(px);
		return this;
	}

	/**
	 * Gets whether the pen is currently down.
	 * @return Whether the pen is currently down.
	 */
	public boolean penDown() {
		return penDown;
	}
	/**
	 * Sets whether the pen is currently down.
	 * @param down Whether the pen should be down.
	 * @return This turtle.
	 */
	public PTurtle3d penDown(boolean down) {
		penDown = down;
		return this;
	}
	/**
	 * Lifts or drops the pen, depending on whether it is currently lifted.
	 * @return This turtle.
	 */
	public PTurtle3d togglePen() {
		penDown = !penDown;
		return this;
	}

	/**
	 * Gets this turtle’s current pen color.
	 * @return The pen color.
	 */
	public int penColor() {
		return penColor;
	}
	/**
	 * Sets this turtle’s current pen color.
	 * @param color The target pen color.
	 * @return This turtle.
	 */
	public PTurtle3d penColor(int color) {
		penColor = color;
		return this;
	}
	
	/**
	 * Gets the current pen mode.
	 * @return The pen mode.
	 */
	public PenLine penLine() {
		return penLine;
	}
	/**
	 * Sets the mode used when drawing.
	 * @param mode One of the constants on `PTurtle3d.PenLine`.
	 * @return This turtle.
	 */
	public PTurtle3d penLine(PenLine mode) {
		penLine = mode;
		return this;
	}
	
	/**
	 * Gets the turtle’s direction as a unit vector.
	 * @return The PVector.
	 */
	public PVector dir() {
		return new PVector(
				PApplet.sin(yAngle) * PApplet.cos(xAngle),
				-PApplet.sin(xAngle),
				PApplet.cos(yAngle) * PApplet.cos(xAngle)
		).normalize();
	}
	/**
	 * Sets the turtle’s direction vector. The direction vector will automatically be normalized when it is set.
	 * @param dirDest The new direction vector. This object will remain unchanged.
	 * @return This turtle.
	 */
	public PTurtle3d dir(PVector dirDest) {
		face(pos.x + dirDest.x, pos.y + dirDest.y, pos.z + dirDest.z);
		return this;
	}
	
	/**
	 * Adds any number of vectors to this turtle’s current direction vector. The final resulting vector will be
	 * normalized before it is set.
	 * @param dirDests The vectors to be added. These objects will remain unchanged.
	 * @return This turtle.
	 */
	public PTurtle3d dirAdd(PVector... dirDests) {
		final PVector result = dir();
		for (PVector dirDest : dirDests) {
			result.add(dirDest);
		}
		dir(result);
		
		return this;
	}
	
	/**
	 * Moves the turtle forward by a provided distance.
	 * @param px Number of steps forward to move the turtle.
	 * @return This turtle.
	 */
	public PTurtle3d forward(float px) {
		forwardProject(px);

		pos.add(PVector.mult(dir(), px));

		return this;
	}

	/**
	 * Draws from the turtle as if it had moved forward, but does not update its position.
	 * @param px Number of steps forward to move the turtle.
	 * @return This turtle.
	 */
	public PTurtle3d forwardProject(float px) {
		if (!penDown) return this;
		
		graphics.push();
		transformToPosition();
		
		switch (penLine) {
			case BOX:
				graphics.fill(penColor);
				
				// Boxes are drawn from the center point (this translation is to accommodate)
				graphics.translate(0, 0, px / 2);
				graphics.scale(penWidth, penHeight, px);
				
				graphics.box(1, 1, 1);
				break;
				
			case LINE:
				graphics.stroke(penColor);
				graphics.strokeWeight(penWidth);
				
				graphics.line(0, 0, 0, 0, 0, px);
		}
		
		graphics.pop();
		
		return this;
	}
	
	/**
	 * Draws a sphere, three times as wide as the pen width, at the turtle’s current position.
	 * @return This turtle.
	 */
	public PTurtle3d dot() {
		return dot(1.5f * penWidth);
	}
	/**
	 * Draws a sphere at the turtle’s current position.
	 * @param pxRadius Radius of the sphere.
	 * @return This turtle.
	 */
	public PTurtle3d dot(float pxRadius) {
		if (penDown) {
			graphics.push();
			transformToPosition();

			graphics.fill(penColor);
			graphics.sphere(pxRadius);
			
			graphics.pop();
		}
		
		return this;
	}
	
	/**
	 * Draws two circles at the turtle’s current position and two radii representing the turtle’s current direction.
	 * @param radius Radius of the figure.
	 * @return This turtle.
	 */
	public PTurtle3d mark(float radius) {
		graphics.push();
		transformToPositionWithoutLastRotation();
		
		graphics.noFill();
		graphics.strokeWeight(2);
		
		// Y plane
		graphics.rotateY(yAngle);
		graphics.rotateX(PApplet.HALF_PI);
		
		graphics.stroke(255, 0, 0, 127);
		graphics.circle(0, 0, 2 * radius);

		graphics.stroke(255, 0, 0);
		graphics.line(0, 0, 0, 0, radius, 0);
		
		// X plane
		graphics.rotateY(PApplet.HALF_PI);

		graphics.stroke(0, 255, 0, 127);
		graphics.circle(0, 0, 2 * radius);

		graphics.rotateZ(xAngle);
		graphics.stroke(255);
		graphics.line(0, 0, 0, 0, radius, 0);
		
		graphics.pop();
		
		return this;
	}
	/**
	 * Draws two circles at the turtle’s current position and two radii representing the turtle’s current direction, at
	 * three times the current pen width.
	 * @return This turtle.
	 */
	public PTurtle3d mark() {
		return mark(penWidth * 1.5f);
	}

	/**
	 * Moves the turtle to the provided location vector.
	 * @param point A vector containing the target point’s coordinates.
	 * @return This turtle.
	 */
	public PTurtle3d moveTo(PVector point) {
		return moveTo(point.x, point.y, point.z);
	}
	/**
	 * Moves the turtle to the provided absolute coordinates.
	 * @param xDest The X coordinate of the target point.
	 * @param yDest The Y coordinate of the target point.
	 * @return This turtle.
	 */
	public PTurtle3d moveTo(float xDest, float yDest) {
		return moveTo(xDest, yDest, pos.z);
	}
	/**
	 * Moves the turtle to the provided absolute coordinates.
	 * @param xDest The X coordinate of the target point.
	 * @param yDest The Y coordinate of the target point.
	 * @param zDest The Z coordinate of the target point.
	 * @return This turtle.
	 */
	public PTurtle3d moveTo(float xDest, float yDest, float zDest) {
		final float yAngleInitial = yAngle;
		final float xAngleInitial = xAngle;

		final float dist = PApplet.sqrt(sq(xDest - pos.x) + sq(yDest - pos.y) + sq(zDest - pos.z));
		
		// Draws the line
		face(xDest, yDest, zDest).forwardProject(dist);
		// Moves to the given position (avoids rounding error)
		pos(new PVector(xDest, yDest, zDest));
		// Restores the original angle
		yAngle(yAngleInitial).xAngle(xAngleInitial);
		
		return this;
	}

	/**
	 * Set the turtle’s angles to face a specified point.
	 * @param xDest The X coordinate of the target point.
	 * @param yDest The Y coordinate of the target point.
	 * @return This turtle.
	 */
	public PTurtle3d face(float xDest, float yDest) {
		return face(xDest, yDest, pos.z);
	}
	/**
	 * Set the turtle’s angles to face a specified point.
	 * @param xDest The X coordinate of the target point.
	 * @param yDest The Y coordinate of the target point.
	 * @param zDest The Z coordinate of the target point.
	 * @return This turtle.
	 */
	public PTurtle3d face(float xDest, float yDest, float zDest) {
		yAngle((PApplet.PI / 2 - PApplet.atan2(zDest - pos.z, xDest - pos.x)));
		xAngle(PApplet.atan2(-(yDest - pos.y), PApplet.sqrt(sq(xDest - pos.x) + sq(zDest - pos.z))));
		return this;
	}
	/**
	 * Set the turtle’s angles to face a specified point.
	 * @param point A vector containing the point’s coordinates.
	 * @return This turtle.
	 */
	public PTurtle3d face(PVector point) {
		face(point.x, point.y, point.z);
		return this;
	}
	
	/**
	 * Creates a new subturtle and assigns it to the current turtle. A subturtle’s space is transformed so that all of
	 * its positioning and direction methods and values run relative to its superturtle’s current position and
	 * direction, that is, (x, y, z) = (0, 0, 0) is the superturtle’s position and (yAngle, xAngle) = (0, 0) is the
	 * superturtle’s direction.
	 * @param inheritStyle Whether to copy this turtle’s pen size and color.
	 * @return The subturtle.
	 */
	public PTurtle3d assignSubturtle(boolean inheritStyle) {
		if (subturtle != null) {
			discardSubturtle();
		}
		
		subturtle = new PTurtle3d(graphics, 0, 0);
		if (inheritStyle) {
			subturtle.penWidth(penWidth).penHeight(penHeight).penColor(penColor);
		}
		
		subturtle.superturtle = this;
		
		return subturtle;
	}
	/**
	 * Creates a new subturtle and assigns it to the current turtle. A subturtle’s space is transformed so that all of
	 * its positioning and direction methods and values run relative to its superturtle’s current position and
	 * direction, that is, (x, y, z) = (0, 0, 0) is the superturtle’s position and (yAngle, xAngle) = (0, 0) is the
	 * superturtle’s direction.
	 * @return The subturtle.
	 */
	public PTurtle3d assignSubturtle() {
		return assignSubturtle(false);
	}
	
	/**
	 * Drops the turtle’s subturtle.
	 * @return This turtle.
	 */
	public PTurtle3d discardSubturtle() {
		subturtle.superturtle = null;
		
		subturtle = null;
		
		return this;
	}
	/**
	 * Releases this subturtle from its parent.
	 * @return The superturtle.
	 */
	public PTurtle3d discard() {
		return superturtle.discardSubturtle();
	}
	
	/**
	 * Gets this turtle’s superturtle.
	 * @return The superturtle.
	 */
	public PTurtle3d superturtle() {
		return superturtle;
	}

	/**
	 * Gets this turtle’s subturtle.
	 * @return The subturtle.
	 */
	public PTurtle3d subturtle() {
		return subturtle;
	}
	
	/**
	 * Gets the turtle at the top of this turtle’s superturtle chain.
	 * @return The topmost superturtle.
	 */
	public PTurtle3d originTurtle() {
		PTurtle3d currentTurtle = this;
		while (currentTurtle.superturtle != null) {
			currentTurtle = currentTurtle.superturtle;
		}
		return currentTurtle;
	}
	
	/**
	 * Converts a point in the graphics’s context to the equivalent within this subturtle’s transformed space.
	 * @param posDest A vector representing the X, Y, and Z coordinates of the target point in the applet’s
	 * context. This object will remain unchanged.
	 * @return A new vector with the point’s X, Y, and Z coordinates.
	 */
	public PVector posInTopContext(PVector posDest) {
		return cancelingTransformationChain(posDest, false);
	}
	/**
	 * Converts a set of XYZ coordinates in the graphics’s context to the equivalent within this subturtle’s
	 * transformed space.
	 * @param xDest The X coordinate of the target point.
	 * @param yDest The Y coordinate of the target point.
	 * @param zDest The Z coordinate of the target point.
	 * @return A new vector with the point’s X, Y, and Z coordinates.
	 */
	public PVector posInTopContext(float xDest, float yDest, float zDest) {
		return posInTopContext(new PVector(xDest, yDest, zDest));
	}

	/**
	 * Converts a direction vector in the graphics’s context to the direction within this subturtle’s transformed
	 * space.
	 * @param dirDest The target direction vector. This object will remain unchanged.
	 * @return The new direction vector.
	 */
	public PVector dirInTopContext(PVector dirDest) {
		return cancelingTransformationChain(dirDest, true).normalize();
	}

	/**
	 * Calculates the matrix that reflects the transformations this turtle applies before drawing.
	 * @return The matrix.
	 */
	public PMatrix3D transformationMatrix() {
		return transformationMatrix(true);
	}
	/**
	 * Calculates the matrix that reflects the transformations this turtle applies before drawing.
	 * @param includeTranslation Whether to translate the matrix to the turtle’s current position.
	 * @return The matrix.
	 */
	public PMatrix3D transformationMatrix(boolean includeTranslation) {
		final PMatrix3D matrix = new PMatrix3D();
		matrix.reset();
		
		if (includeTranslation) {
			matrix.translate(pos.x, pos.y, pos.z);
		}
		
		matrix.rotateY(yAngle);
		matrix.rotateX(xAngle);
		
		matrix.rotateZ(spin);
		
		return matrix;
	}
	
	/**
	 * Creates a new turtle with this turtle’s current position, direction, and style.
	 * @return The new turtle.
	 */
	public PTurtle3d clone() {
		return new PTurtle3d(graphics).pos(pos).yAngle(yAngle).xAngle(xAngle).penDown(penDown).penColor(penColor);
	}
	
	/**
	 * Moves this turtle to a different given turtle’s position and angle. The movement will draw a line if the pen is
	 * down.
	 * @param reference The turtle whose position should be copied over.
	 * @return This turtle.
	 */
	public PTurtle3d imitate(PTurtle3d reference) {
		moveTo(reference.x(), reference.y(), reference.z()).yAngle(reference.yAngle()).xAngle(reference.xAngle());
		return this;
	}
	
	/**
	 * Transforms the canvas according to the turtle’s saved position, preparing it for drawing.
	 * @return This turtle.
	 */
	private PTurtle3d transformToPosition() {
		transformToPositionWithoutLastRotation();

		graphics.rotateY(yAngle);
		graphics.rotateX(xAngle);
		
		graphics.rotateZ(spin);
		
		return this;
	}
	
	private PTurtle3d transformToPositionWithoutLastRotation() {
		if (superturtle != null) {
			superturtle.transformToPosition();
		}
		
		graphics.translate(pos.x, pos.y, pos.z);
		
		return this;
	}
	
	private PVector cancelingTransformationChain(PVector vector, boolean useOnlyRotation) {
		final PVector vectorNew = vector.copy();
		
		// Starts from the top turtle, and iterates through each subturtle, transforming the point using the inverse
		// of the transformation matrix for each turtle. This causes all the transformations to cancel out.
		PTurtle3d currentTurtle = originTurtle();
		while (currentTurtle != this) {
			final PMatrix3D turtleMatrix = currentTurtle.transformationMatrix(!useOnlyRotation);
			turtleMatrix.invert();
			
			turtleMatrix.mult(vectorNew, vectorNew);
			
			currentTurtle = currentTurtle.subturtle;
		}
		return vectorNew;
	}
	
}
