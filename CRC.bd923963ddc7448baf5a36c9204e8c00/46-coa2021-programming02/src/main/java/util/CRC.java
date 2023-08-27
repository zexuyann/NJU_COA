package util;

public class CRC {

    /**
     * CRC计算器
     *
     * @param data       数据流
     * @param polynomial 多项式
     * @return CheckCode
     */
    public static char[] Calculate(char[] data, String polynomial) {
        //TODO
        int kk = polynomial.length(); //多项式 除数的长度
        int k = kk - 1;//左移的长度
        int m = data.length + k;//补0后的总长
        char[] chushu = new char[kk];
        char[] bei = new char[m];
        char[] temp = new char[kk];
        char[] tempres = new char[kk - 1];
        int temp1 = 0;
        //赋值
        for (int i = 0; i < data.length; i++) {
            bei[i] = data[i];
        }
        for (int i = data.length; i < m; i++) {
            bei[i] = '0';
        }
        chushu = polynomial.toCharArray();


        //System.out.println(bei);
        int temp_m = m;//被除数剩余没被除的数的位数
        int count = 0;//被除数中操作到哪一位

        if (bei[0] == '1') {
            for (int i = 0; i < kk - 1; i++) {
                temp1 = bei[i + 1] ^ chushu[i + 1];
                //System.out.println(chushu[i+1]);
                //System.out.println(bei[i+1]);
                if (temp1 == 0)
                    temp[i] = '0';
                else
                    temp[i] = '1';

            }

        } else {
            for (int i = 0; i < kk - 1; i++) {
                temp1 = bei[i + 1] ^ '0';
                if (temp1 == 0)
                    temp[i] = '0';
                else
                    temp[i] = '1';
            }
        }

        temp_m -= kk;
        count += kk;

        while (temp_m > 0) {
            temp[kk - 1] = bei[count];
            if (temp[0] == '1') {
                for (int i = 0; i < kk - 1; i++) {
                    temp1 = temp[i + 1] ^ chushu[i + 1];

                    if (temp1 == 0)
                        temp[i] = '0';
                    else
                        temp[i] = '1';
                    //System.out.println(temp[i]);
                }
            } else {
                for (int i = 0; i < kk - 1; i++) {
                    temp1 = temp[i + 1] ^ '0';
                    if (temp1 == 0)
                        temp[i] = '0';
                    else
                        temp[i] = '1';
                }//System.out.println(temp);
            }
            count++;
            temp_m--;
        }

        for (int i = 0; i < kk - 1; i++) {
            tempres[i] = temp[i];
        }
        System.out.println(tempres);

        return tempres;
    }


    public static char[] Check(char[] data, String polynomial, char[] CheckCode) {
        //TODO
        String p = polynomial;
        int kk = polynomial.length(); //多项式 除数的长度
        int k = kk - 1;//左移的长度
        int m = data.length + k;//补0后的总长
        char[] chushu = new char[kk];
        char[] bei = new char[m];
        char[] temp = new char[kk];
        char[] tempres = new char[kk - 1];
        int temp1 = 0;
        //赋值
        for (int i = 0; i < data.length; i++) {
            bei[i] = data[i];
        }
        for (int i = data.length; i < m; i++) {
            bei[i] = CheckCode[i- data.length];
        }
        chushu = polynomial.toCharArray();
        //System.out.println(bei);
        int temp_m = m;//被除数剩余没被除的数的位数
        int count = 0;//被除数中操作到哪一位

        if (bei[0] == '1') {
            for (int i = 0; i < kk - 1; i++) {
                temp1 = bei[i + 1] ^ chushu[i + 1];
                //System.out.println(chushu[i+1]);
                //System.out.println(bei[i+1]);
                if (temp1 == 0)
                    temp[i] = '0';
                else
                    temp[i] = '1';
                //System .out.println(temp[i]);


            }

        } else {
            for (int i = 0; i < kk - 1; i++) {
                temp1 = bei[i + 1] ^ '0';
                if (temp1 == 0)
                    temp[i] = '0';
                else
                    temp[i] = '1';
            }
        }

        temp_m -= kk;
        count += kk;

        while (temp_m > 0) {
            temp[kk - 1] = bei[count];
            if (temp[0] == '1') {
                for (int i = 0; i < kk - 1; i++) {
                    temp1 = temp[i + 1] ^ chushu[i + 1];

                    if (temp1 == 0)
                        temp[i] = '0';
                    else
                        temp[i] = '1';
                    //System.out.println(temp[i]);
                }
            } else {
                for (int i = 0; i < kk - 1; i++) {
                    temp1 = temp[i + 1] ^ '0';
                    if (temp1 == 0)
                        temp[i] = '0';
                    else
                        temp[i] = '1';
                }//System.out.println(temp);
            }
            count++;
            temp_m--;

        }

        for (int i = 0; i < kk - 2; i++) {
            tempres[i] = temp[i+1];
        }

        //System.out.println(Calculate(bei,p));;

        return Calculate(bei, p);

    }
}
