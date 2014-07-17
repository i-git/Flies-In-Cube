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


import java.awt.EventQueue;

import javax.media.j3d.Alpha;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import java.util.Random;





public class MainFrame extends JFrame implements ActionListener {

	/**
	 * GUI is created using WindowBuilder 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField_x3;
	private JTextField textField_y3;
	private JTextField textField_z3;
	private JTextField textField_angle3;
	private JTextField textField_x2;
	private JTextField textField_y2;
	private JTextField textField_z2;
	private JTextField textField_angle2;
	private JTextField textField_x1;
	private JTextField textField_y1;
	private JTextField textField_z1;
	private JTextField textField_angle1;
	private JTextField textField_fps;
	private JTextField textField_size;
	private JTextField textField_transparencyLevel;
	private JTextField textField_numberOfFlies;
	private JTextField textField_flySize;
	private JTextField textField_negativeGeotaxis;
	private JTextField textField_maxMovementWindow;
	private JTextField textField_minMovementWindow;
	private JTextField textField_maxFlightSpeed;
	private JTextField textField_minFlightSpeed;
	private JTextField textField_walkingSpeedReduction;
	private JTextField textField_sittingProbability;
	private JTextField textField_keepWalkingProbability;
	private JTextField textField_stunnedDuration;

	
	private JCheckBox checkBox_useCamera1;
	private JCheckBox checkBox_useCamera2;
	private JCheckBox checkBox_useCamera3;
	
	private JComboBox comboBox_axis1;
	private JComboBox comboBox_axis2;
	private JComboBox comboBox_axis3;
	
	private JCheckBox checkBox_stunned;
	private JCheckBox checkBox_awesomeMovementPath;
	private JCheckBox checkBox_negativeGeotaxis;
	private JCheckBox chckbxOutputdPaths;
	
	private JButton btnInfoButton;
	private JButton btnSave;
	private JButton btnShow;
	private JTextField textField_resolutionX;
	private JTextField textField_resolutionY;
	
	private JCheckBox chckbxOutputProjectionMatrices;
	
	// for fly generator
	private Fly[] flies;
	private VirtualUniverse2 uni;
	private FlyAnimator[] animator;
	private Alpha alpha;
	
	/*private Chamber chamber;
	private Illumination[] illu;
	private OutputGenerator outputGenerator;
	private Background background;
	private BranchGroup group;*/
	
	// needed frames for cameras:
	public JFrame camera1Frame;
	public JFrame camera2Frame;
	public JFrame camera3Frame;
	
	
	private JTextField textField_chamberColorR;
	private JTextField textField_chamberColorG;
	private JTextField textField_chamberColorB;
	private JTextField textField_flyColorR;
	private JTextField textField_flyColorG;
	private JTextField textField_flyColorB;
	private JTextField textField_repeatVideo;
	private JTextField textField_OutputPath;
	private JTextField textField_capturingTime;
	private JTextField textField_Seed;
	private JTextField textField_Variance;
	private JTextField textField_Smooth;
	
	private JLabel lbl_keepWalking;
	private JLabel lbl_speedReduction;
	
	
	// since Java3D is not able to render small graphic elements sufficiently, an internal factor can be used for:
	// camera positions (x,y,z); chamber size; fly size; max / min flight speed; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("Flies in cube");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 460, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(6, 6, 440, 475);
		contentPane.add(tabbedPane);
		
		JPanel panel_cameras = new JPanel();
		tabbedPane.addTab("Cameras", null, panel_cameras, null);
		panel_cameras.setLayout(null);
		
		JTabbedPane tabbedPane_cameras = new JTabbedPane(JTabbedPane.BOTTOM);
		tabbedPane_cameras.setBounds(6, 6, 407, 249);
		panel_cameras.add(tabbedPane_cameras);
		
		
		
		/** CAMERA 1 **/
		
		JPanel panel_Camera1 = new JPanel();
		tabbedPane_cameras.addTab("Camera 1", null, panel_Camera1, null);
		panel_Camera1.setLayout(null);
		
		JLabel label_8 = new JLabel("Use camera:");
		label_8.setBounds(6, 10, 93, 16);
		panel_Camera1.add(label_8);
		
		checkBox_useCamera1 = new JCheckBox("");
		checkBox_useCamera1.setSelected(true);
		checkBox_useCamera1.setBounds(111, 6, 128, 23);
		panel_Camera1.add(checkBox_useCamera1);
		
		JLabel label_9 = new JLabel("Position:");
		label_9.setBounds(6, 56, 61, 16);
		panel_Camera1.add(label_9);
		
		JLabel label_10 = new JLabel("Rotation:");
		label_10.setBounds(6, 108, 61, 16);
		panel_Camera1.add(label_10);
		
		JLabel label_11 = new JLabel("X:");
		label_11.setBounds(79, 56, 12, 16);
		panel_Camera1.add(label_11);
		
		textField_x1 = new JTextField();
		textField_x1.setToolTipText("decimeter");
		textField_x1.setText("0.0");
		textField_x1.setColumns(10);
		textField_x1.setBounds(103, 50, 47, 28);
		panel_Camera1.add(textField_x1);
		
		JLabel label_12 = new JLabel("Y:");
		label_12.setBounds(155, 56, 20, 16);
		panel_Camera1.add(label_12);
		
		textField_y1 = new JTextField();
		textField_y1.setToolTipText("decimeter");
		textField_y1.setText("0.0");
		textField_y1.setColumns(10);
		textField_y1.setBounds(176, 50, 47, 28);
		panel_Camera1.add(textField_y1);
		
		JLabel label_13 = new JLabel("Z:");
		label_13.setBounds(228, 56, 20, 16);
		panel_Camera1.add(label_13);
		
		textField_z1 = new JTextField();
		textField_z1.setToolTipText("decimeter");
		textField_z1.setText("8.0");
		textField_z1.setColumns(10);
		textField_z1.setBounds(248, 50, 47, 28);
		panel_Camera1.add(textField_z1);
		
		JLabel label_14 = new JLabel("Rotationaxis:");
		label_14.setBounds(82, 108, 93, 16);
		panel_Camera1.add(label_14);
		
		comboBox_axis1 = new JComboBox();
		comboBox_axis1.setModel(new DefaultComboBoxModel(new String[] {"X", "Y", "Z", "X/Y", "X/Z", "Y/Z"}));
		comboBox_axis1.setBounds(164, 104, 75, 27);
		panel_Camera1.add(comboBox_axis1);
		
