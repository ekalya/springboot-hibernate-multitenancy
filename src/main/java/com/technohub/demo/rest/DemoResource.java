package com.technohub.demo.rest;

import com.technohub.alumni.dao.PersonDao;
import com.technohub.alumni.model.Person;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/demo")
@Transactional
public class DemoResource {

    @Autowired
    private PersonDao personDao;

    @GetMapping(value = "/{id}")
    public String getPerson(@PathVariable("id") String id) {
        Person person = this.personDao.getOne(Integer.valueOf(id));
        return String.format("[person: %s %s], [DemoResource instance: %s], [PersonDao instance: %s]",
                person.getFirstName(), person.getLastName(), this, this.personDao);
    }

    @PatchMapping(value = "/{id}")
    public String updatePerson(@PathVariable("id") String id, @RequestBody Map<String, Object> updates) {
        Person person = this.personDao.getOne(Integer.valueOf(id));
        person.setFirstName((String) updates.get("firstName"));
        return String.format("[person: %s %s], [DemoResource instance: %s], [PersonDao instance: %s]",
                person.getFirstName(), person.getLastName(), this, this.personDao);
    }
}
