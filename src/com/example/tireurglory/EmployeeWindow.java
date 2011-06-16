package com.example.tireurglory;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class EmployeeWindow extends Window implements Button.ClickListener,
		Property.ValueChangeListener {

	/** �V���A���o�[�W����ID */
	private static final long serialVersionUID = 7036292358050042225L;

	/** �A�v���P�[�V�����N���X */
	private TireurgloryApplication app;

	/** �ꗗ�\���p�p�l�� */
	private Panel pnlList;
	/** �ꗗ�\���p�e�[�u�� */
	private Table table;

	/** �ǉ��{�^�� */
	private Button btnNew = new Button("�ǉ�", (Button.ClickListener) this);

	/** �ڍו\���p�p�l�� */
	private Panel pnlDetail;
	/** �ڍו\���E�ҏW�t�H�[�� */
	private EmployeeForm form;

	/**
	 * �R���X�g���N�^
	 *
	 * @param app
	 */
	public EmployeeWindow(TireurgloryApplication app) {
		this.app = app;

		setContent(getComponentContainer());
	}

	/**
	 * �R���e���c
	 *
	 * @return
	 */
	private ComponentContainer getComponentContainer() {
		VerticalLayout layout = new VerticalLayout();

		pnlList = getPnlList();
		pnlDetail = getPnlDetail();

		layout.addComponent(pnlList);
		layout.addComponent(pnlDetail);

		return layout;
	}

	/**
	 * �ꗗ�\���p�p�l���𐶐����܂��B
	 *
	 * @return
	 */
	private Panel getPnlList() {
		Panel panel = new Panel();

		VerticalLayout layout = new VerticalLayout();
		{
			// �ꗗ�\���p�e�[�u���𐶐�
			table = new Table();
			table.setSizeFull();
			table.setContainerDataSource(app.getDataSource());
			// �\������v�f�����\�b�h���Ŏw��
			table.setVisibleColumns(EmployeeContainer.NATURAL_COL_ORDER);
			// ���ۂ̕\���Ɏg�p���镶������w��
			table.setColumnHeaders(EmployeeContainer.COL_HEADERS_CAPTION);
			// �s��I���\�ɂ���
			table.setSelectable(true);
			// �f�[�^���ύX���ꂽ�ꍇ�A�`��𑦎����f
			table.setImmediate(true);
			// �f�[�^�̓��e���ύX���ꂽ���Ƃ�ʒm���郊�X�i�[��ݒ�
			table.addListener(this);
			// �����s�I��s�ɐݒ�
			table.setNullSelectionAllowed(false);
			// �\�������̂�I���\�ɂ���
			table.setColumnCollapsingAllowed(true);
			// ��̈ړ����\�ɂ���
			table.setColumnReorderingAllowed(true);

			layout.addComponent(table);

			// �ǉ��{�^��
			layout.addComponent(btnNew);
		}
		panel.setContent(layout);

		return panel;
	}

	/**
	 * �ڍ׃p�l���𐶐����܂��B
	 *
	 * @return
	 */
	private Panel getPnlDetail() {
		Panel panel = new Panel();

		VerticalLayout layout = new VerticalLayout();
		{
			form = new EmployeeForm(app);
			layout.addComponent(form);
		}
		panel.setContent(layout);

		return panel;
	}

	/**
	 * �V�K�ǉ��̏������s���܂��B
	 *
	 */
	private void addNewData() {
		setContent(getComponentContainer());
		// �V�K�ǉ����[�h���w�肷��
		form.addData();
	}

	/*
	 * (�� Javadoc)
	 *
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
	 * ClickEvent)
	 */
	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton();
		if (source == btnNew) {
			addNewData();
		}
	}

	/*
	 * (�� Javadoc)
	 *
	 * @see
	 * com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data
	 * .Property.ValueChangeEvent)
	 */
	public void valueChange(ValueChangeEvent event) {
		Property property = event.getProperty();

		// �Ăяo�������e�[�u���̏ꍇ
		if (property == table) {

			// �I�����ꂽ�f�[�^���擾
			Item item = table.getItem(table.getValue());

			// ���ݕ\�����Ă���f�[�^�ƈقȂ�ꍇ�A�ĕ`�悷��
			if (item != form.getItemDataSource()) {
				form.setItemDataSource(item);
			}
		}
	}
}
