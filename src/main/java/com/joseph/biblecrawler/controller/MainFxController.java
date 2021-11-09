package com.joseph.biblecrawler.controller;

import com.joseph.biblecrawler.JavaFxApplication;
import com.joseph.biblecrawler.crawler.Crawler;
import com.joseph.biblecrawler.crawler.HolyBibleCrawler;
import com.joseph.biblecrawler.crawler.ResurseCrestineCrawler;
import com.joseph.biblecrawler.crawler.YouVersionBibleCrawler;
import com.joseph.biblecrawler.model.TMX;
import com.joseph.biblecrawler.model.URL;
import com.joseph.biblecrawler.repository.UrlRepository;
import com.joseph.biblecrawler.util.CreateTextFile;
import com.joseph.biblecrawler.util.ExcelExporter;
import com.joseph.biblecrawler.util.ExcelImporter;
import com.joseph.biblecrawler.util.TMXCreator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@FxmlView("MainFx.fxml")
public class MainFxController {

    @FXML
    private MenuItem menuAbout;

    @FXML
    private Tab tabCreateExcelSource;

    @FXML
    private TextField txtFolderPath;

    @FXML
    private Button btnSearchFolder;

    @FXML
    private ComboBox<String> cboHtmlSourceURL;

    @FXML
    private Button btnCreateTextTemplate;

    @FXML
    private Button btnCreateExcelSource;

    @FXML
    private Tab tabCreateBibleTMX;

    @FXML
    private TextField txtSourceBible;

    @FXML
    private TextField txtTargetBible;

    @FXML
    private Button btnSearchSourceBible;

    @FXML
    private Button btnSearchTargetBible;

    @FXML
    private Button btnCreateTMX;

    private ObservableList<String> observableUrlList = FXCollections.observableArrayList();

    private static final String DESKTOP_PATH = System.getProperty("user.home") + "\\Desktop";

    private final UrlRepository uriRepository;
    private final ExcelImporter importer;
    private final TMXCreator tmxCreator;

    @Autowired
    public MainFxController(UrlRepository uriRepository, ExcelImporter importer, TMXCreator tmxCreator) {
        this.importer = importer;
        this.uriRepository = uriRepository;
        this.tmxCreator = tmxCreator;
        List<URL> urls = uriRepository.findAll();
        for (URL url : urls) {
            observableUrlList.add(url.toString());
        }
    }