		JLabel label_15 = new JLabel("Angle:");
		label_15.setBounds(248, 108, 47, 16);
		panel_Camera1.add(label_15);
		
		textField_angle1 = new JTextField();
		textField_angle1.setToolTipText("degree");
		textField_angle1.setText("0");
		textField_angle1.setColumns(10);
		textField_angle1.setBounds(294, 102, 47, 28);
		panel_Camera1.add(textField_angle1);
		
		
		
		/** CAMERA 2 **/
		
		JPanel panel_Camera2 = new JPanel();
		tabbedPane_cameras.addTab("Camera 2", null, panel_Camera2, null);
		panel_Camera2.setLayout(null);
		
		JLabel label = new JLabel("Use camera:");
		label.setBounds(6, 10, 93, 16);
		panel_Camera2.add(label);
		
		checkBox_useCamera2 = new JCheckBox("");
		checkBox_useCamera2.setBounds(111, 6, 128, 23);
		panel_Camera2.add(checkBox_useCamera2);
		
		JLabel label_1 = new JLabel("Position:");
		label_1.setBounds(6, 56, 61, 16);
		panel_Camera2.add(label_1);
		
		JLabel label_2 = new JLabel("Rotation:");
		label_2.setBounds(6, 108, 61, 16);
		panel_Camera2.add(label_2);
		
		JLabel label_3 = new JLabel("X:");
		label_3.setBounds(79, 56, 12, 16);
		panel_Camera2.add(label_3);
		
		textField_x2 = new JTextField();
		textField_x2.setToolTipText("decimeter");
		textField_x2.setText("0.0");
		textField_x2.setColumns(10);
		textField_x2.setBounds(103, 50, 47, 28);
		panel_Camera2.add(textField_x2);
		
		JLabel label_4 = new JLabel("Y:");
		label_4.setBounds(155, 56, 20, 16);
		panel_Camera2.add(label_4);
		
		textField_y2 = new JTextField();
		textField_y2.setToolTipText("decimeter");
		textField_y2.setText("0.0");
		textField_y2.setColumns(10);
		textField_y2.setBounds(176, 50, 47, 28);
		panel_Camera2.add(textField_y2);
		
		JLabel label_5 = new JLabel("Z:");
		label_5.setBounds(228, 56, 20, 16);
		panel_Camera2.add(label_5);
		
		textField_z2 = new JTextField();
		textField_z2.setToolTipText("decimeter");
		textField_z2.setText("8.0");
		textField_z2.setColumns(10);
		textField_z2.setBounds(248, 50, 47, 28);
		panel_Camera2.add(textField_z2);
		
		JLabel label_6 = new JLabel("Rotationaxis:");
		label_6.setBounds(82, 108, 93, 16);
		panel_Camera2.add(label_6);
		
		comboBox_axis2 = new JComboBox();
		comboBox_axis2.setModel(new DefaultComboBoxModel(new String[] {"X", "Y", "Z", "X/Y", "X/Z", "Y/Z"}));
		comboBox_axis2.setSelectedIndex(0);
		comboBox_axis2.setBounds(164, 104, 75, 27);
		panel_Camera2.add(comboBox_axis2);
		
		JLabel label_7 = new JLabel("Angle:");
		label_7.setBounds(248, 108, 47, 16);
		panel_Camera2.add(label_7);
		
		textField_angle2 = new JTextField();
		textField_angle2.setToolTipText("degree");
		textField_angle2.setText("-90");
		textField_angle2.setColumns(10);
		textField_angle2.setBounds(294, 102, 47, 28);
		panel_Camera2.add(textField_angle2);
		
		
		
		/** CAMERA 3 **/
		
		JPanel panel_Camera3 = new JPanel();
		tabbedPane_cameras.addTab("Camera 3", null, panel_Camera3, null);
		panel_Camera3.setLayout(null);
		
		JLabel lblUseCamera = new JLabel("Use camera:");
		lblUseCamera.setBounds(6, 10, 93, 16);
		panel_Camera3.add(lblUseCamera);
		
		checkBox_useCamera3 = new JCheckBox("");
		checkBox_useCamera3.setSelected(true);
		checkBox_useCamera3.setBounds(111, 6, 128, 23);
		panel_Camera3.add(checkBox_useCamera3);
		
		JLabel lblPosition_1 = new JLabel("Position:");
		lblPosition_1.setBounds(6, 56, 61, 16);
		panel_Camera3.add(lblPosition_1);
		
		JLabel lblRotation_3 = new JLabel("Rotation:");
		lblRotation_3.setBounds(6, 108, 61, 16);
		panel_Camera3.add(lblRotation_3);
		
		JLabel lblX_2 = new JLabel("X:");
		lblX_2.setBounds(79, 56, 12, 16);
		panel_Camera3.add(lblX_2);
		
		textField_x3 = new JTextField();
		textField_x3.setToolTipText("decimeter");
		textField_x3.setText("0.0");
		textField_x3.setBounds(103, 50, 47, 28);
		panel_Camera3.add(textField_x3);
		textField_x3.setColumns(10);
		
		JLabel lblY_2 = new JLabel("Y:");
		lblY_2.setBounds(155, 56, 20, 16);
		panel_Camera3.add(lblY_2);
		
		textField_y3 = new JTextField();
		textField_y3.setToolTipText("decimeter");
		textField_y3.setText("0.0");
		textField_y3.setBounds(176, 50, 47, 28);
		panel_Camera3.add(textField_y3);
		textField_y3.setColumns(10);
		
		JLabel lblZ_2 = new JLabel("Z:");
		lblZ_2.setBounds(228, 56, 20, 16);
		panel_Camera3.add(lblZ_2);
		
		textField_z3 = new JTextField();
		textField_z3.setToolTipText("decimeter");
		textField_z3.setText("8.0");
		textField_z3.setBounds(248, 50, 47, 28);
		panel_Camera3.add(textField_z3);
		textField_z3.setColumns(10);
		
		JLabel lblRotationaxis = new JLabel("Rotationaxis:");
		lblRotationaxis.setBounds(82, 108, 93, 16);
		panel_Camera3.add(lblRotationaxis);
		
