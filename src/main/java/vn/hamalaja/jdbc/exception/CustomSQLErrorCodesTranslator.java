package vn.hamalaja.jdbc.exception;

import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

/**
 * @author lamhm
 *
 */
public class CustomSQLErrorCodesTranslator extends SQLErrorCodeSQLExceptionTranslator {

	@Override
	protected DataAccessException customTranslate(String task, String sql, SQLException sqlex) {
		System.out.println(String.format("[ERROR] [task:%s] [sql:%s] [errorCode:%d]", task, sql, sqlex.getErrorCode()));
		if (sqlex.getErrorCode() == -12345) {
			return new DeadlockLoserDataAccessException(task, sqlex);
		}

		return null;
	}

}
