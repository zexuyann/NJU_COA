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
        System.out.println(res[6]);
        System.out.println(src1[6]);
        System.out.println(dest1[6]);
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
        System.out.println(res[6]);
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
        //src负数的补码
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
        //
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
        //System.out.println(k);
        // int high=a[0];
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
        //src负数的补码,存入src3中
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
/*
        for(int i=0;i<32;i++)
            System.out.print(dest1[i]);
        System.out.println("  dest1 ");
        for(int i=0;i<32;i++)
            System.out.print(src1[i]);
        System.out.println("  src1 ");

        for(int i=0;i<32;i++)
            System.out.print(src3[i]);
        System.out.println("  src3 ");
*/
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
/////
/*
        for(int i=0;i<65;i++)
            System.out.print(res[i]);
        System.out.println("  初始化的");
*/
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
/////////////
/*
        for(int i=0;i<65;i++)
            System.out.print(res[i]);
        System.out.println();
*/
        //step2
        for(int j=0;j<32;j++){
            if(src1[0]==res[0]){
                res[64]=1;
                move1(res);
///////
/*
                for(int i=0;i<65;i++)
                    System.out.print(res[i]);
                System.out.println("  <- -------");
*/
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
////////
/*
                for(int i=0;i<65;i++)
                    System.out.print(res[i]);
                System.out.println("   <-  ++++++");
*/
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
///////
/*
            for(int i=0;i<65;i++)
                System.out.print(res[i]);
            System.out.println("  +-");
*/
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
 /*
        for(int i=0;i<32;i++){
            System.out.print(yushu[i]);
        }
        System.out.println("aaaaaa");
*/
        //
        //商
        for(int i=0;i<32;i++){
            shang[i]=res[i+33];//+33即左移
        }
        if(count1!=src1[0]){
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
        //除数和被除数相等
        int judge4=0;
        for(int i=0;i<32;i++)
        {
            if(src1[i]!=dest1[i])
                judge4++;
        }
        if(judge4==0){
            for(int i=0;i<32;i++){
                yushu[i]=0;
                shang[i]=0;
            }
            shang[31]=1;
        }


        ////除数和-被除数相等
        int judge7=0;
        int count7=0;
        for(int i=31;i>=0;i--){
            if(src1[i]==1){
                count7=i;
                break;
            }
        }
            for(int j=0;j<count7;j++){
                if((src1[j]+dest1[j])!=1)judge7++;
            }
        for(int j=count7;j<32;j++){
            if(src1[j]!=dest1[j])judge7++;
        }
            if(judge7==0){
                for(int i=0;i<32;i++){
                    shang[i]=1;
                    yushu[i]=0;
                }
            }

            //除数是1
        int judge8=0;
            for(int i=0;i<31;i++){
                if(src1[i]!=0)judge8++;
            }if(src1[31]!=1)judge8++;
            if(judge8==0){
                for(int i=0;i<32;i++){
                    shang[i]=dest1[i];
                    yushu[i]=0;
                }
            }

        //
        // 被除数为+，除数为-，被除数《-除数；
        int judge9=1;
            int[] tres=new int[32];
            for(int i=0;i<32;i++){
                tres[i]=0;
            }
        if(count1==0&&count11==1){
            for(int i=31;i>=0;i--) {
                if ( tres[i]+dest1[i] + src1[i] >= 2 ) {
                    tres[i]  = tres[i]+ dest1[i] + src1[i] - 2;
                    if (i == 0) {
                        continue;
                    }
                    tres[i - 1] += 1;
                    continue;
                }
                if (tres[i]+dest1[i] + src1[i] < 2)
                    tres[i]  =tres[i]+ dest1[i] + src1[i];
            }
            if(tres[0]==1)
                judge9=0;
        }
        if(judge9==0){
            for(int i=0;i<32;i++){
                yushu[i]=dest1[i];
                shang[i]=0;
            }
        }

        // 被除数为-除数为+，-被除数《除数
        int judge10=1;
        int[] tres10=new int[32];
        for(int i=0;i<32;i++){
            tres10[i]=0;
        }
        if(count1==1&&count11==0){
            for(int i=31;i>=0;i--) {
                if ( tres10[i]+dest1[i] + src1[i] >= 2 ) {
                    tres10[i]  = tres10[i]+ dest1[i] + src1[i] - 2;
                    if (i == 0) {
                        continue;
                    }
                    tres10[i - 1] += 1;
                    continue;
                }
                if (tres10[i]+dest1[i] + src1[i] < 2)
                    tres10[i]  =tres10[i]+ dest1[i] + src1[i];
            }
            if(tres10[0]!=0)
                judge10++;
            int count10=0;
            for(int i=0;i<32;i++){
                if(tres10[i]==0)count10++;
            }
            if(count10==32);
            judge10++;

        }
        if(judge10==0){
            for(int i=0;i<32;i++){
                yushu[i]=dest1[i];
                shang[i]=0;
            }
        }

        //除数为+，被除数为+，被除数《除数
        int judge5=1;
        int[] tres5=new int[32];
        for(int i=0;i<32;i++){
            tres5[i]=0;
        }
        if(count1==0&&count11==0){
            for(int i=31;i>=0;i--) {
                if ( tres5[i]+dest1[i] + src3[i] >= 2 ) {
                    tres5[i]  = tres5[i]+ dest1[i] + src3[i] - 2;
                    if (i == 0) {
                        continue;
                    }
                    tres5[i - 1] += 1;
                    continue;
                }
                if (tres5[i]+dest1[i] + src3[i] < 2)
                    tres5[i]  =tres5[i]+ dest1[i] + src3[i];
            }
            if(tres5[0]!=0)
                judge5++;
            int count5=0;
            for(int i=0;i<32;i++){
                if(tres5[i]==0)count5++;
            }
            if(count5==32);
            judge5++;
        }
        if(judge5==0){
            for(int i=0;i<32;i++){
                yushu[i]=dest1[i];
                shang[i]=0;
            }
        }
        //除数为-，被除数为-，被除数>除数
        int judge6=1;
        int[] tres6=new int[32];
        for(int i=0;i<32;i++){
            tres6[i]=0;
        }
        if(count1==0&&count11==0){
            for(int i=31;i>=0;i--) {
                if ( tres6[i]+dest1[i] + src3[i] >= 2 ) {
                    tres6[i]  = tres6[i]+ dest1[i] + src3[i] - 2;
                    if (i == 0) {
                        continue;
                    }
                    tres6[i - 1] += 1;
                    continue;
                }
                if (tres6[i]+dest1[i] + src3[i] < 2)
                    tres6[i]  =tres5[i]+ dest1[i] + src3[i];
            }
            if(tres6[0]!=0)
                judge6++;
            int count6=0;
            for(int i=0;i<32;i++){
                if(tres5[i]==0)count6++;
            }
            if(count6==32);
            judge6++;
        }
        if(judge6==0){
            for(int i=0;i<32;i++){
                yushu[i]=dest1[i];
                shang[i]=0;
            }
        }




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
        //System.out.println(result.toString());
        //System.out.println(remainderReg.toString());
        return result;

    }


}


