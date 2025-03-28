import java.util.ArrayList;

public class Piano {
    private int numeroPiano;
    private ArrayList<Persona> codaPersone;
    private int tempoAttesa; // in secondi

    // Colori ANSI
    public static final String RESET  = "\033[0m";
    public static final String BLUE   = "\033[34m";

    public Piano(int numeroPiano) {
        this.numeroPiano = numeroPiano;
        this.codaPersone = new ArrayList<>();
        this.tempoAttesa = 0;
    }

    public int getNumeroPiano() {
        return numeroPiano;
    }

    public ArrayList<Persona> getCodaPersone() {
        return codaPersone;
    }

    public void aggiungiPersonaCoda(Persona p) {
        codaPersone.add(p);
        System.out.println(BLUE + "[PIANO " + numeroPiano + "] " + p +
                " (dal piano " + numeroPiano + " -> destinazione: " + p.getPianoDestinazione() +
                ") aggiunta in coda" + RESET);
    }

    public Persona rimuoviPersonaCoda() {
        if (!codaPersone.isEmpty()) {
            return codaPersone.remove(0);
        }
        return null;
    }

    public int getTempoAttesa() {
        return tempoAttesa;
    }

    public void incrementaTempoAttesa() {
        tempoAttesa++;
    }

    public void resettaTempoAttesa() {
        tempoAttesa = 0;
    }

    @Override
    public String toString() {
        return BLUE + "[PIANO " + numeroPiano + "] Persone in coda: " + codaPersone.size() +
                " | Tempo attesa: " + tempoAttesa + " sec" + RESET;
    }
}
