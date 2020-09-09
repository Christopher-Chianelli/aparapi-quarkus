package io.quarkus.it.aparapi;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/sum")
public class SumResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Long> computeSum(@QueryParam("a") String a, @QueryParam("a") String b) {
        long[] aList = Arrays.stream(a.split(",")).map(String::trim).mapToLong(Long::parseLong).toArray();
        long[] bList = Arrays.stream(b.split(",")).map(String::trim).mapToLong(Long::parseLong).toArray();
        if (aList.length != bList.length) {
            throw new IllegalArgumentException("a and b have different sizes!");
        }
        MyKernel myKernel = new MyKernel();
        myKernel.setInA(aList);
        myKernel.setInB(bList);
        
        long[] resultList = new long[aList.length];
        myKernel.setResult(resultList);
        
        myKernel.execute(resultList.length);
        
        return Arrays.stream(resultList).boxed().collect(Collectors.toList());
    }
}
