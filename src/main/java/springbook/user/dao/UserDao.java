package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.domain.User;

public class UserDao {
	
	private ConnectionMaker connectionMaker;

	public UserDao(ConnectionMaker connectionMaker) {
	//	this.connectionMaker = connectionMaker;
	
		AnnotationConfigApplicationContext context = 
				new AnnotationConfigApplicationContext(DaoFactory.class);
		
		this.connectionMaker = context.getBean("connectionMaker", ConnectionMaker.class);
	}
	
	public void add(User user) throws ClassNotFoundException, SQLException {

		Connection c = connectionMaker.makeConnection();
		
		// 인터페이스에 정의된 메소드를 사용하므로 클래스가 바뀌다고 해도 메소드 이름이 변경될 걱정은 없다.
		
		
		PreparedStatement ps = c.prepareStatement(
				"insert into users(id, name, password) values(?,?,?)");
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}

	
	public User get(String id) throws ClassNotFoundException, SQLException {
		
//		Class.forName("com.mysql.cj.jdbc.Driver");
//		Connection c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/springbook?serverTimezone=UTC", "root", "1234");

		
		Connection c = connectionMaker.makeConnection();

		
		PreparedStatement ps = c.prepareStatement(
				"select * from users where id = ?");
		
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		
		rs.close();
		ps.close();
		c.close();
		
		return user;
	}

}
