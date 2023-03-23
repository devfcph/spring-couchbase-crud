package org.couchbase.quickstart.controllers.beersample;


import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.couchbase.quickstart.configs.DBProperties;
import org.couchbase.quickstart.models.Profile;
import org.couchbase.quickstart.models.beersample.Beer;
import org.couchbase.quickstart.models.beersample.BeerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.couchbase.quickstart.configs.CollectionNames.PROFILE;

@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

    private Cluster cluster;
    private Collection profileCol;
    private DBProperties dbProperties;
    private Bucket bucket;

    public BeerController(Cluster cluster, Bucket bucket, DBProperties dbProperties) {
        System.out.println("Initializing profile controller, cluster: " + cluster + "; bucket: " + bucket);
        this.cluster = cluster;
        this.bucket = bucket;
        this.profileCol = bucket.defaultCollection();
        this.dbProperties = dbProperties;
    }

    @CrossOrigin(value = "*")
    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Agrega una cerveza al catálogo")
    @ApiResponses( {
            @ApiResponse(code = 201, message = "¡Se ha creado correctamente!", response = Beer.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public ResponseEntity<Beer> save(@RequestBody final BeerRequest beerRequest) {
        Beer beer = beerRequest.getBeer();
        try {
            profileCol.insert(beer.id, beer);
            return  ResponseEntity.status(HttpStatus.CREATED).body(beer);
        }
        catch ( Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin(value="*")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Obtiene una cerveza y sus detalles a través del ID", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 500, message = "Ocurrió un error al localizar la cerveza", response = Error.class)
            })
    public ResponseEntity<Beer> getBeer(@PathVariable("id") UUID id) {
        Beer beer = profileCol.get(id.toString()).contentAs(Beer.class);
        return ResponseEntity.status(HttpStatus.OK).body(beer);
    }

    @CrossOrigin(value="*")
    @PutMapping(path = "/{id}")
    @ApiOperation(value = "Actualiza la información de una cerveza", response = Beer.class)
    @ApiResponses({
            @ApiResponse(code = 201, message = "¡Item actualizado correctamente!", response = Beer.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public ResponseEntity<Beer> update(@PathVariable("id") UUID id, @RequestBody Beer beer) {
        try {
            profileCol.upsert(id.toString(), beer);
            return ResponseEntity.status(HttpStatus.CREATED).body(beer);
        } catch (DocumentNotFoundException dnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin(value="*")
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Elimina un item del catálogo")
    @ApiResponses({
            @ApiResponse(code = 200, message = "¡Exitoso!"),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public ResponseEntity delete(@PathVariable UUID id){

        try {
            profileCol.remove(id.toString());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (DocumentNotFoundException dnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
