package net.alexyu.poc.microservices;

import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import net.alexyu.poc.microservices.model.Query;
import net.alexyu.poc.microservices.model.Report;
import net.alexyu.poc.microservices.model.column.DoubleColumnVector;
import net.alexyu.poc.microservices.model.column.StringColumnVector;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/report")
public class ReportService {

    @POST
    @Path("/query")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Report query(Query query) {
        return new Report("test", Lists.newArrayList(), System.currentTimeMillis());
    }


    @GET
    @Path("/demo")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation
    public Report demo() {
        return new Report("test", Lists.newArrayList(
                StringColumnVector.of("account", "acct1", "acct2", "acct3"),
                DoubleColumnVector.of("DeltaVal", 8332412.1, 1324.12, 6531341.0),
                DoubleColumnVector.of("GammaVal", 2498357.1, 1257981.9, 13257893547.9),
                DoubleColumnVector.of("Val", 413598719805.0, 15479.09, 54798157.0)
        ), System.currentTimeMillis());
    }
}
