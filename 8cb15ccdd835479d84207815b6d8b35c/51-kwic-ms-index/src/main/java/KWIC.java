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
 *  Name:    KWIC.java
 * 
 *  Purpose: Demo system for practice in Software Architecture
 * 
 *  Created: 11 Sep 2002 
 * 
 *  $Id$
 * 
 *  Description:
 *    The basic KWIC system is defined as follows. The KWIC system accepts an ordered 
 *  set of lines, each line is an ordered set of words, and each word is an ordered set
 *  of characters. Any line may be "circularly shifted" by repeadetly removing the first
 *  word and appending it at the end of the line. The KWIC index system outputs a
 *  listing of all circular shifts of all lines in alphabetical order.
 * </file>
*/

//package kwic.ms;

/*
 * $Log$
*/

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 *  This class is an implementation of the main/subroutine architectural solution 
 *  for the KWIC system. This solution is based on functional
 *  decomposition of the system. Thus, the system is decomposed into a number of 
 *  modules, each module being a function. In this solution all functions share access 
 *  to data, which is stored in the "core storage". The system is decomposed into the 
 *  following modules (functions):
 *  <ul>
 *  <li>Master Control (main). This function controls the sequencing among the
 *  other four functions.
 *  <li>Input. This function reads the data lines from the input medium (file) and
 *  stores them in the core for processing by the remaining modules. The characters are
 *  stored in a character array (char[]). The blank space is used to separate words in 
 *  a particular line. Another integer array (int[]) keeps the starting indices of 
 *  each line in the character array.
 *  <li>Circular Shift. This function is called after the input function has
 *  completed its work. It prepares a two-dimensional integer array (int[][]) to keep
 *  track of all circular shifts. The array is organized as follows: for each circular
 *  shift, both index of its original line, together with the index of the starting word of
 *  that circular shift are stored as one column of the array.
 *  <li>Alphabetizing. This function takes as input the arrays produced by the input
 *  and circular shift functions. It produces an array in the same format (int[][]) 
 *  as that produced by circular shift function. In this case, however, the circular 
 *  shifts are listed in another order (they are sorted alphabetically).
 *  <li>Output. This function uses the arrays produced by input and alphabetizing
 *  function. It produces a nicely formated output listing of all circular shifts.
 *  </ul>
 *  @author  dhelic
 *  @version $Id$
*/

public class KWIC{

//----------------------------------------------------------------------
/**
 * Fields
 *
 */
//----------------------------------------------------------------------

/*
 * Core storage for shared data
 *
 */

/**
 * Input characters
 *
 */

  private char[] chars_;

/**
 * Array that keeps line indices (line index is the index of the first character of a line)
 *
 */

  private int[] line_index_;

/**
 * 2D array that keeps circular shift indices (each circular shift index is a column
 * in this 2D array, the first index is the index of the original line from line_index_ 
 * array, the second index is the index of the starting word from chars_ array of 
 * that circular shift)
 *
 */

  private int[][] circular_shifts_;

/**
 * 2D array that keeps circular shift indices, sorted alphabetically
 *
 */

  private int[][] alphabetized_;

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
 * Input function reads the raw data from the specified file and stores it in the core storage.
 * If some system I/O error occurs the program exits with an error message.
 * The format of raw data is as follows. Lines are separated by the line separator
 * character(s) (on Unix '\n', on Windows '\r\n'). Each line consists of a number of
 * words. Words are delimited by any number and combination of the space chracter (' ')
 * and the horizontal tabulation chracter ('\t'). The entered data is parsed in the 
 * following way. All line separators are removed from the data, all horizontal tabulation
 * word delimiters are replaced by a single space character, and all multiple word
 * delimiters are replaced by a single space character. Then the parsed data is represented 
 * in the core as two arrays: chars_ array and line_index_ array.
 * @param file Name of input file
 */

  public void input(String file){
    ArrayList<String> arr=new ArrayList<>();
    int count=0;
    BufferedReader br = null;
    try {
      br=new BufferedReader(new FileReader(file));
      String line=br.readLine();
      while(line!=null) {
        if(line.endsWith(" ")){
          line = line.substring(0,line.length() - 1);
        }
        arr.add(line);
        count+=line.length();
        line=br.readLine();
      }
    }catch(IOException e){
      e.printStackTrace();
    }
    chars_=new char[count];
    line_index_=new int[arr.size()+1];//最后一个存储总字符串长度
    int current_index=0;
    for(int i=0;i<line_index_.length-1;i++){
      line_index_[i]=current_index;
      String current_line=arr.get(i);
      for(int j=0;j<current_line.length();j++){
        chars_[j+current_index]= current_line.charAt(j);
      }
      current_index+=current_line.length();
    }
    line_index_[arr.size()] = current_index;
  }

//----------------------------------------------------------------------
/**
 * This function processes arrays prepared by the input
 * function and produces circular shifts of the stored lines. A circular
 * shift is a line where the first word is removed from the begin of a line
 * and appended at the end of the line. To obtain all circular shifts of a line
 * we repeat this process until we can't obtain any new lines. Circular shifts 
 * are represented as a 2D array that keeps circular shift indices (each circular 
 * shift index is a column in this 2D array, the first index is the index of 
 * the original line from line_index_ array, the second index is the index of 
 * the starting word from chars_ array of that circular shift)
 */

