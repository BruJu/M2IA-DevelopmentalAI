package aicogdev.environnement;

/**
 * Throwable exception by environments in case of illegal action
 */
public class IllegalAction extends RuntimeException {
    public IllegalAction() {
        super("Illegal Action");
    }
}
