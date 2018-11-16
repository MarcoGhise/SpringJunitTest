package it.blog.springjunit.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class SqlDao extends JdbcDaoSupport {
	
	public SqlDao(){}
	
	public SqlDao(DataSource dataSource){
		setDataSource(dataSource);
	}
	
	public TableData getData(String firstName) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TableData> tableDatas = getJdbcTemplate().query(
				"SELECT * FROM Customers WHERE first_name= ?", new Object[] { firstName },
				new BeanPropertyRowMapper(TableData.class));
		/*
		 * Record Exists
		 */
		if (tableDatas.size()>0)
			return tableDatas.get(0);
		else
			return null;
	}
		
}
