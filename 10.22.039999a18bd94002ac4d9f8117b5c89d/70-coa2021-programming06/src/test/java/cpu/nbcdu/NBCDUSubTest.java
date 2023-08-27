package cpu.nbcdu;

import org.junit.Test;
import util.DataType;
import util.Transformer;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class NBCDUSubTest {

    private final NBCDU nbcdu = new NBCDU();
    private final Transformer transformer = new Transformer();
    private DataType src;
    private DataType dest;
    private DataType result;

    @Test
    public void SubTest1() {
        src = new DataType("11000000000000000000000100100101");
        dest = new DataType("11000000000000000000001100001001");
        result = nbcdu.sub(src, dest);
        assertEquals("11000000000000000000000110000100", result.toString());
    }

    @Test
    public void SubTest2() {
        src = new DataType( "11000000000100000000000010001001");
        dest = new DataType("11000000000100000000000010001001");
        result = nbcdu.sub(src, dest);
        assertEquals(      "11000000000000000000000000000000", result.toString());
    }

    @Test
    public void SubTest3() {
        Random r1=new Random();
        Random r2=new Random();
        int a=r1.nextInt(50)-25;
        int b=r2.nextInt(50)-25;
        int c=b-a;
        String aa=""+a;
        String bb=""+b;
        String cc=""+c;
        src = new DataType(transformer.decimalToNBCD(aa));
        dest = new DataType(transformer.decimalToNBCD(bb));
        result = nbcdu.sub(src, dest);
        System.out.println("dest: "+bb);
        System.out.println("src: "+aa);

        //System.out.println(result);
        assertEquals(transformer.decimalToNBCD(cc), result.toString());
    }

    @Test
    public void SubTest4() {
        src = new DataType( transformer.decimalToNBCD("-11"));
        dest = new DataType(transformer.decimalToNBCD("-4"));
        result = nbcdu.sub(src, dest);
        System.out.println("dest "+transformer.decimalToNBCD("4"));
        System.out.println("src  "+transformer.decimalToNBCD("11"));
        assertEquals(      transformer.decimalToNBCD("7"), result.toString());
    }

}
