package com.example.app_javafx.controller;

import com.example.app_javafx.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ToDoController implements Initializable {

    @FXML private TextField taskInput;
    @FXML private ListView<Task> taskListView;
    @FXML private ChoiceBox<String> filterChoiceBox;
    @FXML private Button addButton;
    @FXML private Button removeButton;

    private ObservableList<Task> tasks;
    private FilteredList<Task> filteredTasks;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Инициализация списков
        tasks = FXCollections.observableArrayList();
        filteredTasks = new FilteredList<>(tasks, p -> true);

        // Загрузка сохраненных задач
        loadTasks();

        // Привязка ListView к отфильтрованному списку
        taskListView.setItems(filteredTasks);

        // Настройка ChoiceBox для фильтрации
        filterChoiceBox.getItems().addAll("Все", "Выполненные", "Не выполненные");
        filterChoiceBox.setValue("Все");
        filterChoiceBox.setOnAction(e -> applyFilter(filterChoiceBox.getValue()));

        // Настройка кастомной ячейки с CheckBox
        taskListView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
            @Override
            public ListCell<Task> call(ListView<Task> param) {
                return new ListCell<Task>() {
                    private final CheckBox checkBox = new CheckBox();

                    @Override
                    protected void updateItem(Task item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            checkBox.setText(item.getDescription());
                            checkBox.setSelected(item.isDone());

                            // Обработчик изменения состояния CheckBox
                            checkBox.setOnAction(event -> {
                                item.setDone(checkBox.isSelected());
                                saveTasks();
                                applyFilter(filterChoiceBox.getValue());
                            });

                            setGraphic(checkBox);
                        }
                    }
                };
            }
        });

        // Обработка нажатия Enter в поле ввода
        taskInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addTask();
            }
        });
    }

    @FXML
    private void addTask() {
        String description = taskInput.getText().trim();

        if (!description.isEmpty()) {
            Task newTask = new Task(description);
            tasks.add(newTask);
            taskInput.clear();
            saveTasks();
            System.out.println("Задача добавлена: " + description);
        }
    }

    @FXML
    private void removeTask() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();

        if (selectedTask != null) {
            tasks.remove(selectedTask);
            saveTasks();
            System.out.println("Задача удалена: " + selectedTask.getDescription());
        }
    }

    private void applyFilter(String filterType) {
        switch (filterType) {
            case "Выполненные":
                filteredTasks.setPredicate(Task::isDone);
                break;
            case "Не выполненные":
                filteredTasks.setPredicate(task -> !task.isDone());
                break;
            default: // "Все"
                filteredTasks.setPredicate(task -> true);
                break;
        }
    }

    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("tasks.ser"))) {

            // Преобразуем ObservableList в обычный ArrayList для сериализации
            List<Task> taskList = new ArrayList<>(tasks);
            oos.writeObject(taskList);
            System.out.println("Задачи сохранены в файл tasks.ser");

        } catch (IOException e) {
            System.err.println("Ошибка сохранения задач: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadTasks() {
        File file = new File("tasks.ser");

        if (!file.exists()) {
            System.out.println("Файл tasks.ser не найден. Будет создан новый список задач.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {

            List<Task> loadedTasks = (List<Task>) ois.readObject();
            tasks.setAll(loadedTasks);
            System.out.println("Задачи загружены из файла tasks.ser");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка загрузки задач: " + e.getMessage());
        }
    }
}