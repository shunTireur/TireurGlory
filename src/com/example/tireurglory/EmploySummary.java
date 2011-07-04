package com.example.tireurglory;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class EmploySummary extends Panel {

	private Employee emp;

	public EmploySummary(Employee emp) {
		this.emp = emp;

		HorizontalLayout layout = new HorizontalLayout();

		layout.addComponent(new Label(emp.getFirstName()));

		layout.addComponent(new Label(emp.getLastName()));

		setContent(layout);
	}

	public Employee getEmployee() {
		return emp;
	}

}
