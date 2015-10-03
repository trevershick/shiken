package io.shick.shiken.web.rest;

import io.shick.shiken.Application;
import io.shick.shiken.domain.TestProject;
import io.shick.shiken.repository.TestProjectRepository;
import io.shick.shiken.repository.search.TestProjectSearchRepository;
import io.shick.shiken.web.rest.dto.TestProjectDTO;
import io.shick.shiken.web.rest.mapper.TestProjectMapper;

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
 * Test class for the TestProjectResource REST controller.
 *
 * @see TestProjectResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TestProjectResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_PREFIX = "SAMPLE_TEXT";
    private static final String UPDATED_PREFIX = "UPDATED_TEXT";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Inject
    private TestProjectRepository testProjectRepository;

    @Inject
    private TestProjectMapper testProjectMapper;

    @Inject
    private TestProjectSearchRepository testProjectSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restTestProjectMockMvc;

    private TestProject testProject;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestProjectResource testProjectResource = new TestProjectResource();
        ReflectionTestUtils.setField(testProjectResource, "testProjectRepository", testProjectRepository);
        ReflectionTestUtils.setField(testProjectResource, "testProjectMapper", testProjectMapper);
        ReflectionTestUtils.setField(testProjectResource, "testProjectSearchRepository", testProjectSearchRepository);
        this.restTestProjectMockMvc = MockMvcBuilders.standaloneSetup(testProjectResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        testProject = new TestProject();
        testProject.setName(DEFAULT_NAME);
        testProject.setDescription(DEFAULT_DESCRIPTION);
        testProject.setPrefix(DEFAULT_PREFIX);
        testProject.setActive(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createTestProject() throws Exception {
        int databaseSizeBeforeCreate = testProjectRepository.findAll().size();

        // Create the TestProject
        TestProjectDTO testProjectDTO = testProjectMapper.testProjectToTestProjectDTO(testProject);

        restTestProjectMockMvc.perform(post("/api/testProjects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testProjectDTO)))
                .andExpect(status().isCreated());

        // Validate the TestProject in the database
        List<TestProject> testProjects = testProjectRepository.findAll();
        assertThat(testProjects).hasSize(databaseSizeBeforeCreate + 1);
        TestProject testTestProject = testProjects.get(testProjects.size() - 1);
        assertThat(testTestProject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTestProject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestProject.getPrefix()).isEqualTo(DEFAULT_PREFIX);
        assertThat(testTestProject.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = testProjectRepository.findAll().size();
        // set the field null
        testProject.setName(null);

        // Create the TestProject, which fails.
        TestProjectDTO testProjectDTO = testProjectMapper.testProjectToTestProjectDTO(testProject);

        restTestProjectMockMvc.perform(post("/api/testProjects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testProjectDTO)))
                .andExpect(status().isBadRequest());

        List<TestProject> testProjects = testProjectRepository.findAll();
        assertThat(testProjects).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrefixIsRequired() throws Exception {
        int databaseSizeBeforeTest = testProjectRepository.findAll().size();
        // set the field null
        testProject.setPrefix(null);

        // Create the TestProject, which fails.
        TestProjectDTO testProjectDTO = testProjectMapper.testProjectToTestProjectDTO(testProject);

        restTestProjectMockMvc.perform(post("/api/testProjects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testProjectDTO)))
                .andExpect(status().isBadRequest());

        List<TestProject> testProjects = testProjectRepository.findAll();
        assertThat(testProjects).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTestProjects() throws Exception {
        // Initialize the database
        testProjectRepository.saveAndFlush(testProject);

        // Get all the testProjects
        restTestProjectMockMvc.perform(get("/api/testProjects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(testProject.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX.toString())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getTestProject() throws Exception {
        // Initialize the database
        testProjectRepository.saveAndFlush(testProject);

        // Get the testProject
        restTestProjectMockMvc.perform(get("/api/testProjects/{id}", testProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testProject.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.prefix").value(DEFAULT_PREFIX.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTestProject() throws Exception {
        // Get the testProject
        restTestProjectMockMvc.perform(get("/api/testProjects/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestProject() throws Exception {
        // Initialize the database
        testProjectRepository.saveAndFlush(testProject);

		int databaseSizeBeforeUpdate = testProjectRepository.findAll().size();

        // Update the testProject
        testProject.setName(UPDATED_NAME);
        testProject.setDescription(UPDATED_DESCRIPTION);
        testProject.setPrefix(UPDATED_PREFIX);
        testProject.setActive(UPDATED_ACTIVE);
        
        TestProjectDTO testProjectDTO = testProjectMapper.testProjectToTestProjectDTO(testProject);

        restTestProjectMockMvc.perform(put("/api/testProjects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testProjectDTO)))
                .andExpect(status().isOk());

        // Validate the TestProject in the database
        List<TestProject> testProjects = testProjectRepository.findAll();
        assertThat(testProjects).hasSize(databaseSizeBeforeUpdate);
        TestProject testTestProject = testProjects.get(testProjects.size() - 1);
        assertThat(testTestProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestProject.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testTestProject.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void deleteTestProject() throws Exception {
        // Initialize the database
        testProjectRepository.saveAndFlush(testProject);

		int databaseSizeBeforeDelete = testProjectRepository.findAll().size();

        // Get the testProject
        restTestProjectMockMvc.perform(delete("/api/testProjects/{id}", testProject.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TestProject> testProjects = testProjectRepository.findAll();
        assertThat(testProjects).hasSize(databaseSizeBeforeDelete - 1);
    }
}
