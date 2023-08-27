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
 *  Name:    LineStorage.java
 *
 *  Purpose: LineStorage holds all input lines and provides a public interface to manipulate the lines.
 *
 *  Created: 19 Sep 2002
 *
 *  $Id$
 *
 *  Description:
 *
 * </file>
 */



/*
 * $Log$
 */

import java.util.ArrayList;

/**
 *  LineStorage holds a number of lines and provides a number of public methods
 *  to manipulate the lines. A line is defined as a set of words, and a word consists of a number of
 *  characters. Methods defined by the LineStorage class allow objects of other classes to:
 *  <ul>
 *  <li>set, read and delete a character from a particular word in a particular line
 *  <li>add a new character to a particular word in a particular line
 *  <li>obtain the number of characters in a particular word in a particular line
 *  <li>set, read and delete a word from a particular line
 *  <li>add a new word to a particular line
 *  <li>add an empty word to a particular line
 *  <li>obtain words count in a particular line
 *  <li>set, read and delete a particular line
 *  <li>add a new line
 *  <li>add an empty line
 *  <li>obtain lines count
 *  </ul>
 *  @author  dhelic
 *  @version $Id$
 */

public class LineStorage{

//----------------------------------------------------------------------
/**
 * Fields
 *
 */
//----------------------------------------------------------------------

  /**
   * ArrayList holding all lines. Each line itself is represeneted as an
   * Arraylist object holding all words from that line. The ArrayList class is a
   * standard Java Collection class, which  implements the typical buffer
   * functionality, i.e., it keeps its objects in an array of a fix capacity.
   * When the current capacity is exceeded, ArrayList object resizes its array
   * automatically, and copies the elements of the old array into the new one.
   */

  private ArrayList lines_ = new ArrayList();

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
  /**
   * Adds a word at the end of the specified line.
   * The method takes a string as an argument.
   * @param chars new word
   * @param line line index
   */

  public void addWord(String chars, int line){
    ArrayList current_line = (ArrayList) lines_.get(line);
    current_line.add(chars);
  }


//----------------------------------------------------------------------
  /**
   * Gets the number of words in this particular line.
   * @param line line index
   * @return int
   */

  public int getWordCount(int line){
    ArrayList current_line = (ArrayList)lines_.get(line);
    return current_line.size();
  }


//----------------------------------------------------------------------
  /**
   * Gets the line from the specified position.
   * String array representing the line is returned.
   * @param line line index
   * @return String[]
   * @see #getLineAsString
   * @see #addLine
   * @see #addEmptyLine
   */

  public String[] getLine(int line){
    ArrayList current_line = (ArrayList)lines_.get(line);
    String[] result = new String[current_line.size()];
    for(int i = 0; i < result.length; i++){
      result[i] = (String)current_line.get(i);
    }
    return result;
  }

//----------------------------------------------------------------------
  /**
   * Gets the line from the specified position.
   * A single String representing the line is returned.
   * @param line line index
   * @return String
   * @see #getLine
   * @see #addLine
   * @see #addEmptyLine
   */

  public String getLineAsString(int line){
    ArrayList current_line = (ArrayList)lines_.get(line);
    StringBuffer result = new StringBuffer();
    result.append(current_line.get(0));
    int flag=0;
    for(int i = 1; i < current_line.size(); i ++){
      result.append(" ");
      result.append(current_line.get(i));
    }
    return result.toString();
  }

//----------------------------------------------------------------------
  /**
   * Adds a line at the end of the line array.
   * String array is the argument for the new line
   * @param words new line
   * @see #addEmptyLine
   * @see #getLine
   */

  public void addLine(String[] words){
    ArrayList current_line = new ArrayList();
    for(String word:words){
      current_line.add(word);
    }
    lines_.add(current_line);

  }

//----------------------------------------------------------------------
  /**
   * Adds an empty line at the end of the lines array.
   * @see #getLine
   * @see #getLineAsString
   * @see #addLine
   */

  public void addEmptyLine(){
    ArrayList current_line = new ArrayList();
    lines_.add(current_line);
  }


//----------------------------------------------------------------------
  /**
   * Gets the number of lines.
   * @return int
   */

  public int getLineCount(){
    return lines_.size();
  }

//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}
