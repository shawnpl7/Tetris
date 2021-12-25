import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public interface IObserver {
	/**
	 * 
	 * Observer gets updated regarding the keyevent and performs required actions
	 */
	public void update(KeyEvent keyEvent);
}
