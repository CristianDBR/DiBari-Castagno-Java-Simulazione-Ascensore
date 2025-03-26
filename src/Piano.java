import java.util.ArrayList;

public class Piano {
    private int numeroPiano;
    private ArrayList<Persona> codaPersone;

    public Piano(int numeroPiano) {
        this.numeroPiano = numeroPiano;
        this.codaPersone = new ArrayList<>();
    }

    public int getNumeroPiano() {
        return numeroPiano;
    }

    public ArrayList<Persona> getCodaPersone() {
        return codaPersone;
    }

    public void aggiungiPersonaCoda(Persona p) {
        codaPersone.add(p);
        System.out.println(p + " aggiunta in coda al piano " + numeroPiano);
    }

    public Persona rimuoviPersonaCoda() {
        if (!codaPersone.isEmpty()) {
            return codaPersone.remove(0);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Piano " + numeroPiano + " | Persone in coda: " + codaPersone.size();
    }
}
