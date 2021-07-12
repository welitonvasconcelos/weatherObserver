package com.weatherObserver.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.weatherObserver.entity.AvailableTimeToCity;
import com.weatherObserver.entity.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserDAOTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserDAO userDAO;

	private User user;
	private String cityName;
	private String email;

	@BeforeEach
	public void beforeEach() {
		cityName = "City";
		email = "user@user.com";
		AvailableTimeToCity availableTimeToCity = AvailableTimeToCity.builder().cityName(cityName)
				.start(LocalDateTime.now().minusHours(1l)).end(LocalDateTime.now().plusDays(7)).build();
		List<AvailableTimeToCity> availableTimeToCities = new ArrayList<AvailableTimeToCity>();
		availableTimeToCities.add(availableTimeToCity);
		user = User.builder().email(email).availableTimeToCities(availableTimeToCities).build();
		entityManager.persist(user);
	}

	@Test
	public void shouldFindUser() {
		User userFoundEmail = userDAO.findByEmail(email);
		assertNotNull(userFoundEmail);
		assertEquals(email, userFoundEmail.getEmail());
		assertEquals(cityName, userFoundEmail.getAvailableTimeToCities().get(0).getCityName());

	}

	@Test
	public void shouldNotFindUser() {
		User userFoundEmail = userDAO.findByEmail("test@test.com");
		assertNull(userFoundEmail);
	}

}
