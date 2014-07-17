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
 * Class defines the virtual universe necessary for Java3D containing the local (start point), the actual VirtualUniverse and the cameras
 */
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Locale;
import javax.media.j3d.VirtualUniverse;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;


public class VirtualUniverse2 {
	
	private Locale locale;
	private VirtualUniverse universe;
	private Camera cam1;
	private Camera cam2;
	private Camera cam3;
	
	
	public VirtualUniverse2(Vector3f cam1Pos, Vector3f cam2Pos, Vector3f cam3Pos,
								AxisAngle4f cam1Rot, AxisAngle4f cam2Rot, AxisAngle4f cam3Rot){

		// initialize the virtual universe
		universe = new VirtualUniverse();
		//initialize the locale point in the virtual universe (i.e. the central node getting all BranchGraphs)
		locale = new Locale(universe);
		
		// initialize two cameras
		cam1 = new Camera(cam1Pos, cam1Rot);
		cam2 = new Camera(cam2Pos, cam2Rot);
		cam3 = new Camera(cam3Pos, cam3Rot);
		
		// add the cameras to local (root node)
		locale.addBranchGraph(cam1);
		locale.addBranchGraph(cam2);
		locale.addBranchGraph(cam3);

	}
	
	public Camera getCam1(){
		return this.cam1;
	}
	
	public Camera getCam2(){
		return this.cam2;
	}
	
	public Camera getCam3(){
		return this.cam3;
	}
	
	// since the content of the universe must be added (to the BranchGraph) this function expects a BranchGroup with all the rendering content and 
	// adds this content to the starting point (i.e. to the local)
	public void addContent(BranchGroup content){
		locale.addBranchGraph(content);

	}	
}


