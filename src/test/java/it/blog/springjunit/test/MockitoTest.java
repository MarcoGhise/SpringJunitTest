package it.blog.springjunit.test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import it.blog.springjunit.controller.WebController;
import it.blog.springjunit.dao.SqlDao;
import it.blog.springjunit.dao.TableData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/*
 * https://spring.io/blog/2011/02/11/spring-framework-3-1-m1-released/
 * https://spring.io/blog/2011/06/21/spring-3-1-m2-testing-with-configuration-classes-and-profiles
 * 
 * For testing purpose
 * http://localhost.:8080/SpringJunitTest/lastname?firstname=James
 * Cookie: JSESSIONID=84A42C2FC39AD1911119DD3E34072209;
 */
@ContextConfiguration
@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
public class MockitoTest {
	
	@Autowired
	SqlDao jdbcDao;
	
	@Autowired
	WebController controller;
	
	HttpSession sessionObj;

	Map<Object, Object> attributes;
	
	@Configuration
    @ImportResource(value = {"classpath:springjunittest-servlet.xml"})
    static class TestConfig {
       @Bean
       public SqlDao jdbcDao() {
        return Mockito.mock(SqlDao.class);
       }       
    }
	
	@Before
	public void setup()
	{
		
		sessionObj = Mockito.mock(HttpSession.class);
		
		attributes = new HashMap<Object, Object>();
		
		when(sessionObj.getAttribute(anyString())).thenAnswer(new Answer<Object>() {
            /**
             * @see org.mockito.stubbing.Answer#answer(org.mockito.invocation.InvocationOnMock)
             */
            @Override
            public Object answer(InvocationOnMock aInvocation) throws Throwable {
                String key = (String) aInvocation.getArguments()[0];
                return attributes.get(key);
            }
        });

		Mockito.doAnswer(new Answer<Object>() {
            /**
             * @see org.mockito.stubbing.Answer#answer(org.mockito.invocation.InvocationOnMock)
             */
            @Override
            public Object answer(InvocationOnMock aInvocation) throws Throwable {
                String key = (String) aInvocation.getArguments()[0];
                Object value = aInvocation.getArguments()[1];
                attributes.put(key, value);
                return null;
            }
        }).when(sessionObj).setAttribute(anyString(), anyObject());
	}
	
	@Test	
    public void testDataDb() throws InterruptedException
    {
		String firstName = "Larry";
		String lastName = "Bird";
		
		TableData tableData = new TableData();
		
		tableData.setFirst_name(firstName);
		tableData.setLast_name(lastName);
		
		when(jdbcDao.getData(firstName)).thenReturn(tableData);
		
		ModelMap model = new ModelMap();
		
		controller.getLastName(sessionObj, firstName, model);
		
		assertEquals(model.get("lastname"), lastName);
		assertEquals(sessionObj.getAttribute("LastName"), lastName);
    }
}
