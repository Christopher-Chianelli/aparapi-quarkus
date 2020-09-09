package io.quarkus.aparapi;

import java.util.Map;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class AparapiRecorder {
    private Map<Class<?>, String> kernelToOpenCLMap;
    
    public Map<Class<?>, String> getKernelToOpenCLMap() {
        return kernelToOpenCLMap;
    }
    
    public void setKernelToOpenCLMap(Map<Class<?>, String> kernelToOpenCLMap) {
        this.kernelToOpenCLMap = kernelToOpenCLMap;
    }
}
