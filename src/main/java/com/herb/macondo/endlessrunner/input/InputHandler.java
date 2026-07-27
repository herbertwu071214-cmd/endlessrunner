package com.herb.endlessrunner.input;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputHandler {
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;

    public void keyPressed(KeyEvent e) {
        KeyCode code = e.getCode();
        if (code == KeyCode.A || code == KeyCode.LEFT) leftPressed = true;
        if (code == KeyCode.D || code == KeyCode.RIGHT) rightPressed = true;
        if (code == KeyCode.W || code == KeyCode.UP || code == KeyCode.SPACE) upPressed = true;
        if (code == KeyCode.S || code == KeyCode.DOWN) downPressed = true;
    }

    public void keyReleased(KeyEvent e) {
        KeyCode code = e.getCode();
        if (code == KeyCode.A || code == KeyCode.LEFT) leftPressed = false;
        if (code == KeyCode.D || code == KeyCode.RIGHT) rightPressed = false;
        if (code == KeyCode.W || code == KeyCode.UP || code == KeyCode.SPACE) upPressed = false;
        if (code == KeyCode.S || code == KeyCode.DOWN) downPressed = false;
    }

    public boolean isLeftPressed() { return leftPressed; }
    public boolean isRightPressed() { return rightPressed; }
    public boolean isUpPressed() { return upPressed; }
    public boolean isDownPressed() { return downPressed; }
}

