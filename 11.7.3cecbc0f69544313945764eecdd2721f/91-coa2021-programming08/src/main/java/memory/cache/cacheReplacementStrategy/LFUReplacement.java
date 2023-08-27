package memory.cache.cacheReplacementStrategy;

import memory.Memory;
import memory.cache.Cache;

/**
 * TODO 最近不经常使用算法
 */
public class LFUReplacement implements ReplacementStrategy {

    @Override
    public void hit(int rowNO) {
        Cache a=Cache.getCache();
        a.addVisited(rowNO);
    }

    @Override
    public int replace(int start, int end, char[] addrTag, char[] input) {
        char[] t=addrTag;
        char[] in=input;
        Cache a=Cache.getCache();
        for(int j=start;j<=end;j++){
            if(!a.isValid(j)){
                a.update(j,t,in);
                a.setVisited(j);
                return j;
            }
        }
        int k=start;
        for(int i=k;i<=end;i++){
            if(a.getVisited(i)<a.getVisited(k))k=i;//使用次数少
        }
        if(a.isDirty(k)==true){
            String padd = a.cal_Paddr(k);
            char[] data = a.getData(k);
            Memory memory = Memory.getMemory();
            memory.write(padd,1024,data);
            a.setDirty(k);
        }
        a.update(k,addrTag,input);
        a.setVisited(k);
        return k;
    }
}
