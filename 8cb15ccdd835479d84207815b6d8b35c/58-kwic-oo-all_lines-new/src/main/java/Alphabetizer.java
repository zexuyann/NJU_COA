// -*- Java -*-
/*
 * <copyright>
 *
 *  Copyright (c) 2002
 *  Institute for Information Processing and Computer Supported New Media (IICM),
 *  Graz University of Technology, Austria.
 *
 * </copyright>
 *
 * <file>
 *
 *  Name:    Alphabetizer.java
 *
 *  Purpose: Sorts circular shifts alphabetically
 *
 *  Created: 23 Sep 2002
 *
 *  $Id$
 *
 *  Description:
 *    Sorts circular shifts alphabetically
 * </file>
 */



/*
 * $Log$
 */

/**
 *  An object of the Alphabetizer class sorts all lines, that it gets
 *  from CircularShifter. Methods to access sorted lines are provided.
 *  @author  dhelic
 *  @version $Id$
 */

public class Alphabetizer{

//----------------------------------------------------------------------
/**
 * Fields
 *
 */
//----------------------------------------------------------------------

  /**
   * Array holding sorted indices of lines
   *
   */

  private int sorted_[];

  /**
   * CircularShifter that provides lines
   *
   */

  private CircularShifter shifter_;

//----------------------------------------------------------------------
/**
 * Constructors
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * Methods
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
  /**
   * Sorts all lines from the shifter.
   * @param shifter the source of lines
   */

  public void alpha(CircularShifter shifter){
    this.shifter_ = shifter;
    int flag=0;
    int bottom=shifter.getLineCount();
    sorted_ = new int[bottom];
    for(int i = 0; i < bottom; i ++){
      sorted_[i] = i;
    }
    for(int i=0;i<sorted_.length-1;i++){
      for(int j=i+1;j<sorted_.length;j++){
        String a = shifter_.getLineAsString(sorted_[i]);
        String b = shifter_.getLineAsString(sorted_[j]);
        if(a.compareTo(b)>0) {
          sorted_[i] = sorted_[i] ^ sorted_[j];
          sorted_[j] = sorted_[i] ^ sorted_[j];
          sorted_[i] = sorted_[i] ^ sorted_[j];
        }
      }
    }
  }

//----------------------------------------------------------------------
  /**
   * Gets the line from the specified position.
   * String representing the line is returned.
   * @param line line index
   * @return String[]
   */

  public String getLineAsString(int line){
    return shifter_.getLineAsString(sorted_[line]);
  }

//----------------------------------------------------------------------
  /**
   * Gets the number of lines.
   * @return int
   */

  public int getLineCount(){
    return shifter_.getLineCount();
  }

//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}
