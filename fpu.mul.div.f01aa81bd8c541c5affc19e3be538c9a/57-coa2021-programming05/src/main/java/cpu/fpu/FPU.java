package cpu.fpu;

import cpu.alu.ALU;
import util.DataType;
import util.IEEE754Float;
import util.Transformer;

import java.util.Arrays;

/**
 * floating point unit
 * 执行浮点运算的抽象单元
 * 浮点数精度：使用3位保护位进行计算
 */
public class FPU {

    private final String[][] mulCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.P_ZERO, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.N_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.N_ZERO, IEEE754Float.NaN}
    };

    private final String[][] divCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.NaN},
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.P_INF, IEEE754Float.NaN},
    };

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
    }

    public int to10(int[] a){
        int temp=0;
        int l=a.length;
        for(int i=l-1;i>=0;i--){
            temp+=a[i]*Math.pow(2,l-1-i);
        }
        return temp;
    }

    public int judgeof(int[] src, int[] dest,int of){
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




    public int[] toint(String a){
        String temp=new String();
        int l=a.length();
        int[] b=new int[l];
        for(int i=0;i<l;i++) {
            temp = "" + a.charAt(i);
            b[i] = Integer.parseInt(temp);
        }
        return b;
    }

    public String tostring(int[] a,String b){
        int l=a.length;
        for(int i=0;i<l;i++){
            b=b+""+a[i];
        }
        return b;
    }

    public void move(int[] a) {
        int k = a.length;

        for (int i = k-2; i >=0; i--) {
            a[i+1]=a[i];
        }
    }
    public int[] mul2(int[] src,int []dest){
        int[] src1=new int[27];
        int[] dest1=new int[27];
        int[] res=new int[54];
        for(int i=0;i<27;i++) {
            src1[i] = src[i];
        }
        for(int i=0;i<27;i++) {
            dest1[i] = dest[i];
        }
        for(int i=0;i<54;i++){
            res[i]=0;
        }
        int count=0;
        for(int i=26;i>=0;i--){
            if(dest1[i]!=0){
                count=26;
                for(int j=i+27;j>=i+1;j--){
                    if(res[j]+src1[count]>=2){
                        res[j]=res[j]+src1[count]-2;
                        count--;
                        if(j==0){
                            break;
                        }
                        res[j-1]++;
                        continue;
                    }
                    if(res[j]+src1[count]<2) {
                        res[j] = res[j] + src1[count];
                        count--;
                    }
                }
            }
        }
return res;
    }


    public int[] div1(int [] src, int[] dest) {
        //TODO
        int[] src1=new int[27];//除数
        int[] src3=new int[27];//相反数的补码
        int[] dest1=new int[27];//被除数
        int[] res=new int[55];//寄存器
        int[] yushu=new int[27];
        int[] shang=new int[27];
        for(int i=0;i<27;i++) {
            src1[i] = src[i];
        }
        for(int i=0;i<27;i++) {
            dest1[i] = dest[i];
        }
        //src1相反数的补码,存入src3中
        int count =0;
        for(int i=26;i>=0;i--){
            if(src1[i]==1){
                count=i;
                break;
            }
        }
        for(int i=0;i<count;i++){
            if(src1[i]==1) src3[i]=0;
            else src3[i]=1;
        }
        for(int i=count;i<27;i++){
            src3[i]=src1[i];
        }
//////////

        //初始化寄存器
        for(int i=0;i<55;i++){
            res[i]=0;
        }
        for(int i=27;i<54;i++) {
            //res[i]=dest1[0];
            res[i]=0;
        }
        for(int i=0;i<27;i++){
            res[i]=dest1[i];
        }
        int count1=dest1[0];//被除数的符号
        int count11=src1[0];//除数的符号
/////

        //除法
        //step 1
        for(int j=0;j<27;j++){
            for(int i=26;i>=0;i--){
                if(res[i]+src3[i]>=2){
                    res[i]=res[i]+src3[i]-2;
                    if(i==0)
                    {break;}
                    res[i-1]++;
                    continue;
                }
                if(res[i]+src3[i]<2){
                    res[i]=res[i]+src3[i];
                    continue;
                }
            }
            if(res[0]==1){//not enough
                for(int i=26;i>=0;i--){
                    if(res[i]+src1[i]>=2){
                        res[i]=res[i]+src1[i]-2;
                        if(i==0)
                        {break;}
                        res[i-1]++;
                        continue;
                    }
                    if(res[i]+src1[i]<2){
                        res[i]=res[i]+src1[i];
                        continue;
                    }
                }res[54]=0;
            }
            else{
                res[54]=1;
            }
            movel(res,1);
        }
        //step 3
        //余数
        for(int i=0;i<27;i++){
            yushu[i]=res[i];
        }
        //
        //商
        for(int i=0;i<27;i++){
            shang[i]=res[i+27];
        }

        //特殊处理：
        int judge1=0;
        int judge2=0;
        int k=0;
        for(int i=0;i<27;i++){//判断和除数是否相等
            if(src1[i]!=yushu[i])judge1++;
        }
        if(judge1==0){
            for(int j=0;j<27;j++){
                yushu[j]=0;
            }
            shang[26]+=1;
            for(int i=26;i>=0;i--){
                if(shang[i]>=2){
                    shang[i]-=2;
                    if(i==0)
                    {break;}
                    shang[i-1]++;
                }
            }
        }
        if(judge1!=0){
            for(int i=0;i<27;i++){////判断和除数的相反数是否相等
                if(yushu[i]!=src3[i])judge2++;
            }
            if(judge2==0){
                for(int j=0;j<27;j++){
                    yushu[j]=0;
                }
                for(int i=26;i>=0;i--){
                    if(shang[i]+1>=2){
                        shang[i]=shang[i]+1-2;
                        if(i==0)
                        {break;}
                        shang[i-1]++;
                    }
                    else
                        shang[i]+=1;
                }
            }
        }
        return shang;

    }

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

        float num = Float.parseFloat(floatStr);
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
        if(num > 0.0 && num < Math.pow(2,-126))
        {
            res.append("00000000");
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
                res.append(intToBinary2((i+127)+"",8));
                num *= Math.pow(2,-1 * i);
                res.append(signToBinary(num - 1));
                while(res.length() < 32) res.append("0");
                return res.toString();
            }
        }
        return "";
    }
    /**
     * compute the float mul of dest * src
     */
    public DataType mul(DataType src, DataType dest) {
        // TODO
        int[] src1=new int[32];
        int[] dest1=new int[32];
        int[] res=new int[32];
        int of=0;
        String src11=src.toString();
        String dest11=dest.toString();
        String temp=new String();

        String border=new String();
        border=cornerCheck(mulCorner,src11,dest11);
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
        }add_1[7]=1;
        sub_1[7]=1;
        low[7]=0;//add_1:00000001,   low:00000000,   sub_1:11111111

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
        int ereal=0;
        int ereal1;
        int ereal2;
        int[] tempm=new int[54];
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

        if(Arrays.equals(edest,low)){       //阶码等于全等于0,非规格化数
            mdest[0]=0;
            edest[7]=1;
        }
        if(Arrays.equals(esrc,low)){
            msrc[0]=0;
            esrc[7]=1;
        }
                    //判断符号
        if(sign_dest==sign_src)
            signfinal=0;
        else
            signfinal=1;
                    //尾数相乘，阶码相加后减去偏置常数

        tempm=mul2(msrc,mdest);

        String rs=new String();

        ereal1=to10(esrc);
        ereal2=to10(edest);
        ereal=ereal+ereal1+ereal2-127;
        ereal++;//阶码+1

        int count=0;
        //规格化
        while(ereal>0&&tempm[0]==0){//运算后54位尾数的隐藏位为0且阶码大于0
            movel(tempm,1);
            ereal--;
        }
        rs="";
        rs=tostring(tempm,rs);
        for(int i=0;i<27;i++){
            if(rs.charAt(i)!='0')
                count++;
        }
        while(ereal<0&&count!=0){
            rs=rightShift(rs,1);
            ereal++;
            count=0;
            for(int i=0;i<27;i++){
                if(rs.charAt(i)!='0')
                    count++;
            }
        }
        rese=to2(ereal);

        if(Arrays.equals(rese,sub_1)){
            if(signfinal==0)
                return new DataType(IEEE754Float.P_INF);
            if(signfinal==1)
                return new DataType(IEEE754Float.N_INF);
        }else if(ereal<0){
            if(signfinal==0)
            return new DataType(IEEE754Float.P_ZERO);
            if(signfinal==1)
                return new DataType(IEEE754Float.N_ZERO);
        }else if(Arrays.equals(rese,low)){
            rs=rightShift(rs,1);
        }
        String result= new String();
        String ee=new String();
        char ss = (char)(signfinal + '0');//舍入
        ee=tostring(rese,ee);
        result=round(ss,ee,rs);
        return new DataType(result);
    }











    /**
     * compute the float mul of dest / src
     */
    public DataType div(DataType src, DataType dest) {
        // TODO
        int[] src1=new int[32];
        int[] dest1=new int[32];
        int[] res=new int[32];
        int of=0;
        String src11=src.toString();
        //System.out.println(src11);
        String dest11=dest.toString();
        String temp=new String();

        String border=new String();
        border=cornerCheck(divCorner,src11,dest11);
        if(border!=null)
            return new DataType(border);

        if ( src11.matches(IEEE754Float.NaN_Regular) || dest11.matches(IEEE754Float.NaN_Regular)) {
            return new DataType(IEEE754Float.NaN);
        }
        if(src11.matches(IEEE754Float.P_ZERO)||src11.matches(IEEE754Float.N_ZERO))
        {
            throw new ArithmeticException();
        }
        if(dest11.matches(IEEE754Float.P_ZERO))
        {
            return new DataType(IEEE754Float.P_ZERO);
        }
        if(dest11.matches(IEEE754Float.N_ZERO))
        {
            return new DataType(IEEE754Float.N_ZERO);
        }


        int[] add_1=new int[8];
        int[] sub_1=new int[8];
        int[] low=new int[8];
        for(int i=0;i<7;i++){
            add_1[i]=0;
            sub_1[i]=1;
            low[i]=0;
        }add_1[7]=1;
        sub_1[7]=1;
        low[7]=0;//add_1:00000001,   low:00000000,   sub_1:11111111
        System.out.println("src:  "+src11);///////test
        System.out.println("dest: "+dest11);///////test

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
        int ereal=0;
        int ereal1;
        int ereal2;
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

        //判断符号
        if(sign_dest==sign_src)
            signfinal=0;
        else
            signfinal=1;
        //尾数相，阶码相-后+偏置常数

        tempm=div1(msrc,mdest);
        String rs=new String();
        rs=tostring(tempm,rs);

        //rs=rightShift(rs,1);
        ereal1=to10(edest);
        ereal2=to10(esrc);
        ereal=ereal1-ereal2+127;
        //ereal++;//阶码+1

        tempm=toint(rs);
        int count=0;
        //规格化
        while(ereal>0&&tempm[0]==0){//运算后54位尾数的隐藏位为0且阶码大于0
            movel(tempm,1);
            ereal--;
        }
        rs="";
        rs=tostring(tempm,rs);
        for(int i=0;i<27;i++){
            if(rs.charAt(i)!='0')
                count++;
        }
        while(ereal<0&&count!=0){
            rs=rightShift(rs,1);
            ereal++;
            count=0;
            for(int i=0;i<27;i++){
                if(rs.charAt(i)!='0')
                    count++;
            }
        }
        rese=to2(ereal);



        if(Arrays.equals(rese,sub_1)){
            if(signfinal==0)
                return new DataType(IEEE754Float.P_INF);
            if(signfinal==1)
                return new DataType(IEEE754Float.N_INF);
        }else if(ereal<0){
            if(signfinal==0)
                return new DataType(IEEE754Float.P_ZERO);
            if(signfinal==1)
                return new DataType(IEEE754Float.N_ZERO);
        }else if(Arrays.equals(rese,low)){

            System.out.println("aaaa");
            rs=rightShift(rs,1);
        }
        String result= new String();
        String ee=new String();
        char ss = (char)(signfinal + '0');//舍入
        ee=tostring(rese,ee);
        result=round(ss,ee,rs);

        return new DataType(result);
    }


    private String cornerCheck(String[][] cornerMatrix, String oprA, String oprB) {
        for (String[] matrix : cornerMatrix) {
            if (oprA.equals(matrix[0]) &&
                    oprB.equals(matrix[1])) {
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
        int grs = Integer.parseInt(sig_grs.substring(24, 27), 2);
        if ((sig_grs.substring(27).contains("1")) && (grs % 2 == 0)) {
            grs++;
        }
        String sig = sig_grs.substring(0, 24); // 隐藏位+23位
        if (grs > 4) {
            sig = oneAdder(sig);
        } else if (grs == 4 && sig.endsWith("1")) {
            sig = oneAdder(sig);
        }

        if (Integer.parseInt(sig.substring(0, sig.length() - 23), 2) > 1) {
            sig = rightShift(sig, 1);
            exp = oneAdder(exp).substring(1);
        }
        if (exp.equals("11111111")) {
            return IEEE754Float.P_INF;
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
        StringBuffer temp = new StringBuffer(operand);
        temp = temp.reverse();
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
/*
    public int[] mul1(int[] src, int[] dest) {
        int[] src1=new int[27];//src
        int[] src2=new int[27];//相反数的补码
        int[] dest1=new int[27];//dest
        int[] res=new int[55];//寄存器
        int[] res1=new int[54];//存放结果
        for(int i=0;i<27;i++) {
            src1[i] = src[i];
        }
        for(int i=0;i<27;i++) {
            dest1[i] = dest[i];
        }
        for(int i=0;i<55;i++){
            res[i]=0;
        }
        for(int i=27;i<54;i++)
        {
            res[i]=dest1[i-27];
        }
        //booth
        int judge=0;
        //src负数的补码,存入src3中
        int count =0;
        for(int i=26;i>=0;i--){
            if(src1[i]==1){
                count=i;
                break;
            }
        }
        for(int i=0;i<count;i++){
            if(src1[i]==1) src2[i]=0;
            else src2[i]=1;
        }
        for(int i=count;i<27;i++){
            src2[i]=src1[i];
        }
        //
        for(int j=0;j<27;j++)
        {
            judge=res[54]-res[53];
            if(judge==0){
                move(res);
                res[0]=0;
            }
            if(judge==1){
                for(int i=26;i>=0;i--){
                    if(res[i]+src1[i]>=2){
                        res[i]=res[i]+src1[i]-2;
                        if(i==0)
                        {break;}
                        res[i-1]++;
                        continue;
                    }
                    if(res[i]+src1[i]<2){
                        res[i]=res[i]+src1[i];
                        continue;
                    }
                }
                move(res);
                res[0]=1;
            }
            if(judge==-1){
                for(int i=26;i>=0;i--){
                    if(res[i]+src2[i]>=2){
                        res[i]=res[i]+src2[i]-2;
                        if(i==0)
                        {break;}
                        res[i-1]++;
                        continue;
                    }
                    if(res[i]+src2[i]<2){
                        res[i]=res[i]+src2[i];
                        continue;
                    }
                }
                move(res);
                res[0]=0;
            }
        }
        for(int i=0;i<54;i++){
            res1[i]=res[i];
        }
        return res1;
    }
*/