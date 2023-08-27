package cpu.nbcdu;

import org.junit.Test;
import util.DataType;
import util.Transformer;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class NBCDUAddTest {

	private final NBCDU nbcdu = new NBCDU();
	private final Transformer transformer = new Transformer();
	private DataType src;
	private DataType dest;
	private DataType result;

	@Test
	public void AddTest1() {
		src = new DataType("11000000000000000000000010011000");
		dest = new DataType("11000000000000000000000001111001");
		result = nbcdu.add(src, dest);
		assertEquals("11000000000000000000000101110111", result.toString());
	}

	@Test
	public void AddTest2() {
		Random r1=new Random();
		Random r2=new Random();
		int a=r1.nextInt(1000)-500;
		int b=r2.nextInt(1000)-500;
		int c=a+b;
		String aa=""+a;
		String bb=""+b;
		String cc=""+c;
		src = new DataType(transformer.decimalToNBCD(aa));
		dest = new DataType(transformer.decimalToNBCD(bb));
		result = nbcdu.add(src, dest);
		System.out.println(aa);
		System.out.println(bb);
		System.out.println(result);
		assertEquals(transformer.decimalToNBCD(cc), result.toString());
	}

	@Test
	public void AddTest3() {

		src = new DataType(transformer.decimalToNBCD("-21"));
		dest = new DataType(transformer.decimalToNBCD("-4"));
		result = nbcdu.add(src, dest);
		System.out.println(result);
		assertEquals(transformer.decimalToNBCD("-25"), result.toString());
	}

}
