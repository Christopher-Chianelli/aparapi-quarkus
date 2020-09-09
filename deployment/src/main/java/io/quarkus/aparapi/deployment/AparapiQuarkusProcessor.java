package io.quarkus.aparapi.deployment;


import io.quarkus.arc.deployment.BeanArchiveIndexBuildItem;
import io.quarkus.arc.deployment.BeanRegistrationPhaseBuildItem;
import io.quarkus.bootstrap.classloading.ClassPathElement;
import io.quarkus.bootstrap.classloading.QuarkusClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.aparapi.Kernel;
import com.aparapi.internal.exception.AparapiException;
import com.aparapi.internal.exception.ClassParseException;
import com.aparapi.internal.kernel.KernelRunner;
import com.aparapi.internal.model.ClassModel;
import com.aparapi.internal.model.Entrypoint;
import com.aparapi.internal.writer.KernelWriter;
import io.quarkus.aparapi.AparapiRecorder;
import io.quarkus.aparapi.GPUEnabled;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ApplicationIndexBuildItem;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.DeploymentClassLoaderBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.JniBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveHierarchyBuildItem;
import io.quarkus.runtime.annotations.Recorder;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.jboss.jandex.Type;
import org.jboss.jandex.Type.Kind;

class AparapiQuarkusProcessor {
    private static final Logger logger = Logger.getLogger("AparapiQuarkusProcessor");
    
    private static final String ARCH = System.getProperty("os.arch").toLowerCase();
    private static final String OS = System.getProperty("os.name").toLowerCase();
    
    private static final String FEATURE = "aparapi-quarkus";
    static DotName GPU_ENABLED_ANNOTATION = DotName.createSimple(GPUEnabled.class.getName());
    static DotName KERNEL_CLASS_TYPE = DotName.createSimple(Kernel.class.getName());
    
    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
    
    @BuildStep
    void convertKernelToOpenCI(BuildProducer<ReflectiveClassBuildItem> reflectiveClass,
                               BuildProducer<JniBuildItem> jni,
                               BuildProducer<AparapiBuildItem> aparapi,
                               CombinedIndexBuildItem combinedIndex) throws Exception {
        logger.info("Started");
        try {
            Kernel kernel = new Kernel() {
                public void run() {}
            };
        }
        catch(Throwable t) {
            logger.info("Error");
        }
        logger.info("Finished");
        aparapi.produce(new AparapiBuildItem(new HashMap<>()));
        /*IndexView indexView = combinedIndex.getIndex();
        Collection<ClassInfo> gpuEnabledKernels = indexView.getKnownDirectSubclasses(KERNEL_CLASS_TYPE);
        Map<Class<?>, String> kernelToOpenCLMap = new HashMap<>();
        for (ClassInfo beanClassInfo : gpuEnabledKernels) {
            try {
                logger.info("Found " + beanClassInfo.name());
                Class<?> kernelClass = Class.forName(beanClassInfo.name().toString(),
                                                     false, Thread.currentThread().getContextClassLoader());
                logger.info("Got cm....");
                // ClassModel classModel = ClassModel.createClassModel(kernelClass);
                logger.info("Got cm2....");
                Thread thread = new Thread(() -> {
                    logger.info("Trying...");
                    try {
                        kernelClass.getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                        logger.info("Gotya!");
                        e.printStackTrace();
                    }
                    logger.info("Got built....");
                });
                thread.setUncaughtExceptionHandler((theThread, e) -> {
                    logger.info("Gotya now!");
                    e.printStackTrace();
                });
                Thread.currentThread().setUncaughtExceptionHandler((theThread, e) -> {
                    logger.info("Gotya now!");
                    e.printStackTrace();
                });
                Thread.setDefaultUncaughtExceptionHandler((theThread, e) -> {
                    logger.info("Gotya now!");
                    e.printStackTrace();
                });
                thread.start();
                thread.join();
                //Entrypoint entryPoint = classModel.getEntrypoint("run", kernel);
                logger.info("Got a....");
                //String openCL = KernelWriter.writeToString(entryPoint);
                logger.info("Got b....");
                //kernelToOpenCLMap.put(kernelClass, openCL);
                logger.info("Configured " + kernelClass.getName());
                reflectiveClass.produce(new ReflectiveClassBuildItem(true, true, kernelClass));
            } catch (ClassNotFoundException e) {
                logger.log(Level.SEVERE, "Failed to load bean class", e);
            } /* catch (AparapiException e) {
                logger.log(Level.SEVERE, "Failed to compile to OpenCI", e);
                throw e;
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException e) {
                logger.log(Level.SEVERE, "Failed to create Kernel; make sure it has a public no args constructor", e);
                throw e;
            } 
              catch (InvocationTargetException e) {
                logger.log(Level.SEVERE, "No-args constructor threw an exception", e);
                throw e;
            } 
            catch (IllegalArgumentException e) {
                logger.log(Level.SEVERE, "This shouldn't happen; please report the issue to the maintainer", e);
            }
            catch (Throwable e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        logger.info("Got here....");
        if (!kernelToOpenCLMap.isEmpty()) {
            jni.produce(new JniBuildItem(getNativeLibaries()));
            logger.info("Found the following kernels: ");
            kernelToOpenCLMap.keySet().forEach(kernelClass -> { logger.info(kernelClass.getName()); });
        }
        else {
            logger.info("No Kernels found!");
        }
        aparapi.produce(new AparapiBuildItem(kernelToOpenCLMap));*/
    }
    
    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void recordKernelToOpenCIMap(AparapiRecorder recorder,
                                 AparapiBuildItem aparapi) throws Exception {
        logger.info("Hi!");
        recorder.setKernelToOpenCLMap(aparapi.getKernelToOpenCIMap());
    }
    
    // These are libaries found within the aparapi Jar, might
    // need to move them to a tempoary file first
    private List<String> getNativeLibaries() {
        List<String> libaryList = new ArrayList<>();
        if( isUnix() ) {
            if( is64Bit() ) {
                libaryList.add("/linux/libaparapi_x86_64.so");
            }
            else {
                libaryList.add("/linux/libaparapi_x86.so");
            }
        }
        else if( isMac() && is64Bit() ) {
            libaryList.add("/osx/libaparapi_x86_64.dylib");
        }
        else if( isWindows() && is64Bit() ) {
            libaryList.add("/win/libaparapi_x86_64.dll");
        }
        else if( isWindows() && is32Bit() ) {
            libaryList.add("/win/libaparapi_x86.dll");
        }
        else {
            throw new IllegalStateException("System is not compatable with any of the known native libraries.");
        }
        return libaryList;
    }

    private static boolean isWindows() {
        return OS.contains("win");
    }

    private static boolean isMac() {
        return OS.contains("mac");
    }

    private static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }

    private static boolean is64Bit() {
        return ARCH.contains("64");
    }

    private static boolean is32Bit() {
        return !is64Bit();
    }

}
