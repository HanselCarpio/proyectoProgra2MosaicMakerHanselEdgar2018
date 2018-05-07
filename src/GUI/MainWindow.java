package GUI;

import business.SaveFileBusiness;
import domain.ImageObject;
import domain.MosaicObject;
import java.awt.geom.AffineTransform;
import data.SaveFileData;
import domain.PartsImage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author hansel
 */
public class MainWindow extends Application {

    //atributos
    private final int WIDTH = 1300;
    private final int HEIGHT = 700;
    private ScrollPane scrollPaneImage, scrollPaneMosaic;
    private Pane pane;
    private Scene scene;
    private Canvas canvasImage, canvasMosaic;
    private Label labelPx, labelRows, labelColumns;
    private Button btnSelectImage, btnDeleteMosaic, saveNewImage, btDrawDefaultGrid, btDrawGrid, btnDeleteImage;
    private TextField txtPS, txtRows, txtColumns;
    int column = 0, row = 0;
    int columnsMosaic = 0, rowsMosaic = 0;
    int filas = 0, columnas = 0;
    private int pixelSize = 0;
    private int i, j, k, l;
    private int partsImageWidth, partsImageHeight;
    int x1, y1, posX, posY;
    private ContextMenu contextMenu;
    private MenuItem miDelete, miRotateLeft, miRotateRight, miVerticalFlip, miHorizontalFlip;
    private MenuBar mbMenu;
    private Menu menuFile;
    private MenuItem miSaveProject, miOpenProject, miNewProject;
    private BorderPane root;

    private PartsImage[][] matrizImage;
    private PartsImage[][] matrizMosaic;
    private BufferedImage bufferedImage;

