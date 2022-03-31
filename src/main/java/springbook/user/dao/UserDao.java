package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;

import springbook.user.domain.User;

public class UserDao {
	
	private DataSource dataSoucre;
	
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
		this.dataSoucre = dataSource;
	}
	
	private JdbcContext jdbcContext;
	
	
	public void add(final User user) throws SQLException{
		
		this.jdbcTemplate.update("insert into users(id, name, password) values (?, ?, ?)",	
		user.getId(), user.getName(), user.getPassword());
	}


	
	public User get(String id) throws ClassNotFoundException, SQLException {
			
		Connection c = this.dataSoucre.getConnection();

		PreparedStatement ps = c.prepareStatement(
				"select * from users where id = ?");
		
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();
		
		User user = null;
		
		if(rs.next()) {
		
			user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		}
		
		rs.close();
		ps.close();
		c.close();
		
		if(user == null) throw new EmptyResultDataAccessException(1);
		
		return user;
	}
	
	public void deleteAll(){
		this.jdbcTemplate.update("delete from users");
	}
	

	
	 
	public int getCount() {
		return this.jdbcTemplate.queryForInt("select count(*) from users");
	}
	
	public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
		
		Connection c = null;
		PreparedStatement ps = null;
		
		try {
			c = dataSoucre.getConnection();
			ps = stmt.makePreparedStatement(c);
			ps.executeUpdate();
		}catch(SQLException e) {
			throw e;
		} finally {
			if ( ps != null) { try{ ps.close(); }catch(SQLException e) {}}
			if ( c != null) { try{ c.close(); }catch(SQLException e) {}}
			
		}
		
	}
}

