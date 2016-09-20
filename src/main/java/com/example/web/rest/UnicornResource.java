package com.example.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.example.domain.Unicorn;

import com.example.repository.UnicornRepository;
import com.example.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Unicorn.
 */
@RestController
@RequestMapping("/api")
public class UnicornResource {

    private final Logger log = LoggerFactory.getLogger(UnicornResource.class);
        
    @Inject
    private UnicornRepository unicornRepository;

    /**
     * POST  /unicorns : Create a new unicorn.
     *
     * @param unicorn the unicorn to create
     * @return the ResponseEntity with status 201 (Created) and with body the new unicorn, or with status 400 (Bad Request) if the unicorn has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/unicorns",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Unicorn> createUnicorn(@RequestBody Unicorn unicorn) throws URISyntaxException {
        log.debug("REST request to save Unicorn : {}", unicorn);
        if (unicorn.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("unicorn", "idexists", "A new unicorn cannot already have an ID")).body(null);
        }
        Unicorn result = unicornRepository.save(unicorn);
        return ResponseEntity.created(new URI("/api/unicorns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("unicorn", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /unicorns : Updates an existing unicorn.
     *
     * @param unicorn the unicorn to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated unicorn,
     * or with status 400 (Bad Request) if the unicorn is not valid,
     * or with status 500 (Internal Server Error) if the unicorn couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/unicorns",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Unicorn> updateUnicorn(@RequestBody Unicorn unicorn) throws URISyntaxException {
        log.debug("REST request to update Unicorn : {}", unicorn);
        if (unicorn.getId() == null) {
            return createUnicorn(unicorn);
        }
        Unicorn result = unicornRepository.save(unicorn);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("unicorn", unicorn.getId().toString()))
            .body(result);
    }

    /**
     * GET  /unicorns : get all the unicorns.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of unicorns in body
     */
    @RequestMapping(value = "/unicorns",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Unicorn> getAllUnicorns() {
        log.debug("REST request to get all Unicorns");
        List<Unicorn> unicorns = unicornRepository.findAll();
        return unicorns;
    }

    /**
     * GET  /unicorns/:id : get the "id" unicorn.
     *
     * @param id the id of the unicorn to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the unicorn, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/unicorns/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Unicorn> getUnicorn(@PathVariable Long id) {
        log.debug("REST request to get Unicorn : {}", id);
        Unicorn unicorn = unicornRepository.findOne(id);
        return Optional.ofNullable(unicorn)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /unicorns/:id : delete the "id" unicorn.
     *
     * @param id the id of the unicorn to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/unicorns/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUnicorn(@PathVariable Long id) {
        log.debug("REST request to delete Unicorn : {}", id);
        unicornRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("unicorn", id.toString())).build();
    }

}
