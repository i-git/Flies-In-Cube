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

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;


/**
 * Every camera is represented by its own BranchGroup Object in the Scene Graph.
 * Sketch: BrancGroup --> TransformGroup (camera arm) --> TransformGroup (camera) --> ViewPlatform --> View --> Canvas3D
 * - first TransformGroup for the camera arm rotation
 * - second TransformGroup for camera placement (i.e. for ViewPlatform placement)
 * - ViewPlatform references the View
 * - View references the Canvas3D 
 * - Canvas3D is the image plane
 * @author Bena
 *
 */
class Camera extends BranchGroup {
	// PhysicalBody and PhysicalEnvironment are used for the camera
	private static final PhysicalBody physBody = new PhysicalBody();
	private static final PhysicalEnvironment physEnv = new PhysicalEnvironment();
	
	// to move the camera
	private TransformGroup cameraArmRotationTG;
	private TransformGroup cameraPositionTG;
	
	// the actual movement
	private Transform3D rotation;
	private Transform3D translation;
	
	// every camera needs a viewPlatform and view object
	private ViewPlatform viewPlatform;
	private View view;
	
	// 2D projection of the 3D scene
	private Canvas3D canvas;
		
	public Camera(Vector3f cameraPosition, AxisAngle4f cameraArmRotation){
		super();
		
		// get default graphics configuration
		GraphicsConfigTemplate3D gconfigTempl = new GraphicsConfigTemplate3D();
		GraphicsConfiguration gconfig =GraphicsEnvironment.getLocalGraphicsEnvironment().
				getDefaultScreenDevice().getBestConfiguration( gconfigTempl );
		
		canvas = new Canvas3D(gconfig);
		
		view = new View();
		
		view.setFrontClipDistance(0.05);
		view.setBackClipDistance(15.0);
		
		viewPlatform = new ViewPlatform();
		
		view.setPhysicalBody( physBody );
		view.setPhysicalEnvironment( physEnv );
		view.attachViewPlatform( viewPlatform );
		view.addCanvas3D( canvas );
		
		// first group for translation
		cameraPositionTG = new TransformGroup();
		translation = new Transform3D();
		translation.set(cameraPosition);
		cameraPositionTG.setTransform(translation);
		//cameraPositionTG.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );
		//cameraPositionTG.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		cameraPositionTG.addChild( viewPlatform );
		
		// second group for rotation
		cameraArmRotationTG = new TransformGroup();
		rotation = new Transform3D();
		rotation.set(cameraArmRotation);
		cameraArmRotationTG.setTransform(rotation);
		//cameraArmRotationTG.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );
		//cameraArmRotationTG.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		cameraArmRotationTG.addChild( cameraPositionTG );
		
		this.setCapability(BranchGroup.ALLOW_DETACH);
		this.addChild(cameraArmRotationTG);
		
	}
	
	public Canvas3D getCanvas(){
		return this.canvas;
	}
	
	
	/*********************/
	/** Camera matrices **/
	/*********************/
	// returns the rotation matrix (rotation from camera1 --to--> camera2)
	public Matrix3f getRotationMatrix(){
		Matrix3f rotationMatrix = new Matrix3f();
		this.rotation.get(rotationMatrix);
		return rotationMatrix;
	}
	
	// returns the translation matrix (translation from camera1 --to--> camera2)
	public Vector3f getTranslationVector(){
		Vector3f translationVector = new Vector3f();
		this.translation.get(translationVector);
		return translationVector;
	}
	
	public Matrix3f getIntrinsicCameraMatrix(){
		Matrix3f intrinsicCamMatrix = new Matrix3f();
		// get the field of view
		double fov = this.view.getFieldOfView();
		
		// get width of the canvas
		int width = this.canvas.getWidth();
		// get height of the canvas
		int height = this.canvas.getHeight();
		
		// calculate the focal length based on the field of view (f = x/(2*tan(fov/2)), with x = width or height)
		float f_w =(float) ((double) width / (2*Math.tan(fov/2)));
		// IMPORTANT: since Java3D returns an unknown field of view axes (maybe x-axes) we do not have the other 
		// field of view. If the resolution of the movie is not quadratic (i.e. height != width) then f_h seems
		// to be broken. Therefore we assume quadratic pixel and set the screw parameters for the focal length 
		// (f) to equal size (screw_x = screw_y => f*screw_x/y = f_w/h => f_w=f_h) 
		//float f_h = (float) ((double) height / (2*Math.tan(fov/2))); --> fails!
		float f_h = f_w;
		
		float cameraCenter_w = (float) width/2;
		float cameraCenter_h = (float) height/2;
		
		//float cameraCenter_w = 0.0f;
		//float cameraCenter_h = 0.0f;
		
		// IMPORTANT: The focal lengths f_w / f_h describe the distance from the image plane (the canvas 2D object) to the focal point
		// Since the coordinate system of Java3D is right handed with a z-axes increases towards the viewer, the focal lengths points towards 
		// the viewer too. To point from the viewer to the scene the focal lengths must be inverted.
		
		// first row
		intrinsicCamMatrix.m00 = -f_w;
		intrinsicCamMatrix.m01 = 0.0f;
		intrinsicCamMatrix.m02 = cameraCenter_w;
		// second row
		intrinsicCamMatrix.m10 = 0.0f;
		intrinsicCamMatrix.m11 = -f_h;
		intrinsicCamMatrix.m12 = cameraCenter_h;
		// third row
		intrinsicCamMatrix.m20 = 0.0f;
		intrinsicCamMatrix.m21 = 0.0f;
		intrinsicCamMatrix.m22 = 1.0f;
		
		return intrinsicCamMatrix;
	}
	
