package com.joseph.biblecrawler.controller;

import com.google.common.util.concurrent.*;
import com.joseph.biblecrawler.JavaFxApplication;
import com.joseph.biblecrawler.crawler.*;
import com.joseph.biblecrawler.model.TMX;
import com.joseph.biblecrawler.model.URL;
import com.joseph.biblecrawler.repository.BibleIndexRepository;
import com.joseph.biblecrawler.repository.UrlRepository;
import com.joseph.biblecrawler.util.CreateTextFile;
import com.joseph.biblecrawler.util.ExcelExporter;
import com.joseph.biblecrawler.util.ExcelImporter;
import com.joseph.biblecrawler.util.TMXCreator;
import com.sun.javafx.tk.FileChooserType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private ComboBox<String> cboWebsite;

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

    @FXML
    private Button btnRunAutoCrawling;

    @FXML
    private TextField txtTargetURL;

    @FXML
    private TextField txtSavePath;

    @FXML
    private Button btnSavePath;

    Popup popup;

    private ObservableList<String> observableUrlList = FXCollections.observableArrayList();

    private static final String DESKTOP_PATH = System.getProperty("user.home") + "\\Desktop";

    private final UrlRepository urlRepository;
    public final BibleIndexRepository bibleIndexRepository;
    private final ExcelImporter importer;
    private final TMXCreator tmxCreator;

    public MainFxController(UrlRepository urlRepository, BibleIndexRepository bibleIndexRepository,
                            ExcelImporter importer, TMXCreator tmxCreator) {
        this.importer = importer;
        this.urlRepository = urlRepository;
        this.bibleIndexRepository = bibleIndexRepository;
        this.tmxCreator = tmxCreator;
        List<URL> urls = urlRepository.findAll();
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

        if (validateTabCreateExcelSource()) return;

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

    @FXML
    void clickBtnCreateTMX(ActionEvent event) {

        if (txtSourceBible.getText().equals("") || txtTargetBible.getText().equals("")) {
            showMsgbox("Error","Error",
                    "No source or target set.",
                    Alert.AlertType.ERROR);
            return;
        }

        File sourceFile = new File(txtSourceBible.getText());
        File targetFile = new File(txtTargetBible.getText());

        if (!sourceFile.isFile() || !targetFile.isFile()) {
            return;
        }

        Platform.runLater(() -> {
            popup = showLoadingPopup();
            setUIDisable(true);
        });

        File file = new File(txtSavePath.getText());
        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {

                importer.importFromExcel(sourceFile);
                importer.importFromExcel(targetFile);

                List<TMX> tmxList = tmxCreator.createTMX();
                return ExcelExporter.exportToTMX(tmxList, file.getPath());

            }
        };

        ExecutorService execService = Executors.newSingleThreadExecutor();
        ListeningExecutorService lExecService = MoreExecutors.listeningDecorator(execService);
        ListenableFuture<String> listenableFuture = (ListenableFuture<String>) lExecService.submit(task);
        Futures.addCallback(listenableFuture, new FutureCallback<String>() {
            @Override
            public void onSuccess(@NullableDecl String fileName) {
                Platform.runLater(() -> {
                    setUIDisable(false);
                    popup.hide();
                    showMsgbox("Export File Succeed",
                            "Export to TMX Format Excel File Successfully",
                            "Complete to export tmx files.\n\n" +
                                    "File Name: " + file.getName() + "\n\n" +
                                    "Path: " + file.getParentFile().getPath(),
                            Alert.AlertType.INFORMATION);
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                Platform.runLater(() -> {
                    setUIDisable(false);
                    popup.hide();
                    showMsgbox("Export File Failed",
                            "Failed to Export to TMX Format Excel File",
                            "Export to tmx format excel failed.\n" +
                                    "Check if the file path or excel Source is set correctly.",
                            Alert.AlertType.ERROR);
                });
            }
        });
    }

    private void setUIDisable(boolean disabled) {
        btnCreateTMX.setDisable(disabled);
        btnSavePath.setDisable(disabled);
        btnSearchSourceBible.setDisable(disabled);
        btnSearchTargetBible.setDisable(disabled);
        txtSourceBible.setDisable(disabled);
        txtTargetBible.setDisable(disabled);
        txtSavePath.setDisable(disabled);
    }

    private Popup showLoadingPopup() {
        Popup popup = new Popup();
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("Popup.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        getDirectoryFromChooser(txtFolderPath, title);
    }

    @FXML
    void clickBtnSavePath(ActionEvent event) {
        String title = "Set the path and file name to save the TMX file(Excel).";
        getFileFromChooser(txtSavePath, title, FileChooserType.SAVE);
    }

    private File showDirectoryChooser(String title, Window ownerWindow, String path) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File(path));
        return chooser.showDialog(ownerWindow);
    }

    private File showFileChooser(String title, Window ownerWindow, String path) {
        return showFileChooser(title, ownerWindow, path, FileChooserType.OPEN);
    }

    private File showFileChooser(String title, Window ownerWindow, String path, FileChooserType type) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File(path));
        if (type == FileChooserType.OPEN) {
            return chooser.showOpenDialog(ownerWindow);
        } else {
            FileChooser.ExtensionFilter extensionFilter =
                    new FileChooser.ExtensionFilter("EXCEL file", "*.xls");
            chooser.getExtensionFilters().add(extensionFilter);
            return chooser.showSaveDialog(ownerWindow);
        }
    }

    @FXML
    void clickBtnSearchSourceBible(ActionEvent event) {
        String title = "Choose Excel Source Language File for Creating TMX";
        getFileFromChooser(txtSourceBible, title);
    }

    @FXML
    void clickBtnSearchTargetBible(ActionEvent event) {
        String title = "Choose Excel Target Language File for Creating TMX";
        getFileFromChooser(txtTargetBible, title);
    }

    private void getFileFromChooser(TextField textField, String title) {
        getFileFromChooser(textField, title, FileChooserType.OPEN);
    }

    private String setFilePath(TextField textField) {
        String filePath;
        if (textField.getText().equals("")) {
            filePath = DESKTOP_PATH;
        } else {
            String presentDir = new File(textField.getText()).getParentFile().getPath();
            filePath = presentDir;
        }
        return filePath;
    }

    private void getFileFromChooser(TextField textField, String title, FileChooserType type) {
        String filePath = setFilePath(textField);

        File file = showFileChooser(title, textField.getScene().getWindow(), filePath, type);

        Platform.runLater(() -> {
            if (type == FileChooserType.OPEN) {
                textField.setText(file.getPath());
            } else {
                int indexOfExtension = file.getName().lastIndexOf(".");
                if (!file.getName().substring(indexOfExtension).contains("xls")) {
                    textField.setText(file.getPath() + ".xls");
                } else {
                    textField.setText(file.getPath());
                }
            }
        });
    }

    private void getDirectoryFromChooser(TextField textField, String title) {
        String filePath = setFilePath(textField);

        File file = showDirectoryChooser(title, textField.getScene().getWindow(), filePath);

        Platform.runLater(() -> {
            textField.setText(file.getPath());
        });
    }

    @FXML
    void clickCboHtmlSourceURL(MouseEvent event) {
        cboWebsite.getItems().clear();
        cboWebsite.getItems().addAll(observableUrlList);
    }

    @FXML
    void clickBtnAutoCrawling(ActionEvent event) {

        if (validateTabCreateExcelSource()) return;

        if (txtTargetURL.getText().equals("")) {
            String title = "Error";
            String header = "Error";
            String msg = "No URL set.";
            showMsgbox(title,header,msg, Alert.AlertType.ERROR);
            return;
        }

        AutoCrawler autoCrawler = getAutoCrawler(bibleIndexRepository);
        try {
            autoCrawler.getDefaultDriver();
            autoCrawler.autoCrawling(txtFolderPath.getText(), txtTargetURL.getText());
        } catch (Exception e) {
            String title = "Error";
            String header = "Error";
            String msg = e.getMessage();
            showMsgbox(title,header,msg, Alert.AlertType.ERROR);
            return;
        }
        autoCrawler.closeWebDriver();

    }
    
    private boolean validateTabCreateExcelSource() {
        if (txtFolderPath.getText().equals("")) {
            String title = "Error";
            String header = "Error";
            String msg = "No path set.";
            showMsgbox(title,header,msg, Alert.AlertType.ERROR);
            return true;
        }
        if (cboWebsite.getSelectionModel().getSelectedIndex() < 0) {
            String title = "Error";
            String header = "Error";
            String msg = "No HTML source set.";
            showMsgbox(title,header,msg, Alert.AlertType.ERROR);
            return true;
        }
        return false;
    }

    private AutoCrawler getAutoCrawler(BibleIndexRepository bibleIndexRepository) {
        AutoCrawler autoCrawler;
        switch (cboWebsite.getSelectionModel().getSelectedIndex()) {
            case 1:
                autoCrawler = new HolyBibleAutoCrawler(bibleIndexRepository);
                break;
            case 2:
                autoCrawler = new ResurseCrestineAutoCrawler(bibleIndexRepository);
                break;
            case 3:
                autoCrawler = new Bible4UAutoCrawler(bibleIndexRepository);
                break;
            default:
                autoCrawler = new YouVersionBibleAutoCrawler(bibleIndexRepository);
                break;
        }
        return autoCrawler;
    }

    private Crawler getCrawler() {
        Crawler crawler;
        switch (cboWebsite.getSelectionModel().getSelectedIndex()) {
            case 1:
                crawler = new HolyBibleCrawler();
                break;
            case 2:
                crawler = new ResurseCrestineCrawler();
                break;
            case 3:
                crawler = new Bible4UCrawler();
                break;
            default:
                crawler = new YouVersionBibleCrawler();
                break;
        }
        return crawler;
    }

}

