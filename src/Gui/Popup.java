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
/* Popup windows with parameter information when using awesome movement path
 * 
 */

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

public class Popup extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public Popup() {
		setTitle("Parameter space information");
		setResizable(false);
		setBounds(100, 100, 431, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextArea txtrInfo = new JTextArea();
		txtrInfo.setToolTipText("");
		txtrInfo.setText("Suggested values for awesome A.I. Path generation:\r\n\r\nWalking Speed Reduction: \t0.95 - 1.00\r\nKeep Walking Probability: \t0.95 - 1.00 \r\n\r\n(smaller values will not lead to useful results)\r\n");
		txtrInfo.setEditable(false);
		txtrInfo.setBounds(0, 0, 425, 266);
		contentPane.add(txtrInfo);
	}
}
