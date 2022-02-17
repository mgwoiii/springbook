package springbook.user.dao;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
// application context or bean facetory가 사용할 설정정보라는 표시
public class DaoFactory {

	@Bean // 오브젝트 생성을 담당하는 IoC용 메소드라는 표시
	public UserDao userDao() {
//		ConnectionMaker connectionMaker = new DConnectionMaker();
//		UserDao userDao = new UserDao(connectionMaker);
//		
//		return userDao;
		
		return new UserDao(connectionMaker());
	}
	
	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
	
}
