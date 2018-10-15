package dk.dma.nearmiss.gpssimulator;

import java.util.ArrayList;
import java.util.List;

public class AbstractGps extends Thread implements Subject {
    private List<Observer> listeners = new ArrayList<>();

    @Override
    public void addListener(Observer observer) {
        listeners.add(observer);
    }

    @Override
    public void removeListener(Observer observer) {
        listeners.remove(observer);
    }

    public void notifyListeners() {
        for (Observer listener : listeners) {
            listener.update();
        }
    }

}
