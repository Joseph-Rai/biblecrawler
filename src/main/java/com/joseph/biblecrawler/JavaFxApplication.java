package com.joseph.biblecrawler;

import com.joseph.biblecrawler.controller.MainFxController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;
    public static final String PROGRAM_VER = "Bible Crawler Ver 4.2";
    public static final String PROGRAM_AUTHOR = "Joseph Rai";
    public static final String PROGRAM_COPYRIGHT = "World Mission Society Church of God";
    public static final String PROGRAM_LAST_MODIFIED = "11 Jan 2022";

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = new SpringApplicationBuilder()
                .sources(BiblecrawlerApplication.class)
                .run(args);
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(MainFxController.class);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(MainFxController.class.getResource("Stylesheet.css").toExternalForm());
        stage.setTitle(PROGRAM_VER);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}