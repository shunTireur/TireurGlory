package com.example.tireurglory;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;

public class SampleCRUD extends Window implements LayoutClickListener {

	private HorizontalSplitPanel mainSplit;

	private Label lblCreate;
	private Label lblSearch;

	public SampleCRUD() {

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.addComponent(getMenu());

		mainSplit = new HorizontalSplitPanel();
		mainSplit.setSizeFull();
		mainSplit.setFirstComponent(getFirstComponent());
		mainSplit.setSecondComponent(getSecondComponent());
		layout.addComponent(mainSplit);
		layout.setExpandRatio(mainSplit, 1.0f);

		setContent(layout);
	}

	private Component getMenu() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);

		lblCreate = new Label("[[CREATE]]");
		layout.addComponent(lblCreate);

		lblSearch = new Label("[[SEARCH]]");
		layout.addComponent(lblSearch);

		layout.addListener((LayoutClickListener) this);

		return layout;
	}

	private Component getFirstComponent() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		layout.addComponent(new Label("firstComponent"));

		return layout;
	}

	private Component getSecondComponent() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		layout.addComponent(new Label("secondComponent"));

		return layout;
	}

	public void layoutClick(LayoutClickEvent event) {
		Component child = event.getChildComponent();

		if (child == lblCreate) {
			EmployeeForm form = new EmployeeForm(null);
			form.addData();
			mainSplit.setFirstComponent(form);
		} else if (child == lblSearch) {
			EmployeeSearchPanel panel = new EmployeeSearchPanel();
			mainSplit.setFirstComponent(panel);
		}
	}
}
