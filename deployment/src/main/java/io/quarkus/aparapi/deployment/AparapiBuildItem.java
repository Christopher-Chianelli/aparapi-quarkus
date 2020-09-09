package io.quarkus.aparapi.deployment;

import java.util.Map;

import io.quarkus.builder.item.SimpleBuildItem;

public final class AparapiBuildItem extends SimpleBuildItem {
    final Map<Class<?>, String> kernelToOpenCIMap;
    
    public AparapiBuildItem(Map<Class<?>, String> kernelToOpenCIMap) {
        this.kernelToOpenCIMap = kernelToOpenCIMap;
    }
 
    final public Map<Class<?>, String> getKernelToOpenCIMap() {
        return kernelToOpenCIMap;
    }
}
