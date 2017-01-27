package com.data.postgres;

import com.data.postgres.model.UserSafe;
import com.data.postgres.model.UserDetails;
import com.data.postgres.model.UserRaw;
import com.data.postgres.repository.UserRawRepository;
import com.data.postgres.repository.UserSafeRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostgresTypesApplicationTests {

	@Autowired
	private UserRawRepository userRawRepository;

	@Autowired
	private UserSafeRepository userSafeRepository;

	@Test
	public void JSONBRawUserType_shouldWork(){
		UserRaw user = new UserRaw();
		user.setCode("001");

		UserDetails details = new UserDetails();
		details.setFirstName("Ahmet");
		details.setLastName("Öz");
		details.setAge(26);
		details.setCountry("Turkey");
		user.setValue(details);

		UserRaw savedUser = userRawRepository.save(user);
		assertNotNull(savedUser);
		assertThat(savedUser.getCode(), equalTo("001"));
		assertThat(savedUser.getValue(), notNullValue());

		UserRaw findUser = userRawRepository.findOne(savedUser.getId());
		assertNotNull(findUser);
		assertThat(findUser.getCode(), equalTo("001"));
		assertThat(findUser.getValue(), notNullValue());

		//cast required..
		UserDetails userDetails = (UserDetails) findUser.getValue();
		assertNotNull(userDetails);
	}

	@Test
	public void JSONBUserType_shouldWork(){

		UserSafe user = new UserSafe();
		user.setCode("001");

		UserDetails details = new UserDetails();
		details.setFirstName("Ahmet");
		details.setLastName("Öz");
		details.setAge(26);
		details.setCountry("Turkey");
		user.setValue(details);

		UserSafe savedUser = userSafeRepository.save(user);
		assertNotNull(savedUser);
		assertThat(savedUser.getCode(), equalTo("001"));
		assertThat(savedUser.getValue(), notNullValue());

		UserSafe findUser = userSafeRepository.findOne(savedUser.getId());
		assertNotNull(findUser);
		assertThat(findUser.getCode(), equalTo("001"));
		assertThat(findUser.getValue(), notNullValue());

	}
}
