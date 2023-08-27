package cpu.nbcdu;

import util.DataType;

public class NBCDU {

    /**
     * @param src  A 32-bits NBCD String
     * @param dest A 32-bits NBCD String
     * @return dest + src
     */

    public int to10(int[] a){//2转10进制
        int l=a.length;
        int res=0;
        for(int i=l-1;i>=0;i--){
            res+=a[i]*Math.pow(2,l-1-i);
        }
        return res;
    }

    public int[] to2(int a){
        int[] b=new int[4];
        for(int i=3;i>=0;i--){
            if(a>=Math.pow(2,i)){
                b[3-i]=1;
                a-=Math.pow(2,i);
            }
        }
        return b;
    }
    public int[] qufan(int[] a){
        for(int i=0;i<28;i++){
            if(a[i]==0)
                a[i]=1;
            else
                a[i]=0;
        }
        return a;
    }

    public int[] add1(int[] a){//加1
        int[] res=new int[4];
        for(int i=3;i>=0;i--){
            if(res[i]+a[i]>=2){
                res[i]=res[i]+a[i]-2;
                if(i==0){
                    break;
                }
                res[i-1]++;
            }
        }
        return res;
    }

    public String zheng(String a){
        int[] b=new int[32];
        String temp=new String();
        for(int i=0;i<32;i++){
            temp=""+a.charAt(i);
            b[i]=Integer.parseInt(temp);
        }
        if(b[3]==0)b[3]=1;
        else b[3]=0;
        temp="";
        for(int i=0;i<32;i++)
            temp+=""+b[i];
        return temp;
    }

    DataType add(DataType src, DataType dest) {
        // TODO
        int[] src1=new int[32];
        int[] dest1=new int[32];
        String tempsrc=new String();
        String tempdest=new String();
        String temp=new String();
        tempsrc=src.toString();
        tempdest=dest.toString();
        int signsrc=0;
        int signdest=0;
        for(int i=0;i<32;i++){
            temp=""+tempsrc.charAt(i);
            src1[i]=Integer.parseInt(temp);
        }
        for(int i=0;i<32;i++){
            temp=""+tempdest.charAt(i);
            dest1[i]=Integer.parseInt(temp);
        }
        if(src1[3]==0)
            signsrc=0;//+
        else
            signsrc=1;//-
        if(dest1[3]==0)
            signdest=0;
        else
            signdest=1;
        if(signsrc!=signdest){
            if(signsrc==1){
                tempsrc=zheng(tempsrc);
                return sub(new DataType(tempsrc),new DataType(tempdest));
            }

            else{
                tempdest=zheng(tempdest);
                return sub(new DataType(tempdest),new DataType(tempsrc));
            }
        }
        if(signsrc==signdest){
            int[] temp1=new int[4];//dest 4位
            int[] temp2=new int[4];//src  4位
            int[] temp3=new int[4];//result 4位
            int[] res=new int[32];//存最终结果
            int a,b,c;
            int of=0;
            for(int i=31;i>6;i=i-4){
                for(int j=i;j>i-4;j--){
                    temp1[j-i+3]=dest1[j];
                    temp2[j-i+3]=src1[j];
                }
                a=to10(temp1);
                b=to10(temp2);
                if(a+b+of>=10){
                    c=a+b+of-10;
                    of=1;
                }
                else{
                    c=a+b+of;
                    of=0;
                }
                temp3=to2(c);
                res[i]=temp3[3];
                res[i-1]=temp3[2];
                res[i-2]=temp3[1];
                res[i-3]=temp3[0];
            }
            res[0]=res[1]=1;
            res[2]=0;
            if(signsrc==0)
                res[3]=0;
            else
                res[3]=1;


            String result=new String();
            for(int i=0;i<32;i++){
                result+=""+res[i];
            }
            return new DataType(result);
        }
        return null;
    }

