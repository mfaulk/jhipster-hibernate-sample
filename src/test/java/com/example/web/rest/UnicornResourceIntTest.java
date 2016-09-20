package com.example.web.rest;

import com.example.JhipsterhibernatetutorialApp;

import com.example.domain.Unicorn;
import com.example.repository.UnicornRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UnicornResource REST controller.
 *
 * @see UnicornResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = JhipsterhibernatetutorialApp.class)

public class UnicornResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_COLOR = "AAAAA";
    private static final String UPDATED_COLOR = "BBBBB";

    @Inject
    private UnicornRepository unicornRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUnicornMockMvc;

    private Unicorn unicorn;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UnicornResource unicornResource = new UnicornResource();
        ReflectionTestUtils.setField(unicornResource, "unicornRepository", unicornRepository);
        this.restUnicornMockMvc = MockMvcBuilders.standaloneSetup(unicornResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unicorn createEntity(EntityManager em) {
        Unicorn unicorn = new Unicorn()
                .name(DEFAULT_NAME)
                .color(DEFAULT_COLOR);
        return unicorn;
    }

    @Before
    public void initTest() {
        unicorn = createEntity(em);
    }

    @Test
    @Transactional
    public void createUnicorn() throws Exception {
        int databaseSizeBeforeCreate = unicornRepository.findAll().size();

        // Create the Unicorn

        restUnicornMockMvc.perform(post("/api/unicorns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(unicorn)))
                .andExpect(status().isCreated());

        // Validate the Unicorn in the database
        List<Unicorn> unicorns = unicornRepository.findAll();
        assertThat(unicorns).hasSize(databaseSizeBeforeCreate + 1);
        Unicorn testUnicorn = unicorns.get(unicorns.size() - 1);
        assertThat(testUnicorn.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnicorn.getColor()).isEqualTo(DEFAULT_COLOR);
    }

    @Test
    @Transactional
    public void getAllUnicorns() throws Exception {
        // Initialize the database
        unicornRepository.saveAndFlush(unicorn);

        // Get all the unicorns
        restUnicornMockMvc.perform(get("/api/unicorns?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(unicorn.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())));
    }

    @Test
    @Transactional
    public void getUnicorn() throws Exception {
        // Initialize the database
        unicornRepository.saveAndFlush(unicorn);

        // Get the unicorn
        restUnicornMockMvc.perform(get("/api/unicorns/{id}", unicorn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(unicorn.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUnicorn() throws Exception {
        // Get the unicorn
        restUnicornMockMvc.perform(get("/api/unicorns/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnicorn() throws Exception {
        // Initialize the database
        unicornRepository.saveAndFlush(unicorn);
        int databaseSizeBeforeUpdate = unicornRepository.findAll().size();

        // Update the unicorn
        Unicorn updatedUnicorn = unicornRepository.findOne(unicorn.getId());
        updatedUnicorn
                .name(UPDATED_NAME)
                .color(UPDATED_COLOR);

        restUnicornMockMvc.perform(put("/api/unicorns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUnicorn)))
                .andExpect(status().isOk());

        // Validate the Unicorn in the database
        List<Unicorn> unicorns = unicornRepository.findAll();
        assertThat(unicorns).hasSize(databaseSizeBeforeUpdate);
        Unicorn testUnicorn = unicorns.get(unicorns.size() - 1);
        assertThat(testUnicorn.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnicorn.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void deleteUnicorn() throws Exception {
        // Initialize the database
        unicornRepository.saveAndFlush(unicorn);
        int databaseSizeBeforeDelete = unicornRepository.findAll().size();

        // Get the unicorn
        restUnicornMockMvc.perform(delete("/api/unicorns/{id}", unicorn.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Unicorn> unicorns = unicornRepository.findAll();
        assertThat(unicorns).hasSize(databaseSizeBeforeDelete - 1);
    }
}
