package com.example.tireurglory;

import java.io.Serializable;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.vaadin.data.util.BeanItemContainer;

/**
 * Employee のコンテナクラスです。
 *
 */
public class EmployeeContainer extends BeanItemContainer<Employee> implements
		Serializable {

	/** シリアルバージョンID */
	private static final long serialVersionUID = 2300101343682963531L;

	/**
	 * オブジェクトプロパティ (テーブルやフォームで使用される)
	 */
	public static final Object[] NATURAL_COL_ORDER = new Object[] {
			"firstName", "lastName" };

	/**
	 * プロパティ名称 (オブジェクトプロパティと順序を一致させる)
	 */
	public static final String[] COL_HEADERS_CAPTION = new String[] { "名", "姓" };

	/**
	 * コンストラクタ
	 *
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public EmployeeContainer() throws InstantiationException,
			IllegalAccessException {
		super(Employee.class);
	}

	/**
	 * データコンテナを返却します。
	 *
	 * @return データコンテナ
	 */
	public static EmployeeContainer createWithTestData() {
		EmployeeContainer c = null;

		try {
			c = new EmployeeContainer();

			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				pm.setDetachAllOnCommit(true);
				// DatastoreからEmployeeのデータを全て取得する
				String query = "select from " + Employee.class.getName();
				@SuppressWarnings("unchecked")
				List<Employee> employees = (List<Employee>) pm.newQuery(query)
						.execute();
				// コンテナにデータを追加する
				c.addAll(employees);
			} finally {
				pm.close();
			}

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return c;
	}

}
