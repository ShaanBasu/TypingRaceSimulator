package Part2;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.control.Label;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import java.util.List;
import java.util.ArrayList;


/**
 * RaceDisplay handles the visual representation of the race.
 * It displays the passage text with character-by-character highlighting and real-time updates.
 * 
 * @author Shaan Basu
 * @version 2.0
 */
public class RaceDisplay {
    
    private static Timeline updateTimeline;
    
    /**
     * Creates the main race display window with live updating.
     * Shows passage text with character-by-character highlighting and real-time progress bars.
     */
    public static VBox createRaceDisplay(TypingRaceEnhanced race) {
        VBox mainContainer = new VBox();
        mainContainer.setStyle("-fx-background-color: #1a1a1a; -fx-padding: 20; -fx-spacing: 15;");
        
        // Title for the display
        Label title = new Label("Live Race Display");
        title.setStyle("-fx-font-size: 24; -fx-text-fill: #00ff00; -fx-font-weight: bold;");
        mainContainer.getChildren().add(title);
        
        // Section showing the passage with colored character highlighting
        VBox passageContainer = new VBox();
        passageContainer.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2; -fx-padding: 15; -fx-background-color: #252525;");
        passageContainer.setSpacing(10);
        
        Label passageTitle = new Label("Passage (highlighted by progress):");
        passageTitle.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 14; -fx-font-weight: bold;");
        passageContainer.getChildren().add(passageTitle);
        
        // Create passage display with TextFlow for character-by-character coloring
        TextFlow passageDisplay = new TextFlow();
        passageDisplay.setStyle("-fx-padding: 10;");
        passageContainer.getChildren().add(passageDisplay);
        
        // Section showing each typist's progress with stats
        VBox progressBars = new VBox();
        progressBars.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2; -fx-padding: 15; -fx-background-color: #252525;");
        progressBars.setSpacing(10);
        
        Label progressTitle = new Label("Typist Progress");
        progressTitle.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 16; -fx-font-weight: bold;");
        progressBars.getChildren().add(progressTitle);
        
        mainContainer.getChildren().addAll(passageContainer, progressBars);
        
        // Start live updating every 200ms to show race progression
        if (updateTimeline != null) {
            updateTimeline.stop();
        }
        
        updateTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            Platform.runLater(() -> {
                // Update passage display to reflect latest character highlights
                updatePassageDisplay(passageDisplay, race);
                
                // Refresh progress bars for all typists in the race
                progressBars.getChildren().clear();
                progressBars.getChildren().add(progressTitle);
                for (TypistWrapper typist : race.getTypists()) {
                    HBox progressLine = createProgressLine(typist, race.getPassageLength());
                    progressBars.getChildren().add(progressLine);
                }
            });
        }));
        updateTimeline.setCycleCount(Timeline.INDEFINITE);
        updateTimeline.play();
        
        return mainContainer;
    }
    
    /**
     * Updates the passage display text with dynamic character coloring.
     * Untyped characters appear gray, while typed characters show in the typist's color.
     */
    private static void updatePassageDisplay(TextFlow passageDisplay, TypingRaceEnhanced race) {
        passageDisplay.getChildren().clear();
        String passage = race.getPassage();
        
        // Color each character based on which typist has reached it
        for (int charIndex = 0; charIndex < passage.length(); charIndex++) {
            Text charText = new Text(String.valueOf(passage.charAt(charIndex)));
            
            // Find which typist has progressed furthest at this position
            String charColor = "#888888"; // Default gray for untyped characters
            int maxProgress = 0;
            
            for (TypistWrapper typist : race.getTypists()) {
                if (typist.getProgress() > charIndex && typist.getProgress() > maxProgress) {
                    charColor = typist.getColor();
                    maxProgress = typist.getProgress();
                }
            }
            
            charText.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14; -fx-fill: " + charColor + ";");
            passageDisplay.getChildren().add(charText);
        }
    }
    
    /**
     * Creates a single progress row displaying a typist's current stats.
     * Shows name, position, accuracy, and status indicators.
     */
    private static HBox createProgressLine(TypistWrapper typist, int totalLength) {
        HBox line = new HBox();
        line.setSpacing(10);
        line.setPadding(new Insets(5));
        line.setStyle("-fx-background-color: #1a1a1a;");
        
        // Display the typist's name
        Label name = new Label(typist.getName());
        name.setStyle("-fx-text-fill: white; -fx-min-width: 120;");
        
        // Show current position in passage
        Label progress = new Label(typist.getProgress() + " / " + totalLength);
        progress.setStyle("-fx-text-fill: " + typist.getColor() + "; -fx-font-weight: bold;");
        
        // Display typing accuracy percentage
        Label accuracy = new Label(String.format("Acc: %.2f%%", typist.getAccuracy() * 100));
        accuracy.setStyle("-fx-text-fill: #ffaa00;");
        
        // Show current state (burnout or mistype)
        String status = "";
        if (typist.isBurntOut()) {
            status = " [BURNT OUT]";
        } else if (typist.hasMistyped()) {
            status = " [MISTYPED]";
        }
        Label statusLabel = new Label(status);
        statusLabel.setStyle("-fx-text-fill: #ff5555;");
        
        line.getChildren().addAll(name, progress, accuracy, statusLabel);
        return line;
    }
    
    /**
     * Creates the final race results display showing placement, metrics, and statistics.
     * Handles both valid races and void races with appropriate messaging.
     */
    public static VBox createRaceResults(TypingRaceEnhanced race) {
        // Stop the live update timeline when switching to results
        if (updateTimeline != null) {
            updateTimeline.stop();
        }
        
        VBox mainContainer = new VBox();
        mainContainer.setStyle("-fx-background-color: #1a1a1a; -fx-padding: 20; -fx-spacing: 15;");
        
        // Title for results section
        Label title = new Label("Race Results");
        title.setStyle("-fx-font-size: 28; -fx-text-fill: #00ff00; -fx-font-weight: bold;");
        mainContainer.getChildren().add(title);
        
        // Container for detailed results and performance metrics
        VBox resultsBox = new VBox();
        resultsBox.setStyle("-fx-border-color: #FFD700; -fx-border-width: 2; -fx-padding: 20; -fx-background-color: #252525;");
        resultsBox.setSpacing(12);
        
        List<RaceResult> raceResults = race.getRaceResults();
        
        if (raceResults.isEmpty()) {
            // Show message if race is still in progress
            Label noResults = new Label("Race in progress...");
            noResults.setStyle("-fx-text-fill: white; -fx-font-size: 16;");
            resultsBox.getChildren().add(noResults);
        } else {
            // Check if race was invalidated due to ties
            boolean isVoidRace = !raceResults.isEmpty() && raceResults.get(0).isVoidRace();
            
            if (isVoidRace) {
                // Display void race warning with reason
                String voidReason = raceResults.get(0).getVoidReason();
                Label voidLabel = new Label("⚠️ " + voidReason);
                voidLabel.setStyle("-fx-font-size: 20; -fx-text-fill: #ff6b6b; -fx-font-weight: bold;");
                resultsBox.getChildren().add(voidLabel);
                
                Label explanation = new Label("This race has been invalidated. No winner was declared.");
                explanation.setStyle("-fx-font-size: 14; -fx-text-fill: #ffaa00;");
                resultsBox.getChildren().add(explanation);
                
                // Add separator line
                Label separator = new Label("");
                separator.setStyle("-fx-border-color: #444444; -fx-border-width: 0 0 1 0;");
                separator.setPrefHeight(1);
                resultsBox.getChildren().add(separator);
            }
            
            // Display results sorted by finishing order
            List<RaceResult> sortedResults = new ArrayList<>(raceResults);
            sortedResults.sort((a, b) -> Integer.compare(a.getPosition(), b.getPosition()));
            
            // Display each typist's result with all relevant metrics
            for (int i = 0; i < sortedResults.size(); i++) {
                RaceResult result = sortedResults.get(i);
                // Use medal emoji for placement or position number
                String placeMedal = (result.getPosition() == 1) ? "🏆 " : result.getPosition() + ". ";
                
                // Show typist name and placement
                Label placement = new Label(placeMedal + result.getTypistName());
                // Color code medals: gold, silver, bronze
                String medalColor = (result.getPosition() == 1) ? "#FFD700" : (result.getPosition() == 2) ? "#C0C0C0" : "#CD7F32";
                placement.setStyle("-fx-text-fill: " + medalColor + "; -fx-font-size: 16; -fx-font-weight: bold;");
                resultsBox.getChildren().add(placement);
                
                // Create a metrics row with WPM, accuracy, changes, and burnout
                HBox metricsBox = new HBox();
                metricsBox.setStyle("-fx-spacing: 20; -fx-padding: 0 0 0 20;");
                
                // Calculate and display words per minute
                double wpm = result.calculateWPM();
                Label wpmLabel = new Label(String.format("WPM: %.2f", wpm));
                wpmLabel.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 12;");
                metricsBox.getChildren().add(wpmLabel);
                
                // Show final accuracy percentage
                Label accuracyLabel = new Label(String.format("Accuracy: %.2f%%", result.getFinalAccuracy() * 100));
                accuracyLabel.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 12;");
                metricsBox.getChildren().add(accuracyLabel);
                
                // Show how accuracy changed from start to finish
                double accuracyChange = result.getAccuracyChange();
                String changeColor = (accuracyChange >= 0) ? "#00ff00" : "#ff6b6b";
                String changeSign = (accuracyChange >= 0) ? "+" : "";
                Label changeLabel = new Label(String.format("Accuracy Change: %s%.2f%%", changeSign, accuracyChange * 100));
                changeLabel.setStyle("-fx-text-fill: " + changeColor + "; -fx-font-size: 12;");
                metricsBox.getChildren().add(changeLabel);
                
                // Display burnout count with color indicator
                Label burnoutLabel = new Label(String.format("Burnouts: %d", result.getBurnoutCount()));
                String burnoutColor = (result.getBurnoutCount() == 0) ? "#00ff00" : "#ffa500";
                burnoutLabel.setStyle("-fx-text-fill: " + burnoutColor + "; -fx-font-size: 12;");
                metricsBox.getChildren().add(burnoutLabel);
                
                // Show formatted race completion time
                Label timeLabel = new Label("Time: " + result.getFormattedTime());
                timeLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 12;");
                metricsBox.getChildren().add(timeLabel);
                
                resultsBox.getChildren().add(metricsBox);
                
                // Add separator between each result
                if (i < sortedResults.size() - 1) {
                    Label separator = new Label("");
                    separator.setStyle("-fx-border-color: #444444; -fx-border-width: 0 0 1 0;");
                    separator.setPrefHeight(1);
                    resultsBox.getChildren().add(separator);
                }
            }
        }
        
        mainContainer.getChildren().add(resultsBox);
        
        // Section showing the full passage text that was typed
        VBox passageBox = new VBox();
        passageBox.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2; -fx-padding: 15; -fx-background-color: #252525;");
        
        Label passageTitle = new Label("Passage Text:");
        passageTitle.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 14; -fx-font-weight: bold;");
        
        Label passageText = new Label(race.getPassage());
        passageText.setStyle("-fx-text-fill: #aaaaaa; -fx-font-family: 'Courier New'; -fx-font-size: 12;");
        passageText.setWrapText(true);
        passageText.setMaxWidth(700);
        
        passageBox.getChildren().addAll(passageTitle, passageText);
        mainContainer.getChildren().add(passageBox);
        
        // Add spacer to fill remaining space and maintain dark theme
        Region spacer = new Region();
        spacer.setStyle("-fx-background-color: #1a1a1a;");
        spacer.setPrefHeight(100);
        mainContainer.getChildren().add(spacer);
        
        return mainContainer;
    }
}
