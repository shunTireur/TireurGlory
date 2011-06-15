package com.example.tireurglory;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.vaadin.Application;
import com.vaadin.ui.*;

public class TireurgloryApplication extends Application {
	/** シリアルバージョンID */
	private static final long serialVersionUID = -4222606611989855112L;

	@Override
	public void init() {

		Employee employee = new Employee("Alfred", "Smith", new Date());
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			pm.makePersistent(employee);
		} finally {
			pm.close();
		}

		Window mainWindow = new Window("TireurGlory Application");

		VerticalLayout layout = new VerticalLayout();
		{
			Label label = new Label("Hello Vaadin user");
			layout.addComponent(label);

			try {
				pm = PMF.get().getPersistenceManager();
				String query = "select from " + Employee.class.getName()
						+ " where lastName == 'Smith'";
				@SuppressWarnings("unchecked")
				List<Employee> employees = (List<Employee>) pm.newQuery(query)
						.execute();
				layout.addComponent(new Label(String.valueOf(employees.size())));
				for (Employee emp : employees) {
					layout.addComponent(new Label(emp.getFirstName() + " "
							+ emp.getLastName() + ":" + emp.getHireDate()));
				}
			} finally {
				pm.close();
			}

		}
		mainWindow.addComponent(layout);

		setMainWindow(mainWindow);
	}
}
