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

	/** シリアルバージョンID */
	private static final long serialVersionUID = 7036292358050042225L;

	/** アプリケーションクラス */
	private TireurgloryApplication app;

	/** 一覧表示用パネル */
	private Panel pnlList;
	/** 一覧表示用テーブル */
	private Table table;

	/** 追加ボタン */
	private Button btnNew = new Button("追加", (Button.ClickListener) this);

	/** 詳細表示用パネル */
	private Panel pnlDetail;
	/** 詳細表示・編集フォーム */
	private EmployeeForm form;

	/**
	 * コンストラクタ
	 *
	 * @param app
	 */
	public EmployeeWindow(TireurgloryApplication app) {
		this.app = app;

		setContent(getComponentContainer());
	}

	/**
	 * コンテンツ
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
	 * 一覧表示用パネルを生成します。
	 *
	 * @return
	 */
	private Panel getPnlList() {
		Panel panel = new Panel();

		VerticalLayout layout = new VerticalLayout();
		{
			// 一覧表示用テーブルを生成
			table = new Table();
			table.setSizeFull();
			table.setContainerDataSource(app.getDataSource());
			// 表示する要素をメソッド名で指定
			table.setVisibleColumns(EmployeeContainer.NATURAL_COL_ORDER);
			// 実際の表示に使用する文字列を指定
			table.setColumnHeaders(EmployeeContainer.COL_HEADERS_CAPTION);
			// 行を選択可能にする
			table.setSelectable(true);
			// データが変更された場合、描画を即時反映
			table.setImmediate(true);
			// データの内容が変更されたことを通知するリスナーを設定
			table.addListener(this);
			// 複数行選択不可に設定
			table.setNullSelectionAllowed(false);
			// 表示する列のを選択可能にする
			table.setColumnCollapsingAllowed(true);
			// 列の移動を可能にする
			table.setColumnReorderingAllowed(true);

			layout.addComponent(table);

			// 追加ボタン
			layout.addComponent(btnNew);
		}
		panel.setContent(layout);

		return panel;
	}

	/**
	 * 詳細パネルを生成します。
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
	 * 新規追加の処理を行います。
	 *
	 */
	private void addNewData() {
		setContent(getComponentContainer());
		// 新規追加モードを指定する
		form.addData();
	}

	/*
	 * (非 Javadoc)
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
	 * (非 Javadoc)
	 *
	 * @see
	 * com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data
	 * .Property.ValueChangeEvent)
	 */
	public void valueChange(ValueChangeEvent event) {
		Property property = event.getProperty();

		// 呼び出し元がテーブルの場合
		if (property == table) {

			// 選択されたデータを取得
			Item item = table.getItem(table.getValue());

			// 現在表示しているデータと異なる場合、再描画する
			if (item != form.getItemDataSource()) {
				form.setItemDataSource(item);
			}
		}
	}
}
