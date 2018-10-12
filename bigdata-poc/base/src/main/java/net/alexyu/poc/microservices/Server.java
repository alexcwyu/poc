package net.alexyu.poc.microservices;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;

import javax.servlet.ServletException;
import javax.ws.rs.core.Application;

public class Server {
    private final UndertowJaxrsServer server = new UndertowJaxrsServer();

    public Server(Integer port, String host) {
        Undertow.Builder serverBuilder = Undertow.builder().addHttpListener(port, host);
        server.start(serverBuilder);
    }

    public DeploymentInfo deployApplication(String appPath, Class<? extends Application> applicationClass) {
        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.setApplicationClass(applicationClass.getName());
        return server.undertowDeployment(deployment, appPath);
    }

    public void deploy(DeploymentInfo deploymentInfo) throws ServletException {
        server.deploy(deploymentInfo);
    }

    public static void main(String[] args) throws ServletException {
        Server myServer = new Server(8080, "0.0.0.0");
        DeploymentInfo di = myServer.deployApplication("/rest", ReportApplication.class)
                .setClassLoader(Server.class.getClassLoader())
                .setContextPath("/")
                .setDeploymentName("Report")
                .addServlets(Servlets.servlet("OpenApi", io.swagger.v3.jaxrs2.integration.OpenApiServlet.class).addMapping("/openapi"));;
        myServer.deploy(di);
    }
}
