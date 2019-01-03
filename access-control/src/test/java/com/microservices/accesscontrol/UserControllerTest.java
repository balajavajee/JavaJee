/*package com.microservices.accesscontrol;

import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.bala.accesscontrol.controller.UserController;
import com.bala.accesscontrol.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@SpringBootConfiguration
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Test
	public void testExample() {
		User user = new User();
		user.setId(1233l);
		user.setFamilyName("familyName");
		given(this.userService.getByName("sboot")).willReturn((Collection<User>) user);
		this.mockMvc.perform(get("/users/user1").accept(MediaType.TEXT_PLAIN)).andExpect(status().isOk())
				.andExpect(content().string("familyName"));

	}

	
	@Test
	void testGetAllPageable() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAll() {
		fail("Not yet implemented");
	}

	@Test
	void testGetById() {
		fail("Not yet implemented");
	}

	@Test
	void testGetByName() {
		fail("Not yet implemented");
	}

	@Test
	void testGetByNameOne() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllRoles() {
		fail("Not yet implemented");
	}

	@Test
	void testGetRoleById() {
		fail("Not yet implemented");
	}

	@Test
	void testAddUserUser() {
		fail("Not yet implemented");
	}

	@Test
	void testAddUserListOfUser() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateUserLongUser() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateUserListOfUser() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteUser() {
		fail("Not yet implemented");
	}

}
*/