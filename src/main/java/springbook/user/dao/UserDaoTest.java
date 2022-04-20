package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import junit.framework.TestCase;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@WebAppConfiguration 
@RunWith(SpringJUnit4ClassRunner.class)
// 스프링의 테스트 컨텍스트 프레임워크의 jUnit 확장기능 지정
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserDaoTest extends TestCase{

	@Autowired UserDao dao; 
	@Autowired DataSource dataSource;
	
	private User user1;
	private User user2;
	private User user3;
	
	
	@Before
	public void setUp() {

		this.user1 = new User("gyumm", "박성철", "springno1", Level.BASIC, 1, 0);
		this.user2 = new User("leegg", "이길원", "springno2", Level.SILVER, 55, 10);
		this.user3 = new User("bumjin", "박범진", "springno3", Level.GOLD, 100, 40);
		

	}
	
	@Test
	public void addAndGet() throws SQLException, Exception{
	
	
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
	
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		User userget1 = dao.get(user1.getId());
		
		//assertThat(userget1.getName() , is(user1.getName()));
		//assertThat(userget1.getPassword(), is(user1.getPassword()));
		checkSameUser(userget1, user1);
		
		User userget2 = dao.get(user2.getId());
		
		//assertThat(userget2.getName() , is(user2.getName()));
		//assertThat(userget2.getPassword(), is(user2.getPassword()));
		checkSameUser(userget2, user2);

	} 
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException{
		

		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
	
		dao.get("unknown_id");
	
	}
	
	@Test
	public void getAll() throws Exception {
		dao.deleteAll();
		
		List<User> users0 = dao.getAll();
		assertThat(users0.size(), is(0));
		
		dao.add(user1);
		List<User> users1 = dao.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1, users1.get(0));

		dao.add(user2);
		List<User> users2 = dao.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user1, users2.get(0));
		checkSameUser(user2, users2.get(1));
		

		dao.add(user3);
		List<User> users3 = dao.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user3, users3.get(0));
		checkSameUser(user1, users3.get(1));
		checkSameUser(user2, users3.get(2));
		
		
		
	}
	/*
	 * UserService는 UserDao 인터페이슈ㅡ 타입으로 userDao 빈을 Di 받아 사용하게 만든다.
	 * 대문자로 시작하는 UserDao는 인터페이스 이름이고, 소문자로 시작하는 userDao는 빈 오브젝틍
	 * 테이터 엑세스 로직이 바뀌었다고 비즈니스 로직 코드를 수정하는 일이 있어서는 안된다.
	 * 따라서 DAO의 인터페이스를 사용하고 DI를 적요해야 한다. DI를 적용하려면 당연히
	 * UserService도 스프링의 빈으로 등록되어야 한다.
	 * UserService를 위한 테스트 클래스도 하나 추가하자. 테스트 클래스의 이름은
	 * UserServiceTest로 한다. UserService 의 클래스 레벨 의존관계를 정리해보면 +++++++++++++++++++++++++++++++++++++++++++++++
	 * */
	@Test
	public void update() {
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user2);
		
		user1.setName("오민규");
		user1.setPassword("springno6");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		
		dao.update(user1);
		
		User user1update = dao.get(user1.getId());
		checkSameUser(user1, user1update);
		
		User user2same = dao.get(user2.getId());
		checkSameUser(user2, user2same);
	
	}
//	@Test(expected=DataAccessException.class)
//	public void duplciateKey() {
//		dao.deleteAll();
//		dao.add(user1);
//		dao.add(user1);
//	}
//	
//	@Test
//	public void sqlExceptionTranslate() {
//		dao.deleteAll();
//		
//		try {
//			dao.add(user1);
//			dao.add(user1);
//		}catch(DuplicateKeyException ex) {
//			SQLException sqlEx = (SQLException)ex.getRootCause();
//			SQLExceptionTranslator set =
//					new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
//			
//			assertThat(set.translate(null, null, sqlEx),
//					is(DuplicateKeyException.class));
//		}
//	}
	
	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
	}
}
