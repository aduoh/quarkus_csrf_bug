package com.testsignaturebug;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

//no verification is done this is just to test if the quarkus csrf works
@Slf4j
@Path("/test")
public class UploadResource {
    private final Template testpage;

    public FileResource(final Template testpage) {
        this.testpage = testpage;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        return testpage.instance();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(final MultipartFormDataInput parts) {
        try {
            final File fileToSave = new File("test.xlsx");
            FormValue file = parts.getValues().get("file").iterator().next();
            Files.copy(file.getFileItem().getFile(), fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return Response.ok("test").build();
        } catch (final IOException e) {
            log.error("Error while uploading file", e);
        }
        return Response.serverError().build();
    }
}
