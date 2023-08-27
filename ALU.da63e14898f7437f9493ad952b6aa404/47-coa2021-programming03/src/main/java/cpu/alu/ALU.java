package cpu.alu;

import util.DataType;

import java.util.Arrays;

/**
 * Arithmetic Logic Unit
 * ALU封装类
 */
public class ALU {

    DataType remainderReg;

    /**
     * 返回两个二进制整数的和
     * dest + src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType add(DataType src, DataType dest) {
        // TODO
        int[] src1=new int[32];
        int[] dest1=new int[32];
        int[] res=new int[32];
        String src2=src.toString();
        String dest2=dest.toString();
        String temp=new String();
        for(int i=0;i<32;i++) {
            temp = "" + src2.charAt(i);
            src1[i] = Integer.parseInt(temp);
        }

        for(int i=0;i<32;i++) {
            temp = "" + dest2.charAt(i);
            dest1[i] = Integer.parseInt(temp);
        }
        for(int i=0;i<32;i++){
            res[i]=0;
        }
        for(int i=31;i>=0;i--) {
            if (res[i] + dest1[i] + src1[i] >= 2 ) {
                res[i] = res[i] + dest1[i] + src1[i] - 2;
                if (i == 0) {
                    continue;
                }
                res[i - 1] += 1;
                continue;
            }
            if (res[i] + dest1[i] + src1[i] < 2)
                res[i] = res[i] + dest1[i] + src1[i];
        }
        String res1=new String();
        for(int i=0;i<32;i++){
            res1=res1+""+res[i];
        }
        DataType result=new DataType(res1);
        return result;
    }


    /**
     * 返回两个二进制整数的差
     * dest - src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType sub(DataType src, DataType dest ) {
        // TODO
        int[] src1=new int[32];
        int[] dest1=new int[32];
        int[] res=new int[32];
        String src2=src.toString();
        String dest2=dest.toString();
        String temp=new String();
        for(int i=0;i<32;i++) {
            temp = "" + src2.charAt(i);
            src1[i] = Integer.parseInt(temp);
        }
        for(int i=0;i<32;i++) {
            temp = "" + dest2.charAt(i);
            dest1[i] = Integer.parseInt(temp);
        }
        //src相反数的补码
        int count =0;
        for(int i=31;i>=0;i--){
            if(src1[i]==1){
                count=i;
                break;
            }
        }
        for(int i=0;i<count;i++){
            if(src1[i]==1) src1[i]=0;
            else src1[i]=1;
        }

        for(int i=0;i<32;i++){
            res[i]=0;
        }

        for(int i=31;i>=0;i--) {
            if (res[i] + dest1[i] + src1[i] >= 2 ) {
                res[i] = res[i] + dest1[i] + src1[i] - 2;
                if (i == 0) {
                    continue;
                }
                res[i - 1] += 1;
                continue;
            }
            if (res[i] + dest1[i] + src1[i] < 2)
                res[i] = res[i] + dest1[i] + src1[i];
        }

        String res1=new String();
        for(int i=0;i<32;i++){
            res1=res1+""+res[i];
        }

        DataType result=new DataType(res1);
        return result;
    }



    /**
     * 返回两个二进制整数的乘积(结果低位截取后32位)
     * dest * src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public void move(int[] a) {
        int k = a.length;

        for (int i = k-2; i >=0; i--) {
            a[i+1]=a[i];
        }
    }

    public DataType mul(DataType src, DataType dest) {
        //TODO
        int[] src1=new int[32];//src
        int[] src3=new int[32];//相反数的补码
        int[] dest1=new int[32];//dest
        int[] res=new int[65];//寄存器
        int[] res1=new int[32];//存放结果
        String src2=src.toString();
        String dest2=dest.toString();
        String temp=new String();
        for(int i=0;i<32;i++) {
            temp = "" + src2.charAt(i);
            src1[i] = Integer.parseInt(temp);
        }
        for(int i=0;i<32;i++) {
            temp = "" + dest2.charAt(i);
            dest1[i] = Integer.parseInt(temp);
        }
        for(int i=0;i<65;i++){
            res[i]=0;
        }
        for(int i=32;i<64;i++)
        {
            res[i]=dest1[i-32];
        }
        //booth
        int judge=0;
        //src相反数的补码,存入src3中
        int count =0;
        for(int i=31;i>=0;i--){
            if(src1[i]==1){
                count=i;
                break;
            }
        }
        for(int i=0;i<count;i++){
            if(src1[i]==1) src3[i]=0;
            else src3[i]=1;
        }
        for(int i=count;i<32;i++){
            src3[i]=src1[i];
        }
        //
        for(int j=0;j<32;j++)
        {
            judge=res[64]-res[63];
            if(judge==0){
                move(res);
            }
            if(judge==1){
                for(int i=31;i>=0;i--){
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
            }
            if(judge==-1){
                for(int i=31;i>=0;i--){
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
                move(res);
            }
        }
        for(int i=0;i<32;i++){
            res1[i]=res[i+32];
        }
        String res2=new String();
        for(int i=0;i<32;i++){
            res2=res2+""+res1[i];
        }

        DataType result=new DataType(res2);
        return result;
    }


    /**
     * 返回两个二进制整数的除法结果
     * 请注意使用不恢复余数除法方式实现
     * dest ÷ src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    //左移
    public void move1(int[] a) {
        int k = a.length;
        //System.out.println(k);
        for (int i = 0; i< k-1; i++) {
            a[i]=a[i+1];
        }
    }

    public DataType div(DataType src, DataType dest) {
        //TODO
        int[] src1=new int[32];//除数
        int[] src3=new int[32];//相反数的补码
        int[] dest1=new int[32];//被除数
        int[] res=new int[65];//寄存器
        int[] yushu=new int[32];
        int[] shang=new int[32];
        String src2=src.toString();
        String dest2=dest.toString();
        String temp=new String();
        for(int i=0;i<32;i++) {
            temp = "" + src2.charAt(i);
            src1[i] = Integer.parseInt(temp);
        }
        for(int i=0;i<32;i++) {
            temp = "" + dest2.charAt(i);
            dest1[i] = Integer.parseInt(temp);
        }
        //src1相反数的补码,存入src3中
        int count =0;
        for(int i=31;i>=0;i--){
            if(src1[i]==1){
                count=i;
                break;
            }
        }
        for(int i=0;i<count;i++){
            if(src1[i]==1) src3[i]=0;
            else src3[i]=1;
        }
        for(int i=count;i<32;i++){
            src3[i]=src1[i];
        }
//////////
        //初始化寄存器
        for(int i=0;i<65;i++){
            res[i]=0;
        }
        for(int i=32;i<64;i++) {
            res[i]=dest1[i-32];
        }
        for(int i=0;i<32;i++){
            res[i]=dest1[0];
        }
        int count1=res[0];//被除数的符号
        int count11=src1[0];//除数的符号
        //除法
        //step 1
        if(src1[0]!=count1){
            for(int i=31;i>=0;i--){
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
        }
        else {
            for(int i=31;i>=0;i--){
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
        }
        //step2
        for(int j=0;j<32;j++){
            if(src1[0]==res[0]){
                res[64]=1;
                move1(res);

                for (int i = 31; i >= 0; i--) {
                    if (res[i] + src3[i] >= 2) {
                        res[i] = res[i] + src3[i] - 2;
                        if (i == 0) {
                            break;
                        }
                        res[i - 1]++;
                        continue;
                    }
                    if (res[i] + src3[i] < 2) {
                        res[i] = res[i] + src3[i];
                        continue;
                    }
                }
            }
            else{
                res[64]=0;
                move1(res);

                for (int i = 31; i >= 0; i--) {
                    if (res[i] + src1[i] >= 2) {
                        res[i] = res[i] + src1[i] - 2;
                        if (i == 0) {
                            break;
                        }
                        res[i - 1]++;
                        continue;
                    }
                    if (res[i] + src1[i] < 2) {
                        res[i] = res[i] + src1[i];
                        continue;
                    }
                }
            }
        }
        if(src1[0]==res[0])
            res[64]=1;
        else
            res[64]=0;

        //step 3
        //余数
        for(int i=0;i<32;i++){
            yushu[i]=res[i];
        }
            if(yushu[0]!=count1) {
                if (count1 ==count11) {
                    for (int i = 31; i >= 0; i--) {
                        if (yushu[i] + src1[i] >= 2) {
                            yushu[i] = yushu[i]+src1[i]-2;
                            if (i == 0) {
                                break;
                            }
                            yushu[i - 1]++;
                            continue;
                        }
                        if (yushu[i] + src1[i] < 2) {
                            yushu[i] = yushu[i] + src1[i];
                            continue;
                        }
                    }
                } else {
                    for (int i = 31; i >= 0; i--) {
                        if (yushu[i] + src3[i] >= 2) {
                            yushu[i] = yushu[i] + src3[i] - 2;
                            if (i == 0) {
                                break;
                            }
                            yushu[i - 1]++;
                            continue;
                        }
                        if (yushu[i] + src3[i] < 2) {
                            yushu[i] = yushu[i] + src3[i];
                            continue;
                        }
                    }
                }
            }

        //商
        for(int i=0;i<32;i++){
            shang[i]=res[i+33];//+33即左移
        }
        if(count1!=count11){
            shang[31]+=1;
            for(int i=31;i>=0;i--){
                if(shang[i]>=2){
                    shang[i]-=2;
                    if(i==0)
                    {break;}
                    shang[i-1]++;
                }
            }
        }

        //特殊处理：
        int judge1=0;
        int judge2=0;
        int k=0;
        for(int i=0;i<32;i++){//判断和除数是否相等
            if(src1[i]!=yushu[i])judge1++;
        }
        if(judge1==0){
            for(int j=0;j<32;j++){
                yushu[j]=0;
            }
            shang[31]+=1;
            for(int i=31;i>=0;i--){
                if(shang[i]>=2){
                    shang[i]-=2;
                    if(i==0)
                    {break;}
                    shang[i-1]++;
                }
            }
        }
        if(judge1!=0){
            for(int i=0;i<32;i++){////判断和除数的相反数是否相等
                if(yushu[i]!=src3[i])judge2++;
            }
            if(judge2==0){
                for(int j=0;j<32;j++){
                    yushu[j]=0;
                }
                for(int i=31;i>=0;i--){
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

        //除0异常
        int judge3=0;
        for(int i=0;i<32;i++){
            if(src1[i]!=0)
                judge3++;
        }
        //System.out.println(judge3);
        if(judge3==0)
            throw new ArithmeticException();


        String res2 =new String();
        for(int i=0;i<32;i++){
            res2=res2+""+shang[i];
        }
        String res3=new String();
        for(int i=0;i<32;i++){
            res3=res3+""+yushu[i];
        }
        remainderReg=new DataType(res3);

        DataType result=new DataType(res2);

        return result;

    }


}


