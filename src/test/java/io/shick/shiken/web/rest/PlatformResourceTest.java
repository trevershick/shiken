package io.shick.shiken.web.rest;

import io.shick.shiken.Application;
import io.shick.shiken.domain.Platform;
import io.shick.shiken.repository.PlatformRepository;
import io.shick.shiken.repository.search.PlatformSearchRepository;
import io.shick.shiken.web.rest.dto.PlatformDTO;
import io.shick.shiken.web.rest.mapper.PlatformMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PlatformResource REST controller.
 *
 * @see PlatformResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PlatformResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private PlatformRepository platformRepository;

    @Inject
    private PlatformMapper platformMapper;

    @Inject
    private PlatformSearchRepository platformSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restPlatformMockMvc;

    private Platform platform;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PlatformResource platformResource = new PlatformResource();
        ReflectionTestUtils.setField(platformResource, "platformRepository", platformRepository);
        ReflectionTestUtils.setField(platformResource, "platformMapper", platformMapper);
        ReflectionTestUtils.setField(platformResource, "platformSearchRepository", platformSearchRepository);
        this.restPlatformMockMvc = MockMvcBuilders.standaloneSetup(platformResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        platform = new Platform();
        platform.setName(DEFAULT_NAME);
        platform.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createPlatform() throws Exception {
        int databaseSizeBeforeCreate = platformRepository.findAll().size();

        // Create the Platform
        PlatformDTO platformDTO = platformMapper.platformToPlatformDTO(platform);

        restPlatformMockMvc.perform(post("/api/platforms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(platformDTO)))
                .andExpect(status().isCreated());

        // Validate the Platform in the database
        List<Platform> platforms = platformRepository.findAll();
        assertThat(platforms).hasSize(databaseSizeBeforeCreate + 1);
        Platform testPlatform = platforms.get(platforms.size() - 1);
        assertThat(testPlatform.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlatform.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = platformRepository.findAll().size();
        // set the field null
        platform.setName(null);

        // Create the Platform, which fails.
        PlatformDTO platformDTO = platformMapper.platformToPlatformDTO(platform);

        restPlatformMockMvc.perform(post("/api/platforms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(platformDTO)))
                .andExpect(status().isBadRequest());

        List<Platform> platforms = platformRepository.findAll();
        assertThat(platforms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlatforms() throws Exception {
        // Initialize the database
        platformRepository.saveAndFlush(platform);

        // Get all the platforms
        restPlatformMockMvc.perform(get("/api/platforms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(platform.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPlatform() throws Exception {
        // Initialize the database
        platformRepository.saveAndFlush(platform);

        // Get the platform
        restPlatformMockMvc.perform(get("/api/platforms/{id}", platform.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(platform.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlatform() throws Exception {
        // Get the platform
        restPlatformMockMvc.perform(get("/api/platforms/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlatform() throws Exception {
        // Initialize the database
        platformRepository.saveAndFlush(platform);

		int databaseSizeBeforeUpdate = platformRepository.findAll().size();

        // Update the platform
        platform.setName(UPDATED_NAME);
        platform.setDescription(UPDATED_DESCRIPTION);
        
        PlatformDTO platformDTO = platformMapper.platformToPlatformDTO(platform);

        restPlatformMockMvc.perform(put("/api/platforms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(platformDTO)))
                .andExpect(status().isOk());

        // Validate the Platform in the database
        List<Platform> platforms = platformRepository.findAll();
        assertThat(platforms).hasSize(databaseSizeBeforeUpdate);
        Platform testPlatform = platforms.get(platforms.size() - 1);
        assertThat(testPlatform.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlatform.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deletePlatform() throws Exception {
        // Initialize the database
        platformRepository.saveAndFlush(platform);

		int databaseSizeBeforeDelete = platformRepository.findAll().size();

        // Get the platform
        restPlatformMockMvc.perform(delete("/api/platforms/{id}", platform.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Platform> platforms = platformRepository.findAll();
        assertThat(platforms).hasSize(databaseSizeBeforeDelete - 1);
    }
}