    @Override
    public void start(Stage primaryStage) {
        //se inicializan los componentes de la parte grafica
        this.root = new BorderPane();
        this.pane = new Pane();
        this.scene = new Scene(this.pane, WIDTH, HEIGHT);
        this.scrollPaneImage = new ScrollPane();
        this.scrollPaneMosaic = new ScrollPane();
        this.canvasImage = new Canvas(400, 600);
        this.canvasMosaic = new Canvas(650, 600);
        this.scrollPaneImage.setContent(this.canvasImage);
        this.scrollPaneMosaic.setContent(this.canvasMosaic);
        this.scrollPaneImage.setPrefSize(510, 600);
        this.scrollPaneMosaic.setPrefSize(650, 600);
        this.scrollPaneMosaic.relocate(600, 30);
        this.scrollPaneImage.relocate(50, 30);
        this.scrollPaneImage.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.scrollPaneImage.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.scrollPaneMosaic.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.scrollPaneMosaic.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        this.labelPx = new Label("Pixel size:");
        this.labelRows = new Label("Row:");
        this.labelColumns = new Label("Columns:");
        this.txtPS = new TextField();
        this.txtRows = new TextField();
        this.txtColumns = new TextField();
        this.btnSelectImage = new Button("Select an Image");
        this.btnDeleteImage = new Button("Delete Image");
        this.btnDeleteMosaic = new Button("Delete Mosaic");
        this.saveNewImage = new Button("Save Image");
        this.btDrawDefaultGrid = new Button("Draw default Mosaic");
        this.btDrawGrid = new Button("Draw Mosaic");
        saveNewImage.relocate(1174, 640);
        btnDeleteMosaic.relocate(481, 640);
        btnDeleteImage.relocate(350, 640);
        labelPx.relocate(50, 645);
        btnSelectImage.relocate(230, 640);
        txtPS.relocate(102, 640);
        txtPS.setPrefSize(100, 26);
        btDrawDefaultGrid.relocate(600, 640);
        labelRows.relocate(830, 645);
        txtRows.relocate(860, 640);
        txtRows.setPrefSize(50, 26);
        labelColumns.relocate(948, 645);
        txtColumns.relocate(1000, 640);
        txtColumns.setPrefSize(50, 26);
        btDrawGrid.relocate(1070, 640);
        scene.setFill(Color.LIGHTBLUE);
        pane.setBackground(Background.EMPTY);

        this.contextMenu = new ContextMenu();
        this.miDelete = new MenuItem("Delete");
        this.miRotateLeft = new MenuItem("Rotate to left");
        this.miRotateRight = new MenuItem("Rotate to right");
        this.miVerticalFlip = new MenuItem("Vertical flip");
        this.miHorizontalFlip = new MenuItem("Horizontal flip");
        contextMenu.getItems().addAll(miDelete, new SeparatorMenuItem(), miRotateRight, miRotateLeft,
                new SeparatorMenuItem(), miHorizontalFlip, miVerticalFlip);

        mbMenu = new MenuBar();
        mbMenu.setMinWidth(1320);
        menuFile = new Menu("File");
        mbMenu.getMenus().add(menuFile);
        this.miSaveProject = new MenuItem("save Project");
        this.miOpenProject = new MenuItem("Open Project");
        this.miNewProject = new MenuItem("New Project");
        menuFile.getItems().addAll(miSaveProject, miOpenProject, miNewProject);
        this.root.setTop(mbMenu);

        GraphicsContext graCoMosaic = this.canvasMosaic.getGraphicsContext2D();
        GraphicsContext graCoImage = this.canvasImage.getGraphicsContext2D();

        this.canvasImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
//                selectAnImage((int) event.getX(), (int) event.getY());
                if (matrizImage == null) {
                    System.out.println("Ingrese una imagen primero");
                } else if (matrizImage != null) {
                    for (int x = 0; x < MainWindow.this.row; x++) {
                        for (int y = 0; y < MainWindow.this.column; y++) {
                            if (matrizImage[x][y].pressMouse((int) event.getX(), (int) event.getY())) {
                                i = x;
                                j = y;
                                break;
                            } else {
                                System.err.println("ERROR" + column + MainWindow.this.row);
                            }
                        }
                    }
                }
                System.out.println(event.getX() + ", " + event.getY());
            } // handle
        }
        );

        canvasMosaic.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//            int k, l;

            @Override
            public void handle(MouseEvent event) {
                if (matrizMosaic != null) {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        x1 = (int) event.getX();
                        y1 = (int) event.getY();
                        contextMenu.show(canvasMosaic, event.getScreenX(), event.getScreenY());
                    }
                }
                if (event.getButton() == MouseButton.PRIMARY) {
                    try {
//                        pasteImageOnMosaic(graCoMosaic, (int) event.getX(), (int) event.getY());
                        for (int x = 0; x < MainWindow.this.rowsMosaic; x++) {
                            for (int y = 0; y < MainWindow.this.columnsMosaic; y++) {
                                if (matrizMosaic[x][y].pressMouse((int) event.getX(), (int) event.getY())) {
                                    k = x;
                                    l = y;
                                    break;
                                }
                            }
                        }
                        matrizMosaic[k][l].setiBytes(matrizImage[i][j].getiBytes());
                        matrizMosaic[k][l].printImageOnMosaic(graCoMosaic);
                    } catch (IOException ex) {
                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        btnSelectImage.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                GraphicsContext graCo = canvasImage.getGraphicsContext2D();
                selectImage(primaryStage, graCo, canvasImage);
                if (matrizImage != null) {
                    btnSelectImage.setDisable(true);
                }
            } // handle
        }
        );

        btnDeleteImage.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                graCoImage.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
                matrizImage = null;
                btnSelectImage.setDisable(false);
            } // handle
        }
        );

        btnDeleteMosaic.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                graCoMosaic.clearRect(0, 0, canvasMosaic.getWidth(), canvasMosaic.getHeight());
                matrizMosaic = null;
                txtColumns.setEditable(true);
                txtRows.setEditable(true);
                btDrawDefaultGrid.setDisable(false);
                btDrawGrid.setDisable(false);
                txtColumns.setText("");
                txtRows.setText("");
            } // handle
        }
        );

        btDrawDefaultGrid.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                if (matrizImage != null) {
                    rowsMosaic = row;
                    columnsMosaic = column;
                    txtColumns.setEditable(false);
                    txtRows.setEditable(false);
                    btDrawGrid.setDisable(true);
                    btDrawDefaultGrid.setDisable(true);
                    drawGrid(graCoMosaic, canvasMosaic, rowsMosaic, columnsMosaic);
                } else {
                    System.err.println("no se pinta lo que no existe");
                }
            }
        }
        );

        btDrawGrid.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                if (matrizImage != null) {
                    if (!txtRows.getText().equals("") && !txtColumns.getText().equals("")) {
                        rowsMosaic = Integer.parseInt(txtRows.getText());
                        columnsMosaic = Integer.parseInt(txtColumns.getText());
                        drawGrid(graCoMosaic, canvasMosaic, rowsMosaic, columnsMosaic);
                        txtRows.setText(String.valueOf(rowsMosaic));
                        txtColumns.setText(String.valueOf(columnsMosaic));
                        txtColumns.setEditable(false);
                        txtRows.setEditable(false);
                        btDrawGrid.setDisable(true);
                        btDrawDefaultGrid.setDisable(true);
                    } else {
                        System.err.println("Error in print Grind");
                    }
                } else {
                    System.err.println("no se pinta grid si no hay imagen");
                }
            }
        }
        );

        saveNewImage.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                if (matrizMosaic != null) {
                    exportImage(primaryStage, graCoMosaic);
                } else {
                    System.err.println("no se puede guardar vacio");
                }
            }
        }
        );

        miDelete.setOnAction(
                new EventHandler<ActionEvent>() {
            int k, l;
            Image imagen = null;

            @Override
            public void handle(ActionEvent event
            ) {
                for (int x = 0; x < MainWindow.this.rowsMosaic; x++) {
                    for (int y = 0; y < MainWindow.this.columnsMosaic; y++) {
                        if (matrizMosaic[x][y].pressMouse(x1, y1)) {
                            k = x;
                            l = y;
                            break;
                        }
                    }
                }
                matrizMosaic[k][l].setiBytes(matrizImage[i][j].getiBytes());
                graCoMosaic.clearRect(matrizMosaic[k][l].getPosix() * pixelSize + 2, matrizMosaic[k][l].getPosiy() * pixelSize + 2, pixelSize - 3, pixelSize - 3);
//                matrizMosaic[k][l].setiBytes(null);
                matrizMosaic[k][l].setiBytes(new byte[0]);
            }

        }
        );

        miRotateRight.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                for (int x = 0; x < rowsMosaic; x++) {
                    for (int y = 0; y < columnsMosaic; y++) {
                        if (MainWindow.this.matrizMosaic[x][y].pressMouse(x1, y1)) {
                            k = x;
                            l = y;
                            break;
                        }
                    }
                }
                if (MainWindow.this.matrizMosaic[k][l].getiBytes().length != 0) {
                    try {
                        ((MosaicObject) MainWindow.this.matrizMosaic[k][l]).rotate(1);
                        ((MosaicObject) MainWindow.this.matrizMosaic[k][l]).printImageOnMosaic(graCoMosaic);
                    } catch (IOException ex) {
                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        );

        miRotateLeft.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                for (int x = 0; x < rowsMosaic; x++) {
                    for (int y = 0; y < columnsMosaic; y++) {
                        if (MainWindow.this.matrizMosaic[x][y].pressMouse(x1, y1)) {
                            k = x;
                            l = y;
                            break;
                        }
                    }
                }
                if (MainWindow.this.matrizMosaic[k][l].getiBytes().length != 0) {
                    try {
                        ((MosaicObject) MainWindow.this.matrizMosaic[k][l]).rotate(0);
                        ((MosaicObject) MainWindow.this.matrizMosaic[k][l]).printImageOnMosaic(graCoMosaic);
                    } catch (IOException ex) {
                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        );

        miVerticalFlip.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                for (int x = 0; x < rowsMosaic; x++) {
                    for (int y = 0; y < columnsMosaic; y++) {
                        if (MainWindow.this.matrizMosaic[x][y].pressMouse(x1, y1)) {
                            k = x;
                            l = y;
                            break;
                        }
                    }
                }
                if (MainWindow.this.matrizMosaic[k][l].getiBytes().length != 0) {
                    try {
                        ((MosaicObject) MainWindow.this.matrizMosaic[k][l]).flipVertical(1);
                        MainWindow.this.matrizMosaic[k][l].printImageOnMosaic(graCoMosaic);
                    } catch (IOException ex) {
                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        );

        miHorizontalFlip.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                for (int x = 0; x < rowsMosaic; x++) {
                    for (int y = 0; y < columnsMosaic; y++) {
                        if (MainWindow.this.matrizMosaic[x][y].pressMouse(x1, y1)) {
                            k = x;
                            l = y;
                            break;
                        }
                    }
                }
                if (MainWindow.this.matrizMosaic[k][l].getiBytes().length != 0) {
                    try {
                        ((MosaicObject) MainWindow.this.matrizMosaic[k][l]).flipHorizontal(1);
                        MainWindow.this.matrizMosaic[k][l].printImageOnMosaic(graCoMosaic);
                    } catch (IOException ex) {
                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        );

        miSaveProject.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                try {
                    saveProyect(primaryStage);
                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );

        miOpenProject.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                openProject(primaryStage, canvasImage, graCoImage, graCoMosaic, canvasMosaic);
            }
        }
        );

        miNewProject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newProject(canvasImage, graCoImage, graCoMosaic, canvasMosaic);
                txtRows.setText("");
                txtRows.setEditable(true);
                txtColumns.setText("");
                txtColumns.setEditable(true);
                btDrawDefaultGrid.setDisable(false);
                btDrawGrid.setDisable(false);
                btnDeleteMosaic.setDisable(false);
                btnSelectImage.setDisable(false);
            }
        });

        this.pane.getChildren().add(this.scrollPaneImage);
        this.pane.getChildren().add(this.scrollPaneMosaic);
        this.pane.getChildren().add(this.btnSelectImage);
        this.pane.getChildren().add(this.btnDeleteMosaic);
        this.pane.getChildren().add(this.txtPS);
        this.pane.getChildren().add(this.txtRows);
        this.pane.getChildren().add(this.txtColumns);
        this.pane.getChildren().add(this.labelPx);
        this.pane.getChildren().add(this.labelColumns);
        this.pane.getChildren().add(this.labelRows);
        this.pane.getChildren().add(this.btDrawDefaultGrid);
        this.pane.getChildren().add(this.btDrawGrid);
        this.pane.getChildren().add(this.btnDeleteImage);
        this.pane.getChildren().add(this.saveNewImage);
        this.pane.getChildren().add(this.root);
        primaryStage.setTitle("Mosaic Maker");
        primaryStage.setScene(this.scene);
        primaryStage.resizableProperty().set(false);
        primaryStage.show();
    } // start

    public void selectImage(Stage primaryStage, GraphicsContext graCo, Canvas canvasImage) {
        FileChooser fileChooserSelect = new FileChooser();
        if (!txtPS.getText().equals("")) {
            txtPS.setEditable(false);
            FileChooser.ExtensionFilter chooser = new FileChooser.ExtensionFilter("Image Extends", "*.png", "*.jpg", "*.jpeg");
            fileChooserSelect.getExtensionFilters().add(chooser);
            File selectedDirectory = fileChooserSelect.showOpenDialog(primaryStage);
            if (selectedDirectory != null) {
                try {
                    BufferedImage aux = ImageIO.read(selectedDirectory);
                    if (this.pixelSize != 0) {
                        if (aux.getHeight() >= this.pixelSize && aux.getWidth() >= this.pixelSize) {
                            graCo.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
                            this.bufferedImage = aux;
                            canvasImage.setHeight(this.bufferedImage.getHeight());
                            canvasImage.setWidth(this.bufferedImage.getWidth());
//                            graCo.drawImage(SwingFXUtils.toFXImage(this.bufferedImage, null), 0, 0);
                            imageParts(graCo, canvasImage);
                        }
                    } else {
                        this.bufferedImage = aux;
                        canvasImage.setHeight(this.bufferedImage.getHeight());
                        canvasImage.setWidth(this.bufferedImage.getWidth());
//                        graCo.drawImage(SwingFXUtils.toFXImage(this.bufferedImage, null), 0, 0);

//                    bufferedImage = ImageIO.read(selectedDirectory);
//                    canvasImage.setHeight(bufferedImage.getHeight());
//                    canvasImage.setWidth(bufferedImage.getWidth());
                        imageParts(graCo, canvasImage);

                    }
                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            System.err.println("Ingrese pixel size");
        }
    }

    public byte[] imageToBytes(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", outputStream);
        return outputStream.toByteArray();
    } // imageToBytes

    public void imageParts(GraphicsContext graCoIma, Canvas canvasImage) {
        // determina el ancho y la altura
        pixelSize = Integer.parseInt(txtPS.getText());
        // determina el ancho y la altura
        this.partsImageWidth = (int) this.bufferedImage.getWidth();
        this.partsImageHeight = (int) this.bufferedImage.getHeight();
        //define el las filas y las columnas de la matriz
        row = partsImageHeight / pixelSize;
        column = partsImageWidth / pixelSize;
        canvasImage.setHeight((this.row) * this.pixelSize + ((this.row + 1) * 10));
        canvasImage.setWidth((this.column) * this.pixelSize + ((this.column + 1) * 10));
        filas = 0;
        columnas = 0;
        System.err.println(column + ", " + row);
//        PixelReader pixelReader = image.getPixelReader();
        this.matrizImage = new ImageObject[row][column];
        //ciclos para ir cortando la imagen con el tamaño de los pixeles
        for (int x = 0; x < this.row; x++) {
            for (int y = 0; y < this.column; y++) {
                //Initialize the image array with image parts
//                if (pixelSize < partsImageWidth) {
//                    if (pixelSize < partsImageHeight) {
                //corta la imagen en partes
                try {
                    BufferedImage aux = bufferedImage.getSubimage((y * this.pixelSize), (x * this.pixelSize), this.pixelSize, this.pixelSize);
                    //va guardando las partes de la imagen en una matriz de objetos y la va pintando a la vez
                    this.matrizImage[x][y] = new ImageObject(imageToBytes(aux), y, x, pixelSize);
                    this.matrizImage[x][y].printImageOnMosaic(graCoIma);

                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                //se le resta los pixeles de la parte recortada
//                        partsImageHeight -= pixelSize;
                filas++;
                System.err.println("filas" + filas);
//                    } else {
//                        System.err.println("error en fila");
//                    }
//                } else {
//                    System.err.println("error en columna");
//                }
                // draws the image parts
            } // for y
//            partsImageWidth -= pixelSize;
//            partsImageHeight = (int) this.bufferedImage.getHeight();
            columnas++;
            System.err.println("columnas" + columnas);
        } // for x
    }

    //inicializa el grid
    public void initMosiacPartsImage() {
        //inicializa el grid con el tamaño de la matriz de objetos donde estan las partes de imagenes
        this.matrizMosaic = new MosaicObject[rowsMosaic][columnsMosaic];
        for (int x = 0; x < rowsMosaic; x++) {
            for (int y = 0; y < columnsMosaic; y++) {
                this.matrizMosaic[x][y] = new MosaicObject(new byte[0], y, x, this.pixelSize);
            }
        }
    }

    //pintar el grid
    public void drawGrid(GraphicsContext graCoMosaic, Canvas canvasMosaic, int rowMosaic, int columnMosaic) {
        //se inicia el grid
        if (this.pixelSize > 0 && rowMosaic > 0 && columnMosaic > 0) {
            initMosiacPartsImage();
            canvasMosaic.setHeight(rowMosaic * this.pixelSize);
            canvasMosaic.setWidth(columnMosaic * this.pixelSize);
            for (int x = 0; x <= rowMosaic; x++) {
                graCoMosaic.strokeLine(0, x * this.pixelSize, columnMosaic * this.pixelSize, x * this.pixelSize); // rows
            }
            for (int y = 0; y <= columnMosaic; y++) {
                graCoMosaic.strokeLine(y * this.pixelSize, 0, y * this.pixelSize, this.pixelSize * rowMosaic); // cols
            }
        }
    } // drawGrid

    public void drawGridSave(GraphicsContext graCoMosaic, Canvas canvasMosaic, int rowMosaic, int columnMosaic) {
        //se inicia el grid
//        initMosiacPartsImage(row, column);
        if (this.pixelSize > 0 && rowMosaic > 0 && columnMosaic > 0) {
//            initMosiacPartsImage();
            canvasMosaic.setHeight(rowMosaic * this.pixelSize);
            canvasMosaic.setWidth(columnMosaic * this.pixelSize);
            //pinta lineas de acuerdo a row y column
            for (int x = 0; x <= rowMosaic; x++) {
                graCoMosaic.strokeLine(0, x * this.pixelSize, columnMosaic * this.pixelSize, x * this.pixelSize); // rows
            }
            for (int y = 0; y <= columnMosaic; y++) {
                graCoMosaic.strokeLine(y * this.pixelSize, 0, y * this.pixelSize, this.pixelSize * rowMosaic); // cols
            }
        }
    }

    public void newProject(Canvas canvasImage, GraphicsContext graCoImage, GraphicsContext graCoMosaic, Canvas canvasMosaic) {
        //inicializa todo de nuevo en 0 y borra los canvas
        txtPS.setEditable(true);
        txtPS.setText("");
        this.matrizMosaic = null;
        this.matrizImage = null;
        this.pixelSize = 0;
        this.filas = 0;
        this.column = 0;
        this.columnas = 0;
        this.row = 0;
        this.bufferedImage = null;
        graCoImage.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
        graCoMosaic.clearRect(0, 0, canvasMosaic.getWidth(), canvasMosaic.getHeight());
    }

    public void exportImage(Stage primaryStage, GraphicsContext graCoMosaic) {
        try {
            graCoMosaic.clearRect(0, 0, canvasMosaic.getWidth(), canvasMosaic.getHeight());
//            graCoMosaic.clearRect(0, 0, this.column * this.pixelSize, this.row * this.pixelSize);
            repaintInMosaic(graCoMosaic, rowsMosaic, columnsMosaic);
            WritableImage wim = new WritableImage((int) Math.round(canvasMosaic.getWidth()), (int) Math.round(canvasMosaic.getHeight()));
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
//            canvasMosaic.snapshot(null, wim);
            canvasMosaic.snapshot(snapshotParameters, wim);
            FileChooser fileChooserExport = new FileChooser();
            FileChooser.ExtensionFilter chooser = new FileChooser.ExtensionFilter("PNG", "*.png");
            fileChooserExport.getExtensionFilters().add(chooser);
            File file = fileChooserExport.showSaveDialog(primaryStage);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);

            } catch (IOException ex) {
                Logger.getLogger(MainWindow.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            drawGridSave(graCoMosaic, canvasMosaic, rowsMosaic, columnsMosaic);
            repaintInMosaic(graCoMosaic, rowsMosaic, columnsMosaic);
//            drawGridSave(graCoMosaic, canvasMosaic, rowsMosaic, columnsMosaic);
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
//    

    public void repaintInMosaic(GraphicsContext graCoMosaic, int rowsMosaic, int columnsMosaic) throws IOException {
        //repintar el mosaico
        for (int x = 0; x < rowsMosaic; x++) {
            for (int y = 0; y < columnsMosaic; y++) {
                if (matrizMosaic[x][y].getiBytes().length != 0) {
                    matrizMosaic[x][y].printImageOnMosaic(graCoMosaic);
                    System.err.println("print");
                } else {
                    System.err.println("unprint");
                }
            } // for y
        } // for x
    }

    //guarda la informacion del mosaicObject en .dat falta de hacer
    public void saveProyect(Stage primaryStage) throws IOException, ClassNotFoundException {
        if (matrizImage != null) {
            if (pixelSize != 0 && row != 0 && column != 0) {
                FileChooser fileChooserSafe = new FileChooser();
                FileChooser.ExtensionFilter chooser = new FileChooser.ExtensionFilter("DAT", "*.dat");
                fileChooserSafe.getExtensionFilters().add(chooser);
                File file = fileChooserSafe.showSaveDialog(primaryStage);
                if (file != null) {
                    new SaveFileBusiness().saveProject(matrizImage, matrizMosaic, file.getAbsolutePath());
                }
            } else {
                System.err.println("no se guardo nada");
            }
        } else {
            System.err.println("no se puede guardar");
        }
    }

    public void openProject(Stage primaryStage, Canvas canvasImage, GraphicsContext graCoImage, GraphicsContext graCoMosaic, Canvas canvasMosaic) {
        FileChooser fileChooserOpen = new FileChooser();
        FileChooser.ExtensionFilter chooser = new FileChooser.ExtensionFilter("DAT", "*.dat");
        fileChooserOpen.getExtensionFilters().add(chooser);
        File file = fileChooserOpen.showOpenDialog(primaryStage);
        if (file != null) {
            initOpenedProject(primaryStage, canvasImage, graCoImage, graCoMosaic, canvasMosaic, file);
        }
        if (matrizImage != null) {
            btnSelectImage.setDisable(true);
        }
    }

    public void initOpenedProject(Stage primaryStage, Canvas canvasImage, GraphicsContext graCoImage, GraphicsContext graCoMosaic, Canvas canvasMosaic, File file) {
        try {
            if (file.exists()) {
                List<PartsImage[][]> partsImagesesList = new SaveFileBusiness().recover(file);
                if (partsImagesesList.get(0) != null) {
                    System.err.println("entro");
                    matrizImage = partsImagesesList.get(0);
                    pixelSize = this.matrizImage[0][0].getPixelSize();
                    row = this.matrizImage.length;
                    column = this.matrizImage[0].length;
                    System.err.println((row));
                    System.err.println((column));
                    partsImageHeight = row * pixelSize;
                    partsImageWidth = column * pixelSize;
                    canvasImage.setHeight(partsImageHeight + ((row + 1) * 10));
                    canvasImage.setWidth(partsImageWidth + ((column + 1) * 10));
                    System.err.println((partsImageHeight));
                    System.err.println((partsImageWidth));
                    for (int x = 0; x < row; x++) {
                        for (int y = 0; y < column; y++) {
                            this.matrizImage[x][y].printImageOnMosaic(graCoImage);
                        } 
                    } 
                } else {
                    System.err.println("no entro");
                }
                if (partsImagesesList.get(1) != null) {
                    System.err.println("entro");
                    matrizMosaic = partsImagesesList.get(1);
                    rowsMosaic = matrizMosaic.length;
                    columnsMosaic = matrizMosaic[0].length;
                    canvasMosaic.setHeight(this.rowsMosaic * this.pixelSize);
                    canvasMosaic.setWidth(this.columnsMosaic * this.pixelSize);
                    if (partsImagesesList.get(1) != null) {
                        System.err.println("ENTRO");
                        repaintInMosaic(graCoMosaic, rowsMosaic, columnsMosaic);
                        drawGridSave(graCoMosaic, canvasMosaic, rowsMosaic, columnsMosaic);
                    } else {
                        System.err.println("ERRÓ AL ENTRAR");
                    }
                } else {
                    System.err.println("no entro");
                }
            } 
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(MainWindow.class
                    .getName()).log(Level.SEVERE, null, ex);
        } 
        //inicializa todo con los valores del archivo
        txtPS.setText(String.valueOf(pixelSize));
        txtPS.setEditable(false);
        txtColumns.setEditable(false);
        txtRows.setEditable(false);
        txtRows.setText(String.valueOf(rowsMosaic));
        txtColumns.setText(String.valueOf(columnsMosaic));
        btDrawDefaultGrid.setDisable(true);
        btDrawGrid.setDisable(true);
        btnDeleteMosaic.setDisable(true);
    } // reinit

    public static void main(String[] args) {
        launch(args);
    } // main
} // fin de la clase

