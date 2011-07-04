package com.example.tireurglory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.data.util.BeanItem;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class EmployeeSearchPanel extends Panel implements Serializable, LayoutClickListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 7330270602284838727L;

	private VerticalSplitPanel splitPanel;

	private TextField txtFirstName;
	private Label lblSearch;

	private List<EmploySummary> lblList = new ArrayList<EmploySummary>();

	private VerticalLayout searchResult;

	private EmployeeContainer dataSource = EmployeeContainer.createWithTestData();

	public EmployeeSearchPanel() {

		setSizeFull();

		splitPanel = new VerticalSplitPanel();
		splitPanel.setSizeFull();

		splitPanel.setFirstComponent(getSearchCond());

		setContent(splitPanel);
	}

	private Component getSearchCond() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();

		txtFirstName = new TextField();
		layout.addComponent(txtFirstName);

		lblSearch = new Label("[[search]]");
		layout.addComponent(lblSearch);

		layout.addListener((LayoutClickListener) this);

		return layout;
	}

	public void layoutClick(LayoutClickEvent event) {
		Component child = event.getChildComponent();

		if (child == lblSearch) {

			String firstNameVal = (String) txtFirstName.getValue();

			dataSource.removeAllContainerFilters();
			dataSource.addContainerFilter("firstName", firstNameVal, true, false);

			reflesh();

			return;
		}

		for (EmploySummary panel : lblList) {
			if (child == panel) {
				VerticalLayout layout = new VerticalLayout();

				Button btnBack = new Button("–ß‚é");
				btnBack.addListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						reflesh();
						setContent(splitPanel);
					}
				});
				layout.addComponent(btnBack);

				Employee emp = panel.getEmployee();

				BeanItem<Employee> item = dataSource.getItem(emp);

				EmployeeForm form = new EmployeeForm(this);
				form.setItemDataSource(item);
				layout.addComponent(form);

				setContent(layout);
			}
		}
	}

	private void reflesh() {
		lblList.clear();

		searchResult = new VerticalLayout();

		Collection<Employee> list = dataSource.getItemIds();
		for (Employee emp : list) {
			EmploySummary panel = new EmploySummary(emp);
			searchResult.addComponent(panel);
			lblList.add(panel);
		}
		searchResult.addListener((LayoutClickListener) this);

		splitPanel.setSecondComponent(searchResult);
	}

	public EmployeeContainer getDataSource() {
		return dataSource;
	}
}
