package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class PuzController {
  // Intialising variables required for the room and the puzzle
  @FXML
  private Rectangle p1;
  @FXML
  private Rectangle p2;
  @FXML
  private Rectangle p3;
  @FXML
  private Rectangle p4;
  @FXML
  private Rectangle p5;
  @FXML
  private Rectangle p6;
  @FXML
  private Rectangle p7;
  @FXML
  private Rectangle p8;
  @FXML
  private Rectangle p9;
  @FXML
  private ImageView pic1;
  @FXML
  private ImageView pic2;
  @FXML
  private ImageView pic3;
  @FXML
  private ImageView pic4;
  @FXML
  private ImageView pic5;
  @FXML
  private ImageView pic6;
  @FXML
  private ImageView pic7;
  @FXML
  private ImageView pic8;
  @FXML
  private ImageView pic9;
  @FXML
  private Label status;
  @FXML
  private Label timer;
  @FXML
  private Button check;
  @FXML
  private Button goBackBtn;
  @FXML
  private Canvas gameMaster;
  @FXML
  private Rectangle quizMaster;
  private Image[] alienImages;
  private int currentImageIndex = 0;
  @FXML
  private TextArea objText;
  @FXML
  private TextArea hintsText;
  @FXML
  private ImageView tape;
  @FXML
  private ImageView sdCard;
  @FXML
  private ImageView globe;
  @FXML
  private Button rgbClue;
  @FXML
  private Button rgbClue1;
  @FXML
  private Label collect;

  private List<Rectangle> rectangles;
  private List<ImageView> imageViews;
  private List<Image> correctOrder;

  private Map<Rectangle, ImageView> map = new HashMap<>();
  private Rectangle firstSelected = null;

  // Add this member variable to store row and column data of rectangles
  private Map<Rectangle, int[]> positionMap = new HashMap<>();

  public void initialize() {
    // Displaying the items which are collected/not collected
    hintsText.setText(GameState.getHint());
    if (GameState.isPuzzleSolved && !GameState.isRgbClueFound) {
      rgbClue.setVisible(true);
      rgbClue.setText(GameState.password);
    } else {
      rgbClue.setVisible(false);
    }
    if (GameState.isRgbClueFound) {
      rgbClue1.setVisible(true);
      rgbClue1.setText(GameState.password);
    } else {
      rgbClue1.setVisible(false);
    }
    if (!GameState.isPuzzleSolved) {
      objText.setText(
          "You need to solve the picture puzzle by unscrambling it. Remember you can only move"
              + " neighbouring tiles.");
    } else {
      objText.setText(GameState.objMessage);
    }
    tape.setVisible(GameState.isElectricalTapeFound);
    sdCard.setVisible(GameState.isSdCardFound);
    globe.setVisible(GameState.isGlobeFound);
    timer.setText(GameState.getTimeLeft());
    // Starting the timer thread for the room
    Thread timeThread = new Thread(
        () -> {
          startTimer();
        });
    timeThread.start();
    alienImages = new Image[] {
        new Image("images/move1.png"),
        new Image("images/move2.png"),
        new Image("images/move3.png"),
        new Image("images/move4.png")
    };

    // Start the animation
    startAnimation();
    // Starting the animations for the gamemaster

    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), gameMaster);

    // set the Y-axis translation value
    translateTransition.setByY(-10);

    // set the number of cycles for the animation
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);

    // Set auto-reverse to true to make the label return to its original position
    translateTransition.setAutoReverse(true);

    // Start the animation
    translateTransition.play();
    // Define position (row, column) for each rectangle
    positionMap.put(p1, new int[] { 0, 0 });
    positionMap.put(p2, new int[] { 0, 1 });
    positionMap.put(p3, new int[] { 0, 2 });
    positionMap.put(p4, new int[] { 1, 0 });
    positionMap.put(p5, new int[] { 1, 1 });
    positionMap.put(p6, new int[] { 1, 2 });
    positionMap.put(p7, new int[] { 2, 0 });
    positionMap.put(p8, new int[] { 2, 1 });
    positionMap.put(p9, new int[] { 2, 2 });

    rectangles = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9);
    imageViews = Arrays.asList(pic1, pic2, pic3, pic4, pic5, pic6, pic7, pic8, pic9);

    Image image = new Image("images/puzzle.png");
    correctOrder = splitImage(image); // Assign to correctOrder
    List<Image> shuffledOrder = new ArrayList<>(correctOrder);
    Collections.shuffle(shuffledOrder);

    for (int i = 0; i < 9; i++) {
      imageViews.get(i).setImage(shuffledOrder.get(i));
      map.put(rectangles.get(i), imageViews.get(i));
      rectangles.get(i).setOnMouseClicked(this::swap);
    }
  }

  // Creating and intialising the gamemaster animation
  private void startAnimation() {
    GraphicsContext gc = gameMaster.getGraphicsContext2D();
    AnimationTimer timer = new AnimationTimer() {
      private long lastTime = 0;
      private final long frameDurationMillis = 100; // 1000 milliseconds = 1 second

      @Override
      public void handle(long currentTime) {
        if (currentTime - lastTime >= frameDurationMillis * 1_000_000) {
          if (currentImageIndex < alienImages.length) {
            gc.clearRect(0, 0, gameMaster.getWidth(), gameMaster.getHeight());
            gc.drawImage(alienImages[currentImageIndex], 0, 0);
            currentImageIndex++;
            // Check if we have displayed all images; if so, reset the index to 0
            if (currentImageIndex >= alienImages.length) {
              currentImageIndex = 0;
            }
            lastTime = currentTime;
          }
        }
      }
    };
    timer.start();
  }

  // pressing on the quiz master to open the chat box
  @FXML
  private void clickQuizMaster(MouseEvent event) {
    App.setUi("chat");
  }

  // Method to start the timer for the scene
  private void startTimer() {
    Timeline timeline = new Timeline(
        new KeyFrame(
            // Setting the time countdown to one second
            Duration.seconds(1),
            new EventHandler<ActionEvent>() {
              @Override
              public void handle(ActionEvent event) {
                // Counts down the timer.
                Platform.runLater(
                    new Runnable() {
                      @Override
                      public void run() {
                        // Displaying the time left on the label
                        timer.setText(GameState.getTimeLeft());
                      }
                    });
              }
            }));

    timeline.setCycleCount((GameState.minutes * 60) + GameState.seconds - 1);
    timeline.play();
  }

  // Method to split the original image into 9 pieces for the picture puzzle
  private List<Image> splitImage(Image image) {
    List<Image> pieces = new ArrayList<>();
    PixelReader reader = image.getPixelReader();

    // Calculate piece dimensions based on the original image
    int pieceWidth = (int) image.getWidth() / 3;
    int pieceHeight = (int) image.getHeight() / 3;

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        WritableImage piece = new WritableImage(reader, j * pieceWidth, i * pieceHeight, pieceWidth, pieceHeight);
        pieces.add(piece);
      }
    }
    return pieces;
  }

  // Method to check if the two selected pieces are adjacent
  private boolean isAdjacent(Rectangle first, Rectangle second) {
    int[] pos1 = positionMap.get(first);
    int[] pos2 = positionMap.get(second);

    // Check horizontal and vertical adjacency
    boolean horizontal = pos1[0] == pos2[0] && Math.abs(pos1[1] - pos2[1]) == 1;
    boolean vertical = pos1[1] == pos2[1] && Math.abs(pos1[0] - pos2[0]) == 1;

    return horizontal || vertical;
  }

  // Method to check if the puzzle is in the correct order
  private boolean isCorrectOrder() {
    for (int i = 0; i < 9; i++) {
      if (!imageViews.get(i).getImage().equals(correctOrder.get(i))) {
        return false;
      }
    }
    return true;
  }

  // Method to swap the two selected pieces
  @FXML
  private void swap(MouseEvent event) {
    Rectangle clicked = (Rectangle) event.getSource();
    if (firstSelected == null) {
      firstSelected = clicked;
      firstSelected.setOpacity(0.7);
    } else {
      if (isAdjacent(firstSelected, clicked)) {
        swapImages(firstSelected, clicked);
      } else {
        firstSelected.setOpacity(0);
        firstSelected = null;
        status.setText("They must be neighbouring");
      }
    }
  }

  // Method to check if the puzzle is solved
  @FXML
  private void onCheckPuzzle(ActionEvent event) {
    // If the puzzle is solved, display the correct message
    if (isCorrectOrder()) {
      status.setText("Correct");
      GameState.isPuzzleSolved = true;
      objText.setText(
          "Good Job! You have solved the picture puzzle. Collect the RGB Clue and travel to the"
              + " door for your final puzzle");
      // Thread to send the prompt to the GPT to over look progress
      Thread thread = new Thread(
          () -> {
            GameState.sendPrompt(
                "The player has solved the jigsaw puzzle and has received the passcode to the"
                    + " locked door. The passcode is in an alien language and can be translated"
                    + " using the alien alphabet on the computer. Each letter of the passcode"
                    + " represents a coloured button.");
          });
      // Starting the thread and setting game items
      thread.start();
      rgbClue.setVisible(true);
      rgbClue.setText(GameState.password);
      collect.setText("Collect!");
      check.disableProperty().setValue(true);
    } else {
      status.setText("Not quite! Try again.");
    }
  }

  // Method to swap the two selected pieces
  private void swapImages(Rectangle first, Rectangle second) {
    ImageView firstImage = map.get(first);
    ImageView secondImage = map.get(second);

    Image temp = firstImage.getImage();
    firstImage.setImage(secondImage.getImage());
    secondImage.setImage(temp);

    animateSwap(firstImage, secondImage);

    // After animation is done, reset opacity
    first.setOpacity(0);
    second.setOpacity(0);
    firstSelected = null; // Resetting firstSelected for next selection
  }

  // Method to animate the swap of the two selected pieces
  private void animateSwap(ImageView first, ImageView second) {
    // Create two translate transitions, one for each image
    TranslateTransition tt1 = new TranslateTransition();
    TranslateTransition tt2 = new TranslateTransition();
    // Set the dpositions of the images
    double dx = second.getX() - first.getX();
    double dy = second.getY() - first.getY();
    // Swapping the x and y coordinates of the images
    tt1.setByX(dx);
    tt1.setByY(dy);

    tt2.setByX(-dx);
    tt2.setByY(-dy);

    tt1.setNode(first);
    tt2.setNode(second);
    // Playing the animation for the two images

    tt1.play();
    tt2.play();

    tt1.setOnFinished(
        e -> {
          first.setTranslateX(0);
          first.setTranslateY(0);
          second.setTranslateX(0);
          second.setTranslateY(0);
        });
  }

  // Method to go back to the previous room
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    GameState.currentObj = "RGB Puzzle";
    GameState.currentRoom = "lockedroom";
    App.setUi("lockedroom");
  }

  // Method to collect the rgb clue
  @FXML
  private void clickRgb() {
    GameState.isRgbClueFound = true;
    rgbClue1.setVisible(true);
    rgbClue1.setText(GameState.password);
    rgbClue.setVisible(false);
    collect.setText("");
  }
}