    /***
     *
     * @param src A 32-bits NBCD String
     * @param dest A 32-bits NBCD String
     * @return dest - src
     */
    DataType sub(DataType src, DataType dest) {
        // TODO
        String tempdest=new String();
        String tempsrc=new String();
        tempdest=dest.toString();
        tempsrc=src.toString();
        String temp=new String();
        int[] src1=new int[32];
        int[] dest1=new int[32];
        int signdest=0;
        int signsrc=0;

        for(int i=0;i<32;i++){
            temp=""+tempdest.charAt(i);
            dest1[i]=Integer.parseInt(temp);
        }
        for(int i=0;i<32;i++){
            temp=""+tempsrc.charAt(i);
            src1[i]=Integer.parseInt(temp);
        }
        if(dest1[3]==1)signdest=1;
        if(src1[3]==1)signsrc=1;
        if(signdest!=signsrc){
                tempsrc=zheng(tempsrc);
                return add(new DataType(tempsrc),new DataType(tempdest));
        }
        if(signsrc==signdest){
            int[] temp1=new int[4];//dest 4位
            int[] temp2=new int[4];//src  4位
            int[] temp3=new int[4];//result 4位
            int[] res=new int[32];//存最终结果
            int a,b,c;
            int of=0;

            res[0]=res[1]=1;
            res[2]=0;
            if(signsrc==0)
                res[3]=0;
            else
                res[3]=1;

            for(int i=0;i<32;i++){
                if(dest1[i]==1&&src1[i]==0&&signdest==0)
                {
                    break;
                }
                if(dest1[i]==0&&src1[i]==1&&signdest==0)
                {/*
                    int[] tempk=new int[32];
                    for(int j=0;j<32;j++){
                        tempk[j]=dest1[j];
                    }
                    for(int j=0;j<32;j++){
                        dest1[j]=src1[j];
                    }
                    for(int j=0;j<32;j++){
                        src1[j]=tempk[j];
                    }
                    res[3]=1;//7-9 -> -(9-7)  改变符号
                    break;*/
                    return sub(new DataType(tempdest),new DataType(tempsrc));
                }
                if(dest1[i]==1&&src1[i]==0&&signdest==1)
                {
                    dest1[3]=0;
                    src1[3]=0;
                    res[3]=1;
                    break;
                }

                if(dest1[i]==0&&src1[i]==1&&signdest==1)
                {/*
                    dest1[3]=0;
                    src1[3]=0;
                    int[] tempk=new int[32];
                    for(int j=0;j<32;j++){
                        tempk[j]=dest1[j];
                    }
                    for(int j=0;j<32;j++){
                        dest1[j]=src1[j];
                    }
                    for(int j=0;j<32;j++){
                        src1[j]=tempk[j];
                    }
                    res[3]=0;
                    break;*/
                    return sub(new DataType(zheng(tempdest)),new DataType(zheng(tempsrc)));

                }
            }

                for(int i=31;i>6;i=i-4){
                    for(int j=i;j>i-4;j--){
                        temp1[j-i+3]=dest1[j];
                        temp2[j-i+3]=src1[j];
                    }
                    a=to10(temp1);
                    b=9-to10(temp2);//+6，取反
                    if(a+b+of>=10){
                        c=a+b+of-10;
                        of=1;
                    }
                    else{
                        c=a+b+of;
                        of=0;
                    }
                    if(i==31){
                        if(a+b+1>=10){
                            c=a+b+1-10;
                            of=1;
                        }
                        else{
                            c=a+b+1;
                            of=0;
                        }
                    }

                    temp3=to2(c);
                    res[i]=temp3[3];
                    res[i-1]=temp3[2];
                    res[i-2]=temp3[1];
                    res[i-3]=temp3[0];
                }

                //special
            int flag=0;
                for(int i=4;i<32;i++){
                    if(res[i]!=0)flag++;
                }
                if(flag==0){
                    res[3]=0;
                }//1100000000000000000000000;

            String result=new String();
            for(int i=0;i<32;i++){
                result+=""+res[i];
            }
            return new DataType(result);
        }
        return null;
    }

}
