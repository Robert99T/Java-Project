package org.example;
import javax.swing.*;
import java.sql.*;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksManagement implements Serializable {

   private Map<Employee, List<Task>> employeeMap;
   private Connection connection;


   public TasksManagement(){
       this.employeeMap = new HashMap<>();
       this.connection = DatabaseConnection.getConnection();
       loadEmployeesFromDatabase();
   }

    private void loadEmployeesFromDatabase() {
        String employeeQuery = "SELECT * FROM employees";
        String taskQuery = "SELECT * FROM tasks";
        try (Statement stmt = connection.createStatement();
             ResultSet rsEmp = stmt.executeQuery(employeeQuery)) {
            while (rsEmp.next()) {
                int id = rsEmp.getInt("idEmployee");
                String name = rsEmp.getString("name");
                Employee emp = new Employee(id, name);
                employeeMap.put(emp, new ArrayList<>());
            }
            try (Statement stmtTask = connection.createStatement();
                 ResultSet rsTask = stmtTask.executeQuery(taskQuery)) {

                while (rsTask.next()) {
                    int taskId = rsTask.getInt("idTask");
                    int empId = rsTask.getInt("employee_id");
                    String status = rsTask.getString("statusTask");
                    int startHour = rsTask.getInt("startHour");
                    int endHour = rsTask.getInt("endHour");
                    String typeTask = rsTask.getString("taskType");

                    Task task;
                    if ("SimpleTask".equals(typeTask)) {
                        task = new SimpleTask(taskId, status, startHour, endHour);
                    } else {
                        task = new ComplexTask(taskId, status, startHour, endHour);
                    }
                    for (Employee emp : employeeMap.keySet()) {
                        if (emp.getIdEmployee() == empId) {
                            employeeMap.get(emp).add(task);}
                    }}}
        } catch (SQLException e) {
            System.err.println("Eroare la încărcarea datelor: " + e.getMessage());
        }}

    public void addEmployee(String name) {
        String query = "INSERT INTO employees (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                Employee emp = new Employee(id, name);
                employeeMap.put(emp, new ArrayList<>());
            }
            System.out.println("Angajat adaugat: " + name);
        } catch (SQLException e) {
            System.err.println("Eroare la adaugarea angajatului: " + e.getMessage());
        }
    }

    public void assignTaskToEmployee(int idEmployee, Task task) {
        String query = "INSERT INTO tasks (idTask, employee_id, statusTask, startHour, endHour, taskType) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, task.getIdTask());
            statement.setInt(2, idEmployee);
            statement.setString(3, task.getStatusTask());

            if (task instanceof SimpleTask) {
                SimpleTask simpleTask = (SimpleTask) task;
                statement.setInt(4, simpleTask.getStartHour());
                statement.setInt(5, simpleTask.getEndHour());
                statement.setString(6, "SimpleTask");
            } else if (task instanceof ComplexTask) {
                ComplexTask complexTask = (ComplexTask) task;
                statement.setInt(4, complexTask.getStartHour());
                statement.setInt(5, complexTask.getEndHour());
                statement.setString(6, "ComplexTask");
            }

            statement.executeUpdate();

            for (Employee emp : employeeMap.keySet()) {
                if (emp.getIdEmployee() == idEmployee) {
                    employeeMap.get(emp).add(task);
                }
            }
            System.out.println("Task-ul " + task.getIdTask() + " este atribuit angajatului " + idEmployee);
        } catch (SQLException e) {
            System.out.println("Eroare la adaugarea task-ului: " + e.getMessage());
        }
    }

    public int calculateEmployeeWorkDuration(int idEmployee) {
       int totalDuration = 0;

       String query = "SELECT startHour, endHour FROM tasks WHERE employee_id = ?";
       try (PreparedStatement stmt = connection.prepareStatement(query)) {
           stmt.setInt(1, idEmployee);
           ResultSet rs = stmt.executeQuery();

           while (rs.next()) {
               int startHour = rs.getInt("startHour");
               int endHour = rs.getInt("endHour");
               totalDuration += (endHour - startHour);
           }

       } catch (SQLException e) {
           System.err.println("Eroare la calculul duratei: " + e.getMessage());
       }

       return totalDuration;
   }

   public void modifyTaskStatus(int idEmployee, int idTask, String completed) {
       String query = "UPDATE tasks SET statusTask = ? WHERE idTask = ? AND employee_id = ?";
       try (PreparedStatement stmt = connection.prepareStatement(query)) {
           JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Pending", "In Progress", "Completed"});
           String newStatus = (String) statusCombo.getSelectedItem();
           stmt.setString(1, newStatus);
           stmt.setInt(2, idTask);
           stmt.setInt(3, idEmployee);
           stmt.executeUpdate();

           for (Employee emp : employeeMap.keySet()) {
               if (emp.getIdEmployee() == idEmployee) {
                   for (Task task : employeeMap.get(emp)) {
                       if (task.getIdTask() == idTask) {
                           task.setStatusTask(newStatus);
                       }
                   }
               }
           }

           System.out.println("Statusul taskului " + idTask + " modificat la: " + newStatus);
       } catch (SQLException e) {
           System.err.println("Eroare la modificarea statusului taskului: " + e.getMessage());
       }

   }

    public void showEmployeesAndTasks() {
        for (Map.Entry<Employee, List<Task>> entry : employeeMap.entrySet()) {
            Employee emp = entry.getKey();
            System.out.println("Angajat: " + emp.getName() + " (ID: " + emp.getIdEmployee() + ")");
            for (Task task : entry.getValue()) {
                System.out.println("  - Task ID: " + task.getIdTask() + " | Status: " + task.getStatusTask());
            }
        }
    }

    public String getAllDataString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Employee, List<Task>> entry : employeeMap.entrySet()) {
            sb.append("Employee: ").append(entry.getKey().getName()).append("\n");
            for (Task task : entry.getValue()) {
                sb.append("Task ").append(task.getIdTask()).append(" - ").append(task.getStatusTask()).append("\n");
            }
        }
        return sb.toString();
    }

   public void close() {
       DatabaseConnection.closeConnection();
   }

}
