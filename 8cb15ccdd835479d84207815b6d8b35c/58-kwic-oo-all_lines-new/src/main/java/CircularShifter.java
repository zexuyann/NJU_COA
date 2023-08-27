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
 *  Name:    CircularShifter.java
 *
 *  Purpose: Holds circular shifts of input lines
 *
 *  Created: 23 Sep 2002
 *
 *  $Id$
 *
 *  Description:
 *    Holds circular shifts of input lines
 * </file>
 */



/*
 * $Log$
 */

import java.util.ArrayList;

/**
 *  An object of the CircularShifter class produces and holds all circular shifts of
 *  a set of lines. In principle, the CircularShifter class provides a
 *  similar interface as the LineStorage class, thus allowing to manipulate 
 *  the lines that it holds. However, in the case of the CircularShifter
 *  class the lines are actually circular shifts of a particular set of original
 *  lines. Also, the CircularShifter class does not provide interface for
 *  updating of characters, words, and lines that it holds, but just an
 *  interface for reading characters, words, and lines.
 *  @author  dhelic
 *  @version $Id$
 */

public class CircularShifter{

//----------------------------------------------------------------------
/**
 * Fields
 *
 */
//----------------------------------------------------------------------

  /**
   * LineStorage for circular shifts
   *
   */

  private LineStorage shifts_;

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
   * Produces all circular shifts of lines in a given set. Circular shifts
   * are stored internally and can be queried by means of other methods. Note,
   * that for each line the first circular shift is same as the original line.
   * @param lines A set of lines
   * @see #getLineCount
   */

  public void setup(LineStorage lines){
    int flag=0;
    //shifts_ = new LineStorage();
    for(int i = 0; i < lines.getLineCount(); i ++){
      ArrayList<String> temp = toArrayList(lines.getLine(i));//   ["a","b","c"]
      for(int j = 0 ; j < lines.getWordCount(i); j ++){// 把一行重新排序，例：line1：a b c  line2：b c a  line3：c a b
        shifts_.addLine(toStrings(temp));
        String word = temp.get(0);
        temp.remove(0);//去掉第一个
        temp.add(word);//在最后加上
      }
    }

  }

  public ArrayList<String> toArrayList(String[] temp){
    ArrayList<String> result = new ArrayList<String>();
    for(int i = 0; i < temp.length;i ++){
      result.add(temp[i]);
    }
    return result;
  }

  public String[] toStrings(ArrayList<String> temp){
    String[] result = new String[temp.size()];
    for(int i = 0; i < temp.size();i ++){
      result[i] = temp.get(i);
    }
    return result;
  }


//----------------------------------------------------------------------
  /**
   * Gets the line from the specified position.
   * String representing the line is returned.
   * @param line line index
   * @return String
   */

  public String getLineAsString(int line){
    return shifts_.getLineAsString(line);
  }

//----------------------------------------------------------------------
  /**
   * Gets the number of lines.
   * @return int
   */

  public int getLineCount(){
    return shifts_.getLineCount();
  }

//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}
