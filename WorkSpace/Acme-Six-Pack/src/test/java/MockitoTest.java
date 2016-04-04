

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import security.LoginController;
import services.form.ActorFormService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = true)
@WebAppConfiguration
public class MockitoTest extends AbstractTest{

	// Service under test -------------------------

	
	// Test ---------------------------------------
   
    @Autowired
    private WelcomeController welcomeController;
    
    @Autowired
    private RegisterController registerController;
    
    @Autowired
    private ActorFormService actorFormService;
    
    @Autowired
    private LoginController loginController;

    @Autowired
    private WebApplicationContext wac;
    
    private MockMvc mockMvc;

	
    @Before
    public void setup() {
    	
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(registerController, welcomeController, loginController).build();
        
        MockMvcBuilders.webAppContextSetup(wac);
        
        unauthenticate();
    }
	
	@Test
	public void mockTestGet() throws Exception{	
		RequestBuilder requestBuilder;
		requestBuilder = MockMvcRequestBuilders.get("/welcome/index.do", "");

		mockMvc.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("welcome/index"))
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
	public void mockTestRegister() throws Exception{	
		RequestBuilder requestBuilder;
		requestBuilder = 
				MockMvcRequestBuilders.post("/customer/create", "save")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "admin")
				.param("surname", "admin")
				.param("phone", "admin")
				.param("username", "userExample1")
				.param("password", "admin")
				.param("repeatedPassword", "admin")
				.param("acceptTerm", "true") 
				.param("_acceptTerm", "on")
				.param("_createCreditCard", "on")
				.param("createCreditCard", "true")
				.param("_createSocialIdentity", "on")
				.param("save", "")
				.sessionAttr("actorForm", actorFormService.createForm())
				;

		mockMvc.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
			.andExpect(MockMvcResultMatchers.view().name("redirect:../security/login.do"))
			.andExpect(MockMvcResultMatchers.model().hasNoErrors())
			.andExpect(MockMvcResultMatchers.model().attribute("messageStatus", "customer.commit.ok"))
			;
		
		authenticate("userExample1");
		
		requestBuilder = 
				MockMvcRequestBuilders.post("/welcome/index.do", "")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				;
		
		mockMvc.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("welcome/index"))
			.andExpect(MockMvcResultMatchers.model().hasNoErrors())
		;
	}
	
	
}
