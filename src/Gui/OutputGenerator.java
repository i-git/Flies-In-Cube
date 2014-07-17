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
 * Class generates the output matrices and saves them into a user specified folder
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


import javax.imageio.ImageIO;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GraphicsContext3D;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Raster;
import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class OutputGenerator {
	// array of all needed fly positions (depending on the fps rate)
	private Point3f[][] paths;
	// array of flies
	private Fly[] flies;
	
	// to write csv files
	private FileWriter writerGT;
	
	
	public OutputGenerator(Fly[] flies_, Camera cam1_, Camera cam2_, Camera cam3_, int fps_, String filePathRoot_, 
							boolean useCam1_, boolean useCam2_, boolean useCam3_,
							boolean output2Dpath, boolean outputProjectionMatrix){
		// get all movement paths depending on the given fps rate
		this.paths = this.getAllMovementPahts(fps_, flies_, cam1_);
		// get all flies
		this.flies = flies_;
		
		// set the file and folder names
		String groundTruthFile = filePathRoot_ + "paths.csv";
		String imgCam1Path = filePathRoot_ + "cam1/";
		String imgCam2Path = filePathRoot_ + "cam2/";
		String imgCam3Path = filePathRoot_ + "cam3/";
		
		String paths2DFileCam1 = filePathRoot_ + "paths2DCam1.csv";
		String paths2DFileCam2 = filePathRoot_ + "paths2DCam2.csv";
		String paths2DFileCam3 = filePathRoot_ + "paths2DCam3.csv";
		
		String pathIntrinsicCam1 = filePathRoot_ + "A_cam1.csv";
		String pathIntrinsicCam2 = filePathRoot_ + "A_cam2.csv";
		String pathIntrinsicCam3 = filePathRoot_ + "A_cam3.csv";
		
		String pathExtrinsicCam1 = filePathRoot_ + "D_cam1.csv";
		String pathExtrinsicCam2 = filePathRoot_ + "D_cam2.csv";
		String pathExtrinsicCam3 = filePathRoot_ + "D_cam3.csv";
		
		String pathProjectionMatrixCam1 = filePathRoot_ + "P_cam1.csv";
		String pathProjectionMatrixCam2 = filePathRoot_ + "P_cam2.csv";
		String pathProjectionMatrixCam3 = filePathRoot_ + "P_cam3.csv";
		
		
		// create root folder
		File rootFolder = new File(filePathRoot_);
		// delete content if does not exist
		deleteDirectory(rootFolder);
		rootFolder.mkdir();
		
		// create sub-folders
		File cam1Folder = new File(imgCam1Path);
		File cam2Folder = new File(imgCam2Path);
		File cam3Folder = new File(imgCam3Path);		
		if(useCam1_){
			cam1Folder.mkdir();
		}	
		if(useCam2_){
			cam2Folder.mkdir();
		}
		if(useCam3_){
			cam3Folder.mkdir();
		}
		

		
		
		
		/** WRITE GROUND TRUTH FILE **/
		this.writeGroundTruthFile(groundTruthFile, flies_.length);
		
		/** WRITE THE EXTRINSIC AND INTRINSIC CAMERA MATRICES AND EVERYTHING **/
		if(useCam1_){
			this.writeIntrinsicMatrix(cam1_, pathIntrinsicCam1);
			this.writeExtrinsicMatrix(cam1_, pathExtrinsicCam1);
			if(output2Dpath)
				this.save2Dpath(cam1_, paths, paths2DFileCam1);
			this.saveImageFiles(imgCam1Path, "cam1_", cam1_.getCanvas(), flies_.length);
			
			if(outputProjectionMatrix)
				this.writeProjectionMatrix(cam1_, pathProjectionMatrixCam1);
			
			
		}
		if(useCam2_){
			this.writeIntrinsicMatrix(cam2_, pathIntrinsicCam2);
			this.writeExtrinsicMatrix(cam2_, pathExtrinsicCam2);
			if(output2Dpath)
				this.save2Dpath(cam2_, paths, paths2DFileCam2);
			this.saveImageFiles(imgCam2Path, "cam2_", cam2_.getCanvas(), flies_.length);
					
			if(outputProjectionMatrix)
				this.writeProjectionMatrix(cam2_, pathProjectionMatrixCam2);
			
		}
		if(useCam3_){
			this.writeIntrinsicMatrix(cam3_, pathIntrinsicCam3);
			this.writeExtrinsicMatrix(cam3_, pathExtrinsicCam3);
			if(output2Dpath)
				this.save2Dpath(cam3_, paths, paths2DFileCam3);
			this.saveImageFiles(imgCam3Path, "cam3_", cam3_.getCanvas(), flies_.length);		
			
			if(outputProjectionMatrix)
				this.writeProjectionMatrix(cam3_, pathProjectionMatrixCam3);
			
		}
		
		
	}
	
	public void printSuccess(){
		System.out.println("Success!");
	}
	

	
	
	// Function writes ground truth path of the flies
	private void writeGroundTruthFile(String fileName, int numberOfFlies){
		try{
			this.writerGT = new FileWriter(fileName,false);
			
			writerGT.append("");
			writerGT.append(",");
			// write first line into cvs ground truth file
			for(int flyNumber = 0; flyNumber<numberOfFlies; flyNumber++){
				writerGT.append("fly("+Integer.toString(flyNumber)+")");
				writerGT.append(",");
			}
			writerGT.append("\n");
			writerGT.flush();
			
			// ******** write x coordinates: ********
			// OUTER LOOP: over all time steps
			for(int timeStep = 0; timeStep <paths[0].length; timeStep++){
				writerGT.append("x("+timeStep+")");
				writerGT.append(",");
				
				// INNER LOOP: over all flies
				for(int flyNumber=0; flyNumber < numberOfFlies; flyNumber++){
					writerGT.append(Float.toString(paths[flyNumber][timeStep].x));
					if(flyNumber != numberOfFlies-1)
						writerGT.append(",");
					else
						writerGT.append("\n");
				}
				writerGT.flush();
			}
			
			// ******** write y coordinates: ********
			// OUTER LOOP: over all time steps
			for(int timeStep = 0; timeStep <paths[0].length; timeStep++){
				writerGT.append("y("+timeStep+")");
				writerGT.append(",");
				
				// INNER LOOP: over all flies
				for(int flyNumber=0; flyNumber < numberOfFlies; flyNumber++){
					
					writerGT.append(Float.toString(paths[flyNumber][timeStep].y));
					if(flyNumber != numberOfFlies-1)
						writerGT.append(",");
					else
						writerGT.append("\n");
				}
				writerGT.flush();
			}
			
			// ******** write z coordinates: ********
			// OUTER LOOP: over all time steps
			for(int timeStep = 0; timeStep <paths[0].length; timeStep++){
				writerGT.append("z("+timeStep+")");
				writerGT.append(",");
				
				// INNER LOOP: over all flies
				for(int flyNumber=0; flyNumber < numberOfFlies; flyNumber++){
					writerGT.append(Float.toString(paths[flyNumber][timeStep].z));
					if(flyNumber != numberOfFlies-1)
						writerGT.append(",");
					else
						writerGT.append("\n");
				}
				writerGT.flush();
			}
			writerGT.close();		
		} 
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	// save the intrinsic matrices of all cameras
	private void writeIntrinsicMatrix(Camera camera, String fileName){
		
		try{
			// write intrinsic matrix of the left camera (i.e. A_left)
			this.writerGT = new FileWriter(fileName,false);
			Matrix3f intrinsicMatrixLeft = camera.getIntrinsicCameraMatrix();
			
			//Matrix3f intrinsicMatrixLeft = leftCamera.getIntrinsicViewMatrix();
			
			writerGT.append(
				Float.toString(intrinsicMatrixLeft.m00) + "," + Float.toString(intrinsicMatrixLeft.m01) + "," + Float.toString(intrinsicMatrixLeft.m02)+ "\n" +
				Float.toString(intrinsicMatrixLeft.m10) + "," + Float.toString(intrinsicMatrixLeft.m11) + "," + Float.toString(intrinsicMatrixLeft.m12)+ "\n" +
				Float.toString(intrinsicMatrixLeft.m20) + "," + Float.toString(intrinsicMatrixLeft.m21) + "," + Float.toString(intrinsicMatrixLeft.m22));
			writerGT.flush();
			writerGT.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	// save the extrinsic matrices of all cameras
	private void writeExtrinsicMatrix(Camera camera, String fileName){
		
		try{
			// write extrinsic matrix of the left camera (i.e. D_left)
			this.writerGT = new FileWriter(fileName,false);
			Matrix4f extrinsicMatrixLeft= camera.getExtrinsicCameraMatrix();
			
			//Matrix4f extrinsicMatrixLeft = leftCamera.getExtrinsicViewMatrix();
			
			writerGT.append(
				Float.toString(extrinsicMatrixLeft.m00) + "," + Float.toString(extrinsicMatrixLeft.m01) + "," + 
						Float.toString(extrinsicMatrixLeft.m02) + "," + Float.toString(extrinsicMatrixLeft.m03) + "\n" +
				Float.toString(extrinsicMatrixLeft.m10) + "," + Float.toString(extrinsicMatrixLeft.m11) + "," + 
						Float.toString(extrinsicMatrixLeft.m12) + "," + Float.toString(extrinsicMatrixLeft.m13) + "\n" +
				Float.toString(extrinsicMatrixLeft.m20) + "," + Float.toString(extrinsicMatrixLeft.m21) + "," + 
						Float.toString(extrinsicMatrixLeft.m22) + "," + Float.toString(extrinsicMatrixLeft.m23)+ "\n" +
				Float.toString(extrinsicMatrixLeft.m30) + "," + Float.toString(extrinsicMatrixLeft.m31) + "," + 
						Float.toString(extrinsicMatrixLeft.m32) + "," + Float.toString(extrinsicMatrixLeft.m33));
			writerGT.flush();
			writerGT.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	// save the images
	private void saveImageFiles(String imgPath, String camName, Canvas3D canvas, int numberOfFlies){
		// OUTER LOOP: over all time steps
		for(int timeStep = 0; timeStep <paths[0].length; timeStep++){

			// INNER LOOP: over all flies
			for(int flyNumber=0; flyNumber < numberOfFlies; flyNumber++){
				Transform3D transform = flies[flyNumber].getTransform();
				transform.setTranslation(this.point2vector(paths[flyNumber][timeStep]));
				flies[flyNumber].setTransform(transform);
			}
			
			BufferedImage img = createBufferedImage(canvas);
			
			File file = new File(imgPath+camName+ timeStep + ".jpg");
			
			writeToFile(img,file);
		}
	}
	
	// returns all movement paths in one array: paths[number of flies][number of timesteps]
	private Point3f[][] getAllMovementPahts(int fps, Fly[] flies, Camera cam){
		Point3f[][] paths = new Point3f[flies.length][];
		
		for (int i=0; i<flies.length;i++){
			ArrayList<Point3f> tempPath = this.getMovementPathFPS(fps, flies[i]);
			paths[i] = this.ArrayList2Array(tempPath);
		}
		
		return paths;
	}
	
	// returns the movenentPath at the given frame rate (specified by fps)
	private ArrayList<Point3f> getMovementPathFPS(int fps, Fly fly){
		ArrayList<Point3f> returnPath = new ArrayList<Point3f>();
		
		// movementPath contains position information for every millisecond (1000 positions for every second).
		// if the frame rate is 33 fps, every 1000/33 = 30 position information is needed, therefore the step size is 30
		// and if the total tracking duration was 20 seconds = 20000 ms, 20000/30 positions are captured
		// if the frame rate is 100 fps, then every 1000/100 = 10 ms a position information is recorded and the total number
		// of captured positions is 20000/10.
		int stepSize = (int) 1000/fps;
		
		for (int adr = 0; adr<fly.getKI().getMovementPath().length;adr+=stepSize){
			returnPath.add(fly.getKI().getMovementPath()[adr]);
		}
	
		return returnPath;
	}
	
	// transforms a ArrayList to an Array
	private Point3f[] ArrayList2Array(ArrayList<Point3f> tempPath){
		Point3f[] path = new Point3f[tempPath.size()];
		for(int i=0;i<tempPath.size();i++){
			path[i] = tempPath.get(i);
		}
		return path;
	}
	
	
	/** FOR IMAGE SAVING **/
	private BufferedImage createBufferedImage(Canvas3D canvas){
		GraphicsContext3D  ctx = canvas.getGraphicsContext3D();
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        BufferedImage bi = new BufferedImage( w, h, BufferedImage.TYPE_INT_RGB );
        ImageComponent2D im = new ImageComponent2D( ImageComponent.FORMAT_RGB, bi );
        Raster ras = new Raster( new Point3f( -1.0f, -1.0f, -1.0f ), Raster.RASTER_COLOR, 0, 0, w, h, im, null );
        ctx.readRaster( ras );
        return ras.getImage().getImage();
	}
	
	private void writeToFile(BufferedImage img, File file){
		try{
			ImageIO.write(img, "jpg", file);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	// converts a point to a vector
	private Vector3f point2vector(Point3f point){
		return new Vector3f(point.x, point.y,point.z);
	}
	
	// delete the directory if already exists
	private boolean deleteDirectory(File path) {
		if( path.exists() ) {
			File[] files = path.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					files[i].delete();
				}
			}
	    }
	    return( path.delete() );
	}
	
	
	
	
	
	
	
	
	// TEST (returns the projection matrix)
	/*public float[][] getProjectionMatrix(Camera camera){
		float[][] projection = new float[3][4];
		Matrix3f intrinsic = camera.getIntrinsicCameraMatrix();
		Matrix4f extrinsic = camera.getExtrinsicCameraMatrix();
		
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
	}*/
	
	// saves the 2D path into a file
	public void save2Dpath(Camera camera, Point3f[][] path, String fileName){
		float[][] projection = camera.getProjectionMatrix();
		//float[][] projection = this.getProjectionMatrix(camera);
		
		Point2f[][] path2D = new Point2f[path.length][path[0].length];
		
		for(int flyNumber = 0; flyNumber < path.length; flyNumber++){
			for(int timeStep = 0; timeStep < path[0].length; timeStep++){
				float x = path[flyNumber][timeStep].x * projection[0][0] + 
							path[flyNumber][timeStep].y * projection[0][1] + 
							path[flyNumber][timeStep].z * projection[0][2] +
							projection[0][3];
				
				float y = path[flyNumber][timeStep].x * projection[1][0] + 
						path[flyNumber][timeStep].y * projection[1][1] +	
						path[flyNumber][timeStep].z * projection[1][2] +
						projection[1][3];
				
				float w = path[flyNumber][timeStep].x * projection[2][0] + 
						path[flyNumber][timeStep].y * projection[2][1] +	
						path[flyNumber][timeStep].z * projection[2][2] +
						projection[2][3];
				
				//path2D[flyNumber][timeStep] = new Point2f(camera.getCanvas().getWidth()-x/w, y/w);

				// divide threw the last entry 
				x = x/w;
				y = y/w;
				// OpenCV uses coordinate system origin in the top left corner (not in the bottom left corner like Java3D: 
				// mirror along the x-axes by calculating the image height minus the y-coordinate
				y = camera.getCanvas().getHeight() - y;
				path2D[flyNumber][timeStep] = new Point2f(x,y);
				
				/** Alternative calculation, see link... **/
				// P*=projection matrix construction: http://www.opengl.org/sdk/docs/man/xhtml/gluPerspective.xml
				// [R|t] x P* x M_w 
				/*Transform3D transe =new Transform3D();
				transe.perspective(Math.PI/4, 1, 0.05, 15.0);
				
				Vector4f in = new Vector4f(path[flyNumber][timeStep].x,path[flyNumber][timeStep].y,path[flyNumber][timeStep].z,1);
				
				Matrix4f extrinsic = camera.getExtrinsicCameraMatrix();
				
				extrinsic.transform(in);
				
				Vector4f out = new Vector4f();
				
				transe.transform(in,out);
				
				out = new Vector4f(out.x/out.w*250+250, 500 - (out.y/out.w*250+250), out.y/out.w*250+250,1);
				
				path2D[flyNumber][timeStep] = new Point2f(out.x, out.y);
				System.out.println();*/
				
			}
		}
		
		
		// WRITE TO FILE
		try{
			this.writerGT = new FileWriter(fileName,false);
			
			int numberOfFlies = path.length;
			
			writerGT.append("");
			writerGT.append(",");
			// write first line into cvs ground truth file
			for(int flyNumber = 0; flyNumber<numberOfFlies; flyNumber++){
				writerGT.append("fly("+Integer.toString(flyNumber)+")");
				writerGT.append(",");
			}
			writerGT.append("\n");
			writerGT.flush();
			
			// ******** write x coordinates: ********
			// OUTER LOOP: over all time steps
			for(int timeStep = 0; timeStep <paths[0].length; timeStep++){
				writerGT.append("x("+timeStep+")");
				writerGT.append(",");
				
				// INNER LOOP: over all flies
				for(int flyNumber=0; flyNumber < numberOfFlies; flyNumber++){
					writerGT.append(Float.toString(path2D[flyNumber][timeStep].x));
					if(flyNumber != numberOfFlies-1)
						writerGT.append(",");
					else
						writerGT.append("\n");
				}
				writerGT.flush();
			}
			
			// ******** write y coordinates: ********
			// OUTER LOOP: over all time steps
			for(int timeStep = 0; timeStep <paths[0].length; timeStep++){
				writerGT.append("y("+timeStep+")");
				writerGT.append(",");
				
				// INNER LOOP: over all flies
				for(int flyNumber=0; flyNumber < numberOfFlies; flyNumber++){
					writerGT.append(Float.toString(path2D[flyNumber][timeStep].y));
					if(flyNumber != numberOfFlies-1)
						writerGT.append(",");
					else
						writerGT.append("\n");
				}
				writerGT.flush();
			}
			writerGT.close();		
		} 
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	// save the extrinsic matrices of all cameras
		private void writeProjectionMatrix(Camera camera, String fileName){
			
			try{
				// write projection matrix for camera 'camera'
				this.writerGT = new FileWriter(fileName,false);
				//float[][] projection = this.getProjectionMatrix(camera);
				float[][] projection = camera.getProjectionMatrix();
				//Matrix4f extrinsicMatrixLeft = leftCamera.getExtrinsicViewMatrix();
				
				writerGT.append(
					Float.toString(projection[0][0]) + "," + Float.toString(projection[0][1]) + "," + 
							Float.toString(projection[0][2]) + "," + Float.toString(projection[0][3]) + "\n" +
					Float.toString(projection[1][0]) + "," + Float.toString(projection[1][1]) + "," + 
							Float.toString(projection[1][2]) + "," + Float.toString(projection[1][3]) + "\n" +
					Float.toString(projection[2][0]) + "," + Float.toString(projection[2][1]) + "," + 
							Float.toString(projection[2][2]) + "," + Float.toString(projection[2][3]));
				writerGT.flush();
				writerGT.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	
}
