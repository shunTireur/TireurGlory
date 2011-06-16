package com.example.tireurglory;

import java.io.Serializable;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.vaadin.data.util.BeanItemContainer;

/**
 * Employee �̃R���e�i�N���X�ł��B
 *
 */
public class EmployeeContainer extends BeanItemContainer<Employee> implements
		Serializable {

	/** �V���A���o�[�W����ID */
	private static final long serialVersionUID = 2300101343682963531L;

	/**
	 * �I�u�W�F�N�g�v���p�e�B (�e�[�u����t�H�[���Ŏg�p�����)
	 */
	public static final Object[] NATURAL_COL_ORDER = new Object[] {
			"firstName", "lastName" };

	/**
	 * �v���p�e�B���� (�I�u�W�F�N�g�v���p�e�B�Ə�������v������)
	 */
	public static final String[] COL_HEADERS_CAPTION = new String[] { "��", "��" };

	/**
	 * �R���X�g���N�^
	 *
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public EmployeeContainer() throws InstantiationException,
			IllegalAccessException {
		super(Employee.class);
	}

	/**
	 * �f�[�^�R���e�i��ԋp���܂��B
	 *
	 * @return �f�[�^�R���e�i
	 */
	public static EmployeeContainer createWithTestData() {
		EmployeeContainer c = null;

		try {
			c = new EmployeeContainer();

			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				pm.setDetachAllOnCommit(true);
				// Datastore����Employee�̃f�[�^��S�Ď擾����
				String query = "select from " + Employee.class.getName();
				@SuppressWarnings("unchecked")
				List<Employee> employees = (List<Employee>) pm.newQuery(query)
						.execute();
				// �R���e�i�Ƀf�[�^��ǉ�����
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
