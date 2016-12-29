package vn.hamalaja.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import vn.hamalaja.jdbc.om.Customer;

/**
 * 
 * @author lamhm
 * @see <a
 *      href="http://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/jdbc.html">Tham
 *      khảo các cách tiếp cận khác jdbc khác</a>
 */
@Repository
public class CustomerRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;


	public int countCustomer() {
		return jdbcTemplate.queryForObject("select count(*) from customer", Integer.class);
	}


	public int insertCustomer(String firstName, String lastName) {
		return jdbcTemplate.update("insert into customer (first_name, last_name) values (?, ?)", firstName, lastName);
	}


	public Customer insertAndGetCustomer(final String firstName, final String lastName) {
		// lấy id sau khi insert xong
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement("insert into customer (first_name, last_name) values (?, ?)", new String[] { "id" });
				ps.setString(1, firstName);
				ps.setString(2, lastName);
				return ps;
			}
		}, keyHolder);
		return new Customer(keyHolder.getKey().intValue(), firstName, lastName);
	}


	public int updateCustomer(int id, String lastName) {
		return jdbcTemplate.update("update customer set first_name= COALESCE(?,first_name),last_name = COALESCE(?,last_name) where id = ?", null, lastName, id);
	}


	public Customer getCustomer(final int id) {
		return jdbcTemplate.queryForObject("select first_name, last_name from customer where id = ?", new Object[] { id }, new RowMapper<Customer>() {
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Customer(id, rs.getString("first_name"), rs.getString("last_name"));
			}
		});
	}


	public List<Map<String, Object>> getAllCustomer() {
		return jdbcTemplate.queryForList("select *from customer");
	}


	public Customer getCustomerByStore(final int id) {
		return jdbcTemplate.queryForObject("call sp_customer_geftCustomerById(?)", new Object[] { id }, new RowMapper<Customer>() {
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Customer(id, rs.getString("first_name"), rs.getString("last_name"));
			}
		});
	}


	public List<Customer> findCustomerByLastName(final String lastName) {
		return jdbcTemplate.query("select id, first_name from customer", new RowMapper<Customer>() {
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Customer(rs.getInt("id"), rs.getString("first_name"), lastName);
			}
		});
	}


	public int[] batchUpdate(final List<Customer> customers) {
		return jdbcTemplate.batchUpdate("update customer set first_name = ?, " + "last_name = ? where id = ?", new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, customers.get(i).getFirstName());
				ps.setString(2, customers.get(i).getLastName());
				ps.setLong(3, customers.get(i).getId());
			}


			public int getBatchSize() {
				return customers.size();
			}
		});
	}


	/**
	 * Mỗi batch 100 phần tử
	 * 
	 * @param customers
	 * @return
	 */
	public int[][] multiBatchUpdate(final Collection<Customer> customers) {
		return jdbcTemplate.batchUpdate("update Customer set first_name = ?, last_name = ? where id = ?", customers, 100,
				new ParameterizedPreparedStatementSetter<Customer>() {
					public void setValues(PreparedStatement ps, Customer argument) throws SQLException {
						ps.setString(1, argument.getFirstName());
						ps.setString(2, argument.getLastName());
						ps.setLong(3, argument.getId());
					}
				});
	}
}
