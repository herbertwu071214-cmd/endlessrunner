package com.herb.endlessrunner.input;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputHandler {
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;

    public boolean keyPressed(KeyEvent e) {
        KeyCode code = e.getCode();

        if (code == KeyCode.A || code == KeyCode.LEFT) {
            if (leftPressed) return false;
            leftPressed = true;
            return true;
        }

        if (code == KeyCode.D || code == KeyCode.RIGHT) {
            if (rightPressed) return false;
            rightPressed = true;
            return true;
        }

        if (code == KeyCode.W || code == KeyCode.UP || code == KeyCode.SPACE) {
            if (upPressed) return false;
            upPressed = true;
            return true;
        }

        if (code == KeyCode.S || code == KeyCode.DOWN) {
            if (downPressed) return false;
            downPressed = true;
            return true;
        }

        return false;
    }

    public void keyReleased(KeyEvent e) {
        KeyCode code = e.getCode();

        if (code == KeyCode.A || code == KeyCode.LEFT) {
            leftPressed = false;
        } else if (code == KeyCode.D || code == KeyCode.RIGHT) {
            rightPressed = false;
        } else if (code == KeyCode.W || code == KeyCode.UP || code == KeyCode.SPACE) {
            upPressed = false;
        } else if (code == KeyCode.S || code == KeyCode.DOWN) {
            downPressed = false;
        }
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }
}
