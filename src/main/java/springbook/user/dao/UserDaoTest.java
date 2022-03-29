package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
// 스프링의 테스트 컨텍스트 프레임워크의 jUnit 확장기능 지정
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserDaoTest {

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	UserDao dao; 
	
	private User user1;
	private User user2;
	private User user3;
	
	
	@Before
	public void setUp() {
		

	
		this.dao = this.context.getBean("userDao", UserDao.class);
		
		this.user1 = new User("gyumm", "박성철", "springno1");
		this.user2 = new User("leegg", "이길원", "springno2");
		//this.user3 = new User("bukk", "박범진", "springno3");
		

		System.out.println(this.context);
		System.out.println(this);
	}
	
	@Test
	public void addAnxdGet() throws SQLException, Exception{
	
	
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
	
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		User userget1 = dao.get(user1.getId());
		
		assertThat(userget1.getName() , is(user1.getName()));
		assertThat(userget1.getPassword(), is(user1.getPassword()));
		
		
		User userget2 = dao.get(user2.getId());
		
		assertThat(userget2.getName() , is(user2.getName()));
		assertThat(userget2.getPassword(), is(user2.getPassword()));
		
	} 
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException{
		

		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
	
		dao.get("unknown_id");
	
	}
}
