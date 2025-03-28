public class Persona {
    private int id;
    private int pianoDestinazione;

    public Persona(int id, int pianoPartenza) {
        this.id = id;
        do {
            this.pianoDestinazione = (int) (Math.random() * 10) + 1;
        } while (this.pianoDestinazione == pianoPartenza);
    }

    public void saliSuAscensore(Ascensore a) {
        if (a.isPorteAperte() && a.getPianoCorrente() == this.pianoDestinazione) {
            System.out.println("[PERSONA " + id + "] Sale al piano " + a.getPianoCorrente());
        }
    }

    public void scendiDaAscensore(Ascensore a) {
        if (a.isPorteAperte() && a.getPianoCorrente() == this.pianoDestinazione) {
            System.out.println("[PERSONA " + id + "] Scende al piano " + a.getPianoCorrente());
        }
    }

    public int getPianoDestinazione() {
        return pianoDestinazione;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Persona " + id + " (-> " + pianoDestinazione + ")";
    }
}
