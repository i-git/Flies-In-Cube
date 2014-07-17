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

import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.Color3f;

public class Chamber extends Box {
	
	// size of the chamber (cube with size chamberSize x chamberSize)
	private float chamberSize;
	// level of transparency (1.0 = transparent, 0.0 = intransparent)
	private float transparencyLevel;
	
	public Chamber(float chamberSize_, float transparencyLevel_, Color3f chamberColor_){	
		super(chamberSize_,chamberSize_,chamberSize_, new Appearance());

		this.chamberSize = chamberSize_;
		this.transparencyLevel = transparencyLevel_;
		
		// set transparency
		Appearance appearanceBox = new Appearance();
		appearanceBox.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, transparencyLevel));
		// set color to light blue
		ColoringAttributes coloringAttr = new ColoringAttributes();
		coloringAttr.setColor(chamberColor_);
		// add color to appearance
		appearanceBox.setColoringAttributes(coloringAttr);
		
		this.setAppearance(appearanceBox);
	}
	
	public float getChamberSize(){
		return chamberSize;
	}
	
	public float getTransparencyLevel(){
		return transparencyLevel;
	}
	
	public Box getChamber(){
		return this;
	}

}
