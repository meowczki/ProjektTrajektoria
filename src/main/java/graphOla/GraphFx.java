package graphOla;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class GraphFx extends Application {

    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/fxml/graph.fxml"));
        Scene scene=new Scene(root,800,600);
        stage.setTitle("Trajektoria planety");
        stage.setScene(scene);
        stage.show();
    }
}
