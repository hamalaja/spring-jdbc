package vn.hamalaja.jdbc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hamalaja.jdbc.dao.CustomerRepository;
import vn.hamalaja.jdbc.om.Customer;

/**
 * @author lamhm
 *
 */
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {
	@Autowired
	private CustomerRepository repository;


	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public @ResponseBody int count() {
		return repository.countCustomer();
	}


	@RequestMapping(value = "/insert", method = RequestMethod.GET)
	public @ResponseBody int insert(@RequestParam("first_name") String firstName, @RequestParam("last_name") String lastName) {
		return repository.insertCustomer(firstName, lastName);
	}


	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public @ResponseBody int update(@RequestParam("id") int id, @RequestParam("last_name") String lastName) {
		return repository.updateCustomer(id, lastName);
	}


	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Customer find(@RequestParam("id") int id) {
		return repository.getCustomerByStore(id);
	}


	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public @ResponseBody List<Customer> find(@RequestParam("last_name") String lastName) {
		return repository.findCustomerByLastName(lastName);
	}


	@RequestMapping(value = "/getall", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getAll() {
		return repository.getAllCustomer();
	}


	@RequestMapping(value = "/update/all", method = RequestMethod.GET)
	public @ResponseBody int[] updateAll() {
		List<Customer> customers = repository.findCustomerByLastName("Ha");
		for (Customer customer : customers) {
			customer.setFirstName("Ha " + customer.getFirstName());
		}
		return repository.batchUpdate(customers);
	}

}
