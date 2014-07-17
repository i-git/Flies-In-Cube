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
 * Every fly has an artificial intelligence (KI). This class is used to calculate the movement path based on the behavioral
 * parameters: negative geotaxis, max. / min. movement window, max. / min. flight speed, walking speed reduction, 
 * sitting probability, keep walking probability, stunned? (duration) and also chamber size and fly size.
 * The calculated movement path is stored in an array of Point3f entries called movementPath. The temporal resolution 
 * of the 3D coordinates is one point every millisecond. 
 */


import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class KI {
	/** parameters **/
	private Point3f[] velocityArray;
	// movement path array
	private Point3f[] movementPath;
	// random number generator
	private Random rand;
	// chamber size for boundaries (i.e. [-chamberSize, +chamberSize])
	private float chamberSize;
	// own size
	private float flySize;
	// movement time in ms 
	private int movementTime;
	// inject stunned flies?
	private boolean stunned;
	// use negative geotaxis?
	private boolean negativeGeotaxis;
	// walking speed reduced?
	private boolean slowWalking;
	
	
	/** predefined characteristics **/ 
	// maximal flight speed in meters / millisecond
	private float maxFlightSpeed = 0.0005f; // 0.0005f
	// minimal flight speed in meters / millisecond
	private float minFlightSpeed = 0.00005f; // 0.00005f
	// walking speed reduction
	private float walkingSpeedReduction = 0.1f; //0.1f
	
	// maximal milliseconds to spend moving in one direction
	private int maxMovementWindow = 1500;
	// minimal milliseconds to spend moving in one direction
	private int minMovementWindow = 200;
	
	// duration of stunned state (in ms)
	private int stunnedDuration = 1000;
	// intensity of negative geotaxis
	private float negativeGeotaxisIntensity = 0.002f;
	
	// probability of sitting
	private float sittingProbability = 0.002f; // 0.002f
	//probatility of changing movement from sitting/walking to flying
	private float keepWalkingProbability = 0.02f;
	
	//variance of the normally distributed number
	private float variance;
	// smoothness factor teta for velocity vector calculation: v(t) = teta * v(t-1) + gauss
	private float smooth;
	
	// multiplicator for starting position generation method
	private int multiplicator;
	
	
	// constructor
	public KI(float chamberSize_, int movementTime_, float flySize_, boolean stunned_, boolean negativeGeotaxis_,
				float negativeGeotaxisIntensity_, int maxMovementWindow_, int minMovementWindow_, float maxFlightSpeed_, float minFlightSpeed_,
				float walkingSpeedReduction_, float sittingProbability_, float keepWalkingProbability_, int stunnedDuration_, long seed, boolean awesome, float variance, float smooth, long currentSeed){
		this.chamberSize = chamberSize_;
		this.movementTime = movementTime_;
		this.flySize = flySize_;
		this.stunned = stunned_;
		this.negativeGeotaxis = negativeGeotaxis_;
		this.slowWalking = false;
		
		this.negativeGeotaxisIntensity = negativeGeotaxisIntensity_;
		this.maxMovementWindow = maxMovementWindow_;
		this.minMovementWindow = minMovementWindow_;
		this.maxFlightSpeed = maxFlightSpeed_;
		this.minFlightSpeed = minFlightSpeed_;
		this.walkingSpeedReduction = walkingSpeedReduction_;
		this.sittingProbability = sittingProbability_;
		this.keepWalkingProbability = keepWalkingProbability_;
		this.stunnedDuration = stunnedDuration_;
		this.smooth = smooth;
		this.variance = variance;
	
		
		
		
		//random generator with seed; Input -1 creates random generator without seed
		if (seed == -1)
			rand = new Random();
		else
			rand = new Random(currentSeed);
		
	
		// initialize first velocity vector
		float velocityX = getMovementVelocity();
		float velocityY = getMovementVelocity();
		float velocityZ = getMovementVelocity();
		velocityArray = new Point3f[movementTime];
		velocityArray[0] = new Point3f(velocityX,velocityY,velocityZ);
		
		
		// generate the movement path
		Vector3f startPosition = generateStartPosition();
		movementPath = new Point3f[movementTime];
		movementPath[0] = new Point3f(startPosition.x,startPosition.y, startPosition.z);
		
	
		// If use awesome movement path flag is set the awesome movement path will be used, else the normal movement path
		if (awesome) { 
			generateAwesomeMovementPath();}
		else
			generateMovementPath(); 
		
		
	}
//	
private void generateAwesomeMovementPath(){
		
		
		for (int i = 1; i < movementTime; i++){
			
			
			// normally distributed random numbers with variance divided by 1000000 to have
			// an easier input handling for the variance.
			
			float gaussX =  (float) (rand.nextGaussian() * Math.sqrt(variance/1000000.0f));
			float gaussY =  (float) (rand.nextGaussian() * Math.sqrt(variance/1000000.0f));
			float gaussZ =  (float) (rand.nextGaussian() * Math.sqrt(variance/1000000.0f));
			
			
			// calculate velocities for random walk: v(t) = teta * v(t-1) + gauss with teta element of [0..1].
			// gauss is normally distributed random number calculated above
			
			float velocityX = smooth*velocityArray[i-1].x + gaussX;
			float velocityY = smooth*velocityArray[i-1].y + gaussY;
			float velocityZ = smooth*velocityArray[i-1].z + gaussZ;
			

			
			
			/** ADJUST THE VELOCITY **/		
			
			// stunned flies stay at the bottom for stunnedDuration and move more slowly for the first milliseconds
			if(stunned && i<stunnedDuration){
				velocityY=0.0f;
			}
			
			// if negative geotaxis flag is set a random number is generated and compared with the 
			// negativeGeotaxisIntensity, if it is smaller or equal to that value the offset in Y-direction is forced 
			// to be positive (or zero), so that the overall movement is up (or horizontal)
			if(negativeGeotaxis){
				float negGeo = rand.nextFloat();
				if(negGeo<=negativeGeotaxisIntensity){
					velocityY=Math.abs(velocityY);
				}
			}
			
			// fly moves slower while touching a wall
			if(wallContact(movementPath[i-1]) ){
				

				this.slowWalking = false;
				
				
				// if random value < sittingProbability => set all offsets to 0 (no movement at all)
				if(rand.nextFloat() < sittingProbability){
					velocityX=0.0f;
					velocityY=0.0f;
					velocityZ=0.0f;
					
				}
				// first moment of wall contact: reduce movement speed and set slowWalking flag
				if (!slowWalking){
					velocityX *= walkingSpeedReduction;
					velocityY *= walkingSpeedReduction;
					velocityZ *= walkingSpeedReduction;
					this.slowWalking = true;
				}
				// contact left or right wall: set offset in X-direction to 0 
				// (or stop a if rand.nextFloat() > keepWalkingProbability)
				if(contactLeftRight(movementPath[i-1].x)){
					if(rand.nextFloat() <= keepWalkingProbability){
						velocityX=0.0f;
					}
				}
				// contact top or bottom wall: set offset in Y-direction to 0 
				// (or stop a if rand.nextFloat() > keepWalkingProbability)
				if(contactBottomTop(movementPath[i-1].y)){
					if(rand.nextFloat() <= keepWalkingProbability){
						velocityY=0.0f;
					}
				}
				// contact front or back wall: set offset in Z-direction to 0 
				// (or stop a if rand.nextFloat() > keepWalkingProbability)
				if(contactFrontBack(movementPath[i-1].z)){
					if(rand.nextFloat() <= keepWalkingProbability){
						velocityZ=0.0f;
					}
				}
			}		
			/** END OF OFFET ADJUSTMENT **/
			
			
			// save velocities in velocityArray
			velocityArray[i] = new Point3f(velocityX,velocityY,velocityZ);
			
			
			// finally add the velocities to all current 3D positions
			float coordX = addOffset(movementPath[i-1].x,velocityArray[i].x);
			float coordY = addOffset(movementPath[i-1].y,velocityArray[i].y);
			float coordZ = addOffset(movementPath[i-1].z,velocityArray[i].z);
			
		movementPath[i]= new Point3f(coordX,coordY,coordZ);
		}
		
		
	}
	// generate movement path
	private void generateMovementPath(){
		
		// separates the given movement time into several time wiows
		// movement in this specific time window specifies movement in the same direction
		// i.e. every Integer entry of changeMovementDirection is one time span of constant movement
		ArrayList<Integer> changeMovementDirection = this.generateMovementChanges();
		// elemNumber is used to iterate through the ArrayList changeMovementDirection
		int elemNumber = 0;
		// get the first time window of movement 
		int movementLength = changeMovementDirection.get(elemNumber);
		// get the total number of time windows
		int movementPeriods = changeMovementDirection.size();
		
		// offset is added to the current position (for all dimensions separately; if the fly touches a wall this
		// dimension is not incremented so that the fly stays inside the chamber boundary's). 
		float offsetX = getMovementOffset();
		float offsetY = getMovementOffset();
		float offsetZ = getMovementOffset();
		
		for (int i = 1; i < movementTime; i++){
			
			/** ADJUST THE OFFSET **/		
			// end of the movement time window but not end of the total movement period:
			if(i>movementLength && elemNumber < movementPeriods-1){
				movementLength = changeMovementDirection.get(++elemNumber);
				// get new offset for all dimensions
				offsetX = getMovementOffset();
				offsetY = getMovementOffset();
				offsetZ = getMovementOffset();
				
				this.slowWalking = false;
			}
			
			// stunned flies stay at the bottom for stunnedDuratoin and move more slowly for the first milliseconds
			if(stunned && i<stunnedDuration){
				offsetY=0.0f;
			}
			
			// if negative geotaxis flag is set a random number is generated and compared with the 
			// negativeGeotaxisIntensity, if it is smaller or equal to that value the offset in Y-direction is forced 
			// to be positive (or zero), so that the overall movement is up (or horizontal)
			if(negativeGeotaxis){
				float negGeo = rand.nextFloat();
				if(negGeo<=negativeGeotaxisIntensity){
					offsetY=Math.abs(offsetY);
				}
			}
			
			// fly moves slower while touching a wall
			if(wallContact(movementPath[i-1]) ){
				// if random value < sittingProbability => set all offsets to 0 (no movement at all)
				if(rand.nextFloat() < sittingProbability){
					offsetX=0.0f;
					offsetY=0.0f;
					offsetZ=0.0f;
				}
				// first moment of wall contact: reduce movement speed and set slowWalking flag
				if (!slowWalking){
					offsetX*=walkingSpeedReduction;
					offsetY*=walkingSpeedReduction;
					offsetZ*=walkingSpeedReduction;
					this.slowWalking = true;
				}
				// contact left or right wall: set offset in X-direction to 0 
				// (or stop a if rand.nextFloat() > keepWalkingProbability)
				if(contactLeftRight(movementPath[i-1].x)){
					if(rand.nextFloat() <= keepWalkingProbability){
						offsetX=0.0f;
					}
				}
				// contact top or bottom wall: set offset in Y-direction to 0 
				// (or stop a if rand.nextFloat() > keepWalkingProbability)
				if(contactBottomTop(movementPath[i-1].y)){
					if(rand.nextFloat() <= keepWalkingProbability){
						offsetY=0.0f;
					}
				}
				// contact front or back wall: set offset in Z-direction to 0 
				// (or stop a if rand.nextFloat() > keepWalkingProbability)
				if(contactFrontBack(movementPath[i-1].z)){
					if(rand.nextFloat() <= keepWalkingProbability){
						offsetZ=0.0f;
					}
				}
			}		
			/** END OF OFFET ADJUSTMENT **/
			
			// finally add the offsets to all current 3D positions
			float coordX = addOffset(movementPath[i-1].x, offsetX);
			float coordY = addOffset(movementPath[i-1].y, offsetY);
			float coordZ = addOffset(movementPath[i-1].z, offsetZ);
			
			// add this new position to the movementPath
			movementPath[i]= new Point3f(coordX,coordY,coordZ);
			
			//movementPath[i] = new Point3f(-0.5f,0.5f,-1.0f);
			
		}
	}
	
	
	
	public Point3f[] getMovementPath(){
		return this.movementPath;
	}

	public Vector3f getStartPosition(){
		return new Vector3f(movementPath[0].x, movementPath[0].y, movementPath[0].z);
	}

	
	// generates the initial fly position
	private Vector3f generateStartPosition(){	
		// every initial position can be positive or negative (the middle of the chamber is the point (0,0,0))
		// pick random sign (if sign = true change the entry to negative) 
		boolean x_sign = rand.nextBoolean();
		boolean y_sign = rand.nextBoolean();
		boolean z_sign = rand.nextBoolean();
		
		// choose different multiplicators for the random number to make sure that
		// the modulo operator creates normally distributed results between [0,chamberSize]
		
		if (chamberSize < 10.0f)
			multiplicator = 10;
		else if (chamberSize >= 10.0f && chamberSize < 100.0f)
			multiplicator = 100;
		else if (chamberSize >= 100.0f && chamberSize < 1000.f)
			multiplicator = 1000;
		else if (chamberSize >= 1000.f && chamberSize < 10000.f)
			multiplicator = 10000;
		
		// pick a random initial position (smaller than the chamber size) for the x coordinate
		float x = (rand.nextFloat()*multiplicator)%chamberSize;
		// change sign?
		if(x_sign)
			x=-x;
		// random position for the y coordinate
		float y = (rand.nextFloat()*multiplicator)%chamberSize;
		// change sign?
		if(y_sign)
			y=-y;
		// random position for the z coordinate
		float z = (rand.nextFloat()*multiplicator)%chamberSize;
		// change sign?
		if(z_sign)
			z=-z;
		
		// if the fly is stunned it will start at the bottom of the chamber (y=-chamberSize + flySize)
		if(stunned){
			y = -chamberSize + flySize;
		}
		
		return new Vector3f(x,y,z);	
		//return new Vector3f(-0.5f,0.5f,-1.0f);
	}
	
	
	// separates the given movement time into several time windows
	// movement in this specific time window specifies movement in the same direction
	private ArrayList<Integer> generateMovementChanges(){
		ArrayList<Integer> changeMovementDirection = new ArrayList<Integer>();
		// generate a random number between MIN_MOVEMENT_TIME_WINDOW and MAX_MOVEMENT_TIME_WINDOW
		int element = rand.nextInt(maxMovementWindow - minMovementWindow) + minMovementWindow;
		
		do{
			changeMovementDirection.add(new Integer(element));
			element += rand.nextInt(maxMovementWindow - minMovementWindow) + minMovementWindow;	
		} while(element < movementTime);
		
		return changeMovementDirection;
	}
	
	private float getMovementVelocity(){
		float velocity = (float) (rand.nextGaussian() * Math.sqrt(0.05));
		velocity *= maxFlightSpeed;
		if(velocity < minFlightSpeed){
			velocity = minFlightSpeed;
		}
		return velocity;
		}
	
	// generate a new one dimensional movement offset
	private float getMovementOffset(){
		float offset = rand.nextFloat();
		offset *= maxFlightSpeed;
		if(offset < minFlightSpeed){
			offset = minFlightSpeed;
		}
		int sign = rand.nextInt(2);
		offset *= Math.pow(-1,sign);
		
		return offset;
	}
	// add offset to specific coordinate
	private float addOffset(float coord, float offset){
		coord += offset;
		if (coord > chamberSize-flySize){
			coord = chamberSize-flySize;
		}
		else if(coord < -chamberSize+flySize){
			coord = -chamberSize+flySize;
		}
		return coord;
	}
	
	//private float addVelocity (float coord, double velocity){
	//	coord+=velocity;
	//	if (coord > chamberSize-flySize){
	//		coord = chamberSize-flySize;
	//	}
	//	else if (coord < chamberSize+flySize){
	//		coord = -chamberSize+flySize;
	//	}
	//	return coord;
	//}
	
	// do the fly touches any wall?
	private boolean wallContact(Point3f position){
		boolean contact = false;
		if(Math.abs(position.x) == chamberSize-flySize || Math.abs(position.y) == chamberSize-flySize || Math.abs(position.z) == chamberSize-flySize ){
			contact = true;
		}
		return contact;
	}
	// do fly touches bottom or top wall?
	private boolean contactBottomTop(float yCoord){
		boolean contact = false;
		if (Math.abs(yCoord)== chamberSize-flySize){
			contact = true;
		}
		return contact;
	}
	// do fly touches left or right wall?
	private boolean contactLeftRight(float xCoord){
		boolean contact = false;
		if (Math.abs(xCoord)== chamberSize-flySize){
			contact = true;
		}
		return contact;
	}
	// do fly touches front or back wall?
	private boolean contactFrontBack(float zCoord){
		boolean contact = false;
		if (Math.abs(zCoord)== chamberSize-flySize){
			contact = true;
		}
		return contact;
	}
}
