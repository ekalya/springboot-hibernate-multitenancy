package com.technohub.alumni.dao;

import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.technohub.demo.config.alumni.DvdRentalTenantContext;
import com.technohub.demo.main.Application;
import com.technohub.alumni.model.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class PersonDaoIT {

	@Autowired
	private PersonDao personDao;

	@BeforeClass
	public static void beforeClass() {
		DvdRentalTenantContext.setTenantId("tenant_1");
	}

    @AfterClass
    public static void afterClass() {
        DvdRentalTenantContext.clear();
    }

	@Test
	public void shouldHave200Actors() {
		Assert.assertThat(this.personDao.count(), Matchers.equalTo(200L));
	}

	@Test
	public void actorShouldBePenelope() {
		Person actor = this.personDao.findOne(1);
		Assert.assertThat(actor.getFirstName(), Matchers.equalTo("Penelope"));
	}
}