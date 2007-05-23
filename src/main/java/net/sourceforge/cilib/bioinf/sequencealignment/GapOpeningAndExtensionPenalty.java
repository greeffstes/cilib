/*
 * GapOpeningAndExtensionPenalty.java
 *
 * Created on Sep 21, 2004
 *
 * Copyright (C) 2007 - CIRG@UP
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package net.sourceforge.cilib.bioinf.sequencealignment;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Method that penalises indels as gap groups.
 * Penalty for k contiguous indels (1 gap group) = Gop + (k-1)Gep where Gop is GapOpeningPenalty and Gep is GapExtensionPenalty
 * 
 * @author Fabien Zablocki
 */
public class GapOpeningAndExtensionPenalty implements GapPenaltiesMethod {

	private double gapOpeningPenalty = 2.0;  // default, can be set in XML
	private double gapExtensionPenalty = 1.0; // default, can be set in XML
	private ArrayList<String> alignment;
	private boolean verbose = false;
	
	public void setVerbose(boolean verbose)
	{
		this.verbose = verbose;
	}
	
	public void setGapExtensionPenalty(double gapExtensionPenalty) 
	{
		this.gapExtensionPenalty = gapExtensionPenalty;
	}

	public void setGapOpeningPenalty(double gapOpeningPenalty) 
	{
		this.gapOpeningPenalty = gapOpeningPenalty;
	}
	
	public double getPenalty(ArrayList<String> _alignment) 
	{
		this.alignment = _alignment;
		//	Now modify the fitness based on the formula to penalize gaps and gap groups
		int openingGapsCounter = 0;
		int extensionGapsCounter = 0;

		for (ListIterator l = alignment.listIterator(); l.hasNext(); )
		{
			String s = (String) l.next();
			
			for (int i = 0; i < s.length(); i++)
			{
				if(s.charAt(i) == '-')  //first indel of potential group
				{
					openingGapsCounter++;  //increment Opening Gaps
					
					while(i < s.length()-1)  //look if any extension
					{
						if ( s.charAt(++i) == '-') extensionGapsCounter++;
						else break;
					}
				}
			}	
		}

		double gapPenalty = (extensionGapsCounter*gapExtensionPenalty) + (openingGapsCounter*gapOpeningPenalty);
		
		//	prints the current alignment if verbose on
		if (verbose)
		{
			System.out.println("Penalty: "+gapPenalty+" ["+openingGapsCounter+" opening gap(s)*"+gapOpeningPenalty+" + "+extensionGapsCounter+" extension gap(s)]*"+gapExtensionPenalty+"]");
			for (ListIterator j = alignment.listIterator(); j.hasNext(); )
			{
				String s = (String) j.next();
				System.out.println("'" + s + "'");
			}
		}
		return gapPenalty;
	}
}