package com.example.tireurglory;

import com.vaadin.Application;


public class TireurgloryApplication extends Application {

	/** シリアルバージョンID */
	private static final long serialVersionUID = -4222606611989855112L;

	/** データソース */
	private EmployeeContainer dataSource = EmployeeContainer
			.createWithTestData();

	@Override
	public void init() {

		setMainWindow(new SampleCRUD());

	}

	/**
	 * データソースを返却します。
	 *
	 * @return dataSource
	 */
	public EmployeeContainer getDataSource() {
		return dataSource;
	}
}
