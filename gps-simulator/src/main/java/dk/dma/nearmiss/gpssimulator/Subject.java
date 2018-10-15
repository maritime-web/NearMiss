package dk.dma.nearmiss.gpssimulator;


interface Subject {
    void addListener(Observer observer);

    @SuppressWarnings("unused")
    void removeListener(Observer observer);

    void notifyListeners();
}
