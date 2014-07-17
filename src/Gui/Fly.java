/*****************************************************************************
 * Copyright (c) 2011-2013 The Flies In Cube Team as listed in CREDITS.txt   *
 * http://fim.uni-muenster.de                                             	 *
 *                                                                           *
 * This file is part of Flies In Cube.                                       *
 * Flies In Cube is available under multiple licenses.                       *
 * The different licenses are subject to terms and condition as provided     *
 * in the files specifying the license. See "LICENSE.txt" for details        *
 *                                                                           *
 *****************************************************************************
 *                                                                           *
 * Flies In Cube is free software: you can redistribute it and/or modify     *
 * it under the terms of the GNU General Public License as published by      *
 * the Free Software Foundation, either version 3 of the License, or         *
 * (at your option) any later version. See "LICENSE-gpl.txt" for details.    *
 *                                                                           *
 * Flies In Cube is distributed in the hope that it will be useful,          *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of            *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the              *
 * GNU General Public License for more details.                              *
 *                                                                           *
 *****************************************************************************
 *                                                                           *
 * For non-commercial academic use see the license specified in the file     *
 * "LICENSE-academic.txt".                                                   *
 *                                                                           *
 *****************************************************************************
 *                                                                           *
 * If you are interested in other licensing models, including a commercial-  *
 * license, please contact the author at fim@uni-muenster.de      			 *
 *                                                                           *
 *****************************************************************************/

package Gui;
/**
 * Class Fly represents a fly object, represented by a sphere and implemented as a child of Java3d-TransformGroup (to modify 
 * the position of the sphere).
 * A fly can have one of the following movement types: SIT, WALK, JUMP, FLY (not implemented)
 * 
 * Every fly has a ki (artificial intelligence) which controls the fly's behavior. The most important array of the ki is the 
 * movementPath array which includes the 3D fly position for every millisecond.
 */

import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Fly extends TransformGroup {
	
	// fly is represented by a Sphere
	private Sphere fly;
	// color of the fly
	private ColoringAttributes coloringAttr;
	// appearance attribute needed to seth the color of the fly
	private Appearance app;
	// Transform3D object for movement
	private Transform3D transform;
	
	
	// artificial intelligence
	private KI ki;

	// constructor
	public Fly(float chamberSize_, float size_, int movementTime_, boolean stunned_, boolean negativeGeotaxis_, Color3f flyColor_,
				float negativeGeotaxisIntensity, int maxMovementWindow, int minMovementWindow, float maxFlightSpeed, float minFlightSpeed,
				float walkingSpeedReduction, float sittingProbability, float keepWalkingProbability, int stunnedDuration, long seed, boolean awesome, float variance, float smooth, long currentSeed){
		super();
	
		fly = new Sphere(size_);
		
		// set Color of the fly to dark brown
		coloringAttr = new ColoringAttributes();
		coloringAttr.setColor(flyColor_);
		// add color to appearance
		app = new Appearance();
		app.setColoringAttributes(coloringAttr);
		fly.setAppearance(app);

		
		// initialize ki
		ki = new KI(chamberSize_,movementTime_,size_, stunned_, negativeGeotaxis_,
				negativeGeotaxisIntensity, maxMovementWindow, minMovementWindow, maxFlightSpeed, minFlightSpeed,
				walkingSpeedReduction, sittingProbability, keepWalkingProbability, stunnedDuration, seed, awesome, variance, smooth, currentSeed);
		
		// set fly to it's initial position 
		transform = new Transform3D();
		transform.setTranslation(ki.getStartPosition());
		this.addChild(fly);
		this.setTransform(transform);	
		this.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		this.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	}
	
	public Sphere getFly(){
		return fly;
	}
	
	public Transform3D getTransform(){
		return transform;
	}
	
	public Vector3f getStartPosition(){
		return ki.getStartPosition();
	}
	
	public Point3f getStartPoint(){
		return this.vector2point(ki.getStartPosition());
	}
	
	public KI getKI(){
		return this.ki;
	}
	
	// transforms a given Vector3f to a Point3f
	private Point3f vector2point(Vector3f vec){
		float x=vec.x;
		float y=vec.y;
		float z=vec.z;
		return new Point3f(x,y,z);
	}
}