	// returns the extrinsic camera matrix
	public Matrix4f getExtrinsicCameraMatrix(){
		Matrix4f extrinsicMatrix = new Matrix4f();
		
		Matrix3f rotationMatrix = new Matrix3f();
		Vector3f translationVector = new Vector3f();
		
		this.rotation.get(rotationMatrix);
		this.translation.get(translationVector);
		
		// IMPORTANT:
		// The orientation of the rotation and translation (extrinsic transformation) was from the woorld-coordinate system to the camera-coordinate
		// system. To get the rotation and translation from the camera-coordinate system to the world-coordinate system, the rotation and 
		// translation got to be inverted!
		// R^(-1) = R^(T) (R is orthogonal)
		rotationMatrix.transpose();
		// t^(-1) = (-x, -y, -z)
		translationVector.set(-translationVector.x,-translationVector.y,-translationVector.z);
		
		// first row
		extrinsicMatrix.m00 = rotationMatrix.m00;
		extrinsicMatrix.m01 = rotationMatrix.m01;
		extrinsicMatrix.m02 = rotationMatrix.m02;
		extrinsicMatrix.m03 = translationVector.x;
		
		// second row
		extrinsicMatrix.m10 = rotationMatrix.m10;
		extrinsicMatrix.m11 = rotationMatrix.m11;
		extrinsicMatrix.m12 = rotationMatrix.m12;
		extrinsicMatrix.m13 = translationVector.y;
		
		// third row
		extrinsicMatrix.m20 = rotationMatrix.m20;
		extrinsicMatrix.m21 = rotationMatrix.m21;
		extrinsicMatrix.m22 = rotationMatrix.m22;
		extrinsicMatrix.m23 = translationVector.z;
		
		// fourth row
		extrinsicMatrix.m30 = 0.0f;
		extrinsicMatrix.m31 = 0.0f;
		extrinsicMatrix.m32 = 0.0f;
		extrinsicMatrix.m33 = 1.0f;
		
		return extrinsicMatrix;
	}
	
	// returns the extrinsic camera matrix
		public float[][] getProjectionMatrix(){
			
			float[][] projection = new float[3][4];
			Matrix3f intrinsic = this.getIntrinsicCameraMatrix();
			Matrix4f extrinsic = this.getExtrinsicCameraMatrix();
			
			projection[0][0] = intrinsic.m00 * extrinsic.m00 + intrinsic.m02 * extrinsic.m20;
			projection[0][1] = intrinsic.m00 * extrinsic.m01 + intrinsic.m02 * extrinsic.m21;
			projection[0][2] = intrinsic.m00 * extrinsic.m02 + intrinsic.m02 * extrinsic.m22;
			projection[0][3] = intrinsic.m00 * extrinsic.m03 + intrinsic.m02 * extrinsic.m23;
			
			projection[1][0] = intrinsic.m11 * extrinsic.m10 + intrinsic.m12 * extrinsic.m20;
			projection[1][1] = intrinsic.m11 * extrinsic.m11 + intrinsic.m12 * extrinsic.m21;
			projection[1][2] = intrinsic.m11 * extrinsic.m12 + intrinsic.m12 * extrinsic.m22;
			projection[1][3] = intrinsic.m11 * extrinsic.m13 + intrinsic.m12 * extrinsic.m23;
			
			projection[2][0] = extrinsic.m20;
			projection[2][1] = extrinsic.m21;
			projection[2][2] = extrinsic.m22;
			projection[2][3] = extrinsic.m23;
			
			
			return projection;
		}
	
	
	// returns the essential matrix
	public Matrix3f getEssentialMatrix(){
		Matrix3f essentialMatrix = new Matrix3f();
		
		Matrix3f rotationMatrix = new Matrix3f();
		Vector3f translationVector = new Vector3f();
		
		this.rotation.get(rotationMatrix);
		this.translation.get(translationVector);
		
		// first row
		essentialMatrix.m00 = 0.0f;
		essentialMatrix.m01 = -translationVector.z;
		essentialMatrix.m02 = translationVector.y;
		// second row
		essentialMatrix.m10 = translationVector.z;
		essentialMatrix.m11 = 0.0f;
		essentialMatrix.m12 = -translationVector.x;
		// third row
		essentialMatrix.m20 = -translationVector.y;
		essentialMatrix.m21 = translationVector.x;
		essentialMatrix.m22 = 0.0f;
		
		essentialMatrix.mul(rotationMatrix);
		
		return essentialMatrix;
	}
}

