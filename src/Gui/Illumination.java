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
 * Class handles the illumination of the scene.
 * 
 * Right now just one Illumination object is added to the Graph. 
 * ToDo: Add the possibility to include more, custom placed light sources around the cube
 * 
 */
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.DirectionalLight;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

public class Illumination extends DirectionalLight {
	
	// color of the light
	private Color3f color;
	// direction of the illumination
	private Vector3f direction;
	// position of the light source ????? (not sure... see docu)
	private Point3d point;
	
	public Illumination(Color3f color_, Vector3f direction_, Point3d point_){
		super();
		
		this.color = color_;
		this.direction = direction_;
		this.point = point_;

		this.setColor(color);
		this.setDirection(direction);
		
		// use a sufficently sized bounding sphere to illuminate the scene
		BoundingSphere bounds = new BoundingSphere(point,1000);
		this.setInfluencingBounds(bounds);
	}
	
	
}
