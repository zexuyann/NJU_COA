package memory.cache.cacheReplacementStrategy;

import memory.Memory;
import memory.cache.Cache;

import java.util.Arrays;

/**
 * TODO 先进先出算法
 */
public class FIFOReplacement implements ReplacementStrategy {

    @Override
    public void hit(int rowNO) {
        //Cache a=Cache.getCache();
        //a.setTimeStamp(rowNO);
    }

    @Override
    public int replace(int start, int end, char[] addrTag, char[] input) {
        Cache a=Cache.getCache();
            for(int j=start;j<=end;j++){
                if(a.isValid(j)==false){
                    a.update(j,addrTag,input);
                    a.setTimeStamp(j);
                    return j;
                }
            }
            int k=start;
            for(int i=k;i<=end;i++){
                if(a.getTimeStamp(i)<a.getTimeStamp(k))k=i;//时间早，反而小
            }
        if(a.isDirty(k)==true){
            String padd = a.cal_Paddr(k);
            char[] data = a.getData(k);
            Memory memory = Memory.getMemory();
            memory.write(padd,1024,data);
            a.setDirty(k);
        }
            a.update(k,addrTag,input);
            a.setTimeStamp(k);

        return k;
    }
}
