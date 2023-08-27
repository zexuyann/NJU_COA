package cpu.alu;

import org.junit.Test;
import util.DataType;
import util.Transformer;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ALUDivTest {

    private final ALU alu = new ALU();
    private final Transformer transformer = new Transformer();
    private DataType src;
    private DataType dest;
    private DataType result;

    @Test
    public void DivSpecialTest100() {
        Transformer t = new Transformer();
        dest = new DataType(t.floatToBinary("0.4375"));
        src = new DataType(t.floatToBinary("0.5"));
        result = alu.div(src, dest);
        String quotient = t.floatToBinary("0.875");
        //String remainder = t.intToBinary("0");
        assertEquals(quotient, result.toString());
        //assertEquals(remainder, alu.remainderReg.toString());
    }

    @Test
    public void DivTest100() {
        src =  new DataType("00000100000000000000000000000000");
        dest = new DataType("00000111000000000000000000000000");
        result = alu.div(src, dest);
        String quotient = "00000000011100000000000000000000";
        assertEquals(quotient, result.toString());
    }
    /**
     * 10 / 10 = 1 (0)
     */
    @Test
    public void DivTest1() {
        src = new DataType("00000000000000000000000000001010");
        dest = new DataType("00000000000000000000000000001010");
        result = alu.div(src, dest);
        String quotient = "00000000000000000000000000000001";
        String remainder = "00000000000000000000000000000000";
        assertEquals(quotient, result.toString());
        assertEquals(remainder, alu.remainderReg.toString());
    }



    /**
     * -8 / 2 = -4 (0)
     * 除法算法固有的bug
     */
    @Test
    public void DivSpecialTest() {
        Transformer t = new Transformer();
        src = new DataType(t.intToBinary("2"));
        dest = new DataType(t.intToBinary("-8"));
        result = alu.div(src, dest);
        String quotient = t.intToBinary("-4");
        String remainder = t.intToBinary("0");
        assertEquals(quotient, result.toString());
        assertEquals(remainder, alu.remainderReg.toString());
    }

    /**
     * 0 / 0  除0异常
     */
    @Test(expected = ArithmeticException.class)
    public void DivExceptionTest1() {
        src = new DataType("00000000000000000000000000000000");
        dest = new DataType("00000000000000000000000000000000");
        result = alu.div(src, dest);
    }


    @Test
    public void DivSpecialTest1() {
        Transformer t = new Transformer();
        Random r1=new Random();
        Random r2=new Random();
        int a=r1.nextInt(20)-10;
        int b=r2.nextInt(20)-10;
        int c=a/b;
        int d=a%b;
        String aa=a+"";
        String bb=b+"";
        String cc=""+c;
        String dd=""+d;
        src = new DataType(t.intToBinary(bb));
        dest = new DataType(t.intToBinary(aa));
        result = alu.div(src, dest);
        String quotient = t.intToBinary(cc);
        String remainder = t.intToBinary(dd);
        //System.out.println(aa);
        //System.out.println(bb);
        assertEquals(quotient, result.toString());
        assertEquals(remainder, alu.remainderReg.toString());
    }

    @Test
    public void DivSpecialTest2() {
        Transformer t = new Transformer();
        src = new DataType(t.intToBinary("2"));
        dest = new DataType(t.intToBinary("9"));
        result = alu.div(src, dest);
        String quotient = t.intToBinary("4");
        String remainder = t.intToBinary("1");
        assertEquals(quotient, result.toString());
        assertEquals(remainder, alu.remainderReg.toString());
    }

}