		comboBox_axis3 = new JComboBox();
		comboBox_axis3.setModel(new DefaultComboBoxModel(new String[] {"X", "Y", "Z", "X/Y", "X/Z", "Y/Z"}));
		comboBox_axis3.setSelectedIndex(1);
		comboBox_axis3.setBounds(164, 104, 75, 27);
		panel_Camera3.add(comboBox_axis3);
		
		JLabel lblAngle_1 = new JLabel("Angle:");
		lblAngle_1.setBounds(248, 108, 47, 16);
		panel_Camera3.add(lblAngle_1);
		
		textField_angle3 = new JTextField();
		textField_angle3.setToolTipText("degree");
		textField_angle3.setText("45");
		textField_angle3.setBounds(294, 102, 47, 28);
		panel_Camera3.add(textField_angle3);
		textField_angle3.setColumns(10);
		
		
		
		/** GENERAL CAMERA INFORMATIONS **/
		
		JLabel lblFps = new JLabel("FPS:");
		lblFps.setBounds(33, 273, 97, 16);
		panel_cameras.add(lblFps);
		
		textField_fps = new JTextField();
		textField_fps.setToolTipText("frames per second");
		textField_fps.setText("30");
		textField_fps.setBounds(132, 267, 54, 28);
		panel_cameras.add(textField_fps);
		textField_fps.setColumns(10);
		
		JLabel lblResolutionX = new JLabel("Resolution X:");
		lblResolutionX.setBounds(33, 331, 89, 16);
		panel_cameras.add(lblResolutionX);
		
		textField_resolutionX = new JTextField();
		textField_resolutionX.setToolTipText("pixel");
		textField_resolutionX.setText("500");
		textField_resolutionX.setBounds(134, 325, 60, 28);
		panel_cameras.add(textField_resolutionX);
		textField_resolutionX.setColumns(10);
		
		JLabel lblResolutionY = new JLabel("Resolution Y:");
		lblResolutionY.setBounds(198, 331, 89, 16);
		panel_cameras.add(lblResolutionY);
		
		textField_resolutionY = new JTextField();
		textField_resolutionY.setToolTipText("pixel");
		textField_resolutionY.setText("500");
		textField_resolutionY.setBounds(305, 325, 60, 28);
		panel_cameras.add(textField_resolutionY);
		textField_resolutionY.setColumns(10);
		
		
		
		/** CHAMBER **/
		
		JPanel panel_chamber = new JPanel();
		tabbedPane.addTab("Chamber", null, panel_chamber, null);
		panel_chamber.setLayout(null);
		
		JLabel lblSize = new JLabel("Size:");
		lblSize.setBounds(6, 22, 41, 16);
		panel_chamber.add(lblSize);
		
		textField_size = new JTextField();
		textField_size.setToolTipText("decimeter");
		textField_size.setText("2.0");
		textField_size.setBounds(130, 16, 57, 28);
		panel_chamber.add(textField_size);
		textField_size.setColumns(10);
		
		JLabel lblTransparencyLevel = new JLabel("Transparency level:");
		lblTransparencyLevel.setBounds(6, 83, 130, 16);
		panel_chamber.add(lblTransparencyLevel);
		
		textField_transparencyLevel = new JTextField();
		textField_transparencyLevel.setToolTipText("1/100 %");
		textField_transparencyLevel.setText("0.9");
		textField_transparencyLevel.setBounds(130, 77, 41, 28);
		panel_chamber.add(textField_transparencyLevel);
		textField_transparencyLevel.setColumns(10);
		
		JLabel lblColorrgb = new JLabel("Color (RGB):");
		lblColorrgb.setBounds(6, 137, 100, 16);
		panel_chamber.add(lblColorrgb);
		
		textField_chamberColorR = new JTextField();
		textField_chamberColorR.setToolTipText("red in 1/100 %");
		textField_chamberColorR.setText("0.0");
		textField_chamberColorR.setBounds(130, 129, 57, 28);
		panel_chamber.add(textField_chamberColorR);
		textField_chamberColorR.setColumns(10);
		
		textField_chamberColorG = new JTextField();
		textField_chamberColorG.setToolTipText("green in 1/100 %");
		textField_chamberColorG.setText("0.2");
		textField_chamberColorG.setBounds(199, 129, 57, 28);
		panel_chamber.add(textField_chamberColorG);
		textField_chamberColorG.setColumns(10);
		
		textField_chamberColorB = new JTextField();
		textField_chamberColorB.setToolTipText("blue in 1/100%");
		textField_chamberColorB.setText("0.3");
		textField_chamberColorB.setBounds(268, 129, 57, 28);
		panel_chamber.add(textField_chamberColorB);
		textField_chamberColorB.setColumns(10);
		
		
		
		/** FLIES **/
		
		JPanel panel_flies = new JPanel();
		tabbedPane.addTab("Flies", null, panel_flies, null);
		panel_flies.setLayout(null);
		
		JTabbedPane tabbedPane_flies = new JTabbedPane(JTabbedPane.BOTTOM);
		tabbedPane_flies.setBounds(0, 0, 419, 423);
		panel_flies.add(tabbedPane_flies);
		
		
		
		/** GENERAL FLY SETTINGS **/
		
		
		
		/** A.I. FLY SETTINGS **/
		
		JPanel panel_general = new JPanel();
		tabbedPane_flies.addTab("General", null, panel_general, null);
		panel_general.setLayout(null);
		
		JLabel lblNumberOfFlies = new JLabel("Number of flies:");
		lblNumberOfFlies.setBounds(6, 26, 110, 16);
		panel_general.add(lblNumberOfFlies);
		
		JLabel lblInjectStunnedFlies = new JLabel("Inject stunned:");
		lblInjectStunnedFlies.setBounds(6, 89, 100, 16);
		panel_general.add(lblInjectStunnedFlies);
		
		checkBox_stunned = new JCheckBox("");
		checkBox_stunned.setToolTipText("flies start from the bottom");
		checkBox_stunned.setBounds(120, 88, 45, 23);
		panel_general.add(checkBox_stunned);
		
		textField_numberOfFlies = new JTextField();
		textField_numberOfFlies.setToolTipText("total number");
		textField_numberOfFlies.setText("5");
		textField_numberOfFlies.setBounds(118, 20, 49, 28);
		panel_general.add(textField_numberOfFlies);
		textField_numberOfFlies.setColumns(1);
		
		JLabel lblFlySize = new JLabel("Fly size:");
		lblFlySize.setBounds(6, 152, 61, 16);
		panel_general.add(lblFlySize);
		
