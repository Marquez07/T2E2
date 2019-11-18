package ejercicio2;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
 
public class Ejercicio2 extends Application {
 
    private final TableView<Persona> table = new TableView<>();
    private final ObservableList<Persona> data =
            FXCollections.observableArrayList(
            new Persona("Jacob", "Smith", "jacob.smith@example.com"),
            new Persona("Isabella", "Johnson", "isabella.johnson@example.com"),
            new Persona("Ethan", "Williams", "ethan.williams@example.com"),
            new Persona("Emma", "Jones", "emma.jones@example.com"),
            new Persona("Michael", "Brown", "michael.brown@example.com"));
    final HBox hb = new HBox();
 
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Tablas");
        stage.setWidth(450);
        stage.setHeight(550);
 
        final Label titulo = new Label("AGENDA");
        titulo.setFont(new Font("Arial Black", 20));
 
        table.setEditable(true);
        
        Callback<TableColumn<Persona, String>, 
            TableCell<Persona, String>> cellFactory
                = (TableColumn<Persona, String> p) -> new EditingCell();
 
        TableColumn<Persona, String> nombre = 
            new TableColumn<>("Nombre");
        TableColumn<Persona, String> apellido = 
            new TableColumn<>("Apellido");
        TableColumn<Persona, String> email = 
            new TableColumn<>("Email");
 
        nombre.setMinWidth(100);
        nombre.setCellValueFactory(
            new PropertyValueFactory<>("nombre"));
        nombre.setCellFactory(cellFactory);        
        nombre.setOnEditCommit(
            e -> {
                ((Persona) e.getTableView().getItems().get(
                        e.getTablePosition().getRow())
                        ).setNombre(e.getNewValue());
        });
 
 
        apellido.setMinWidth(100);
        apellido.setCellValueFactory(
            new PropertyValueFactory<>("apellido"));
        apellido.setCellFactory(cellFactory);
        apellido.setOnEditCommit(
            e -> {
                ((Persona) e.getTableView().getItems().get(
                        e.getTablePosition().getRow())
                        ).setApellido(e.getNewValue());
        });
 
        email.setMinWidth(200);
        email.setCellValueFactory(
            new PropertyValueFactory<>("email"));
        email.setCellFactory(cellFactory);
        email.setOnEditCommit(
            e -> {
                ((Persona) e.getTableView().getItems().get(
                        e.getTablePosition().getRow())
                        ).setEmail(e.getNewValue());
        });
 
        table.setItems(data);
        table.getColumns().addAll(nombre, apellido, email);
 
        final TextField addFirstName = new TextField();
        addFirstName.setPromptText("Nombre...");
        addFirstName.setMaxWidth(nombre.getPrefWidth());
        final TextField addLastName = new TextField();
        addLastName.setMaxWidth(apellido.getPrefWidth());
        addLastName.setPromptText("Apellido...");
        final TextField addEmail = new TextField();
        addEmail.setMaxWidth(email.getPrefWidth());
        addEmail.setPromptText("Email...");
 
        final Button boton = new Button("Añadir");
        boton.setOnAction((ActionEvent e) -> {
            data.add(new Persona(
                    addFirstName.getText(),
                    addLastName.getText(),
                    addEmail.getText()));
            addFirstName.clear();
            addLastName.clear();
            addEmail.clear();
        });
 
        hb.getChildren().addAll(addFirstName, addLastName, addEmail, boton);
        hb.setSpacing(3);
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(titulo, table, hb);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        stage.setScene(scene);
        stage.show();
    }
 
    public static class Persona {
 
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty email;
 
        private Persona(String fName, String lName, String email) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.email = new SimpleStringProperty(email);
        }
 
        public String getNombre() {
            return firstName.get();
        }
 
        public void setNombre(String fName) {
            firstName.set(fName);
        }
 
        public String getApellido() {
            return lastName.get();
        }
 
        public void setApellido(String fName) {
            lastName.set(fName);
        }
 
        public String getEmail() {
            return email.get();
        }
 
        public void setEmail(String fName) {
            email.set(fName);
        }
    }
 
    class EditingCell extends TableCell<Persona, String> {
 
        private TextField textField;
 
        public EditingCell() {
        }
 
        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }
 
        @Override
        public void cancelEdit() {
            super.cancelEdit();
 
            setText((String) getItem());
            setGraphic(null);
        }
 
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
 
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }
 
        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
            textField.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> arg0, 
                Boolean arg1, Boolean arg2) -> {
                    if (!arg2) {
                        commitEdit(textField.getText());
                    }
            });
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}