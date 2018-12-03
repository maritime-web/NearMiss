package dk.dma.nearmiss.observer;


interface Subject {
    void addListener(Observer observer);

    @SuppressWarnings("unused")
    void removeListener(Observer observer);

    void notifyListeners();
}
