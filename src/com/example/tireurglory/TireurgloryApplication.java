package com.example.tireurglory;

import com.vaadin.Application;


public class TireurgloryApplication extends Application {

	/** �V���A���o�[�W����ID */
	private static final long serialVersionUID = -4222606611989855112L;

	/** �f�[�^�\�[�X */
	private EmployeeContainer dataSource = EmployeeContainer
			.createWithTestData();

	@Override
	public void init() {

		setMainWindow(new SampleCRUD());

	}

	/**
	 * �f�[�^�\�[�X��ԋp���܂��B
	 *
	 * @return dataSource
	 */
	public EmployeeContainer getDataSource() {
		return dataSource;
	}
}
