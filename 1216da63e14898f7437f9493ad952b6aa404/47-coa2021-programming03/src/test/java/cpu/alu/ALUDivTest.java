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

    /**
     * 10 / 10 = 1 (0)
     */
    @Test
    public void DivTest1() {
        src = new DataType("11111111111111111111111111111111");
        dest = new DataType("11111111111111111111111111111101");
        result = alu.div(src, dest);
        String quotient = "00000000000000000000000000000011";
        String remainder = "00000000000000000000000000000000";
        assertEquals(quotient, result.toString());
        assertEquals(remainder, alu.remainderReg.toString());
    }

    @Test
    public void DivSpecialTest1() {
        Transformer t = new Transformer();
        Random r1=new Random();
        Random r2=new Random();
        int a=r1.nextInt(5)-5;
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
        src = new DataType(t.intToBinary("1"));
        dest = new DataType(t.intToBinary("-3"));
        result = alu.div(src, dest);
        String quotient = t.intToBinary("-3");
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

}
