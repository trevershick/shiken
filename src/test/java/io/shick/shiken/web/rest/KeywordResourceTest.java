package io.shick.shiken.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import io.shick.shiken.Application;
import io.shick.shiken.domain.Keyword;
import io.shick.shiken.repository.KeywordRepository;
import io.shick.shiken.web.rest.dto.KeywordDTO;
import io.shick.shiken.web.rest.mapper.KeywordMapper;


/**
 * Test class for the KeywordResource REST controller.
 *
 * @see KeywordResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class KeywordResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private KeywordRepository keywordRepository;

    @Inject
    private KeywordMapper keywordMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restKeywordMockMvc;

    private Keyword keyword;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        KeywordResource keywordResource = new KeywordResource();
        ReflectionTestUtils.setField(keywordResource, "keywordRepository", keywordRepository);
        ReflectionTestUtils.setField(keywordResource, "keywordMapper", keywordMapper);
        this.restKeywordMockMvc = MockMvcBuilders.standaloneSetup(keywordResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        keyword = new Keyword();
        keyword.setName(DEFAULT_NAME);
        keyword.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createKeyword() throws Exception {
        int databaseSizeBeforeCreate = keywordRepository.findAll().size();

        // Create the Keyword
        KeywordDTO keywordDTO = keywordMapper.keywordToKeywordDTO(keyword);

        restKeywordMockMvc.perform(post("/api/keywords")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(keywordDTO)))
                .andExpect(status().isCreated());

        // Validate the Keyword in the database
        List<Keyword> keywords = keywordRepository.findAll();
        assertThat(keywords).hasSize(databaseSizeBeforeCreate + 1);
        Keyword testKeyword = keywords.get(keywords.size() - 1);
        assertThat(testKeyword.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testKeyword.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = keywordRepository.findAll().size();
        // set the field null
        keyword.setName(null);

        // Create the Keyword, which fails.
        KeywordDTO keywordDTO = keywordMapper.keywordToKeywordDTO(keyword);

        restKeywordMockMvc.perform(post("/api/keywords")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(keywordDTO)))
                .andExpect(status().isBadRequest());

        List<Keyword> keywords = keywordRepository.findAll();
        assertThat(keywords).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllKeywords() throws Exception {
        // Initialize the database
        keywordRepository.saveAndFlush(keyword);

        // Get all the keywords
        restKeywordMockMvc.perform(get("/api/keywords"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getKeyword() throws Exception {
        // Initialize the database
        keywordRepository.saveAndFlush(keyword);

        // Get the keyword
        restKeywordMockMvc.perform(get("/api/keywords/{name}", keyword.getName()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingKeyword() throws Exception {
        // Get the keyword
        restKeywordMockMvc.perform(get("/api/keywords/{name}", "xxx"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKeyword() throws Exception {
        // Initialize the database
        keywordRepository.saveAndFlush(keyword);

		int databaseSizeBeforeUpdate = keywordRepository.findAll().size();

        // Update the keyword
        keyword.setName(UPDATED_NAME);
        keyword.setDescription(UPDATED_DESCRIPTION);

        KeywordDTO keywordDTO = keywordMapper.keywordToKeywordDTO(keyword);

        restKeywordMockMvc.perform(put("/api/keywords")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(keywordDTO)))
                .andExpect(status().isOk());

        // Validate the Keyword in the database
        List<Keyword> keywords = keywordRepository.findAll();
        assertThat(keywords).hasSize(databaseSizeBeforeUpdate);
        Keyword testKeyword = keywords.get(keywords.size() - 1);
        assertThat(testKeyword.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testKeyword.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteKeyword() throws Exception {
        // Initialize the database
        keywordRepository.saveAndFlush(keyword);

		int databaseSizeBeforeDelete = keywordRepository.findAll().size();

        // Get the keyword
        restKeywordMockMvc.perform(delete("/api/keywords/{name}", keyword.getName())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Keyword> keywords = keywordRepository.findAll();
        assertThat(keywords).hasSize(databaseSizeBeforeDelete - 1);
    }
}