		textField_flySize = new JTextField();
		textField_flySize.setToolTipText("decimeter");
		textField_flySize.setText("0.02");
		textField_flySize.setBounds(118, 146, 70, 28);
		panel_general.add(textField_flySize);
		textField_flySize.setColumns(10);
		
		JLabel label_16 = new JLabel("Color (RGB):");
		label_16.setBounds(6, 208, 100, 16);
		panel_general.add(label_16);
		
		textField_flyColorR = new JTextField();
		textField_flyColorR.setToolTipText("red in 1/100 %");
		textField_flyColorR.setText("0.4");
		textField_flyColorR.setColumns(10);
		textField_flyColorR.setBounds(119, 202, 57, 28);
		panel_general.add(textField_flyColorR);
		
		textField_flyColorG = new JTextField();
		textField_flyColorG.setToolTipText("green in 1/100 %");
		textField_flyColorG.setText("0.2");
		textField_flyColorG.setColumns(10);
		textField_flyColorG.setBounds(188, 202, 57, 28);
		panel_general.add(textField_flyColorG);
		
		textField_flyColorB = new JTextField();
		textField_flyColorB.setToolTipText("blue in 1/100 %");
		textField_flyColorB.setText("0.1");
		textField_flyColorB.setColumns(10);
		textField_flyColorB.setBounds(257, 202, 57, 28);
		panel_general.add(textField_flyColorB);
		
		JPanel panel_AI = new JPanel();
		tabbedPane_flies.addTab("A.I.", null, panel_AI, null);
		panel_AI.setLayout(null);
		
		JLabel lblNegativeGeotaxis = new JLabel("Negative geotaxis:");
		lblNegativeGeotaxis.setBounds(6, 6, 129, 16);
		panel_AI.add(lblNegativeGeotaxis);
		
		checkBox_negativeGeotaxis = new JCheckBox("");
		checkBox_negativeGeotaxis.setToolTipText("flies are oriented to the top");
		checkBox_negativeGeotaxis.setSelected(true);
		checkBox_negativeGeotaxis.setBounds(196, 6, 56, 16);
		panel_AI.add(checkBox_negativeGeotaxis);
		
		JLabel lblNegativeGeotaxisIntensity = new JLabel("Negative geotaxis intensity:");
		lblNegativeGeotaxisIntensity.setBounds(6, 34, 184, 16);
		panel_AI.add(lblNegativeGeotaxisIntensity);
		
		textField_negativeGeotaxis = new JTextField();
		textField_negativeGeotaxis.setToolTipText("1/100 %");
		textField_negativeGeotaxis.setText("0.002");
		textField_negativeGeotaxis.setBounds(196, 28, 61, 28);
		panel_AI.add(textField_negativeGeotaxis);
		textField_negativeGeotaxis.setColumns(10);
		
		JLabel lblMaxMovementDuration = new JLabel("Max. movement window:");
		lblMaxMovementDuration.setBounds(6, 62, 166, 16);
		panel_AI.add(lblMaxMovementDuration);
		
		JLabel lblMinMovementDuration = new JLabel("Min. movement window:");
		lblMinMovementDuration.setBounds(6, 90, 166, 16);
		panel_AI.add(lblMinMovementDuration);
		
		JLabel lblMaxFlightSpeed = new JLabel("Max. flight speed:");
		lblMaxFlightSpeed.setBounds(6, 118, 166, 16);
		panel_AI.add(lblMaxFlightSpeed);
		
		JLabel lblMinFlightSpeed = new JLabel("Min. flight speed:");
		lblMinFlightSpeed.setBounds(6, 146, 129, 16);
		panel_AI.add(lblMinFlightSpeed);
		
		JLabel lblWalkingSpeedReduction = new JLabel("Walking speed reduction:");
		lblWalkingSpeedReduction.setBounds(6, 174, 166, 16);
		panel_AI.add(lblWalkingSpeedReduction);
		
		JLabel lblSittingPropability = new JLabel("Sitting probability:");
		lblSittingPropability.setBounds(6, 202, 166, 16);
		panel_AI.add(lblSittingPropability);
		
		JLabel lblKeepWalkingProbability = new JLabel("Keep walking probability:");
		lblKeepWalkingProbability.setBounds(6, 230, 166, 16);
		panel_AI.add(lblKeepWalkingProbability);
		
		JLabel lblStunnedDuration = new JLabel("Stunned duration:");
		lblStunnedDuration.setBounds(6, 258, 166, 16);
		panel_AI.add(lblStunnedDuration);
		
		textField_maxMovementWindow = new JTextField();
		textField_maxMovementWindow.setToolTipText("milli seconds");
		textField_maxMovementWindow.setText("1000");
		textField_maxMovementWindow.setBounds(196, 56, 61, 28);
		panel_AI.add(textField_maxMovementWindow);
		textField_maxMovementWindow.setColumns(10);
		
		textField_minMovementWindow = new JTextField();
		textField_minMovementWindow.setToolTipText("milli seconds");
		textField_minMovementWindow.setText("200");
		textField_minMovementWindow.setBounds(196, 84, 61, 28);
		panel_AI.add(textField_minMovementWindow);
		textField_minMovementWindow.setColumns(10);
		
		textField_maxFlightSpeed = new JTextField();
		textField_maxFlightSpeed.setToolTipText("decimeter / milli second");
		textField_maxFlightSpeed.setText("0.003");
		textField_maxFlightSpeed.setBounds(196, 112, 88, 28);
		panel_AI.add(textField_maxFlightSpeed);
		textField_maxFlightSpeed.setColumns(10);
		
		textField_minFlightSpeed = new JTextField();
		textField_minFlightSpeed.setToolTipText("decimeter / milli second");
		textField_minFlightSpeed.setText("0.0005");
		textField_minFlightSpeed.setBounds(196, 140, 88, 28);
		panel_AI.add(textField_minFlightSpeed);
		textField_minFlightSpeed.setColumns(10);
		
		textField_walkingSpeedReduction = new JTextField();
		textField_walkingSpeedReduction.setToolTipText("factor");
		textField_walkingSpeedReduction.setText("0.1");
		textField_walkingSpeedReduction.setBounds(196, 168, 61, 28);
		panel_AI.add(textField_walkingSpeedReduction);
		textField_walkingSpeedReduction.setColumns(10);
		
