

import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import controllers.RegisterController;
import controllers.WelcomeController;
import domain.Gym;
import security.Credentials;
import security.LoginController;
import services.GymService;
import services.form.ActorFormService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
@WebAppConfiguration
public class GenericTest extends AbstractTest{

	// Service under test -------------------------

	
	// Test ---------------------------------------

    @Mock
	//@InjectMocks
    @Autowired
    private GymService gymService;
    
    
    //@InjectMocks
    //private GymService gymRepository;
    
    @Autowired
    private WelcomeController welcomeController;
    
    //@InjectMocks
    @Autowired
    private RegisterController registerController;
    
    @Autowired
    private WebApplicationContext wac;
    
    @Autowired
    private ActorFormService actorFormService;
    
    @Autowired
    private LoginController loginController;

    private MockMvc mockMvc;

	
    @Before
    public void setup() {
/*    	 mTomcat = new Tomcat();
    	 mTomcat.setPort(0);
    	 mTomcat.setBaseDir(mWorkingDir);
    	 mTomcat.getHost().setAppBase(mWorkingDir);
    	 mTomcat.getHost().setAutoDeploy(true);
    	 mTomcat.getHost().setDeployOnStartup(true);

    	
   */ 	
    	
    	// Mockito.reset(gymService);
        MockitoAnnotations.initMocks(this);

        // this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
       
        mockMvc = MockMvcBuilders.standaloneSetup(registerController, welcomeController, loginController).build();
        
        
        MockMvcBuilders.webAppContextSetup(wac);
        

    }
    
	@Test
	public void testPopulateDatabase(){
		System.out.println("Test Populate");
	}
	
	@Test
	public void simpleMockitoTest(){
        Collection<Gym> gyms;
        
        gyms = gymService.findAll();
        
        Mockito.when(gymService.findAll()).thenReturn(gyms);
        
        // gymRepository.findAllByService(serviceId)
	}
	
	@Test
	public void mockTestGet() throws Exception{	
		RequestBuilder requestBuilder;
		// MockHttpServletRequestBuilder mmv;
		requestBuilder = MockMvcRequestBuilders.get("/security/login.do", "");
		
		// mmv = MockMvcRequestBuilders.request(HttpMethod.GET, "/", "port:8080");
		// HttpHeaders headers;
		
		//headers.set
		MockHttpServletRequest servlet2;
		
		servlet2 = new MockHttpServletRequest();
		servlet2.setLocalPort(8080);
		
		// servlet2 = requestBuilder;
		
		
		// requestBuilder.
		// mockMvc.perform(requestBuilder)
		mockMvc.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("security/login"))
//			               .andExpect(model().attribute("todo", hasProperty("id", is(1L))))
//                .andExpect(model().attribute("todo", hasProperty("description", is("Lorem ipsum"))))
//                .andExpect(model().attribute("todo", hasProperty("title", is("Foo"))))
			;
	}
	
	@Test
	public void mockTestGet2() throws Exception{	
		RequestBuilder requestBuilder;
		requestBuilder = MockMvcRequestBuilders.get("/welcome/index.do", "");

		mockMvc.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("welcome/index"))
//			               .andExpect(model().attribute("todo", hasProperty("id", is(1L))))
//                .andExpect(model().attribute("todo", hasProperty("description", is("Lorem ipsum"))))
//                .andExpect(model().attribute("todo", hasProperty("title", is("Foo"))))
			;
	}
	
	@Test
	public void mockTestPost() throws Exception{	
		RequestBuilder requestBuilder;
		// MockHttpServletRequestBuilder mmv;
		requestBuilder = MockMvcRequestBuilders.post("/security/login.do", "")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "admin")
				.param("password", "admin")
				.sessionAttr("credentials", new Credentials())
				;
		
		// mmv = MockMvcRequestBuilders.request(HttpMethod.GET, "/", "port:8080");
		// HttpHeaders headers;
		
		//headers.set

		// servlet2 = requestBuilder;
		
		
		// requestBuilder.
		// mockMvc.perform(requestBuilder)
		mockMvc.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("security/login"))
			.andExpect(MockMvcResultMatchers.model().hasNoErrors())
			// .andExpect(MockMvcResultMatchers.redirectedUrl("/welcome/index.do"))
			;
	}
	
	@Test
	public void mockTestGetRegister() throws Exception{	
		RequestBuilder requestBuilder;
		requestBuilder = MockMvcRequestBuilders.get("/customer/create.do", "");

		mockMvc.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("actorForm/create"))
			;
	}
	
	@Test
	// @Transactional(noRollbackFor=Exception.class, noRollbackForClassName="MockMvc")
	public void mockTestPostRegister() throws Exception{	
		RequestBuilder requestBuilder;
		requestBuilder = 
				MockMvcRequestBuilders.post("/customer/create", "save")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "admin")
				.param("surname", "admin")
				.param("phone", "admin")
				.param("username", "jeeioi")
				.param("password", "admin")
				.param("repeatedPassword", "admin")
				.param("acceptTerm", "true")
				.param("save", "")
				.sessionAttr("actorForm", actorFormService.createForm())
				//.sessionAttr("actorForm", new ActorForm())

				;

		mockMvc.perform(requestBuilder)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			//.andExpect(MockMvcResultMatchers.view().name("security/login"))
			.andExpect(MockMvcResultMatchers.model().hasNoErrors())
			.andExpect(MockMvcResultMatchers.model().attribute("message", "customer.commit.ok"))
			//.andExpect(MockMvcResultMatchers.view().name("security/login"))
			//.andExpect(MockMvcResultMatchers.redirectedUrl("/index"))
			;
		
	}
	
/*	@Test
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
	}*/
	
	
}
