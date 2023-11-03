package com.eclipse.restserver;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.EntityPart;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * This class hosts a file upload service using the MultiPart API. The client
 * file in C:/temp/XXXX.jpg is uploaded and written to C:/temp2/XXXX.jpg on the
 * server. To run this on your own system just select two folders, place a file
 * in one, and it will be uploaded to the server and the service will write it
 * to the server side folder. Change the folder and file to match your OS.
 *
 * curl -X POST -F name=XXXX.jpg -F part=@C:/temp/XXXX.jpg
 * http://localhost:8080/05_MultiPart_Server/services/multiparts/
 *
 * @author Ken Fogel
 */
@Path("multiparts")
@RequestScoped
public class FileUploadService {

    /**
     * This method responds to a POST with a String and an EntityPart
     * (InputStream). It then saves the stream to a folder on the server
     * machine.
     *
     * Change the path to the location that you wish the uploaded file to be
     * stored in.
     *
     * @param part1 The name of the file when stored on the server
     * @param part2 This is the file that is being uploaded.
     * @return Response indicating success or failure.
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormParam("name") String part1,
            @FormParam("part") EntityPart part2) {
        
        try {
            Files.copy(
                    part2.getContent(), // InputStream
                    // CHANGE "C:/temp2/" to a folder on your system
                    Paths.get("C:/temp2/" + part1),  // Path to where the file will be written
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(e.getMessage()).build();
        }
        return Response.ok("File successfully uploaded!").build();
    }
}
