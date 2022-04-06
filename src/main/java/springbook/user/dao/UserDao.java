package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.User;

public class UserDao {

	public void setDataSource(DataSource dataSource) {
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
	}
	
	private JdbcTemplate jdbcTemplate;

	private RowMapper<User> userMapper =
			new RowMapper<User>() {
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User user = new User();
					user.setId(rs.getString("id"));
					user.setName(rs.getString("name"));
					user.setPassword(rs.getString("password"));
					
					return user;
				}
			};

	
	public void add(final User user) throws SQLException{
		
		this.jdbcTemplate.update("insert into users(id, name, password) values (?, ?, ?)",	
		user.getId(), user.getName(), user.getPassword());
	}


	
	public User get(String id) {
		return this.jdbcTemplate.queryForObject("select * from users where id = ? ",
				new Object[] {id},  // sql에 바인딩 할 파라미터 값, 가변인자 대신 배열을 사용한다.
				this.userMapper);
	}
	
	public void deleteAll(){
		this.jdbcTemplate.update("delete from users");
	}
	

	
	 
	public int getCount() {
		return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
	}
	
	
	public List<User> getAll(){
		return this.jdbcTemplate.query("select * from users order by id", 
				this.userMapper);
	}
}

