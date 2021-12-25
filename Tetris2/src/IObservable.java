import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public interface IObservable {
	/**
	 * 
	 * Notifies observers of keyevent 
	 */
	public void notifyObservers(KeyEvent keyEvent);
}