  public void circularShift(){
    circular_shifts_ = new int[line_index_.length-1][];
    int current_index = 0;
    for(int i = 1; i < line_index_.length;i ++) { //前n-1行
      int length = 0;
      for (int j = current_index; j < line_index_[i]; j++) {  //第i-1行
        if (chars_[j] == ' ') {
          length++;
        }
      }
      int[] temp = new int[length + 1]; //当前行所含块数
      int k = current_index;
      temp[0] = current_index; //第0块的索引
      int count = 1;
      for (; k < line_index_[i]; k++) {
        if (chars_[k] == ' ') {
          temp[count] = k + 1;
          count++;
        }
      }
      current_index = k;
      circular_shifts_[i - 1] = temp;
    }
  }

//----------------------------------------------------------------------
/**
 * This function sorts circular shifts lines alphabetically. The sorted shifts
 * are represented in the same way as the unsorted shifts with the only difference
 * that now they are ordered alphabetically. This function implements binary search
 * to sort the shifts.
 */

  public void alphabetizing(){
    int number = 0;
    int flag=0;
    for(int i = 0; i < circular_shifts_.length;i ++){
      number += circular_shifts_[i].length;
    }
    int[] key = new int[number];
    int now = 0;
    for(int i = 0;i < circular_shifts_.length;i ++){
      for(int j = 0;j < circular_shifts_[i].length;j ++){
        key[now] = circular_shifts_[i][j];
        now++;
      }
    }
    key = bubblesort(key);
    alphabetized_ = new int[1][];
    alphabetized_[0] = key;
  }

  public int getRealLine(int index){//找到属于哪一行
    int flag=0;
    for(int i = 0; i < circular_shifts_.length; i ++){
      if(line_index_[i] <= index && index < line_index_[i + 1]){
        return i;
      }
    }
    return -1;
  }

  public int[] bubblesort(int[] key) {
    int flag=0;
    for(int i=0;i<key.length-1;i++){
      for(int j=i+1;j<key.length;j++){
        String a = getstring(getRealLine(key[i]),key[i]);
        String b = getstring(getRealLine(key[j]),key[j]);
        if(a.compareTo(b)>0) {
          key[i] = key[i] ^ key[j];
          key[j] = key[i] ^ key[j];
          key[i] = key[i] ^ key[j];
        }
      }
    }
    return key;
  }

  public String getstring(int line,int index){
    int flag=0;
    int start = line_index_[line];
    int last = line_index_[line + 1];
    int length = last - start ;
    int i = 0;

    int basic = (index == start)?0:1;
    char[] result = new char[length + basic];
    for(int j = index;j < last;j++,i++){  // 例：第一行为123 456 789， index为4，start为0，last为11，result[i]为456 789
      result[i] = chars_[j];
    }
    if(index != start){ //4！=0，789后加一个空格
      result[i] = ' ';
      for(int j = start; j < index-1; j++, i++){  //在result中加上前index-start位，当index！=start时执行这一步
        result[i + basic ] = chars_[j];  //basic让i+1
      }
    }

    String res = new String(result);
    int m = res.length() - 1; //m为最后一位的索引值
    while(res.charAt(m) == '\0'){
      m --;
    }//结尾为空格，舍去
    return res.substring(0,m + 1);
  }

//----------------------------------------------------------------------
/**
 * This function prints the sorted shifts at the standard output.
 */

  public void output(){
    int flag=0;
    int[] result = alphabetized_[0];
    for(int i = 0;i < result.length;i ++){
      String res = getstring(getRealLine(result[i]),result[i]);
      System.out.println(res);
    }
  }

//----------------------------------------------------------------------
/**
 * This function controls all other functions in the system. It implements
 * the sequence of calls to other functions to obtain the desired functionality
 * of the system. Before any other function is called, main function checks the 
 * command line arguments. The program expects exactly one command line argument
 * specifying the name of the file that contains the data. If the program have
 * not been started with proper command line arguments, main function exits
 * with an error message. Otherwise, the input function is called first to read the 
 * data from the file. After that the circularShift and alphabetizing 
 * functions are called to produce and sort the shifts respectivelly. Finally, the output
 * function prints the sorted shifts at the standard output.
 * @param args command line argumnets
 */

  public static void main(String[] args){
    KWIC kwic = new KWIC();
    kwic.input("Test_Case.txt");
    kwic.circularShift();
    kwic.alphabetizing();
    kwic.output();
  }

//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}
