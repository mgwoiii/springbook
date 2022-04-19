package springbook.user.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configurable
// application context or bean facetory가 사용할 설정정보라는 표시
public class DaoFactory {

	@Bean // 오브젝트 생성을 담당하는 IoC용 메소드라는 표시
	public UserDaoJDBC userDao() {
		UserDaoJDBC userDao = new UserDaoJDBC();
		userDao.setDataSource(dataSource());
		return userDao;
	}
	
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource ();

		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/springbook?serverTimezone=UTC");
		dataSource.setUsername("root");
		dataSource.setPassword("1234");

		return dataSource;
	}
}