		textField_sittingProbability = new JTextField();
		textField_sittingProbability.setToolTipText("1/100 %");
		textField_sittingProbability.setText("0.002");
		textField_sittingProbability.setBounds(196, 196, 61, 28);
		panel_AI.add(textField_sittingProbability);
		textField_sittingProbability.setColumns(10);
		
		textField_keepWalkingProbability = new JTextField();
		textField_keepWalkingProbability.setToolTipText("1/100 %");
		textField_keepWalkingProbability.setText("0.02");
		textField_keepWalkingProbability.setBounds(196, 224, 61, 28);
		panel_AI.add(textField_keepWalkingProbability);
		textField_keepWalkingProbability.setColumns(10);
		
		textField_stunnedDuration = new JTextField();
		textField_stunnedDuration.setToolTipText("milli seconds");
		textField_stunnedDuration.setText("1000");
		textField_stunnedDuration.setBounds(196, 252, 61, 28);
		panel_AI.add(textField_stunnedDuration);
		textField_stunnedDuration.setColumns(10);
		
		JLabel lblStd = new JLabel("Std.: 0.002");
		lblStd.setBounds(296, 34, 85, 16);
		panel_AI.add(lblStd);
		
		JLabel lblStd_1 = new JLabel("Std.: 1000");
		lblStd_1.setBounds(296, 62, 72, 16);
		panel_AI.add(lblStd_1);
		
		JLabel lblStd_2 = new JLabel("Std.: 200");
		lblStd_2.setBounds(296, 90, 61, 16);
		panel_AI.add(lblStd_2);
		
		JLabel lblStd_3 = new JLabel("Std.: 0.003");
		lblStd_3.setBounds(296, 118, 85, 16);
		panel_AI.add(lblStd_3);
		
		JLabel lblStd_4 = new JLabel("Std.: 0.0005");
		lblStd_4.setBounds(296, 146, 96, 16);
		panel_AI.add(lblStd_4);
		
		lbl_speedReduction = new JLabel("Std.: 0.1");
		lbl_speedReduction.setBounds(296, 174, 61, 16);
		panel_AI.add(lbl_speedReduction);
		
		JLabel lblStd_6 = new JLabel("Std.: 0.002");
		lblStd_6.setBounds(296, 202, 85, 16);
		panel_AI.add(lblStd_6);
		
		lbl_keepWalking = new JLabel("Std.: 0.02");
		lbl_keepWalking.setBounds(296, 230, 61, 16);
		panel_AI.add(lbl_keepWalking);
		
		JLabel lblStd_8 = new JLabel("Std.: 1000");
		lblStd_8.setBounds(296, 258, 72, 16);
		panel_AI.add(lblStd_8);
		
		JLabel lblUseAwesomeAi = new JLabel("Gaussian Random Walk:");
		lblUseAwesomeAi.setBounds(6, 344, 166, 14);
		panel_AI.add(lblUseAwesomeAi);
		
		
		//Disable maxFlightSpeed, minFlightSpeed, minMovementPath, maxMovementPath and enable Viariance and Smoothness, when checkBox_awesomeMovementPath is selected.
		//Info Button only visible when using awesome movement path.
		//Also change standart Values for different path generation models.
		
		checkBox_awesomeMovementPath = new JCheckBox("");
		checkBox_awesomeMovementPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean selected = checkBox_awesomeMovementPath.getModel().isSelected();
				
