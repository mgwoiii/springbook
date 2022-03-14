package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.domain.User;

public class UserDao {
	
	private DataSource dataSoucre;

	public void setDataSource(DataSource dataSource) {
		this.dataSoucre = dataSource;
	}

	public void add(User user) throws ClassNotFoundException, SQLException {

		StatementStrategy st = new AddStatement(user);
		jdbcContextWithStatementStrategy(st);
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
	
	public void deleteAll() throws SQLException{
		
		StatementStrategy st = new DeleteAllStatement();
		// 선정한 전략 클래스의 오브젝트 생성
		
		jdbcContextWithStatementStrategy(st);
		// 컨텍스트 호출 전략 오브젝트 전달
		
	}
	
	private PreparedStatement makeStatement(Connection c) throws SQLException {
		PreparedStatement ps;
		ps= c.prepareStatement("delete from users");
		
		return ps;
	}
	 
	public int getCount() throws SQLException {
		
		Connection c =  null;
		PreparedStatement ps =  null;
		ResultSet rs = null;
		
		try {
			c = dataSoucre.getConnection();
			ps = c.prepareStatement("select count(*) from users");
		
			rs = ps.executeQuery();
			rs.next();
			
			return rs.getInt(1);
		
		}catch(SQLException e) {
			throw e;
		}finally {
			
			if(rs != null) {
				try {
					rs.close();
				}catch(SQLException e) {
					
				}
			}

			if(ps != null) {
				try {
					ps.close();
				}catch(SQLException e) {
					
				}
			}
			
			if(c != null) {
				try {
					c.close();
				}catch(SQLException e) {
					
				}
			}
		}
		
		

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

