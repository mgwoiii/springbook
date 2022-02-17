package springbook.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.domain.User;

public class UserDaoTest {

	public static void main(String[] args ) throws ClassNotFoundException, SQLException{
		
		//ConnectionMaker connectionMaker = new DConnectionMaker();
		// UserDao가 사용할 ConnectionMaker 구현 클래스를 결정하고 오브젝트를 만든다.
		
		ApplicationContext context = 
				new AnnotationConfigApplicationContext(DaoFactory.class);
		
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		//UserDao dao = new DaoFactory().userDao();
		
		
		//UserDao dao = new UserDao(connectionMaker);
		
		User user = new User();
		user.setId("whiteship2");
		user.setName("백기선");
		user.setPassword("married");
		
		dao.add(user);
		
		System.out.println("성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		
		System.out.println(user2.getId() + "조회 성공");
	}
	
	
}
