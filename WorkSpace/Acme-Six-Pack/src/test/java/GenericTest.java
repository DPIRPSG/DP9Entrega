

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import controllers.WelcomeController;
import domain.Gym;

import repositories.GymRepository;
import security.LoginController;
import services.GymService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class GenericTest extends AbstractTest{

	// Service under test -------------------------

	
	// Test ---------------------------------------

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;
    
    @Autowired
    private GymService gymService;
    
    @Autowired
    private GymRepository gymRepository;
    
    @InjectMocks
    private LoginController loginController;

    private MockMvc mockMvc;
	
    @Before
    public void setup() {
         MockitoAnnotations.initMocks(this);

    }
    
	@Test
	public void testPopulateDatabase(){
		System.out.println("Test Populate");
	}
	
	@Test
	public void simpleServletTest(){
		System.out.println("Simple servlet Test");
        
		mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        
        MockHttpServletRequest request;
        request = new MockHttpServletRequest();
        request.setContextPath("/security/login");
        request.addParameter("userName", "me");
        request.addParameter("password", "secret");
        
        
        
        RequestBuilder request2;
        
        request2 
        
		mockMvc.perform(request2);
        
        mockMvc.perform(
        		post("/security/login")
                .param("userName", "me")
                .param("password", "secret"))
              .andExpect(status().isOk());
	}
	
	
}
