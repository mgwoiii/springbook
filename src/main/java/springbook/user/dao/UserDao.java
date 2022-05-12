package springbook.user.dao;

import java.sql.Connection;
import java.util.List;

import springbook.user.domain.User;

public interface UserDao {
	void add(Connection c, User user);
	User get(Connection c,String id);
	List<User> getAll();
	void deleteAll();
	int getCount();
	
	public void update(Connection c,User user1);
}
