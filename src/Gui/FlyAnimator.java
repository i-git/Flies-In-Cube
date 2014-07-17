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
 * FlyAnimator is used to interpolate between the 3D positions of the flies (positions stored in the ki of the flies)
 */
import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.PositionPathInterpolator;

public class FlyAnimator{
	
	// PositionPathInterpolator used for animation
	private PositionPathInterpolator positionPathInterpolator;
	
	// Constructor:
	// @params: 
	// Fly object (including the movement path)
	// Alpha object for timing
	// Float array knots for timing (see alpha object documentation)
	public FlyAnimator(Fly fly, Alpha alpha_, float[] knots_){
		positionPathInterpolator = new PositionPathInterpolator(alpha_, fly, fly.getTransform(), knots_, fly.getKI().getMovementPath());
		positionPathInterpolator.setSchedulingBounds(new BoundingSphere());
	}
	
	public PositionPathInterpolator getInterpolator(){
		return positionPathInterpolator;
	}
	
}
