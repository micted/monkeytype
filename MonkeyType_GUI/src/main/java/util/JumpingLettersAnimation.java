package util;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;



public class JumpingLettersAnimation {
    private final Text text;
    private final Timeline timeline;

    public JumpingLettersAnimation(Text text) {
        this.text = text;

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void applyAnimation() {
        text.setTextOrigin(VPos.TOP);
        text.setFont(Font.font(24));

        double textHeight = text.getLayoutBounds().getHeight();

        KeyValue initKeyValueY = new KeyValue(text.translateYProperty(), 0.0);
        KeyFrame initFrameY = new KeyFrame(Duration.ZERO, initKeyValueY);

        KeyValue middleKeyValueY = new KeyValue(text.translateYProperty(), -10.0);
        KeyFrame middleFrameY = new KeyFrame(Duration.seconds(1.5), middleKeyValueY);

        KeyValue endKeyValueY = new KeyValue(text.translateYProperty(), 0.0);
        KeyFrame endFrameY = new KeyFrame(Duration.seconds(3), endKeyValueY);

        timeline.getKeyFrames().addAll(initFrameY, middleFrameY, endFrameY);
        timeline.play();
    }

    public void stopAnimation() {
        timeline.stop();
    }
}
