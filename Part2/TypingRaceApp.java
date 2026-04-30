package Part2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Part1.Typist;
import java.util.ArrayList;
import java.util.List;

/**
 * TypingRaceApp - A comprehensive graphical typing race simulator with:
 * - Interactive race configuration
 * - Customizable typists with multiple attributes
 * - Difficulty modifiers
 * - Live race visualization
 * - Results display
 * 
 * @author Shaan Basu
 * @version 1.0
 */
public class TypingRaceApp extends Application {
    
    private Stage stage;
    private String selectedPassage;
    private int selectedTypistCount;
    private boolean autocorrectEnabled;
    private boolean caffeineModeEnabled;
    private boolean nightShiftEnabled;
    private List<TypistConfiguration> typistConfigs;
    private TypingRaceEnhanced currentRace;
    private StatisticsManager statisticsManager;
    private RewardSystem rewardSystem;
    
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        typistConfigs = new ArrayList<>();
        statisticsManager = new StatisticsManager();
        rewardSystem = new RewardSystem();
        
        Scene startScene = createStartPage();
        stage.setScene(startScene);
        stage.setTitle("Typing Race Simulator - Enhanced");
        stage.setWidth(900);
        stage.setHeight(700);
        stage.show();
    }
    
    // Creating the start page
    private Scene createStartPage() {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-background-color: #1a1a1a;");
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));
        
        Label title = new Label("Typing Race Simulator");
        title.setStyle("-fx-font-size: 48; -fx-text-fill: #00ff00; -fx-font-weight: bold;");
        
        Label subtitle = new Label("Test your typing skills!");
        subtitle.setStyle("-fx-font-size: 18; -fx-text-fill: #888888;");
        
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(30));
        titleBox.setStyle("-fx-background-color: #252525; -fx-border-color: #00ff00; -fx-border-width: 2; -fx-border-radius: 10;");
        titleBox.getChildren().addAll(title, subtitle);
        
        Button startRaceBtn = createStyledButton("Start New Race", 18);
        Button statsBtn = createStyledButton("Statistics", 18);
        Button leaderboardBtn = createStyledButton("Leaderboard", 18);
        Button exitBtn = createStyledButton("Exit", 18);

        
        // Making it so that when the button is pressed it creates their respective pages.
        startRaceBtn.setOnAction(e -> {
            typistConfigs.clear();
            Scene configScene = createRaceConfigPage();
            stage.setScene(configScene);
        });
        
        statsBtn.setOnAction(e -> {
            Scene statsScene = createStatsPage();
            stage.setScene(statsScene);
        });
        
        leaderboardBtn.setOnAction(e -> {
            Scene leaderboardScene = createLeaderboardPage();
            stage.setScene(leaderboardScene);
        });
        
        exitBtn.setOnAction(e -> stage.close());
        
        VBox buttonBox = new VBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20));
        buttonBox.getChildren().addAll(startRaceBtn, statsBtn, leaderboardBtn, exitBtn);
        
        VBox contentBox = new VBox(30, titleBox, buttonBox);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setStyle("-fx-background-color: #1a1a1a;");
        
        layout.getChildren().add(contentBox);
        
        return new Scene(layout, 900, 700);
    }
    
    // Method which creates the race Configuration page 
    // It returns the scene which contains all the content of the created race config page
    private Scene createRaceConfigPage() {
        ScrollPane scrollPane = new ScrollPane();
        VBox mainLayout = new VBox(15);
        mainLayout.setStyle("-fx-background-color: #1a1a1a; -fx-padding: 20;");
        
        // Passage Selection
        mainLayout.getChildren().add(createPassageSelectionSection());
        
        // Typist Count Selection
        mainLayout.getChildren().add(createTypistCountSection());
        
        // Difficulty Modifiers
        mainLayout.getChildren().add(createDifficultyModifiersSection());
        
        // Navigation Buttons
        HBox navButtons = new HBox(15);
        navButtons.setAlignment(Pos.CENTER);
        
        Button nextBtn = createStyledButton("Next: Configure Typists", 16);
        nextBtn.setOnAction(e -> {
            if (validateRaceConfig()) {
                Scene typistConfigScene = createTypistConfigPage(1);
                stage.setScene(typistConfigScene);
            }
        });
        
        Button backBtn = createStyledButton("Back", 14);
        backBtn.setOnAction(e -> {
            Scene startScene = createStartPage();
            stage.setScene(startScene);
        });
        
        navButtons.getChildren().addAll(backBtn, nextBtn);
        mainLayout.getChildren().add(navButtons);
        
        scrollPane.setContent(mainLayout);
        scrollPane.setStyle("-fx-background-color: #1a1a1a;");
        scrollPane.setFitToWidth(true);
        
        return new Scene(scrollPane, 900, 700);
    }
    
    // This method creates the passage selection section in the race config page
    private VBox createPassageSelectionSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-border-color: #444; -fx-border-width: 1; -fx-padding: 15; -fx-background-color: #252525;");
        
        Label title = new Label("Step 1: Select Passage");
        title.setStyle("-fx-font-size: 18; -fx-text-fill: #00ff00; -fx-font-weight: bold;");
        section.getChildren().add(title);
        
        // A toggle group makes it so that only one of the button in the togglegroup can be selected at a time.
        ToggleGroup passageGroup = new ToggleGroup();
        
        RadioButton shortBtn = new RadioButton("Short Passage (120 chars)");
        RadioButton mediumBtn = new RadioButton("Medium Passage (250 chars)");
        RadioButton longBtn = new RadioButton("Long Passage (450 chars)");
        RadioButton customBtn = new RadioButton("Custom Passage");
        
        shortBtn.setToggleGroup(passageGroup);
        mediumBtn.setToggleGroup(passageGroup);
        longBtn.setToggleGroup(passageGroup);
        customBtn.setToggleGroup(passageGroup);
        
        String radioStyle = "-fx-text-fill: #cccccc; -fx-font-size: 13;";
        shortBtn.setStyle(radioStyle);
        mediumBtn.setStyle(radioStyle);
        longBtn.setStyle(radioStyle);
        customBtn.setStyle(radioStyle);
        
        // We then have to create the text area for when the user selects the cutsombtn
        TextArea customPassageArea = new TextArea();
        customPassageArea.setStyle("-fx-control-inner-background: #333333; -fx-text-fill: #cccccc;");
        customPassageArea.setWrapText(true);
        customPassageArea.setPrefRowCount(4);
        customPassageArea.setPromptText("Enter your custom passage here...");
        customPassageArea.setVisible(false); // We initially set its visibility to false and change it when the custombtn is selected
        
        // Preview labels: We use the passageLibrary class to choose one of the short, medium and long passages and then use that to show the user a preview of the passage.
        Label shortPreview = new Label("Preview: " + PassageLibrary.getPassage("SHORT").substring(0, Math.min(80, PassageLibrary.getPassage("SHORT").length())) + "...");
        Label mediumPreview = new Label("Preview: " + PassageLibrary.getPassage("MEDIUM").substring(0, Math.min(80, PassageLibrary.getPassage("MEDIUM").length())) + "...");
        Label longPreview = new Label("Preview: " + PassageLibrary.getPassage("LONG").substring(0, Math.min(80, PassageLibrary.getPassage("LONG").length())) + "...");
        
        String previewStyle = "-fx-text-fill: #888888; -fx-font-size: 11;";
        shortPreview.setStyle(previewStyle);
        mediumPreview.setStyle(previewStyle);
        longPreview.setStyle(previewStyle);
        shortPreview.setWrapText(true);
        mediumPreview.setWrapText(true);
        longPreview.setWrapText(true);
        
        // We then make it so that the preview is shown when the user selects their button 
        shortBtn.setOnAction(e -> {
            selectedPassage = PassageLibrary.getPassage("SHORT");
            customPassageArea.setVisible(false);
        });
        
        mediumBtn.setOnAction(e -> {
            selectedPassage = PassageLibrary.getPassage("MEDIUM");
            customPassageArea.setVisible(false);
        });
        
        longBtn.setOnAction(e -> {
            selectedPassage = PassageLibrary.getPassage("LONG");
            customPassageArea.setVisible(false);
        });
        
        // When custom button is selected we make it so that the text area is visible
        customBtn.setOnAction(e -> customPassageArea.setVisible(true));
        customPassageArea.textProperty().addListener((obs, oldVal, newVal) -> {
            if (customBtn.isSelected()) {
                selectedPassage = newVal;
            }
        });
        
        VBox shortBox = new VBox(3);
        shortBox.getChildren().addAll(shortBtn, shortPreview);
        
        VBox mediumBox = new VBox(3);
        mediumBox.getChildren().addAll(mediumBtn, mediumPreview);
        
        VBox longBox = new VBox(3);
        longBox.getChildren().addAll(longBtn, longPreview);
        
        section.getChildren().addAll(shortBox, mediumBox, longBox, customBtn, customPassageArea);
        
        // Select first option by default
        shortBtn.setSelected(true);
        selectedPassage = PassageLibrary.getPassage("SHORT");
        
        return section;
    }
    
    // This method creates the Number of Typists in the race Count.
    // It returns the created section as a VBox.
    private VBox createTypistCountSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-border-color: #444; -fx-border-width: 1; -fx-padding: 15; -fx-background-color: #252525;");
        
        Label title = new Label("Step 2: Number of Typists (2-6)");
        title.setStyle("-fx-font-size: 18; -fx-text-fill: #00ff00; -fx-font-weight: bold;");
        section.getChildren().add(title);
        
        ToggleGroup countGroup = new ToggleGroup();
        HBox countBox = new HBox(15);
        countBox.setAlignment(Pos.CENTER_LEFT);
        
        // A for loop is used to create the options for increasing number of typists from 2 -6 inclusive
        for (int i = 2; i <= 6; i++) {
            RadioButton btn = new RadioButton(i + " Typists");
            btn.setToggleGroup(countGroup);
            btn.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 13;");
            
            int count = i;
            btn.setOnAction(e -> selectedTypistCount = count);
            
            if (i == 2) btn.setSelected(true);
            countBox.getChildren().add(btn);
        }
        
        selectedTypistCount = 2;  // Default
        section.getChildren().add(countBox);
        
        return section;
    }
    
    // This method creates the difficulty modifier selection section
    // It returns the section in a VBox
    private VBox createDifficultyModifiersSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-border-color: #444; -fx-border-width: 1; -fx-padding: 15; -fx-background-color: #252525;");
        
        Label title = new Label("Step 3: Difficulty Modifiers (Optional)");
        title.setStyle("-fx-font-size: 18; -fx-text-fill: #00ff00; -fx-font-weight: bold;");
        section.getChildren().add(title);
        
        //We use a checkBox and also not a toggle group as all the difficulty modifiers can be put together at the same time if wished.
        CheckBox autocorrectCheck = new CheckBox("Autocorrect: Halve slideBack amount");
        CheckBox caffeineCheck = new CheckBox("Caffeine Mode: 10-turn speed boost, then burnout risk");
        CheckBox nightShiftCheck = new CheckBox("Night Shift: 15% accuracy reduction for all");
        
        String checkStyle = "-fx-text-fill: #cccccc; -fx-font-size: 13;";
        autocorrectCheck.setStyle(checkStyle);
        caffeineCheck.setStyle(checkStyle);
        nightShiftCheck.setStyle(checkStyle);
        
        autocorrectCheck.selectedProperty().addListener((obs, oldVal, newVal) -> autocorrectEnabled = newVal);
        caffeineCheck.selectedProperty().addListener((obs, oldVal, newVal) -> caffeineModeEnabled = newVal);
        nightShiftCheck.selectedProperty().addListener((obs, oldVal, newVal) -> nightShiftEnabled = newVal);
        
        section.getChildren().addAll(autocorrectCheck, caffeineCheck, nightShiftCheck);
        
        return section;
    }
    
    // This creates the Configuration page for the typists
    // It makes it so that the user can customise the typists name, their symbol, their colour, their typing style, their keyboard style, and their accessories
    private Scene createTypistConfigPage(int typistNumber) {
        VBox mainLayout = new VBox(15);
        mainLayout.setStyle("-fx-background-color: #1a1a1a; -fx-padding: 20;");
        
        Label title = new Label("Configure Typist " + typistNumber + " of " + selectedTypistCount);
        title.setStyle("-fx-font-size: 20; -fx-text-fill: #00ff00; -fx-font-weight: bold;");
        mainLayout.getChildren().add(title);
        
        // Name input
        HBox nameBox = new HBox(10);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-text-fill: #cccccc; -fx-min-width: 120;");
        TextField nameField = new TextField();
        nameField.setStyle("-fx-control-inner-background: #333333; -fx-text-fill: #cccccc;");
        nameField.setPrefWidth(200);
        nameField.setText("Typist " + typistNumber);
        nameBox.getChildren().addAll(nameLabel, nameField);
        mainLayout.getChildren().add(nameBox);
        
        // Symbol/Emoji selection
        HBox symbolBox = new HBox(10);
        symbolBox.setAlignment(Pos.CENTER_LEFT);
        Label symbolLabel = new Label("Symbol/Emoji:");
        symbolLabel.setStyle("-fx-text-fill: #cccccc; -fx-min-width: 120;");
        ComboBox<String> symbolCombo = new ComboBox<>();
        symbolCombo.getItems().addAll("①", "②", "③", "④", "⑤", "⑥", "🚀", "⚡", "🎮", "💻", "🏃", "🎯");
        symbolCombo.setValue("" + (char)('①' + typistNumber - 1));
        symbolCombo.setStyle("-fx-control-inner-background: #333333; -fx-text-fill: #cccccc;");
        symbolBox.getChildren().addAll(symbolLabel, symbolCombo);
        mainLayout.getChildren().add(symbolBox);
        
        // Color selection
        HBox colorBox = new HBox(10);
        colorBox.setAlignment(Pos.CENTER_LEFT);
        Label colorLabel = new Label("Color:");
        colorLabel.setStyle("-fx-text-fill: #cccccc; -fx-min-width: 120;");
        ComboBox<String> colorCombo = new ComboBox<>();
        colorCombo.getItems().addAll("#FF5555", "#55FF55", "#5555FF", "#FFFF55", "#FF55FF", "#55FFFF");
        String defaultColor = "#" + String.format("%06X", (int)(Math.random() * 0xFFFFFF));
        colorCombo.setValue(defaultColor);
        colorCombo.setStyle("-fx-control-inner-background: #333333; -fx-text-fill: #cccccc;");
        
        // Color preview box
        javafx.scene.shape.Rectangle colorPreview = new javafx.scene.shape.Rectangle(30, 30);
        colorPreview.setStyle("-fx-fill: " + colorCombo.getValue() + "; -fx-stroke: #888888; -fx-stroke-width: 1;");
        colorCombo.setOnAction(e -> colorPreview.setStyle("-fx-fill: " + colorCombo.getValue() + "; -fx-stroke: #888888; -fx-stroke-width: 1;"));
        
        colorBox.getChildren().addAll(colorLabel, colorCombo, colorPreview);
        mainLayout.getChildren().add(colorBox);
        
        // Typing Style with legend
        mainLayout.getChildren().add(createTypingStyleSection());
        
        // Keyboard Type with legend
        mainLayout.getChildren().add(createKeyboardTypeSection());
        
        // Accessory with legend
        mainLayout.getChildren().add(createAccessorySection());
        
        // Navigation buttons
        HBox navButtons = new HBox(15);
        navButtons.setAlignment(Pos.CENTER);
        navButtons.setPadding(new Insets(20, 0, 0, 0));
        
        Button backBtn = createStyledButton("Back", 14);
        Button nextBtn = typistNumber < selectedTypistCount 
            ? createStyledButton("Next Typist", 14)
            : createStyledButton("Start Race", 14);
        
        backBtn.setOnAction(e -> {
            if (typistNumber == 1) {
                Scene configScene = createRaceConfigPage();
                stage.setScene(configScene);
            } else {
                Scene prevScene = createTypistConfigPage(typistNumber - 1);
                stage.setScene(prevScene);
            }
        });
        
        nextBtn.setOnAction(e -> {
            // Save current typist config
            @SuppressWarnings("unchecked")
            ComboBox<String> typingStyleCombo = (ComboBox<String>)mainLayout.lookup("#typingstyle");
            @SuppressWarnings("unchecked")
            ComboBox<String> keyboardTypeCombo = (ComboBox<String>)mainLayout.lookup("#keyboardtype");
            @SuppressWarnings("unchecked")
            ComboBox<String> accessoryCombo = (ComboBox<String>)mainLayout.lookup("#accessory");
            
            if (typingStyleCombo == null || keyboardTypeCombo == null || accessoryCombo == null) {
                showAlert("Error: Failed to retrieve configuration. Please try again.");
                return;
            }
            
            TypistConfiguration.TypingStyle style = TypistConfiguration.TypingStyle.valueOf(typingStyleCombo.getValue());
            TypistConfiguration.KeyboardType keyboard = TypistConfiguration.KeyboardType.valueOf(keyboardTypeCombo.getValue());
            TypistConfiguration.Accessory accessory = null;
            String accessoryValue = accessoryCombo.getValue();
            if (accessoryValue != null && !accessoryValue.equals("NONE")) {
                accessory = TypistConfiguration.Accessory.valueOf(accessoryValue);
            }
            
            TypistConfiguration config = new TypistConfiguration(
                nameField.getText(),
                symbolCombo.getValue(),
                colorCombo.getValue(),
                style,
                keyboard,
                accessory
            );
            typistConfigs.add(config);
            
            if (typistNumber < selectedTypistCount) {
                Scene nextScene = createTypistConfigPage(typistNumber + 1);
                stage.setScene(nextScene);
            } else {
                startRace();
            }
        });
        
        navButtons.getChildren().addAll(backBtn, nextBtn);
        mainLayout.getChildren().add(navButtons);
        
        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setStyle("-fx-background-color: #1a1a1a;");
        scrollPane.setFitToWidth(true);
        
        return new Scene(scrollPane, 900, 700);
    }
    
    private VBox createTypingStyleSection() {
        VBox section = new VBox(8);
        section.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2; -fx-padding: 15; -fx-background-color: #252525;");
        
        Label sectionLabel = new Label("Typing Style");
        sectionLabel.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 14; -fx-font-weight: bold;");
        section.getChildren().add(sectionLabel);
        
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("TOUCH_TYPIST", "HUNT_AND_PECK", "PHONE_THUMBS", "VOICE_TO_TEXT");
        combo.setValue("TOUCH_TYPIST");
        combo.setStyle("-fx-control-inner-background: #333333; -fx-text-fill: #cccccc;");
        combo.setId("typingstyle");
        section.getChildren().add(combo);
        
        // Legend
        VBox legend = new VBox(5);
        legend.setStyle("-fx-padding: 10; -fx-border-color: #444; -fx-border-width: 1; -fx-background-color: #1a1a1a;");
        
        addLegendEntry(legend, "TOUCH_TYPIST", "High accuracy, standard typing speed");
        addLegendEntry(legend, "HUNT_AND_PECK", "Moderate accuracy, slower speed");
        addLegendEntry(legend, "PHONE_THUMBS", "Lower accuracy, variable speed");
        addLegendEntry(legend, "VOICE_TO_TEXT", "Very high accuracy, unique characteristics");
        
        section.getChildren().add(legend);
        return section;
    }
    
    private VBox createKeyboardTypeSection() {
        VBox section = new VBox(8);
        section.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2; -fx-padding: 15; -fx-background-color: #252525;");
        
        Label sectionLabel = new Label("Keyboard Type");
        sectionLabel.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 14; -fx-font-weight: bold;");
        section.getChildren().add(sectionLabel);
        
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("MECHANICAL", "MEMBRANE", "TOUCHSCREEN", "STENOGRAPHY");
        combo.setValue("MECHANICAL");
        combo.setStyle("-fx-control-inner-background: #333333; -fx-text-fill: #cccccc;");
        combo.setId("keyboardtype");
        section.getChildren().add(combo);
        
        // Legend
        VBox legend = new VBox(5);
        legend.setStyle("-fx-padding: 10; -fx-border-color: #444; -fx-border-width: 1; -fx-background-color: #1a1a1a;");
        
        addLegendEntry(legend, "MECHANICAL", "Responsive, good accuracy and speed");
        addLegendEntry(legend, "MEMBRANE", "Balanced keyboard, moderate performance");
        addLegendEntry(legend, "TOUCHSCREEN", "Challenging, lower accuracy bonus");
        addLegendEntry(legend, "STENOGRAPHY", "Specialized, unique characteristics");
        
        section.getChildren().add(legend);
        return section;
    }
    
    private VBox createAccessorySection() {
        VBox section = new VBox(8);
        section.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2; -fx-padding: 15; -fx-background-color: #252525;");
        
        Label sectionLabel = new Label("Accessory (Optional)");
        sectionLabel.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 14; -fx-font-weight: bold;");
        section.getChildren().add(sectionLabel);
        
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("NONE", "WRIST_SUPPORT", "ENERGY_DRINK", "NOISE_CANCELLING");
        combo.setValue("NONE");
        combo.setStyle("-fx-control-inner-background: #333333; -fx-text-fill: #cccccc;");
        combo.setId("accessory");
        section.getChildren().add(combo);
        
        // Legend
        VBox legend = new VBox(5);
        legend.setStyle("-fx-padding: 10; -fx-border-color: #444; -fx-border-width: 1; -fx-background-color: #1a1a1a;");
        
        addLegendEntry(legend, "NONE", "No special equipment");
        addLegendEntry(legend, "WRIST_SUPPORT", "Reduces fatigue, improves accuracy");
        addLegendEntry(legend, "ENERGY_DRINK", "Speed boost, burnout risk increase");
        addLegendEntry(legend, "NOISE_CANCELLING", "Better focus, improved typing");
        
        section.getChildren().add(legend);
        return section;
    }
    
    private void addLegendEntry(VBox legend, String option, String description) {
        HBox entry = new HBox(10);
        entry.setStyle("-fx-padding: 5;");
        
        Label optionLabel = new Label("• " + option);
        optionLabel.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 11; -fx-min-width: 140;");
        
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 11;");
        descLabel.setWrapText(true);
        
        entry.getChildren().addAll(optionLabel, descLabel);
        legend.getChildren().add(entry);
    }
    
    
    // ========== START RACE ==========
    private void startRace() {
        // Create the enhanced race
        currentRace = new TypingRaceEnhanced(selectedPassage);
        
        // Add typists with configurations applied
        for (int i = 0; i < typistConfigs.size(); i++) {
            TypistConfiguration config = typistConfigs.get(i);
            
            // Create base typist with initial accuracy (higher range for better progression)
            double baseAccuracy = 0.75 + (Math.random() * 0.15);  // Between 0.75 and 0.90
            Typist basePart1Typist = new Typist(config.getSymbol().charAt(0), config.getTypistName(), baseAccuracy);
            
            // Create wrapper with GUI features
            TypistWrapper typist = new TypistWrapper(basePart1Typist, config.getColor(), config);
            
            // Apply accuracy modifiers
            double modifiedAccuracy = baseAccuracy + config.calculateAccuracyModifier();
            modifiedAccuracy = Math.max(0.0, Math.min(1.0, modifiedAccuracy));  // Clamp between 0 and 1
            typist.setAccuracy(modifiedAccuracy);
            
            currentRace.addTypist(typist);
        }
        
        // Apply difficulty modifiers
        currentRace.setAutocorrect(autocorrectEnabled);
        currentRace.setCaffeineMode(caffeineModeEnabled);
        currentRace.setNightShift(nightShiftEnabled);
        
        // Set callback to show results when race completes
        currentRace.setRaceCompletionCallback(this::showRaceResults);
        
        // Show live race display
        VBox raceDisplay = RaceDisplay.createRaceDisplay(currentRace);
        Scene raceScene = new Scene(raceDisplay, 900, 700);
        stage.setScene(raceScene);
        
        // Start the race (non-blocking Timeline-based approach)
        currentRace.startRace();
    }
    
    // ========== RACE RESULTS ==========
    private void showRaceResults() {
        // Record race results to statistics manager and reward system
        for (RaceResult result : currentRace.getRaceResults()) {
            statisticsManager.recordRace(result);
            rewardSystem.recordRaceResult(result);
        }
        
        VBox resultsDisplay = RaceDisplay.createRaceResults(currentRace);
        
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20));
        
        Button newRaceBtn = createStyledButton("Start New Race", 14);
        Button mainMenuBtn = createStyledButton("Main Menu", 14);
        
        newRaceBtn.setOnAction(e -> {
            typistConfigs.clear();
            Scene configScene = createRaceConfigPage();
            stage.setScene(configScene);
        });
        
        mainMenuBtn.setOnAction(e -> {
            Scene startScene = createStartPage();
            stage.setScene(startScene);
        });
        
        buttonBox.getChildren().addAll(newRaceBtn, mainMenuBtn);
        
        VBox mainLayout = new VBox(15);
        mainLayout.setStyle("-fx-background-color: #1a1a1a;");
        mainLayout.getChildren().addAll(resultsDisplay, buttonBox);
        
        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setStyle("-fx-background-color: #1a1a1a;");
        scrollPane.setFitToWidth(true);
        
        Scene resultsScene = new Scene(scrollPane, 900, 700);
        stage.setScene(resultsScene);
    }
    
    // ========== STATISTICS PAGE ==========
    private Scene createStatsPage() {
        VBox mainLayout = new VBox(15);
        mainLayout.setStyle("-fx-background-color: #1a1a1a; -fx-padding: 20;");
        
        // Title
        Label title = new Label("Statistics and Analytics");
        title.setStyle("-fx-font-size: 28; -fx-text-fill: #00ff00; -fx-font-weight: bold;");
        mainLayout.getChildren().add(title);
        
        // Check if any races have been completed
        if (statisticsManager.getAllTypists().isEmpty()) {
            Label noDataLabel = new Label("No races completed yet. Complete a race to see statistics!");
            noDataLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #cccccc;");
            mainLayout.getChildren().add(noDataLabel);
        } else {
            // Create scrollable content
            VBox contentBox = new VBox(15);
            contentBox.setStyle("-fx-padding: 10; -fx-background-color: #1a1a1a;");
            
            // Summary statistics
            VBox summaryBox = new VBox(10);
            summaryBox.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2; -fx-padding: 15; -fx-background-color: #252525;");
            
            Label summaryTitle = new Label("Overall Statistics");
            summaryTitle.setStyle("-fx-font-size: 18; -fx-text-fill: #00ff00; -fx-font-weight: bold;");
            summaryBox.getChildren().add(summaryTitle);
            
            Label totalRaces = new Label(String.format("Total Races: %d", statisticsManager.getTotalRaces()));
            totalRaces.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 14;");
            summaryBox.getChildren().add(totalRaces);
            
            Label totalBurnouts = new Label(String.format("Total Burnout Events: %d", statisticsManager.getTotalBurnoutEvents()));
            totalBurnouts.setStyle("-fx-text-fill: #ffaa00; -fx-font-size: 14;");
            summaryBox.getChildren().add(totalBurnouts);
            
            contentBox.getChildren().add(summaryBox);
            
            // Per-typist statistics
            VBox typistStatsBox = new VBox(10);
            typistStatsBox.setStyle("-fx-border-color: #FFD700; -fx-border-width: 2; -fx-padding: 15; -fx-background-color: #252525;");
            
            Label typistTitle = new Label("Typist Performance");
            typistTitle.setStyle("-fx-font-size: 18; -fx-text-fill: #FFD700; -fx-font-weight: bold;");
            typistStatsBox.getChildren().add(typistTitle);
            
            for (String typistName : statisticsManager.getAllTypists()) {
                VBox typistBox = new VBox(5);
                typistBox.setStyle("-fx-padding: 10; -fx-border-color: #444444; -fx-border-width: 1; -fx-background-color: #1a1a1a;");
                
                Label nameLabel = new Label("🏃 " + typistName);
                nameLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #00ff00; -fx-font-weight: bold;");
                typistBox.getChildren().add(nameLabel);
                
                HBox statsLine1 = new HBox(30);
                Label races = new Label(String.format("Races: %d", statisticsManager.getTotalRaces(typistName)));
                races.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12;");
                Label wins = new Label(String.format("Wins: %d", statisticsManager.getFirstPlaceCount(typistName)));
                wins.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 12;");
                statsLine1.getChildren().addAll(races, wins);
                typistBox.getChildren().add(statsLine1);
                
                HBox statsLine2 = new HBox(30);
                double avgWpm = statisticsManager.getAverageWPM(typistName);
                Label wpm = new Label(String.format("Avg WPM: %.2f", avgWpm));
                wpm.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 12;");
                double personalBest = statisticsManager.getPersonalBestWPM(typistName);
                Label best = new Label(String.format("Best WPM: %.2f", personalBest));
                best.setStyle("-fx-text-fill: #00dd00; -fx-font-size: 12;");
                statsLine2.getChildren().addAll(wpm, best);
                typistBox.getChildren().add(statsLine2);
                
                HBox statsLine3 = new HBox(30);
                double avgAcc = statisticsManager.getAverageAccuracy(typistName);
                Label accuracy = new Label(String.format("Avg Accuracy: %.2f%%", avgAcc * 100));
                accuracy.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12;");
                Label burnouts = new Label(String.format("Burnouts: %d", statisticsManager.getTotalBurnoutEvents(typistName)));
                burnouts.setStyle("-fx-text-fill: #ffaa00; -fx-font-size: 12;");
                statsLine3.getChildren().addAll(accuracy, burnouts);
                typistBox.getChildren().add(statsLine3);
                
                typistStatsBox.getChildren().add(typistBox);
            }
            
            contentBox.getChildren().add(typistStatsBox);
            
            // Scroll pane for content
            ScrollPane scrollPane = new ScrollPane(contentBox);
            scrollPane.setStyle("-fx-background-color: #1a1a1a; -fx-control-inner-background: #1a1a1a; -fx-padding: 0;");
            scrollPane.setFitToWidth(true);
            mainLayout.getChildren().add(scrollPane);
        }
        
        // Back button
        Button backBtn = createStyledButton("Back to Menu", 14);
        backBtn.setOnAction(e -> {
            Scene startScene = createStartPage();
            stage.setScene(startScene);
        });
        
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().add(backBtn);
        
        mainLayout.getChildren().add(buttonBox);
        
        ScrollPane mainScroll = new ScrollPane(mainLayout);
        mainScroll.setStyle("-fx-background-color: #1a1a1a; -fx-control-inner-background: #1a1a1a; -fx-padding: 0;");
        mainScroll.setFitToWidth(true);
        
        return new Scene(mainScroll, 900, 700);
    }
    
    private Scene createLeaderboardPage() {
        VBox mainLayout = new VBox(15);
        mainLayout.setStyle("-fx-background-color: #1a1a1a; -fx-padding: 20;");
        
        // Title
        Label title = new Label("🏆 Global Leaderboard & Rankings");
        title.setStyle("-fx-font-size: 28; -fx-text-fill: #FFD700; -fx-font-weight: bold;");
        mainLayout.getChildren().add(title);
        
        List<LeaderboardEntry> leaderboard = rewardSystem.getLeaderboard();
        
        if (leaderboard.isEmpty()) {
            Label noDataLabel = new Label("No races completed yet. Complete a race to appear on the leaderboard!");
            noDataLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #cccccc;");
            mainLayout.getChildren().add(noDataLabel);
        } else {
            VBox contentBox = new VBox(10);
            contentBox.setStyle("-fx-padding: 10; -fx-background-color: #1a1a1a;");
            
            // Column headers
            HBox headerBox = new HBox(50);
            headerBox.setStyle("-fx-padding: 10; -fx-background-color: #252525; -fx-border-color: #00ff00; -fx-border-width: 2;");
            
            Label rankHeader = new Label("Rank");
            rankHeader.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold; -fx-min-width: 50;");
            Label nameHeader = new Label("Typist");
            nameHeader.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold; -fx-min-width: 150;");
            Label titleHeader = new Label("Title/Badge");
            titleHeader.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold; -fx-min-width: 150;");
            Label pointsHeader = new Label("Points");
            pointsHeader.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold; -fx-min-width: 80;");
            Label winsHeader = new Label("Wins");
            winsHeader.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold; -fx-min-width: 60;");
            Label earningsHeader = new Label("Earnings");
            earningsHeader.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold; -fx-min-width: 80;");
            
            headerBox.getChildren().addAll(rankHeader, nameHeader, titleHeader, pointsHeader, winsHeader, earningsHeader);
            contentBox.getChildren().add(headerBox);
            
            // Leaderboard entries
            int rank = 1;
            for (LeaderboardEntry entry : leaderboard) {
                HBox entryBox = new HBox(50);
                String bgColor = (rank == 1) ? "#1a3a1a" : (rank == 2) ? "#1a2a3a" : (rank == 3) ? "#3a2a1a" : "#252525";
                entryBox.setStyle("-fx-padding: 12; -fx-background-color: " + bgColor + "; -fx-border-color: #444444; -fx-border-width: 1;");
                
                // Rank with medal
                String medal = (rank == 1) ? "🥇" : (rank == 2) ? "🥈" : (rank == 3) ? "🥉" : (rank + ".");
                Label rankLabel = new Label(medal);
                rankLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-min-width: 50; -fx-font-size: 14;");
                
                // Name
                Label nameLabel = new Label(entry.getTypistName());
                nameLabel.setStyle("-fx-text-fill: #ffffff; -fx-min-width: 150;");
                
                // Title/Badge
                Label titleLabel = new Label(entry.getTitle());
                titleLabel.setStyle("-fx-text-fill: #00ff00; -fx-min-width: 150; -fx-font-weight: bold;");
                
                // Cumulative Points
                Label pointsLabel = new Label(String.valueOf(entry.getCumulativePoints()));
                pointsLabel.setStyle("-fx-text-fill: #00ff00; -fx-min-width: 80; -fx-font-weight: bold;");
                
                // Total Wins (1st place only)
                Label winsLabel = new Label(String.valueOf(entry.getFirstPlaceWins()));
                winsLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-min-width: 60;");
                
                // Earnings
                Label earningsLabel = new Label(String.format("💰 %.0f", entry.getTotalEarnings()));
                earningsLabel.setStyle("-fx-text-fill: #FFD700; -fx-min-width: 80;");
                
                entryBox.getChildren().addAll(rankLabel, nameLabel, titleLabel, pointsLabel, winsLabel, earningsLabel);
                contentBox.getChildren().add(entryBox);
                
                rank++;
            }
            
            // Additional stats section
            VBox statsBox = new VBox(10);
            statsBox.setStyle("-fx-border-color: #FFD700; -fx-border-width: 2; -fx-padding: 15; -fx-background-color: #252525; -fx-margin-top: 10;");
            
            Label statsTitle = new Label("Achievement Statistics");
            statsTitle.setStyle("-fx-font-size: 16; -fx-text-fill: #FFD700; -fx-font-weight: bold;");
            statsBox.getChildren().add(statsTitle);
            
            for (LeaderboardEntry entry : leaderboard) {
                HBox statsLine = new HBox(20);
                Label nameLabel = new Label(entry.getTypistName() + ":");
                nameLabel.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold; -fx-min-width: 150;");
                
                Label streakLabel = new Label(String.format("Best Win Streak: %d", entry.getBestWinStreak()));
                streakLabel.setStyle("-fx-text-fill: #aaaaaa;");
                
                Label burnoutLabel = new Label(String.format("Races Without Burnout: %d", entry.getRacesWithoutBurnout()));
                burnoutLabel.setStyle("-fx-text-fill: #aaaaaa;");
                
                statsLine.getChildren().addAll(nameLabel, streakLabel, burnoutLabel);
                statsBox.getChildren().add(statsLine);
            }
            
            contentBox.getChildren().add(statsBox);
            
            // Badge Legend section
            VBox legendBox = new VBox(8);
            legendBox.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2; -fx-padding: 15; -fx-background-color: #252525; -fx-margin-top: 10;");
            
            Label legendTitle = new Label("Badge/Title Guide");
            legendTitle.setStyle("-fx-font-size: 16; -fx-text-fill: #00ff00; -fx-font-weight: bold;");
            legendBox.getChildren().add(legendTitle);
            
            String[][] badges = {
                {"🆕 Novice", "Default title for new racers"},
                {"🏃 Racer", "Completed 3+ races"},
                {"📈 Experienced", "Completed 10+ races"},
                {"🌟 Rising Star", "Accumulated 200+ points"},
                {"⭐ Contender", "Won 5+ races (1st place)"},
                {"👑 Champion", "Won 10+ races (1st place)"},
                {"💪 Iron Fingers", "Completed 5+ races without burning out"},
                {"🔥 Speed Demon", "3 or more consecutive wins"}
            };
            
            for (String[] badge : badges) {
                HBox badgeLine = new HBox(20);
                Label badgeLabel = new Label(badge[0]);
                badgeLabel.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold; -fx-min-width: 180;");
                Label descLabel = new Label(badge[1]);
                descLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12;");
                descLabel.setWrapText(true);
                badgeLine.getChildren().addAll(badgeLabel, descLabel);
                legendBox.getChildren().add(badgeLine);
            }
            
            contentBox.getChildren().add(legendBox);
            
            // Points System Legend section
            VBox pointsLegendBox = new VBox(8);
            pointsLegendBox.setStyle("-fx-border-color: #FFD700; -fx-border-width: 2; -fx-padding: 15; -fx-background-color: #252525; -fx-margin-top: 10;");
            
            Label pointsTitle = new Label("Points System Breakdown");
            pointsTitle.setStyle("-fx-font-size: 16; -fx-text-fill: #FFD700; -fx-font-weight: bold;");
            pointsLegendBox.getChildren().add(pointsTitle);
            
            String[][] pointsRules = {
                {"🥇 1st Place", "+3 points"},
                {"🥈 2nd Place", "+2 points"},
                {"🥉 3rd Place", "+1 point"},
                {"⚡ Speed Bonus", "+1 point (for WPM > 50)"},
                {"😫 Burnout Penalty", "-1 point per burnout event"},
                {"💰 Earnings Formula", "Base earnings by position (100/75/50 coins) + WPM bonuses - burnout penalties"}
            };
            
            for (String[] rule : pointsRules) {
                HBox ruleLine = new HBox(20);
                Label ruleLabel = new Label(rule[0]);
                ruleLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-min-width: 180;");
                Label ruleValueLabel = new Label(rule[1]);
                ruleValueLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12;");
                ruleValueLabel.setWrapText(true);
                ruleLine.getChildren().addAll(ruleLabel, ruleValueLabel);
                pointsLegendBox.getChildren().add(ruleLine);
            }
            
            // Add note about how points accumulate
            Label pointsNote = new Label("💡 Points accumulate across all races. Higher points = higher rank on leaderboard. Earnings are tracked separately for financial progression.");
            pointsNote.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 11; -fx-padding: 10; -fx-text-alignment: left;");
            pointsNote.setWrapText(true);
            pointsLegendBox.getChildren().add(pointsNote);
            
            contentBox.getChildren().add(pointsLegendBox);
            
            ScrollPane scrollPane = new ScrollPane(contentBox);
            scrollPane.setStyle("-fx-background-color: #1a1a1a; -fx-control-inner-background: #1a1a1a; -fx-padding: 0;");
            scrollPane.setFitToWidth(true);
            mainLayout.getChildren().add(scrollPane);
        }
        
        // Back button
        Button backBtn = createStyledButton("Back to Menu", 14);
        backBtn.setOnAction(e -> {
            Scene startScene = createStartPage();
            stage.setScene(startScene);
        });
        
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().add(backBtn);
        
        mainLayout.getChildren().add(buttonBox);
        
        ScrollPane mainScroll = new ScrollPane(mainLayout);
        mainScroll.setStyle("-fx-background-color: #1a1a1a; -fx-control-inner-background: #1a1a1a; -fx-padding: 0;");
        mainScroll.setFitToWidth(true);
        
        return new Scene(mainScroll, 1000, 800);
    }
    
    // ========== UTILITY METHODS ==========
    private Button createStyledButton(String text, int fontSize) {
        Button btn = new Button(text);
        btn.setStyle("-fx-font-size: " + fontSize + "; -fx-padding: 10 30; " +
                     "-fx-background-color: #00ff00; -fx-text-fill: #000000; " +
                     "-fx-font-weight: bold; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> 
            btn.setStyle("-fx-font-size: " + fontSize + "; -fx-padding: 10 30; " +
                        "-fx-background-color: #00dd00; -fx-text-fill: #000000; " +
                        "-fx-font-weight: bold; -fx-cursor: hand;")
        );
        btn.setOnMouseExited(e -> 
            btn.setStyle("-fx-font-size: " + fontSize + "; -fx-padding: 10 30; " +
                        "-fx-background-color: #00ff00; -fx-text-fill: #000000; " +
                        "-fx-font-weight: bold; -fx-cursor: hand;")
        );
        return btn;
    }
    
    private boolean validateRaceConfig() {
        if (selectedPassage == null || selectedPassage.trim().isEmpty()) {
            showAlert("Please select a passage");
            return false;
        }
        if (selectedTypistCount < 2 || selectedTypistCount > 6) {
            showAlert("Please select between 2 and 6 typists");
            return false;
        }
        return true;
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Configuration");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
