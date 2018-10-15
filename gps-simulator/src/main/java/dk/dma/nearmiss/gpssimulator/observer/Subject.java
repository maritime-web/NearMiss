package dk.dma.nearmiss.gpssimulator.observer;


interface Subject {
    void addListener(Observer observer);

    @SuppressWarnings("unused")
    void removeListener(Observer observer);

    void notifyListeners();
}
