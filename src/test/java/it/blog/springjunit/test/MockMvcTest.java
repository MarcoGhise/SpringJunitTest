package it.blog.springjunit.test;

import it.blog.springjunit.dao.SqlDao;
import it.blog.springjunit.dao.TableData;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@WebAppConfiguration
@ContextConfiguration
@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
public class MockMvcTest {

	private MockMvc mockMvc;

	@Autowired
    private WebApplicationContext webAppContext;

	@Autowired
	SqlDao jdbcDao;
	
	@Configuration
    @ImportResource(value = {"classpath:springjunittest-servlet.xml"})
    static class TestConfig {
       @Bean
       public SqlDao jdbcDao() {
        return Mockito.mock(SqlDao.class);
       }       
    }
	
	@Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
        
        String firstName = "James";
		String lastName = "Dean";
		
		TableData tableData = new TableData();
		
		tableData.setFirst_name(firstName);
		tableData.setLast_name(lastName);
		
		when(jdbcDao.getData(firstName)).thenReturn(tableData);
    }

	
	@Test
	public void findUser()
			throws Exception {

		RequestBuilder request = get("/lastname").param("firstname", "James").contentType(MediaType.TEXT_HTML);
		
		mockMvc.perform(request)
	            .andExpect(status().isOk())
	            .andExpect(model().attribute("lastname", "Dean"))
	            .andDo(print());	            	            		
	}
	
	@Test
	public void findUserJson()
			throws Exception {

		RequestBuilder request = get("/lastnamejson").param("firstname", "James").contentType(MediaType.TEXT_HTML);
		
		mockMvc.perform(request)
	            .andExpect(status().isOk())
	            .andExpect(content().contentType("application/json"))
	            .andExpect(jsonPath("name").value("Dean"))
	            .andDo(print());	            	            
	}

}
