package transformer;

public class Transformer {
    /**
     * Integer to binaryString
     *
     * @param numStr to be converted
     * @return result
     */
    public  String intToBinary(String numStr) {
        int num = Integer.parseInt(numStr);
        String result=new String();
        for (int i = 0; i < 32; i++)
        {
            int temp = (num & 0x80000000 >>> i) >>> (31 - i);
            result += ""+temp;
        }
        //System.out.println(result);
        return result;
    }

    /**
     * BinaryString to Integer
     *
     * @param binStr : Binary string in 2's complement
     * @return :result
     */
    public  String binaryToInt(String binStr) {
        StringBuffer buffer = new StringBuffer(binStr);
        //判断符号
        boolean isNeg = buffer.charAt(0) == '1';
        int index = buffer.lastIndexOf("1");
        if (isNeg)
            for (int i = 1; i < index; ++i) {
                if (buffer.charAt(i) == '0') buffer.setCharAt(i, '1');
                else buffer.setCharAt(i, '0');
            }

        int sum = 0;
        for (int i = 1; i < buffer.length(); i++) {
            sum += (buffer.charAt(i) - '0') * Math.pow(2, buffer.length() - 1 - i);
        }
        if (isNeg) sum = -1 * sum;

        return (sum + "");
    }


    /**
     * The decimal number to its NBCD code
     * */
    public String decimalToNBCD(String decimalStr) {
        //TODO:
        StringBuffer binStr = new StringBuffer();
        if(decimalStr.charAt(0) == '-') {
            binStr.append("1101");
            decimalStr = decimalStr.substring(1);
        }
        else binStr.append("1100");
        for(int j = 0; j < 7 - decimalStr.length(); j++) binStr.append("0000");
        for(int i = 0; i < decimalStr.length(); i++){
            binStr.append(intToBinary(decimalStr.substring(i,i+1),4));
        }
        return binStr.toString();
    }

    public StringBuffer intToBinary(String numStr, int bits)
    {

        int num = Integer.parseInt(numStr);
        StringBuffer binStr = new StringBuffer();
        while(binStr.length() != bits)
        {
            binStr.append(num % 2);
            num /= 2;
        }
        return binStr.reverse();
    }

    /**
     * NBCD code to its decimal number
     * */

        public  String NBCDToDecimal(String NBCDStr) {

            String sign = NBCDStr.substring(0,4);
            int sum = 0;
            int tempsum=0;
            for(int i = 4; i < NBCDStr.length() ; i += 4)
            {
                String subStr = NBCDStr.substring(i,i+4);
                for(int j = 0; j < subStr.length(); j++)
                {
                    tempsum += (subStr.charAt(j) - '0') * Math.pow(2,3 - j);
                }
                sum = sum * 10 + tempsum;
                tempsum=0;
            }
            //System.out.println(sign.equals("1100") ? sum + "" : "-" + sum);
            return (sign.equals("1100") ? sum + "" : "-" + sum);
        }

    /**
     * Float true value to binaryString
     * @param floatStr : The string of the float true value
     * */

    public String signToBinary(float num)
    {
        StringBuffer res = new StringBuffer();
        while(num != 0.0)
        {
            num *= 2;
            if(num >= 1)
            {
                res.append('1');
                num -= 1;
            }
            else res.append('0');
        }
        return res.toString();
    }

    public StringBuffer intToBinary2(String numStr, int bits)
    {
        int num = Integer.parseInt(numStr);
        StringBuffer binStr = new StringBuffer();
        while (binStr.length() !=  bits)
        {
            binStr.append(num % 2);
            num /= 2;
        }
        return binStr.reverse();
    }


    public String floatToBinary(String floatStr) {

        float num = Float.parseFloat(floatStr);///////////。。。。。。。。。。。。。。。。。。。。。。。。。
        if(num == 0.0)
            return "00000000000000000000000000000000";
        else if(num == -0.0)
            return "10000000000000000000000000000000";
        else if(num >= Math.pow(2,128))
            return "+Inf";
        else if(num <= -1 * Math.pow(2,128))
            return "-Inf";
        StringBuffer res = new StringBuffer();
        if(num < 0.0)
            res.append('1');
        else
            res.append('0');

        num = Math.abs(num);
        if(num > 0.0 && num < Math.pow(2,-126))///////////。。。。。。。。。。。。。。。。。。。。。。。。。
        {
            res.append("00000000");///////////。。。。。。。。。。。。。。。。。。。。。。。。。
            num *= Math.pow(2,126);
            res.append(signToBinary(num));
            while(res.length() < 32)
                res.append("0");
            return
                res.toString();
        }
        //规格化数
        for(int i = -126; i <= 127; i++)
        {
            if(num >= Math.pow(2,i) && num < Math.pow(2,i+1))
            {
                res.append(intToBinary2((i+127)+"",8));///////////。。。。。。。。。。。。。。。。。。。。。。。。。
                num *= Math.pow(2,-1 * i);
                res.append(signToBinary(num - 1));
                while(res.length() < 32) res.append("0");
                return res.toString();
            }
        }
        return "";
    }


    /**
     * Binary code to its float true value
     * */
        public String binaryToFloat(String binStr ) {
            char sign = binStr.charAt(0);
            String order = binStr.substring(1,9);
            String sig = binStr.substring(9);

            StringBuffer res = new StringBuffer(sign == '0' ? "" : "-");

            int trueOrderNum = 0;
            int tempsum=0;
            for(int i = 0; i <order.length(); i++)
            {
                tempsum += (order.charAt(i) - '0') * Math.pow(2,7-i);///////////。。。。。。。。。。。。。。。。。。。。。。。。。
            }
            int OrderNum = Integer.parseInt(tempsum+"");
            //非规格化数
            if(OrderNum == 0) trueOrderNum = -126;
                //规格化数
            else if(OrderNum != 255) trueOrderNum = OrderNum - 127;
            float tempnum2=0;
            for(int i = 0; i < sig.length(); i++) {
                tempnum2 += (sig.charAt(i) - '0') / Math.pow(2,(i+1));
            }

            float num = tempnum2;
            //无穷
            if(num == 0 && OrderNum == 255) return (sign == '1') ? "-Inf" : "+Inf";
            else if(num != 0 && OrderNum == 255)  return "NaN";
            else if(OrderNum != 0) num += 1;

            res.append(num * Math.pow(2,trueOrderNum));
            //System.out.println(res.toString());
            return res.toString();
        }


}
