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

	/** シリアルバージョンID */
	private static final long serialVersionUID = -7256417175627382941L;

	/** アプリケーションクラス */
	private TireurgloryApplication app;

	/** 保存ボタン */
	private Button save = new Button("保存", (ClickListener) this);
	/** キャンセルボタン */
	private Button cancel = new Button("キャンセル", (ClickListener) this);
	/** 編集ボタン */
	private Button edit = new Button("編集", (ClickListener) this);
	/** 削除ボタン */
	private Button delete = new Button("削除", (ClickListener) this);

	/** 新規追加モードフラグ */
	private boolean newContactMode = false;
	/** 新規追加を行なう場合に使用するデータオブジェクト */
	private Employee newEmployee = null;

	/**
	 * コンストラクタ
	 *
	 * @param beanClass
	 */
	private EmployeeForm(Class<Employee> beanClass) {
		super(beanClass);
	}

	/**
	 * コンストラクタ
	 *
	 * @param app
	 */
	public EmployeeForm(TireurgloryApplication app) {
		super(Employee.class);

		this.app = app;

		// 即時反映を行なわない。編集はバッファリングモードで
		setImmediate(true);
		setWriteThrough(false);

		// ボタンを配置
		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(true);
		footer.addComponent(save);
		footer.addComponent(cancel);
		footer.addComponent(edit);
		footer.addComponent(delete);
		footer.setVisible(false);

		setFooter(footer);

		// 詳細画面生成ルールをカスタマイズ
		setFormFieldFactory(new DefaultFieldFactory() {
			/** シリアルバージョンID */
			private static final long serialVersionUID = -8497110002821579568L;

			@Override
			public Field createField(Item item, Object propertyId,
					Component uiContext) {
				Field field = super.createField(item, propertyId, uiContext);

				// キャプションの設定
				List<Object> list = Arrays
						.asList(EmployeeContainer.NATURAL_COL_ORDER);
				int idx = list.indexOf(propertyId);
				String caption = EmployeeContainer.COL_HEADERS_CAPTION[idx];
				field.setCaption(caption);

				// 初期表示で「null」と表示しないよう、空白を設定する。
				if (field instanceof TextField) {
					((TextField) field).setNullRepresentation("");
				}

				return field;
			}
		});
	}

	/**
	 * 新規登録用データを設定します。
	 */
	public void addData() {
		newEmployee = new Employee();
		setItemDataSource(new BeanItem<Employee>(newEmployee));

		// 新規追加モードをtrueに
		newContactMode = true;
		setReadOnly(false);
	}


	/*
	 * (非 Javadoc)
	 *
	 * @see
	 * com.vaadin.addon.beanvalidation.BeanValidationForm#setItemDataSource(
	 * com.vaadin.data.Item)
	 */
	@Override
	public void setItemDataSource(Item newDataSource) {

		// 新規追加フラグを初期化
		newContactMode = false;

		if (newDataSource != null) {

			// テーブルの表示と同じく、データを表示する順番を定義する
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
	 * (非 Javadoc)
	 *
	 * @see com.vaadin.ui.Form#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);

		// 保存・キャンセル・編集ボタンの状態を変更
		save.setVisible(!readOnly);
		cancel.setVisible(!readOnly);
		edit.setVisible(readOnly);
		delete.setVisible(readOnly);
	}

	/*
	 * (非 Javadoc)
	 *
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
	 * ClickEvent)
	 */
	public void buttonClick(ClickEvent event) {
		Button source = event.getButton();

		if (source == save) { // ●保存が押されたとき

			try {
				// 入力値検証を行う
				validate();

				// データを確定する
				commit();

				// データストアを更新
				@SuppressWarnings("unchecked")
				BeanItem<Employee> item = (BeanItem<Employee>) getItemDataSource();
				Employee employee = (Employee) item.getBean();
				PersistenceManager pm = PMF.get().getPersistenceManager();
				try {
					pm.makePersistent(employee);
				} finally {
					pm.close();
				}

				// 新規追加モードの場合は、テーブルのコンテナに追加されたデータを反映し、
				// 自分自身が保持しているデータと同期をとる
				if (newContactMode) {
					Item addedItem = app.getDataSource().addItem(newEmployee);
					setItemDataSource(addedItem);
					newContactMode = false;
				}

				// 読み取り専用にする
				setReadOnly(true);

			} catch (InvalidValueException e) {

				// エラーメッセージを画面に表示する
				getWindow().showNotification(e.getMessage(),
						Notification.TYPE_WARNING_MESSAGE);

			}

		} else if (source == cancel) { // ●キャンセルが押されたとき

			// 編集内容を破棄し、読み取り専用に
			discard();
			setReadOnly(true);

		} else if (source == edit) { // ●編集が押されたとき

			// 読み取り専用を解除し、内容を編集可能とする。
			setReadOnly(false);

		} else if (source == delete) { // ●削除が押されたとき

			// データストアから削除
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
