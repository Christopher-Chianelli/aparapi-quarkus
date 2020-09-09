package io.quarkus.it.aparapi;

import com.aparapi.Kernel;
import io.quarkus.aparapi.GPUEnabled;

@GPUEnabled
public class MyKernel extends Kernel {
    public long[] inA;
    public long[] inB;
    public long[] result;
    
    @Override
    public void run() {
        int i = getGlobalId();
        result[i] = inA[i] + inB[i];
    }
    
    public long[] getInA() {
        return inA;
    }
 
    public void setInA(long[] inA) {
        this.inA = inA;
    }

    public long[] getInB() {
        return inB;
    }

    public void setInB(long[] inB) {
        this.inB = inB;
    }

    public long[] getResult() {
        return result;
    }
    
    public void setResult(long[] result) {
        this.result = result;
    }

}
