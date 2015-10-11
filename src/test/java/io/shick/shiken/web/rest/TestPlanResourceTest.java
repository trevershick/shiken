package io.shick.shiken.web.rest;

import io.shick.shiken.Application;
import io.shick.shiken.domain.TestPlan;
import io.shick.shiken.repository.TestPlanRepository;
import io.shick.shiken.repository.search.TestPlanSearchRepository;
import io.shick.shiken.web.rest.dto.TestPlanDTO;
import io.shick.shiken.web.rest.mapper.TestPlanMapper;

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
 * Test class for the TestPlanResource REST controller.
 *
 * @see TestPlanResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TestPlanResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;
    private static final String DEFAULT_APIKEY = "SAMPLE_TEXT";
    private static final String UPDATED_APIKEY = "UPDATED_TEXT";

    @Inject
    private TestPlanRepository testPlanRepository;

    @Inject
    private TestPlanMapper testPlanMapper;

    @Inject
    private TestPlanSearchRepository testPlanSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restTestPlanMockMvc;

    private TestPlan testPlan;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestPlanResource testPlanResource = new TestPlanResource();
        ReflectionTestUtils.setField(testPlanResource, "testPlanRepository", testPlanRepository);
        ReflectionTestUtils.setField(testPlanResource, "testPlanMapper", testPlanMapper);
        ReflectionTestUtils.setField(testPlanResource, "testPlanSearchRepository", testPlanSearchRepository);
        this.restTestPlanMockMvc = MockMvcBuilders.standaloneSetup(testPlanResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        testPlan = new TestPlan();
        testPlan.setName(DEFAULT_NAME);
        testPlan.setDescription(DEFAULT_DESCRIPTION);
        testPlan.setActive(DEFAULT_ACTIVE);
        testPlan.setApikey(DEFAULT_APIKEY);
    }

    @Test
    @Transactional
    public void createTestPlan() throws Exception {
        int databaseSizeBeforeCreate = testPlanRepository.findAll().size();

        // Create the TestPlan
        TestPlanDTO testPlanDTO = testPlanMapper.testPlanToTestPlanDTO(testPlan);

        restTestPlanMockMvc.perform(post("/api/testPlans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testPlanDTO)))
                .andExpect(status().isCreated());

        // Validate the TestPlan in the database
        List<TestPlan> testPlans = testPlanRepository.findAll();
        assertThat(testPlans).hasSize(databaseSizeBeforeCreate + 1);
        TestPlan testTestPlan = testPlans.get(testPlans.size() - 1);
        assertThat(testTestPlan.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTestPlan.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestPlan.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testTestPlan.getApikey()).isEqualTo(DEFAULT_APIKEY);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = testPlanRepository.findAll().size();
        // set the field null
        testPlan.setName(null);

        // Create the TestPlan, which fails.
        TestPlanDTO testPlanDTO = testPlanMapper.testPlanToTestPlanDTO(testPlan);

        restTestPlanMockMvc.perform(post("/api/testPlans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testPlanDTO)))
                .andExpect(status().isBadRequest());

        List<TestPlan> testPlans = testPlanRepository.findAll();
        assertThat(testPlans).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTestPlans() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get all the testPlans
        restTestPlanMockMvc.perform(get("/api/testPlans"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(testPlan.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].apikey").value(hasItem(DEFAULT_APIKEY.toString())));
    }

    @Test
    @Transactional
    public void getTestPlan() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

        // Get the testPlan
        restTestPlanMockMvc.perform(get("/api/testPlans/{id}", testPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testPlan.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.apikey").value(DEFAULT_APIKEY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTestPlan() throws Exception {
        // Get the testPlan
        restTestPlanMockMvc.perform(get("/api/testPlans/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestPlan() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

		int databaseSizeBeforeUpdate = testPlanRepository.findAll().size();

        // Update the testPlan
        testPlan.setName(UPDATED_NAME);
        testPlan.setDescription(UPDATED_DESCRIPTION);
        testPlan.setActive(UPDATED_ACTIVE);
        testPlan.setApikey(UPDATED_APIKEY);
        
        TestPlanDTO testPlanDTO = testPlanMapper.testPlanToTestPlanDTO(testPlan);

        restTestPlanMockMvc.perform(put("/api/testPlans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testPlanDTO)))
                .andExpect(status().isOk());

        // Validate the TestPlan in the database
        List<TestPlan> testPlans = testPlanRepository.findAll();
        assertThat(testPlans).hasSize(databaseSizeBeforeUpdate);
        TestPlan testTestPlan = testPlans.get(testPlans.size() - 1);
        assertThat(testTestPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestPlan.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestPlan.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testTestPlan.getApikey()).isEqualTo(UPDATED_APIKEY);
    }

    @Test
    @Transactional
    public void deleteTestPlan() throws Exception {
        // Initialize the database
        testPlanRepository.saveAndFlush(testPlan);

		int databaseSizeBeforeDelete = testPlanRepository.findAll().size();

        // Get the testPlan
        restTestPlanMockMvc.perform(delete("/api/testPlans/{id}", testPlan.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TestPlan> testPlans = testPlanRepository.findAll();
        assertThat(testPlans).hasSize(databaseSizeBeforeDelete - 1);
    }
}