		if (selected) {
			textField_maxFlightSpeed.setEnabled(false);
			textField_minFlightSpeed.setEnabled(false);
			textField_minMovementWindow.setEnabled(false);
			textField_maxMovementWindow.setEnabled(false);
			textField_Variance.setEnabled(true);
			textField_Smooth.setEnabled(true);
			textField_keepWalkingProbability.setText("0.95");
			textField_walkingSpeedReduction.setText("0.95");
			btnInfoButton.setEnabled(true);
			lbl_speedReduction.setText("Std.: 0.95");
			lbl_keepWalking.setText("Std.: 0.95");
		} 
		else
		{
			textField_maxFlightSpeed.setEnabled(true);
			textField_minFlightSpeed.setEnabled(true);
			textField_minMovementWindow.setEnabled(true);
			textField_maxMovementWindow.setEnabled(true);
			textField_Variance.setEnabled(false);
			textField_Smooth.setEnabled(false);
			textField_keepWalkingProbability.setText("0.02");
			textField_walkingSpeedReduction.setText("0.1");
			btnInfoButton.setEnabled(false);
			lbl_speedReduction.setText("Std.: 0.1");
			lbl_keepWalking.setText("Std.: 0.02");
			
			}
			}
		});
		
		checkBox_awesomeMovementPath.setBounds(196, 341, 97, 23);
		panel_AI.add(checkBox_awesomeMovementPath);
		
		JLabel lblVariance = new JLabel("Variance:");
		lblVariance.setBounds(6, 286, 88, 14);
		panel_AI.add(lblVariance);
		
		textField_Variance = new JTextField();
		textField_Variance.setText("0.003");
		textField_Variance.setEnabled(false);
		textField_Variance.setBounds(196, 280, 61, 28);
		panel_AI.add(textField_Variance);
		textField_Variance.setColumns(10);
		
		JLabel lblStd_9 = new JLabel("Std.: 0.003");
		lblStd_9.setBounds(296, 285, 72, 16);
		panel_AI.add(lblStd_9);
		
		JLabel lblSmooth = new JLabel("Smoothness:");
		lblSmooth.setBounds(6, 314, 88, 14);
		panel_AI.add(lblSmooth);
		
		textField_Smooth = new JTextField();
		textField_Smooth.setText("0.999");
		textField_Smooth.setEnabled(false);
		textField_Smooth.setColumns(10);
		textField_Smooth.setBounds(196, 308, 61, 28);
		panel_AI.add(textField_Smooth);
		
		JLabel lblStd_10 = new JLabel("Std.: 0.999");
		lblStd_10.setBounds(296, 309, 72, 16);
		panel_AI.add(lblStd_10);
		
		//Info button for parameter information when using awesome movementpath
		btnInfoButton = new JButton("Info");
		btnInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Popup pop = new Popup();
				pop.setLocation(570,100);
		    	pop.setVisible(true);  	
			}
		});
		btnInfoButton.setBounds(296, 341, 79, 23);
		btnInfoButton.setEnabled(false);
		panel_AI.add(btnInfoButton);
		
		
		/** GENERAL SETTINGS **/
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("General", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblRepeatVideo = new JLabel("Repeat video:");
		lblRepeatVideo.setBounds(6, 24, 92, 16);
		panel.add(lblRepeatVideo);
		
		textField_repeatVideo = new JTextField();
		textField_repeatVideo.setToolTipText("number of replay iterations");
		textField_repeatVideo.setText("-1");
		textField_repeatVideo.setBounds(110, 18, 48, 28);
		panel.add(textField_repeatVideo);
		textField_repeatVideo.setColumns(10);
		
		JLabel lblForInfinite = new JLabel("(-1 for infinite loop)");
		lblForInfinite.setBounds(185, 24, 148, 16);
		panel.add(lblForInfinite);
		
		JLabel lblOutputPath = new JLabel("Output path:");
		lblOutputPath.setBounds(6, 138, 92, 16);
		panel.add(lblOutputPath);
		
		textField_OutputPath = new JTextField();
		textField_OutputPath.setToolTipText("path where to store the data");
		textField_OutputPath.setText("/Users/Bena/Desktop/test/");
		textField_OutputPath.setBounds(110, 132, 303, 28);
		panel.add(textField_OutputPath);
		textField_OutputPath.setColumns(10);
		
		JLabel label_17 = new JLabel("Capturing time:");
		label_17.setBounds(6, 80, 107, 16);
		panel.add(label_17);
		
		textField_capturingTime = new JTextField();
		textField_capturingTime.setToolTipText("milli seconds");
		textField_capturingTime.setText("5000");
		textField_capturingTime.setColumns(10);
		textField_capturingTime.setBounds(110, 74, 75, 28);
		panel.add(textField_capturingTime);
		
		chckbxOutputProjectionMatrices = new JCheckBox("Output projection matrices");
		chckbxOutputProjectionMatrices.setToolTipText("Saves the projection matrices");
		chckbxOutputProjectionMatrices.setBounds(6, 264, 215, 23);
		panel.add(chckbxOutputProjectionMatrices);
		
		chckbxOutputdPaths = new JCheckBox("Output 2D paths");
		chckbxOutputdPaths.setToolTipText("Saves the 2D paths of the flies");
		chckbxOutputdPaths.setSelected(true);
		chckbxOutputdPaths.setBounds(6, 194, 179, 23);
		panel.add(chckbxOutputdPaths);
		
		JLabel lblSeed = new JLabel("Seed:");
		lblSeed.setBounds(10, 333, 46, 14);
		panel.add(lblSeed);
		
		textField_Seed = new JTextField();
		textField_Seed.setToolTipText("set seed");
		textField_Seed.setText("-1");
		textField_Seed.setBounds(112, 322, 48, 28);
		panel.add(textField_Seed);
		textField_Seed.setColumns(10);
		
		JLabel lblForNo = new JLabel("(-1 for no seed)");
		lblForNo.setBounds(185, 328, 148, 16);
		panel.add(lblForNo);
		
		
		/** MAIN FRAME BUTTONS **/
		
		btnSave = new JButton("Save");
		btnSave.setEnabled(false);
		btnSave.setBounds(16, 478, 117, 29);
		contentPane.add(btnSave);
		btnSave.addActionListener(this);
		
		btnShow = new JButton("Show");
		btnShow.setBounds(317, 478, 117, 29);
		contentPane.add(btnShow);
		btnShow.addActionListener(this);
	}

		
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == btnShow){
			
			// JFrames already in use?
			if(camera1Frame != null){
				camera1Frame.setVisible(false);
				camera1Frame.dispose();
				camera1Frame = null;
			}
			if(camera2Frame != null){
				camera2Frame.setVisible(false);
				camera2Frame.dispose();
				camera2Frame = null;
			}
			if(camera3Frame != null){
				camera3Frame.setVisible(false);
				camera3Frame.dispose();
				camera3Frame = null;
			}
			
			// free the memory
			flies = null;
			uni = null;
			animator = null;
			alpha = null;
			
			/*chamber = null;
			illu = null;
			background = null;
			group = null;*/		
			
			Runtime.getRuntime().gc();
		
			// GET THE VALUES
			
			// general camera values
			int resolutionX = Integer.parseInt(this.textField_resolutionX.getText());
			int resolutionY = Integer.parseInt(this.textField_resolutionY.getText());
			
			// camera1:
			boolean useCamera1 = this.checkBox_useCamera1.isSelected();
			float posX1 = Float.parseFloat(this.textField_x1.getText());
			float posY1 = Float.parseFloat(this.textField_y1.getText());
			float posZ1 = Float.parseFloat(this.textField_z1.getText());
			int rotationAxis1 = this.comboBox_axis1.getSelectedIndex();
			int angle1 = Integer.parseInt(this.textField_angle1.getText());
			
			// camera2:
			boolean useCamera2 = this.checkBox_useCamera2.isSelected();
			float posX2 = Float.parseFloat(this.textField_x2.getText());
			float posY2 = Float.parseFloat(this.textField_y2.getText());
			float posZ2 = Float.parseFloat(this.textField_z2.getText());
			int rotationAxis2 = this.comboBox_axis2.getSelectedIndex();
			int angle2 = Integer.parseInt(this.textField_angle2.getText());
			
			// camera3:
			boolean useCamera3 = this.checkBox_useCamera3.isSelected();
			float posX3 = Float.parseFloat(this.textField_x3.getText());
			float posY3 = Float.parseFloat(this.textField_y3.getText());
			float posZ3 = Float.parseFloat(this.textField_z3.getText());
			int rotationAxis3 = this.comboBox_axis3.getSelectedIndex();
			int angle3 = Integer.parseInt(this.textField_angle3.getText());
			
			// chamber values
			float size = Float.parseFloat(this.textField_size.getText());
			float transparencyLevel = Float.parseFloat(this.textField_transparencyLevel.getText());
			float chamberColorR = Float.parseFloat(this.textField_chamberColorR.getText());
			float chamberColorG = Float.parseFloat(this.textField_chamberColorG.getText());
			float chamberColorB = Float.parseFloat(this.textField_chamberColorB.getText());
			Color3f chamberColor = new Color3f(chamberColorR, chamberColorG, chamberColorB);
			
			// general fly values:
			int numberOfFlies = Integer.parseInt(this.textField_numberOfFlies.getText());
			boolean stunned = this.checkBox_stunned.isSelected();
			float flySize = Float.parseFloat(this.textField_flySize.getText());
			float flyColorR = Float.parseFloat(this.textField_flyColorR.getText());
			float flyColorG = Float.parseFloat(this.textField_flyColorG.getText());
			float flyColorB = Float.parseFloat(this.textField_flyColorB.getText());
			Color3f flyColor = new Color3f(flyColorR, flyColorG, flyColorB);
			
			// fly ki values:
			boolean awesome = this.checkBox_awesomeMovementPath.isSelected();
			boolean negativeGeotaxis = this.checkBox_negativeGeotaxis.isSelected();
			float negativeGeotaxisIntensity = Float.parseFloat(this.textField_negativeGeotaxis.getText());
			int maxMovementWindow = Integer.parseInt(this.textField_maxMovementWindow.getText());
			int minMovementWindow = Integer.parseInt(this.textField_minMovementWindow.getText());
			float maxFlightSpeed = Float.parseFloat(this.textField_maxFlightSpeed.getText());
			float minFlightSpeed = Float.parseFloat(this.textField_minFlightSpeed.getText());
			float walkingSpeedReduction = Float.parseFloat(this.textField_walkingSpeedReduction.getText());
			float sittingProbability = Float.parseFloat(this.textField_sittingProbability.getText());
			float keepWalkingProbability = Float.parseFloat(this.textField_keepWalkingProbability.getText());
			int stunnedDuration = Integer.parseInt(this.textField_stunnedDuration.getText());
			float variance = Float.parseFloat(this.textField_Variance.getText());
			float smooth = Float.parseFloat(this.textField_Smooth.getText());
			
			// get general settings
			int repeatVideo = Integer.parseInt(this.textField_repeatVideo.getText());
			int capturingTime = Integer.parseInt(this.textField_capturingTime.getText());
			long seed = Long.parseLong(this.textField_Seed.getText());
			
			// create virtual universe
			Vector3f cam1Pos = new Vector3f(posX1, posY1, posZ1);
			Vector3f cam2Pos = new Vector3f(posX2, posY2, posZ2);
			Vector3f cam3Pos = new Vector3f(posX3, posY3, posZ3);
			AxisAngle4f cam1Rot = new AxisAngle4f(1.0f, 0.0f, 0.0f, this.degrees2Radians(angle1));
			AxisAngle4f cam2Rot = new AxisAngle4f(0.0f, 1.0f, 0.0f, this.degrees2Radians(angle2));
			AxisAngle4f cam3Rot = new AxisAngle4f(1.0f, 0.0f, 0.0f, this.degrees2Radians(angle3));
			switch (rotationAxis1){
			case 0:
				cam1Rot = new AxisAngle4f(1.0f, 0.0f, 0.0f, this.degrees2Radians(angle1));
				break;
			case 1:
				cam1Rot = new AxisAngle4f(0.0f, 1.0f, 0.0f, this.degrees2Radians(angle1));
				break;
			case 2:
				cam1Rot = new AxisAngle4f(0.0f, 0.0f, 1.0f, this.degrees2Radians(angle1));
				break;
			case 3:
				cam1Rot = new AxisAngle4f(1.0f, 1.0f, 0.0f, this.degrees2Radians(angle1));
				break;
			case 4:
				cam1Rot = new AxisAngle4f(1.0f, 0.0f, 1.0f, this.degrees2Radians(angle1));
				break;
			case 5:
				cam1Rot = new AxisAngle4f(0.0f, 1.0f, 1.0f, this.degrees2Radians(angle1));
				break;
			}
			switch (rotationAxis2){
			case 0:
				cam2Rot = new AxisAngle4f(1.0f, 0.0f, 0.0f, this.degrees2Radians(angle2));
				break;
			case 1:
				cam2Rot = new AxisAngle4f(0.0f, 1.0f, 0.0f, this.degrees2Radians(angle2));
				break;
			case 2:
				cam2Rot = new AxisAngle4f(0.0f, 0.0f, 1.0f, this.degrees2Radians(angle2));
				break;
			case 3:
				cam2Rot = new AxisAngle4f(1.0f, 1.0f, 0.0f, this.degrees2Radians(angle2));
				break;
			case 4:
				cam2Rot = new AxisAngle4f(1.0f, 0.0f, 1.0f, this.degrees2Radians(angle2));
				break;
			case 5:
				cam2Rot = new AxisAngle4f(0.0f, 1.0f, 1.0f, this.degrees2Radians(angle2));
				break;
			}
			switch (rotationAxis3){
			case 0:
				cam3Rot = new AxisAngle4f(1.0f, 0.0f, 0.0f, this.degrees2Radians(angle3));
				break;
			case 1:
				cam3Rot = new AxisAngle4f(0.0f, 1.0f, 0.0f, this.degrees2Radians(angle3));
				break;
			case 2:
				cam3Rot = new AxisAngle4f(0.0f, 0.0f, 1.0f, this.degrees2Radians(angle3));
				break;
			case 3:
				cam3Rot = new AxisAngle4f(1.0f, 1.0f, 0.0f, this.degrees2Radians(angle3));
				break;
			case 4:
				cam3Rot = new AxisAngle4f(1.0f, 0.0f, 1.0f, this.degrees2Radians(angle3));
				break;
			case 5:
				cam3Rot = new AxisAngle4f(0.0f, 1.0f, 1.0f, this.degrees2Radians(angle3));
				break;
			}
			
			
			uni = new VirtualUniverse2(cam1Pos, cam2Pos, cam3Pos, cam1Rot, cam2Rot, cam3Rot);
			
			BranchGroup group = new BranchGroup();
			//group = new BranchGroup();
			
			// create white background
			Background background = new Background(1.0f,1.0f,1.0f);
			//background = new Background(1.0f,1.0f,1.0f);
			background.setApplicationBounds(new BoundingSphere(new Point3d(), 100.0));
			group.addChild(background);
			
			// create chamber
			Chamber chamber = new Chamber(size, transparencyLevel, chamberColor);
			//chamber = new Chamber(size,transparencyLevel,chamberColor);
			group.addChild(chamber);
			
			// create flies
			flies = createFlies(numberOfFlies,group, capturingTime, size, flySize, stunned, negativeGeotaxis, flyColor,
					negativeGeotaxisIntensity, maxMovementWindow, minMovementWindow, maxFlightSpeed, minFlightSpeed,
					walkingSpeedReduction, sittingProbability, keepWalkingProbability, stunnedDuration, repeatVideo, seed, awesome, variance, smooth);
			
			// create light conditions
			Illumination[] illu = new Illumination[1];
			//illu = new Illumination[1];
			illu[0]=new Illumination(new Color3f(20.0f,20.0f,20.0f), new Vector3f(5,-6,-10), new Point3d(0,0,0));
			group.addChild(illu[0]);
			
			
			//frame.addBranchGraph(group);
			uni.addContent(group);
			
			if(useCamera1){
				camera1Frame = new JFrame("Camera 1");
				camera1Frame.setUndecorated(true);
				camera1Frame.setSize(resolutionX, resolutionY);
				//Insets insets = camera1Frame.getInsets();
				//camera1Frame.setSize(resolutionX+insets.left+insets.right, resolutionY+insets.bottom+insets.top);
				camera1Frame.getContentPane().add(uni.getCam1().getCanvas());
				camera1Frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				camera1Frame.setLocation(30, 30);
				camera1Frame.setVisible(true);
				
				// BUG in setVisible (?): force size of canvas object in JFrame to given camera resolution
				//uni.getCam1().getCanvas().setSize(resolutionX, resolutionY);
				
				camera1Frame.setResizable(false);
			}
			if(useCamera2){
				camera2Frame = new JFrame("Camera 2");
				camera2Frame.setUndecorated(true);
				camera2Frame.setSize(resolutionX, resolutionY);
				camera2Frame.getContentPane().add(uni.getCam2().getCanvas());
				camera2Frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				camera2Frame.setLocation(60+resolutionX, 30);
				camera2Frame.setVisible(true);
				
				// BUG in setVisible (?): force size of canvas object in JFrame to given camera resolution
				//uni.getCam2().getCanvas().setSize(resolutionX, resolutionY);
				
				camera2Frame.setResizable(false);
			}
			if(useCamera3){
				camera3Frame = new JFrame("Camera 3");
				camera3Frame.setUndecorated(true);
				camera3Frame.setSize(resolutionX, resolutionY);
				camera3Frame.getContentPane().add(uni.getCam3().getCanvas());
				camera3Frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				camera3Frame.setLocation(90+2*resolutionX,30);
				camera3Frame.setVisible(true);
				
				// BUG in setVisible (?): force size of canvas object in JFrame to given camera resolution
				//uni.getCam3().getCanvas().setSize(resolutionX, resolutionY);
				
				camera3Frame.setResizable(false);
			}
			btnSave.setEnabled(true);
			
		}
		else if(evt.getSource() == btnSave){
			int fps = Integer.parseInt(this.textField_fps.getText());
			String outputPath = this.textField_OutputPath.getText();
			
			boolean useCamera1 = this.checkBox_useCamera1.isSelected();
			boolean useCamera2 = this.checkBox_useCamera2.isSelected();
			boolean useCamera3 = this.checkBox_useCamera3.isSelected();
			
			// output 2D path?
			boolean output2Dpath = this.chckbxOutputdPaths.isSelected();
			boolean outputProjectionMatrix = this.chckbxOutputProjectionMatrices.isSelected();
			
			alpha.pause();
			
			// free outputGenerator memory
			//outputGenerator = null;		
			//Runtime.getRuntime().gc();
			
			//OutputGenerator outputGenerator = new OutputGenerator(flies, uni.getCam1(), uni.getCam2(), uni.getCam3(), fps, outputPath, 
			//														useCamera1, useCamera2, useCamera3);
			
			OutputGenerator outputGenerator = new OutputGenerator(flies, uni.getCam1(), uni.getCam2(), uni.getCam3(), fps, outputPath, 
					useCamera1, useCamera2, useCamera3,output2Dpath,outputProjectionMatrix);
			
			//	outputGenerator = new OutputGenerator(flies, uni.getCam1(), uni.getCam2(), uni.getCam3(), fps, outputPath, 
			//			useCamera1, useCamera2, useCamera3);
			outputGenerator.printSuccess();
			JOptionPane.showMessageDialog(this, "Saving done!");
			
			
			alpha.resume();
			btnSave.setEnabled(false);
		}
		
	}
	
	
	
	// create and return flies
		private Fly[] createFlies(int numberOfFlies, BranchGroup group, int trackingDuration,
											float chamberSize, float flySize, boolean stunned, boolean negativeGeotaxis, Color3f flyColor,
											float negativeGeotaxisIntensity, int maxMovementWindow, int minMovementWindow, float maxFlightSpeed, float minFlightSpeed,
											float walkingSpeedReduction, float sittingProbability, float keepWalkingProbability, int stunnedDuration,
											int repeatVideo, long seed, boolean awesome, float variance, float smooth){
			
			Fly[] flies = new Fly[numberOfFlies];
			animator = new FlyAnimator[numberOfFlies];
			
			alpha = new Alpha(repeatVideo,trackingDuration);
			alpha.setStartTime(System.currentTimeMillis() + 100);
			//alpha.resume();
			
			float[] knots = new float[trackingDuration];
			knots[0]=0.0f;
			
			for (int i = 2; i<=trackingDuration; i++){
				knots[i-1]= (float) i/trackingDuration;
			}
			
			//random generator for currentSeed
			Random rand = new Random(seed);
			
			for(int i = 0; i<numberOfFlies;i++){
				
				// each fly gets different seed
				long currentSeed = rand.nextLong();
				
				flies[i] = new Fly(chamberSize, flySize, trackingDuration, stunned, negativeGeotaxis, flyColor,
						negativeGeotaxisIntensity, maxMovementWindow, minMovementWindow, maxFlightSpeed, minFlightSpeed,
						walkingSpeedReduction, sittingProbability, keepWalkingProbability, stunnedDuration, seed, awesome, variance, smooth, currentSeed);
				
				
				
				// Animations
				animator[i] = new FlyAnimator(flies[i],alpha,knots);
				
				group.addChild(animator[i].getInterpolator());
				
				group.addChild(flies[i]);
			}
			return flies;

		}
		
		// f: degrees -> radians  ==>  f(x) = (x*PI)/180
		private float degrees2Radians(float degrees){
			return (float) (degrees*Math.PI)/180;
		}
}