    @FXML
    void clickMenuClose(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void clickMenuAbout(ActionEvent event) {
        String title = JavaFxApplication.PROGRAM_VER;
        String header = "" +
                "Program Author: " + JavaFxApplication.PROGRAM_AUTHOR +
                "\n\nCopy Right: " + JavaFxApplication.PROGRAM_COPYRIGHT +
                "\n\nLast Modified Date: " + JavaFxApplication.PROGRAM_LAST_MODIFIED;
        String msg = "" +
                "This program parses information from Bible websites to help create Bible TMX files.\n\n" +
                "Since parsing can cause copyright disputes, users are legally responsible for using the program.";
        showMsgbox(title, header, msg, Alert.AlertType.INFORMATION);
    }

    @FXML
    void clickBtnCreateExcelSource(ActionEvent event) {

        if (txtFolderPath.getText().equals("") || cboHtmlSourceURL.getValue().equals("")) {
            String title = "Error";
            String header = "Error";
            String msg = "No path or HTML source set.";
            showMsgbox(title,header,msg, Alert.AlertType.ERROR);
            return;
        }

        Crawler crawler = getCrawler();
        crawler.setFilePath(txtFolderPath.getText());
        String excelFileName;
        String msg;
        try {
            excelFileName = crawler.exportToExcelFile();
            msg = "Complete to export excel files.\n\n" +
                    "File Name: " + excelFileName + "\n\n" +
                    "Path: " + txtFolderPath.getText();

            String title = "Export File Succeed";
            String header = "Export to Excel File Successfully";
            showMsgbox(title, header, msg, Alert.AlertType.INFORMATION);

        } catch (Exception exception) {
            String title = "Export File Failed";
            String header = "Failed to Export to Excel File";
            msg = "Export to Excel failed.\n" +
                    "Check if the Source File path or HTML Source is set correctly.";
            showMsgbox(title, header, msg, Alert.AlertType.ERROR);
        }
    }

    private void showMsgbox(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("Stylesheet.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            alert.close();
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    private Crawler getCrawler() {
        Crawler crawler;
        switch (cboHtmlSourceURL.getSelectionModel().getSelectedIndex()) {
            case 1:
                crawler = new HolyBibleCrawler();
                break;
            case 2:
                crawler = new ResurseCrestineCrawler();
                break;
            case 0:
            default:
                crawler = new YouVersionBibleCrawler();
                break;
        }
        return crawler;
    }

    @FXML
    void clickBtnCreateTMX(ActionEvent event) {
        if (txtSourceBible.getText().equals("") || txtTargetBible.getText().equals("")) {
            String title = "Error";
            String header = "Error";
            String msg = "No source or target set.";
            showMsgbox(title,header,msg, Alert.AlertType.ERROR);
            return;
        }

        File sourceFile = new File(txtSourceBible.getText());
        File targetFile = new File(txtTargetBible.getText());

        if (!sourceFile.isFile() || !targetFile.isFile()) {
            return;
        }

        String title = "Setting for Path file saved";
        String header = "Please choose the path file will be saved.";
        String msg = "Click the OK button and select a path.";
        showMsgbox(title,header,msg, Alert.AlertType.INFORMATION);

        Popup dialog = null;
        try {
            dialog = showLoadingPopup();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File dir = showDirectoryChooser("Set the path where the target file will be saved.",
                btnCreateTMX.getScene().getWindow(), DESKTOP_PATH);
        if (dir == null) {
            return;
        }

        try {
            importer.importFromExcel(sourceFile);
            importer.importFromExcel(targetFile);

            List<TMX> tmxList = tmxCreator.createTMX();

            if (dir.isDirectory()) {
                String fileName = ExcelExporter.exportToTMX(tmxList, dir.getPath());
                msg = "Complete to export tmx files.\n\n" +
                        "File Name: " + fileName + "\n\n" +
                        "Path: " + dir.getPath();

                title = "Export File Succeed";
                header = "Export to TMX Format Excel File Successfully";
                showMsgbox(title,header,msg, Alert.AlertType.INFORMATION);
            }
        } catch (IOException e) {
            e.printStackTrace();
            title = "Export File Failed";
            header = "Failed to Export to TMX Format Excel File";
            msg = "Export to tmx format excel failed.\n" +
                    "Check if the file path or excel Source is set correctly.";
            showMsgbox(title,header,msg, Alert.AlertType.ERROR);
        }
        dialog.hide();
    }

    private Popup showLoadingPopup() throws IOException {
        Popup popup = new Popup();
        Parent parent = FXMLLoader.load(getClass().getResource("Popup.fxml"));
        popup.getContent().add(parent);
        popup.show(btnCreateTMX.getScene().getWindow());
        return popup;
    }

    @FXML
    void clickBtnCreateTextTemplate(ActionEvent event) {
        if (txtFolderPath.getText().equals("")) {
            String title = "Error";
            String header = "Error";
            String msg = "No source path set.";
            showMsgbox(title,header,msg, Alert.AlertType.ERROR);
            return;
        }

        File dir = new File(txtFolderPath.getText());
        if (dir.isDirectory()) {
            CreateTextFile.createTextFile(txtFolderPath.getText());
            String title = "Create File Succeed";
            String header = "Create File Succeed";
            String msg;
            msg = "Complete to create text files.\n\n" +
                                "Path: " + txtFolderPath.getText();
            showMsgbox(title, header, msg, Alert.AlertType.INFORMATION);
        } else {
            String title = "Create File Failed";
            String header = "Create File Failed";
            String msg;
            msg = "Invalid file path.";
            showMsgbox(title, header, msg, Alert.AlertType.ERROR);
        }
    }

    @FXML
    void clickBtnSearchFolder(ActionEvent event) {
        String title = "Choose Folder for Parsing Task";
        File targetDir;
        if (txtFolderPath.getText().equals("")) {
            targetDir = showDirectoryChooser(title, btnSearchFolder.getScene().getWindow(), DESKTOP_PATH);
        } else {
            targetDir = showDirectoryChooser(title, btnSearchFolder.getScene().getWindow(), txtFolderPath.getText());
        }
        Platform.runLater(() -> {
            txtFolderPath.setText(targetDir.getPath());
        });
    }

    private File showDirectoryChooser(String title, Window ownerWindow, String path) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File(path));
        return chooser.showDialog(ownerWindow);
    }

    private File showFileChooser(String title, Window ownerWindow, String path) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File(path));
        return chooser.showOpenDialog(ownerWindow);
    }

    @FXML
    void clickBtnSearchSourceBible(ActionEvent event) {
        String title = "Choose Excel Source Language File for Creating TMX";
        File sourceFile;
        if (txtSourceBible.getText().equals("")) {
            sourceFile = showFileChooser(title, btnSearchSourceBible.getScene().getWindow(), DESKTOP_PATH);
        } else {
            String presentDir = new File(txtSourceBible.getText()).getParentFile().getPath();
            sourceFile = showFileChooser(title, btnSearchSourceBible.getScene().getWindow(), presentDir);
        }
        Platform.runLater(() -> {
            txtSourceBible.setText(sourceFile.getPath());
        });
    }

    @FXML
    void clickBtnSearchTargetBible(ActionEvent event) {
        String title = "Choose Excel Target Language File for Creating TMX";
        File targetFile;
        if (txtTargetBible.getText().equals("")) {
            targetFile = showFileChooser(title, btnSearchTargetBible.getScene().getWindow(), DESKTOP_PATH);
        } else {
            String presentDir = new File(txtTargetBible.getText()).getParentFile().getPath();
            targetFile = showFileChooser(title, btnSearchTargetBible.getScene().getWindow(), presentDir);
        }
        Platform.runLater(() -> {
            txtTargetBible.setText(targetFile.getPath());
        });
    }

    @FXML
    void clickCboHtmlSourceURL(MouseEvent event) {
        cboHtmlSourceURL.getItems().clear();
        cboHtmlSourceURL.getItems().addAll(observableUrlList);
    }

}

