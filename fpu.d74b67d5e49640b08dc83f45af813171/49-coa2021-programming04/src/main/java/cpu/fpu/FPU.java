package cpu.fpu;

import util.CRC;
import util.DataType;
import util.IEEE754Float;

import java.util.Arrays;

/**
 * floating point unit
 * 执行浮点运算的抽象单元
 * 浮点数精度：使用3位保护位进行计算
 */
public class FPU {

    private final String[][] addCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.P_INF, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.P_INF, IEEE754Float.NaN}
    };

    private final String[][] subCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.P_INF, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.N_INF, IEEE754Float.NaN}
    };

    /**
     * compute the float add of (dest + src)
     */
    public void qufan(int[] a){
        int l=a.length;
        int count=0;
        for(int i=l-1;i>=0;i--){
            if(a[i]==1){
                count=i;
                break;
            }
        }
        for(int i=0;i<count;i++){
            if(a[i]==1)a[i]=0;
            else a[i]=1;
        }
        //return null;
    }

    public int to10(int[] a){
        int temp=0;
        int l=a.length;
        for(int i=l-1;i>=0;i--){
            temp+=a[i]*Math.pow(2,l-1-i);
        }
        return temp;
    }

    public String tostring(int[] a,String res){
        for(int i=0;i<a.length;i++){
            res+=""+a[i];
        }
        return res;
    }


    public int judgeof(int[] src, int[] dest,int of){//判断溢出
        int l=src.length;
        int[] res=new int[l];

        for(int i=0;i<l;i++){
            res[i]=0;
        }
        for(int i=l-1;i>=0;i--) {
            if (res[i] + dest[i] + src[i] >= 2 ) {
                res[i] = res[i] + dest[i] + src[i] - 2;
                if (i == 0) {
                    of=1;
                    continue;
                }
                res[i - 1] += 1;
                continue;
            }
            if (res[i] + dest[i] + src[i] < 2)
                res[i] = res[i] + dest[i] + src[i];
        }

        return of;
    }



    public int[] add1(int[] src, int[] dest,int of) {
        int l=src.length;
        int[] res=new int[l];

        for(int i=0;i<l;i++){
            res[i]=0;
        }
        for(int i=l-1;i>=0;i--) {
            if (res[i] + dest[i] + src[i] >= 2 ) {
                res[i] = res[i] + dest[i] + src[i] - 2;
                if (i == 0) {
                    of=1;
                    continue;
                }
                res[i - 1] += 1;
                continue;
            }
            if (res[i] + dest[i] + src[i] < 2)
                res[i] = res[i] + dest[i] + src[i];
        }

        return res;
    }

    public void mover(int[] a,int k) {   //右移操作
        int l = a.length;
        for(int j=0;j<k;j++){
            for (int i = l-2; i >=0; i--) {
                a[i+1]=a[i];
            }
            a[0]=0;
        }
    }

    public void movel(int[] a,int k) {   //左移操作
        int l = a.length;
        for(int j=0;j<k;j++){
            for (int i = 0; i <l-1; i++) {
                a[i]=a[i+1];
            }
            a[l-1]=0;
        }
    }
    public int[] to2(int a){
        int[] b=new int[8];
        for(int i=7;i>=0;i--){
            if(a>=Math.pow(2,i)){
                b[7-i]=1;
                a-=Math.pow(2,i);
            }
        }
        return b;
    }





    public DataType add(DataType src, DataType dest) {
        // TODO
        int[] src1=new int[32];
        int[] dest1=new int[32];
        int[] res=new int[32];
        int of=0;
        String src11=src.toString();
        String dest11=dest.toString();
        String temp=new String();

        String border=new String();
        border=cornerCheck(addCorner,src11,dest11);

        if(border!=null)
            return new DataType(border);

        if ( src11.matches(IEEE754Float.NaN_Regular) || dest11.matches(IEEE754Float.NaN_Regular)) {
            return new DataType(IEEE754Float.NaN);
        }
        int[] add_1=new int[8];
        int[] sub_1=new int[8];
        int[] low=new int[8];
        for(int i=0;i<7;i++){
            add_1[i]=0;
            sub_1[i]=1;
            low[i]=0;
        }add_1[7]=1;    //00000001
        sub_1[7]=1;     //11111111
        low[7]=0;       //00000000

        for(int i=0;i<32;i++) {
            temp = "" + src11.charAt(i);
            src1[i] = Integer.parseInt(temp);
        }
        for(int i=0;i<32;i++) {
            temp = "" + dest11.charAt(i);
            dest1[i] = Integer.parseInt(temp);
        }
        for(int i=0;i<32;i++){
            res[i]=0;
        }
        int sign_src=0;
        int sign_dest=0;
        int signfinal=0;
        sign_dest=dest1[0];//记录符号位
        sign_src=src1[0];

        int[] esrc=new int[8];
        int[] edest=new int[8];
        int[] tempm=new int[27];
        int[] rese=new int[8];

        for(int i=1;i<9;i++){//记录阶码
            esrc[i-1]=src1[i];
            edest[i-1]=dest1[i];
        }

        int[] msrc=new int[27];
        int[] mdest=new int[27];

        for(int i=1;i<24;i++){
            msrc[i]=src1[i+8];
            mdest[i]=dest1[i+8];
        }
        for(int i=24;i<27;i++){
            msrc[i]=0;
            mdest[i]=0;
        }
        msrc[0]=1;
        mdest[0]=1;//初始化27位尾数，包括隐藏位1和grs码

        if(Arrays.equals(edest,esrc)&&Arrays.equals(mdest,msrc)&&sign_dest==1&&sign_src==0){
            return new DataType(IEEE754Float.P_ZERO);
        }
        if(Arrays.equals(edest,esrc)&&Arrays.equals(mdest,msrc)&&sign_dest==0&&sign_src==1){
            return new DataType(IEEE754Float.P_ZERO);
        }
        //有非规格化数
        int[] zero=new int[8];
        for(int i=0;i<8;i++)zero[i]=0;
        if(Arrays.equals(zero,edest)){//阶码等于全等于0
            mdest[0]=0;
            edest[7]=1;
        }
        if(Arrays.equals(zero,esrc)){
            msrc[0]=0;
            esrc[7]=1;
        }

        int tempsrc=to10(esrc);
        int tempdest=to10(edest);
        int deta=tempdest-tempsrc;//阶值的差
        if(deta>0){ //   阶数!=
            String right = new String();
            for (int i = 0; i < 27; i++) {
                right = right + "" + msrc[i];
            }
            right=rightShift(right, deta);//右移
            for (int i = 0; i < 27; i++) {
                temp = "" + right.charAt(i);
                msrc[i] = Integer.parseInt(temp);
            }
            rese=edest;
            signfinal=sign_dest;
        }
        if(deta<0){
            String right = new String();
            for (int i = 0; i < 27; i++) {
                right = right + "" + mdest[i];
            }
            right=rightShift(right, -deta);
            for (int i = 0; i < 27; i++) {
                temp = "" + right.charAt(i);
                mdest[i] = Integer.parseInt(temp);
            }
            rese=esrc;
            signfinal=sign_src;
        }
        if(deta==0){
            rese=edest;
            for(int i=0;i<27;i++){
                if(mdest[i]>msrc[i]) {
                    signfinal=sign_dest;
                    break;
                }
                if(mdest[i]<msrc[i]) {
                    signfinal=sign_src;
                    break;
                }
                signfinal=sign_dest;
            }
        }

        36
        if(sign_dest!=sign_src){
            if(signfinal==sign_dest) qufan(msrc);
            else
                qufan(mdest);
        }

        //尾数相加
        tempm=add1(msrc,mdest,of);
        of=judgeof(msrc,mdest,of);
        //规格化
        if(of==1&&sign_dest==sign_src) {//尾数溢出，需要尾数右移，阶码+1
            String right = new String();
            for (int i = 0; i < 27; i++) {
                right = right + "" + tempm[i];
            }
            right=rightShift(right, 1);
            for (int i = 0; i < 27; i++) {
                temp = "" + right.charAt(i);
                tempm[i] = Integer.parseInt(temp);
            }
            rese = add1(rese, add_1, of);

            if (Arrays.equals(rese,sub_1)&&signfinal==0) {
                return new DataType(IEEE754Float.P_INF);
            }
            if (Arrays.equals(rese,sub_1)&&signfinal==1) {
                return new DataType(IEEE754Float.N_INF);
            }
            String ee=new String();
            String mm=new String();
            String result=new String();
            char ss = (char)(signfinal + '0');;//舍入
            ee=tostring(rese,ee);
            //System.out.println("ee "+ee);
            mm=tostring(tempm,mm);
            //System.out.println("mm "+mm);
            result=round(ss,ee,mm);
            DataType result1=new DataType(result);

            return result1;
        }

        int count=0;
        for(int i=0;i<27;i++){
            if(tempm[i]==1){
                count=i;
                break;
            }
        }
        int[] kk=new int[8];
        for(int i=0;i<7;i++)kk[i]=0;
        kk[7]=1;

        if(count!=0){
            for(int i=0;i<count;i++){//规格化，让隐藏位变成1
                if(Arrays.equals(rese,kk)){//kk 00000001
                    if(tempm[0]==0)//还是非规格化数
                        rese[7]=0;//就是非规格化数，不规格化了
                    break;
                }
                movel(tempm,1);
                rese=add1(rese,sub_1,of);
                if(Arrays.equals(rese,low)&&signfinal==0){
                    DataType pzero=new DataType(IEEE754Float.P_ZERO);
                    return pzero;
                }

                if(Arrays.equals(rese,low)&&signfinal==1){
                    DataType nzero=new DataType(IEEE754Float.N_ZERO);
                    return nzero;
                }
            }
        }
        String ee=new String();
        String mm=new String();
        String result=new String();
        char ss = (char)(signfinal + '0');;//舍入
        ee=tostring(rese,ee);
        mm=tostring(tempm,mm);
        result=round(ss,ee,mm);
        DataType result1=new DataType(result);
        return result1;
    }

    /**
     * compute the float add of (dest - src)
     */
    public DataType sub(DataType src, DataType dest) {
        // TODO
        int[] src0=new int[32];
        int of=0;
        String src00=src.toString();
        String temp1=new String();
        for(int i=0;i<32;i++) {
            temp1 = "" + src00.charAt(i);
            src0[i] = Integer.parseInt(temp1);
        }
        src0[0]=1-src0[0];
        String a=new String();
        a=tostring(src0,a);
        DataType src2=new DataType(a);

        int[] src1=new int[32];
        int[] dest1=new int[32];
        int[] res=new int[32];
        String src11=src2.toString();
        String dest11=dest.toString();
        String temp=new String();

        String border=new String();
        border=cornerCheck(addCorner,src11,dest11);
        //System.out.println(border);
        if(border!=null)
            return new DataType(border);

        if ( src11.matches(IEEE754Float.NaN_Regular) || dest11.matches(IEEE754Float.NaN_Regular)) {
            return new DataType(IEEE754Float.NaN);
        }
        //System.out.println(src11);
        //System.out.println(dest11);


        int[] add_1=new int[8];
        int[] sub_1=new int[8];
        int[] low=new int[8];
        for(int i=0;i<7;i++){
            add_1[i]=0;
            sub_1[i]=1;
            low[i]=0;
        }add_1[7]=1;
        sub_1[7]=1;
        low[7]=0;

        for(int i=0;i<32;i++) {
            temp = "" + src11.charAt(i);
            src1[i] = Integer.parseInt(temp);
        }
        for(int i=0;i<32;i++) {
            temp = "" + dest11.charAt(i);
            dest1[i] = Integer.parseInt(temp);
        }
        for(int i=0;i<32;i++){
            res[i]=0;
        }
        int sign_src=0;
        int sign_dest=0;
        int signfinal=0;
        sign_dest=dest1[0];//记录符号位
        sign_src=src1[0];

        int[] esrc=new int[8];
        int[] edest=new int[8];
        int[] tempm=new int[27];
        int[] rese=new int[8];

        for(int i=1;i<9;i++){//记录阶码
            esrc[i-1]=src1[i];
            edest[i-1]=dest1[i];
        }

        int[] msrc=new int[27];
        int[] mdest=new int[27];

        for(int i=1;i<24;i++){
            msrc[i]=src1[i+8];
            mdest[i]=dest1[i+8];
        }
        for(int i=24;i<27;i++){
            msrc[i]=0;
            mdest[i]=0;
        }
        msrc[0]=1;
        mdest[0]=1;//初始化27位尾数，包括隐藏位1和grs码

        if(Arrays.equals(edest,esrc)&&Arrays.equals(mdest,msrc)&&sign_dest==1&&sign_src==0){
            return new DataType(IEEE754Float.P_ZERO);
        }
        if(Arrays.equals(edest,esrc)&&Arrays.equals(mdest,msrc)&&sign_dest==0&&sign_src==1){
            return new DataType(IEEE754Float.P_ZERO);
        }
        //有非规格化数
        int count1=0;
        int count2=0;
        for(int i=0;i<8;i++){
            if(edest[i]!=low[i])
                count1++;
        }
        for(int i=0;i<8;i++){
            if(esrc[i]!=low[i])
                count2++;
        }
        if(count1==0){//阶码等于全等于0
            mdest[0]=0;
            edest[7]=1;
        }
        if(count2==0){
            msrc[0]=0;
            esrc[7]=1;
        }


        int tempsec=to10(esrc);
        int tempdest=to10(edest);
        int deta=tempdest-tempsec;
        //System.out.println(deta);//阶值的差
        if(deta>0){                 //阶数bu同
            String right = new String();

            for (int i = 0; i < 27; i++) {
                right = right + "" + msrc[i];
            }

            right=rightShift(right, deta);

            for (int i = 0; i < 27; i++) {
                temp = "" + right.charAt(i);
                msrc[i] = Integer.parseInt(temp);
            }                                           //右移
            rese=edest;
            signfinal=sign_dest;
        }

        if(deta<0){
            //System.out.println("aaa");//右移
            String right = new String();

            for (int i = 0; i < 27; i++) {
                right = right + "" + mdest[i];
            }

            right=rightShift(right, -deta);

            for (int i = 0; i < 27; i++) {
                temp = "" + right.charAt(i);
                mdest[i] = Integer.parseInt(temp);
            }
            rese=esrc;
            signfinal=sign_src;
        }
        if(deta==0){
            rese=edest;
            for(int i=0;i<27;i++){
                if(mdest[i]>msrc[i]) {
                    signfinal=sign_dest;
                    break;
                }
                if(mdest[i]<msrc[i]) {
                    signfinal=sign_src;
                    break;
                }
                signfinal=sign_dest;
            }
        }
        if(sign_dest!=sign_src){
            if(signfinal==sign_dest) qufan(msrc);
            else
                qufan(mdest);
        }

        //尾数相加
        tempm=add1(msrc,mdest,of);

        of=judgeof(msrc,mdest,of);
        //System.out.println(of);
        //规格化
        if(of==1&&sign_dest==sign_src) {//尾数溢出，需要尾数右移，阶码+1
            //System.out.println("aaaa");
            String right = new String();

            for (int i = 0; i < 27; i++) {
                right = right + "" + tempm[i];
            }

            right=rightShift(right, 1);

            for (int i = 0; i < 27; i++) {
                temp = "" + right.charAt(i);
                tempm[i] = Integer.parseInt(temp);
            }
            rese = add1(rese, add_1, of);

            if (Arrays.equals(rese,sub_1)&&signfinal==0) {
                return new DataType(IEEE754Float.P_INF);
            }
            if (Arrays.equals(rese,sub_1)&&signfinal==1) {
                return new DataType(IEEE754Float.N_INF);
            }
            String ee=new String();
            String mm=new String();
            String result=new String();
            char ss = (char)(signfinal + '0');;//舍入
            ee=tostring(rese,ee);
            //System.out.println("ee "+ee);
            mm=tostring(tempm,mm);
            //System.out.println("mm "+mm);
            result=round(ss,ee,mm);
            DataType result1=new DataType(result);

            return result1;
        }
        //System.out.println(signfinal);
        int count=0;
        for(int i=0;i<27;i++){
            if(tempm[i]==1){
                count=i;
                break;
            }
        }
        int[] kk=new int[8];
        for(int i=0;i<7;i++)kk[i]=0;
        kk[7]=1;
        int judge1=0;
        for(int i=0;i<8;i++){
            if(kk[i]!=rese[i])judge1++;
        }

        if(count!=0){
            for(int i=0;i<count;i++){
                judge1=0;
                for(int j=0;j<8;j++){
                    if(kk[j]!=rese[j])judge1++;
                }
                if(judge1==0){
                    if(tempm[0]==0)
                        rese[7]=0;
                    break;
                }
                movel(tempm,1);
                rese=add1(rese,sub_1,of);
                if(Arrays.equals(rese,low)&&signfinal==0){
                    DataType pzero=new DataType(IEEE754Float.P_ZERO);
                    return pzero;
                }

                if(Arrays.equals(rese,low)&&signfinal==1){
                    DataType nzero=new DataType(IEEE754Float.N_ZERO);
                    return nzero;
                }/*
                for(int o=0;o<8;o++){
                    System.out.print(edest[o]);
                }
                System.out.println();
                for(int p=0;p<8;p++){
                    System.out.print(esrc[p]);
                }
               System.out.println();*/
            }
        }
        String ee=new String();
        String mm=new String();
        String result=new String();
        char ss = (char)(signfinal + '0');;//舍入
        //System.out.print(ss);
        ee=tostring(rese,ee);
        //System.out.print(" "+ee);
        mm=tostring(tempm,mm);
        //System.out.print(" "+mm);
        //System.out.println();
        result=round(ss,ee,mm);
        //System.out.println(result);
        DataType result1=new DataType(result);

        return result1;





    }


    private String cornerCheck(String[][] cornerMatrix, String oprA, String oprB) {
        for (String[] matrix : cornerMatrix) {
            if (oprA.equals(matrix[0]) && oprB.equals(matrix[1])) {
                return matrix[2];
            }
        }
        return null;
    }

    /**
     * right shift a num without considering its sign using its string format
     *
     * @param operand to be moved
     * @param n       moving nums of bits
     * @return after moving
     */
    private String rightShift(String operand, int n) {
        StringBuilder result = new StringBuilder(operand);  //保证位数不变
        boolean sticky = false;
        for (int i = 0; i < n; i++) {
            sticky = sticky || result.toString().endsWith("1");
            result.insert(0, "0");
            result.deleteCharAt(result.length() - 1);
        }
        if (sticky) {
            result.replace(operand.length() - 1, operand.length(), "1");
        }
        return result.substring(0, operand.length());
    }

    /**
     * 对GRS保护位进行舍入
     *
     * @param sign    符号位
     * @param exp     阶码
     * @param sig_grs 带隐藏位和保护位的尾数
     * @return 舍入后的结果
     */
    private String round(char sign, String exp, String sig_grs) {
        int grs = Integer.parseInt(sig_grs.substring(24), 2);
        String sig = sig_grs.substring(0, 24);
        if (grs > 4) {
            sig = oneAdder(sig);
        } else if (grs == 4 && sig.endsWith("1")) {
            sig = oneAdder(sig);
        }

        if (Integer.parseInt(sig.substring(0, sig.length() - 23), 2) > 1) {
            sig = rightShift(sig, 1);
            exp = oneAdder(exp).substring(1);
        }
        return sign + exp + sig.substring(sig.length() - 23);
    }

    /**
     * add one to the operand
     *
     * @param operand the operand
     * @return result after adding, the first position means overflow (not equal to the carray to the next) and the remains means the result
     */
    private String oneAdder(String operand) {
        int len = operand.length();
        StringBuilder temp = new StringBuilder(operand);
        temp.reverse();
        int[] num = new int[len];
        for (int i = 0; i < len; i++) num[i] = temp.charAt(i) - '0';  //先转化为反转后对应的int数组
        int bit = 0x0;
        int carry = 0x1;
        char[] res = new char[len];
        for (int i = 0; i < len; i++) {
            bit = num[i] ^ carry;
            carry = num[i] & carry;
            res[i] = (char) ('0' + bit);  //显示转化为char
        }
        String result = new StringBuffer(new String(res)).reverse().toString();
        return "" + (result.charAt(0) == operand.charAt(0) ? '0' : '1') + result;  //注意有进位不等于溢出，溢出要另外判断
    }

}
