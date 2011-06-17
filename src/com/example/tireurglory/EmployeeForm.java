package com.example.tireurglory;

import java.util.Arrays;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.vaadin.addon.beanvalidation.BeanValidationForm;
import com.vaadin.data.Item;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class EmployeeForm extends BeanValidationForm<Employee> implements
		ClickListener {

	/** �V���A���o�[�W����ID */
	private static final long serialVersionUID = -7256417175627382941L;

	/** �A�v���P�[�V�����N���X */
	private TireurgloryApplication app;

	/** �ۑ��{�^�� */
	private Button save = new Button("�ۑ�", (ClickListener) this);
	/** �L�����Z���{�^�� */
	private Button cancel = new Button("�L�����Z��", (ClickListener) this);
	/** �ҏW�{�^�� */
	private Button edit = new Button("�ҏW", (ClickListener) this);
	/** �폜�{�^�� */
	private Button delete = new Button("�폜", (ClickListener) this);

	/** �V�K�ǉ����[�h�t���O */
	private boolean newContactMode = false;
	/** �V�K�ǉ����s�Ȃ��ꍇ�Ɏg�p����f�[�^�I�u�W�F�N�g */
	private Employee newEmployee = null;

	/**
	 * �R���X�g���N�^
	 *
	 * @param beanClass
	 */
	private EmployeeForm(Class<Employee> beanClass) {
		super(beanClass);
	}

	/**
	 * �R���X�g���N�^
	 *
	 * @param app
	 */
	public EmployeeForm(TireurgloryApplication app) {
		super(Employee.class);

		this.app = app;

		// �������f���s�Ȃ�Ȃ��B�ҏW�̓o�b�t�@�����O���[�h��
		setImmediate(true);
		setWriteThrough(false);

		// �{�^����z�u
		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(true);
		footer.addComponent(save);
		footer.addComponent(cancel);
		footer.addComponent(edit);
		footer.addComponent(delete);
		footer.setVisible(false);

		setFooter(footer);

		// �ڍ׉�ʐ������[�����J�X�^�}�C�Y
		setFormFieldFactory(new DefaultFieldFactory() {
			/** �V���A���o�[�W����ID */
			private static final long serialVersionUID = -8497110002821579568L;

			@Override
			public Field createField(Item item, Object propertyId,
					Component uiContext) {
				Field field = super.createField(item, propertyId, uiContext);

				// �L���v�V�����̐ݒ�
				List<Object> list = Arrays
						.asList(EmployeeContainer.NATURAL_COL_ORDER);
				int idx = list.indexOf(propertyId);
				String caption = EmployeeContainer.COL_HEADERS_CAPTION[idx];
				field.setCaption(caption);

				// �����\���Łunull�v�ƕ\�����Ȃ��悤�A�󔒂�ݒ肷��B
				if (field instanceof TextField) {
					((TextField) field).setNullRepresentation("");
				}

				return field;
			}
		});
	}

	/**
	 * �V�K�o�^�p�f�[�^��ݒ肵�܂��B
	 */
	public void addData() {
		newEmployee = new Employee();
		setItemDataSource(new BeanItem<Employee>(newEmployee));

		// �V�K�ǉ����[�h��true��
		newContactMode = true;
		setReadOnly(false);
	}


	/*
	 * (�� Javadoc)
	 *
	 * @see
	 * com.vaadin.addon.beanvalidation.BeanValidationForm#setItemDataSource(
	 * com.vaadin.data.Item)
	 */
	@Override
	public void setItemDataSource(Item newDataSource) {

		// �V�K�ǉ��t���O��������
		newContactMode = false;

		if (newDataSource != null) {

			// �e�[�u���̕\���Ɠ������A�f�[�^��\�����鏇�Ԃ��`����
			List<Object> orderedProperties = Arrays
					.asList(EmployeeContainer.NATURAL_COL_ORDER);
			super.setItemDataSource(newDataSource, orderedProperties);
			setReadOnly(true);
			getFooter().setVisible(true);
		} else {
			super.setItemDataSource(null);
			getFooter().setVisible(false);
		}
	}

	/*
	 * (�� Javadoc)
	 *
	 * @see com.vaadin.ui.Form#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);

		// �ۑ��E�L�����Z���E�ҏW�{�^���̏�Ԃ�ύX
		save.setVisible(!readOnly);
		cancel.setVisible(!readOnly);
		edit.setVisible(readOnly);
		delete.setVisible(readOnly);
	}

	/*
	 * (�� Javadoc)
	 *
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
	 * ClickEvent)
	 */
	public void buttonClick(ClickEvent event) {
		Button source = event.getButton();

		if (source == save) { // ���ۑ��������ꂽ�Ƃ�

			try {
				// ���͒l���؂��s��
				validate();

				// �f�[�^���m�肷��
				commit();

				// �f�[�^�X�g�A���X�V
				@SuppressWarnings("unchecked")
				BeanItem<Employee> item = (BeanItem<Employee>) getItemDataSource();
				Employee employee = (Employee) item.getBean();
				PersistenceManager pm = PMF.get().getPersistenceManager();
				try {
					pm.makePersistent(employee);
				} finally {
					pm.close();
				}

				// �V�K�ǉ����[�h�̏ꍇ�́A�e�[�u���̃R���e�i�ɒǉ����ꂽ�f�[�^�𔽉f���A
				// �������g���ێ����Ă���f�[�^�Ɠ������Ƃ�
				if (newContactMode) {
					Item addedItem = app.getDataSource().addItem(newEmployee);
					setItemDataSource(addedItem);
					newContactMode = false;
				}

				// �ǂݎ���p�ɂ���
				setReadOnly(true);

			} catch (InvalidValueException e) {

				// �G���[���b�Z�[�W����ʂɕ\������
				getWindow().showNotification(e.getMessage(),
						Notification.TYPE_WARNING_MESSAGE);

			}

		} else if (source == cancel) { // ���L�����Z���������ꂽ�Ƃ�

			// �ҏW���e��j�����A�ǂݎ���p��
			discard();
			setReadOnly(true);

		} else if (source == edit) { // ���ҏW�������ꂽ�Ƃ�

			// �ǂݎ���p���������A���e��ҏW�\�Ƃ���B
			setReadOnly(false);

		} else if (source == delete) { // ���폜�������ꂽ�Ƃ�

			// �f�[�^�X�g�A����폜
			@SuppressWarnings("unchecked")
			BeanItem<Employee> item = (BeanItem<Employee>) getItemDataSource();
			Employee employee = (Employee) item.getBean();
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				Object obj = pm.getObjectById(Employee.class, employee.getId());
				pm.deletePersistent(obj);
			} finally {
				pm.close();
			}

			app.getDataSource().removeItem(employee);
			setItemDataSource(null);
		}
	}

}
