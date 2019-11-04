package aicogdev.environnement;

/**
 * Exception jetable par les environnements en cas d'action illégale
 */
public class IllegalAction extends RuntimeException {
    public IllegalAction() {
        super("Illegal Action");
    }
}
